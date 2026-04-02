package com.Khue.InventoryMgtSystem.services.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.dto.TransactionDTO;
import com.Khue.InventoryMgtSystem.dto.Transaction.TransactionRequest;
import com.Khue.InventoryMgtSystem.enums.TransactionStatus;
import com.Khue.InventoryMgtSystem.enums.TransactionType;
import com.Khue.InventoryMgtSystem.exceptions.NameValueRequiredException;
import com.Khue.InventoryMgtSystem.exceptions.NotFoundException;
import com.Khue.InventoryMgtSystem.models.Product;
import com.Khue.InventoryMgtSystem.models.Supplier;
import com.Khue.InventoryMgtSystem.models.Transaction;
import com.Khue.InventoryMgtSystem.models.User;
import com.Khue.InventoryMgtSystem.repository.ProductRepository;
import com.Khue.InventoryMgtSystem.repository.SupplierRepository;
import com.Khue.InventoryMgtSystem.repository.TransactionRepository;

import com.Khue.InventoryMgtSystem.services.TransactionService;
import com.Khue.InventoryMgtSystem.services.UserService;
import com.Khue.InventoryMgtSystem.specification.TransactionFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final ProductRepository productRepository;
  private final SupplierRepository supplierRepository;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Override
  public Response purchase(TransactionRequest transactionRequest) {
    Long productId = transactionRequest.getProductId();
    Long supplierId = transactionRequest.getSupplierId();
    Integer quantity = transactionRequest.getQuantity();

    if (supplierId == null)
      throw new NameValueRequiredException("Supplier Id is required");

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    Supplier supplier = supplierRepository.findById(supplierId)
        .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

    User user = userService.getCurrentLoggedInUser();

    // Khi mua , phải update lại số lượng hàng hóa trong kho tăng lên
    product.setStockQuantity(product.getStockQuantity() + quantity);

    productRepository.save(product);

    // Tạo tracsaction
    Transaction transaction = Transaction.builder()
        .transactionType(TransactionType.PURCHASE)
        .transactionStatus(TransactionStatus.COMPLETED)
        .product(product)
        .user(user)
        .supplier(supplier)
        .totalProduct(quantity)
        .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity))) // SL * price
        .description(transactionRequest.getDescription())
        .note(transactionRequest.getNote())
        .build();

    transactionRepository.save(transaction);

    return Response.builder()
        .status(200)
        .message("Purchase Made successfully")
        .build();
  }

  @Override
  public Response sell(TransactionRequest transactionRequest) {
    Long productId = transactionRequest.getProductId();
    Integer quantity = transactionRequest.getQuantity();

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    if (quantity > product.getStockQuantity()) {
      throw new NameValueRequiredException("Insufficient stock quantity for this sell");
    }

    User user = userService.getCurrentLoggedInUser();

    // update product
    product.setStockQuantity(product.getStockQuantity() - quantity);
    productRepository.save(product);

    // create a transaction
    Transaction transaction = Transaction.builder()
        .transactionType(TransactionType.SALE)
        .transactionStatus(TransactionStatus.COMPLETED)
        .product(product)
        .user(user)
        .totalProduct(quantity)
        .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
        .description(transactionRequest.getDescription())
        .note(transactionRequest.getNote())
        .build();

    transactionRepository.save(transaction);
    return Response.builder()
        .status(200)
        .message("Product Sale successfully made")
        .build();
  }

  @Override
  public Response returnToSupplier(TransactionRequest transactionRequest) {
    Long productId = transactionRequest.getProductId();
    Long supplierId = transactionRequest.getSupplierId();
    Integer quantity = transactionRequest.getQuantity();

    if (supplierId == null)
      throw new NameValueRequiredException("Supplier Id is Required");

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    Supplier supplier = supplierRepository.findById(supplierId)
        .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

    User user = userService.getCurrentLoggedInUser();

    // update product
    product.setStockQuantity(product.getStockQuantity() - quantity);
    productRepository.save(product);

    // create a transaction
    Transaction transaction = Transaction.builder()
        .transactionType(TransactionType.RETURN_TO_SUPPLIER)
        .transactionStatus(TransactionStatus.PROCESSING)
        .product(product)
        .user(user)
        .totalProduct(quantity)
        .totalPrice(BigDecimal.ZERO)
        .description(transactionRequest.getDescription())
        .note(transactionRequest.getNote())
        .build();

    transactionRepository.save(transaction);

    return Response.builder()
        .status(200)
        .message("Product Returned in progress")
        .build();
  }

  @Override
  public Response getAllTransactions(int page, int size, String filter) {

    // Pageable dùng để phân trang (size : số lượng bản ghi trên mỗi trang)
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

    // spec : Truyền vào các điều kiện để lọc user
    Specification<Transaction> spec = TransactionFilter.byFilter(filter);

    // Trả về 1 page các kết quả sau khi lọc
    Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

    List<TransactionDTO> transactionDTOS = modelMapper.map(transactionPage.getContent(),
        new TypeToken<List<TransactionDTO>>() {
        }.getType());

    transactionDTOS.forEach(transactionDTO -> {
      transactionDTO.setUser(null);
      transactionDTO.setProduct(null);
      transactionDTO.setSupplier(null);
    });

    return Response.builder()
        .status(200)
        .message("success")
        .transactions(transactionDTOS)
        .totalElements(transactionPage.getTotalElements()) // Tổng số record
        .totalPages(transactionPage.getTotalPages()) // tổng Page
        .build();
  }

  @Override
  public Response getTransactionById(Long id) {
    Transaction transaction = transactionRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

    TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

    // Set null bởi vì trong Transaction có User , mà trong user có transaction ,
    // lặp vô hạn
    transactionDTO.getUser().setTransaction(null);

    return Response.builder()
        .status(200)
        .message("success")
        .transaction(transactionDTO)
        .build();
  }

  @Override
  public Response getAllTransactionByMonthAndYear(int month, int year) {
    List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

    List<TransactionDTO> transactionDTOS = modelMapper.map(transactions, new TypeToken<List<TransactionDTO>>() {
    }.getType());

    transactionDTOS.forEach(transactionDTO -> {
      transactionDTO.setUser(null);
      transactionDTO.setProduct(null);
      transactionDTO.setSupplier(null);
    });

    return Response.builder()
        .status(200)
        .message("success")
        .transactions(transactionDTOS)
        .build();
  }

  @Override
  public Response updateTransactionStatus(Long transactionId, TransactionStatus status) {
    Transaction existingTransaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

    existingTransaction.setTransactionStatus(status);
    existingTransaction.setUpdateAt(LocalDateTime.now());

    transactionRepository.save(existingTransaction);

    return Response.builder()
        .status(200)
        .message("Transaction Status Successfully Updated")
        .build();
  }

}
