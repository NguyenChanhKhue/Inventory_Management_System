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
import java.util.Map;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.Khue.InventoryMgtSystem.dto.ProductDTO;
import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.exceptions.NameValueRequiredException;
import com.Khue.InventoryMgtSystem.exceptions.NotFoundException;
import com.Khue.InventoryMgtSystem.models.Category;
import com.Khue.InventoryMgtSystem.models.Product;
import com.Khue.InventoryMgtSystem.repository.CategoryRepository;
import com.Khue.InventoryMgtSystem.repository.ProductRepository;
import com.Khue.InventoryMgtSystem.repository.TransactionRepository;
import com.Khue.InventoryMgtSystem.services.AuditLogService;

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

  private final Cloudinary cloudinary;

  private final AuditLogService auditLogService;

  @Override
  public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
    Category category = categoryRepository.findById(productDTO.getCategoryId())
        .orElseThrow(() -> new NotFoundException("Category Not Found"));

    Product productToSave = Product.builder()
        .name(productDTO.getName())
        .sku(productDTO.getSku())

        .importPrice(productDTO.getImportPrice())
        .unit(productDTO.getUnit())
        .location(productDTO.getLocation())
        .minStock(productDTO.getMinStock() != null ? productDTO.getMinStock() : 0)
        .stockQuantity(productDTO.getStockQuantity() != null ? productDTO.getStockQuantity() : 0)
        .description(productDTO.getDescription())
        .category(category)
        .isActive(true)
        .build();

    if (imageFile != null && !imageFile.isEmpty()) {
      productToSave.setImgUrl(saveImage(imageFile));
    }

    productRepository.save(productToSave);

    auditLogService.logAction("CREATE", "Product", productToSave.getId(), "Tạo mới sản phẩm: " + productToSave.getName());

    return Response.builder()
        .status(200)
        .message("Đã lưu sản phẩm thành công")
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
          .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục này"));
      existingProduct.setCategory(category);
    }

    if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
      existingProduct.setName(productDTO.getName());
    }

    if (productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
      existingProduct.setSku(productDTO.getSku());
    }



    if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
      existingProduct.setStockQuantity(productDTO.getStockQuantity());
    }

    if (productDTO.getImportPrice() != null && productDTO.getImportPrice().compareTo(BigDecimal.ZERO) >= 0) {
      existingProduct.setImportPrice(productDTO.getImportPrice());
    }

    if (productDTO.getUnit() != null) {
      existingProduct.setUnit(productDTO.getUnit());
    }

    if (productDTO.getLocation() != null) {
      existingProduct.setLocation(productDTO.getLocation());
    }

    if (productDTO.getMinStock() != null && productDTO.getMinStock() >= 0) {
      existingProduct.setMinStock(productDTO.getMinStock());
    }

    if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
      existingProduct.setDescription(productDTO.getDescription());
    }

    productRepository.save(existingProduct);

    auditLogService.logAction("UPDATE", "Product", existingProduct.getId(), "Cập nhật sản phẩm: " + existingProduct.getName());

    return Response.builder()
        .status(200)
        .message("Cập nhật sản phẩm thành công")
        .build();
  }

  @Override
  public Response getAllProducts() {
    List<Product> productList = productRepository.findAllActive();

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
    
    if (!product.isActive()) {
        throw new NotFoundException("Product is inactive or deleted");
    }

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

    product.setActive(false);
    productRepository.save(product);

    auditLogService.logAction("DELETE", "Product", product.getId(), "Xóa sản phẩm: " + product.getName());

    return Response.builder()
        .status(200)
        .message("Đã xóa sản phẩm thành công")
        .build();
  }

  @Override
  public Response searchProduct(String input) {
    List<Product> products = productRepository.searchActiveProducts(input);

    if (products.isEmpty()) {
      throw new NotFoundException("Không tìm thấy sản phẩm nào");
    }

    List<ProductDTO> productDTOList = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {
    }.getType());

    return Response.builder()
        .status(200)
        .message("success")
        .products(productDTOList)
        .build();
  }

  @Override
  public Response getLowStockProducts() {
    List<Product> products = productRepository.findLowStockProducts();
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

    try {
      Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
      String url = uploadResult.get("secure_url").toString();
      log.info("Saved product image to Cloudinary: {}", url);
      return url;
    } catch (Exception e) {
      log.error("Error Saving Image to Cloudinary", e);
      throw new IllegalArgumentException("Error Saving Image " + e.getMessage());
    }
  }
}
