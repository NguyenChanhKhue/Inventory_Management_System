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
import com.Khue.InventoryMgtSystem.services.AuditLogService;
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
  private final AuditLogService auditLogService;

  @Override
  public Response purchase(TransactionRequest transactionRequest) {
    Long productId = transactionRequest.getProductId();
    Long supplierId = transactionRequest.getSupplierId();
    Integer quantity = transactionRequest.getQuantity();

    if (quantity == null || quantity <= 0) {
      throw new NameValueRequiredException("Số lượng nhập hàng phải lớn hơn 0");
    }

    if (supplierId == null)
      throw new NameValueRequiredException("Vui lòng chọn Nhà cung cấp");

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
        .transactionType(TransactionType.IMPORT)
        .transactionStatus(TransactionStatus.COMPLETED)
        .product(product)
        .user(user)
        .supplier(supplier)
        .totalProduct(quantity)
        .totalPrice(product.getImportPrice().multiply(BigDecimal.valueOf(quantity))) // SL * importPrice
        .description(transactionRequest.getDescription())
        .note(transactionRequest.getNote())
        .build();

    transactionRepository.save(transaction);

    auditLogService.logAction("CREATE", "Transaction", transaction.getId(), "Tạo phiếu nhập hàng (SL: " + quantity + ", SP: " + product.getName() + ")");

    return Response.builder()
        .status(200)
        .message("Nhập hàng thành công")
        .build();
  }

  @Override
  public Response sell(TransactionRequest transactionRequest) {
    Long productId = transactionRequest.getProductId();
    Integer quantity = transactionRequest.getQuantity();

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    if (quantity == null || quantity <= 0) {
      throw new NameValueRequiredException("Số lượng xuất bán phải lớn hơn 0");
    }

    if (quantity > product.getStockQuantity()) {
      throw new NameValueRequiredException("Insufficient stock quantity for this sell");
    }

    User user = userService.getCurrentLoggedInUser();

    // update product
    product.setStockQuantity(product.getStockQuantity() - quantity);
    productRepository.save(product);

    // create a transaction
    Transaction transaction = Transaction.builder()
        .transactionType(TransactionType.EXPORT)
        .transactionStatus(TransactionStatus.COMPLETED)
        .product(product)
        .user(user)
        .totalProduct(quantity)
        .totalPrice(product.getImportPrice().multiply(BigDecimal.valueOf(quantity)))
        .description(transactionRequest.getDescription())
        .note(transactionRequest.getNote())

        .build();

    transactionRepository.save(transaction);

    auditLogService.logAction("CREATE", "Transaction", transaction.getId(), "Tạo phiếu xuất bán (SL: " + quantity + ", SP: " + product.getName() + ")");

    return Response.builder()
        .status(200)
        .message("Xuất bán thành công")
        .build();
  }

  @Override
  public Response returnToSupplier(TransactionRequest transactionRequest) {
    Long productId = transactionRequest.getProductId();
    Long supplierId = transactionRequest.getSupplierId();
    Integer quantity = transactionRequest.getQuantity();

    if (supplierId == null)
      throw new NameValueRequiredException("Vui lòng chọn Nhà cung cấp");

    if (quantity == null || quantity <= 0) {
      throw new NameValueRequiredException("Số lượng trả hàng phải lớn hơn 0");
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    Supplier supplier = supplierRepository.findById(supplierId)
        .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

    if (quantity > product.getStockQuantity()) {
      throw new NameValueRequiredException("Insufficient stock quantity for return");
    }

    User user = userService.getCurrentLoggedInUser();

    // create a transaction
    Transaction transaction = Transaction.builder()
        .transactionType(TransactionType.RETURN_TO_SUPPLIER)
        .transactionStatus(TransactionStatus.PROCESSING)
        .product(product)
        .user(user)
        .supplier(supplier)
        .totalProduct(quantity)
        .totalPrice(product.getImportPrice().multiply(BigDecimal.valueOf(quantity)))
        .description(transactionRequest.getDescription())
        .note(transactionRequest.getNote())
        .build();

    transactionRepository.save(transaction);

    auditLogService.logAction("CREATE", "Transaction", transaction.getId(), "Tạo phiếu trả hàng cho NCC (SL: " + quantity + ", SP: " + product.getName() + ")");

    return Response.builder()
        .status(200)
        .message("Đã tạo phiếu trả hàng")
        .build();
  }

  @Override
  public Response adjust(TransactionRequest transactionRequest) {
    Long productId = transactionRequest.getProductId();
    Integer newQuantity = transactionRequest.getQuantity();

    if (newQuantity == null || newQuantity < 0) {
      throw new NameValueRequiredException("Số lượng phải lớn hơn hoặc bằng 0");
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product Not Found"));

    int oldQuantity = product.getStockQuantity();
    int difference = newQuantity - oldQuantity;

    User user = userService.getCurrentLoggedInUser();

    // update product
    product.setStockQuantity(newQuantity);
    productRepository.save(product);

    // create a transaction
    Transaction transaction = Transaction.builder()
        .transactionType(TransactionType.ADJUSTMENT)
        .transactionStatus(TransactionStatus.COMPLETED)
        .product(product)
        .user(user)
        .totalProduct(difference) // can be negative or positive depending on adjustment
        .totalPrice(BigDecimal.ZERO)
        .description(transactionRequest.getDescription())
        .note(transactionRequest.getNote() != null ? transactionRequest.getNote() : "Adjusted. Old: " + oldQuantity + ", New: " + newQuantity)
        .build();

    transactionRepository.save(transaction);

    auditLogService.logAction("CREATE", "Transaction", transaction.getId(), "Tạo phiếu điều chỉnh kho (Chênh lệch: " + difference + ", SP: " + product.getName() + ")");

    return Response.builder()
        .status(200)
        .message("Kiểm kê / Điều chỉnh kho thành công")
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

    TransactionStatus oldStatus = existingTransaction.getTransactionStatus();

    if (oldStatus == status) {
        throw new NameValueRequiredException("Trạng thái mới không được trùng với trạng thái hiện tại.");
    }

    if (oldStatus == TransactionStatus.CANCELLED) {
        throw new NameValueRequiredException("Phiếu giao dịch này đã bị Hủy, không thể thay đổi trạng thái được nữa.");
    }

    if (oldStatus == TransactionStatus.COMPLETED && status == TransactionStatus.PROCESSING) {
        throw new NameValueRequiredException("Phiếu đã Hoàn thành không thể quay lại trạng thái Đang xử lý.");
    }

    // Logic Hoàn Thành Phiếu Trả Hàng
    if (status == TransactionStatus.COMPLETED && oldStatus == TransactionStatus.PROCESSING) {
      if (existingTransaction.getTransactionType() == TransactionType.RETURN_TO_SUPPLIER) {
        Product product = existingTransaction.getProduct();
        int qty = existingTransaction.getTotalProduct();
        if (product.getStockQuantity() < qty) {
           throw new NameValueRequiredException("Không thể hoàn thành phiếu trả hàng vì tồn kho hiện tại không đủ.");
        }
        product.setStockQuantity(product.getStockQuantity() - qty);
        productRepository.save(product);
      }
    }

    // Logic Hủy Phiếu và Hoàn Trả Kho
    if (status == TransactionStatus.CANCELLED && oldStatus != TransactionStatus.CANCELLED) {
      Product product = existingTransaction.getProduct();
      int qty = existingTransaction.getTotalProduct();
      TransactionType type = existingTransaction.getTransactionType();

      if (type == TransactionType.EXPORT) {
        // Hủy bán -> Trả hàng về kho
        product.setStockQuantity(product.getStockQuantity() + qty);
      } else if (type == TransactionType.IMPORT) {
        // Hủy nhập -> Rút hàng khỏi kho
        if (product.getStockQuantity() < qty) {
           throw new NameValueRequiredException("Không thể hủy phiếu nhập vì số lượng tồn kho hiện tại không đủ để trừ");
        }
        product.setStockQuantity(product.getStockQuantity() - qty);
      } else if (type == TransactionType.RETURN_TO_SUPPLIER) {
        // Hủy trả hàng -> Hàng quay lại kho (CHỈ KHI TRƯỚC ĐÓ ĐÃ HOÀN THÀNH VÀ TRỪ KHO)
        if (oldStatus == TransactionStatus.COMPLETED) {
           product.setStockQuantity(product.getStockQuantity() + qty);
        }
      } else if (type == TransactionType.ADJUSTMENT) {
        // Hủy điều chỉnh -> Trả về như cũ (qty là phần chênh lệch)
        product.setStockQuantity(product.getStockQuantity() - qty);
      }
      productRepository.save(product);
    }

    existingTransaction.setTransactionStatus(status);
    existingTransaction.setUpdateAt(LocalDateTime.now());

    transactionRepository.save(existingTransaction);

    auditLogService.logAction("UPDATE", "Transaction", existingTransaction.getId(), "Cập nhật trạng thái phiếu giao dịch thành: " + status.name());

    return Response.builder()
        .status(200)
        .message("Cập nhật trạng thái giao dịch thành công")
        .build();
  }

}
