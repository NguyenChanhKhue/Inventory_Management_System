package com.Khue.InventoryMgtSystem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Khue.InventoryMgtSystem.dto.ProductDTO;
import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.exceptions.NameValueRequiredException;
import com.Khue.InventoryMgtSystem.services.Impl.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;
  private final ObjectMapper objectMapper;

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Response> saveProduct(
      @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
      @RequestParam("productData") String productDataJson) {
    try {
      ProductDTO productDTO = objectMapper.readValue(productDataJson, ProductDTO.class);
      return ResponseEntity.ok(productService.saveProduct(productDTO, imageFile));
    } catch (Exception e) {
      throw new NameValueRequiredException("Dữ liệu sản phẩm không hợp lệ: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Response> updateProduct(
      @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
      @RequestParam("productData") String productDataJson) {
    try {
      ProductDTO productDTO = objectMapper.readValue(productDataJson, ProductDTO.class);
      return ResponseEntity.ok(productService.updateProduct(productDTO, imageFile));
    } catch (Exception e) {
      throw new NameValueRequiredException("Dữ liệu sản phẩm không hợp lệ: " + e.getMessage());
    }
  }


  @GetMapping("/all")
  public ResponseEntity<Response> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Response> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
    return ResponseEntity.ok(productService.deleteProduct(id));
  }

  @GetMapping("/search")
  public ResponseEntity<Response> searchProduct(@RequestParam String input) {
    return ResponseEntity.ok(productService.searchProduct(input));
  }

  @GetMapping("/low-stock")
  public ResponseEntity<Response> getLowStockProducts() {
    return ResponseEntity.ok(productService.getLowStockProducts());
  }
}
