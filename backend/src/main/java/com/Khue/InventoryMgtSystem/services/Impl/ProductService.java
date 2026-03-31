package com.Khue.InventoryMgtSystem.services.Impl;

import org.springframework.web.multipart.MultipartFile;

import com.Khue.InventoryMgtSystem.dto.ProductDTO;
import com.Khue.InventoryMgtSystem.dto.Response;

public interface ProductService {
  Response saveProduct (ProductDTO productDTO , MultipartFile imageFile);

  Response updateProduct (ProductDTO productDTO , MultipartFile imageFile);

  Response getAllProducts();

  Response getProductById(Long id);

  Response deleteProduct(Long id);

  Response searchProduct(String input);
}
