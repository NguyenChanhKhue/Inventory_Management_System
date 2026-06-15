package com.Khue.InventoryMgtSystem.services.Impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Khue.InventoryMgtSystem.dto.ProductDTO;
import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.exceptions.NameValueRequiredException;
import com.Khue.InventoryMgtSystem.exceptions.NotFoundException;
import com.Khue.InventoryMgtSystem.models.Category;
import com.Khue.InventoryMgtSystem.models.Product;
import com.Khue.InventoryMgtSystem.repository.CategoryRepository;
import com.Khue.InventoryMgtSystem.repository.ProductRepository;
import com.Khue.InventoryMgtSystem.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  private final ModelMapper modelMapper;

  private final CategoryRepository categoryRepository;

  private final TransactionRepository transactionRepository;

  @Value("${app.product-images-dir:./product-images}")
  private String productImagesDir;

  @Override
  public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
    Category category = categoryRepository.findById(productDTO.getCategoryId())
        .orElseThrow(() -> new NotFoundException("Category Not Found"));

    Product productToSave = Product.builder()
        .name(productDTO.getName())
        .sku(productDTO.getSku())
        .price(productDTO.getPrice())
        .stockQuantity(productDTO.getStockQuantity())
        .description(productDTO.getDescription())
        .category(category)
        .build();

    if (imageFile != null && !imageFile.isEmpty()) {
      productToSave.setImgUrl(saveImage(imageFile));
    }

    productRepository.save(productToSave);

    return Response.builder()
        .status(200)
        .message("Product Successfully Saved")
        .build();
  }

  @Override
  public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
    Product existingProduct = productRepository.findById(productDTO.getProductId())
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    if (imageFile != null && !imageFile.isEmpty()) {
      existingProduct.setImgUrl(saveImage(imageFile));
    }

    if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0) {
      Category category = categoryRepository.findById(productDTO.getCategoryId())
          .orElseThrow(() -> new NotFoundException("Category Not Found"));
      existingProduct.setCategory(category);
    }

    if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
      existingProduct.setName(productDTO.getName());
    }

    if (productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
      existingProduct.setSku(productDTO.getSku());
    }

    if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
      existingProduct.setPrice(productDTO.getPrice());
    }

    if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
      existingProduct.setStockQuantity(productDTO.getStockQuantity());
    }

    if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
      existingProduct.setDescription(productDTO.getDescription());
    }

    productRepository.save(existingProduct);

    return Response.builder()
        .status(200)
        .message("Product Updated successfully")
        .build();
  }

  @Override
  public Response getAllProducts() {
    List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    List<ProductDTO> productDTOList = modelMapper.map(productList, new TypeToken<List<ProductDTO>>() {
    }.getType());

    return Response.builder()
        .status(200)
        .message("success")
        .products(productDTOList)
        .build();
  }

  @Override
  public Response getProductById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    return Response.builder()
        .status(200)
        .message("success")
        .product(modelMapper.map(product, ProductDTO.class))
        .build();
  }

  @Override
  public Response deleteProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    if (transactionRepository.existsByProduct_Id(id)) {
      throw new NameValueRequiredException(
          "Cannot delete this product because it is already used in transactions");
    }

    try {
      productRepository.delete(product);
      productRepository.flush();
    } catch (DataIntegrityViolationException ex) {
      throw new NameValueRequiredException(
          "Cannot delete this product because it is already used in transactions");
    }

    return Response.builder()
        .status(200)
        .message("Product Deleted successfully")
        .build();
  }

  @Override
  public Response searchProduct(String input) {
    List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(input, input);

    if (products.isEmpty()) {
      throw new NotFoundException("Product Not Found");
    }

    List<ProductDTO> productDTOList = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {
    }.getType());

    return Response.builder()
        .status(200)
        .message("success")
        .products(productDTOList)
        .build();
  }

  private String saveImage(MultipartFile imageFile) {
    String contentType = imageFile.getContentType();
    if (contentType == null || !contentType.startsWith("image/") || imageFile.getSize() > 1024 * 1024 * 1024) {
      throw new IllegalArgumentException("Only image files under 1 GB is allowed");
    }

    File directory = new File(productImagesDir);
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IllegalArgumentException("Error creating image directory");
    }

    String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
    File destinationFile = new File(directory, uniqueFileName);

    try {
      imageFile.transferTo(destinationFile);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error Saving Image " + e.getMessage());
    }

    log.info("Saved product image to {}", destinationFile.getAbsolutePath());
    return "products/" + uniqueFileName;
  }
}
