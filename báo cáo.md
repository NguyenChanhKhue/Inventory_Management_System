# Báo Cáo Phân Tích Và Thiết Kế Hệ Thống Quản Lý Kho (IMS)

Tài liệu này mô tả chi tiết quá trình phân tích, thiết kế và lên kế hoạch triển khai cho Hệ thống Quản lý Kho (Inventory Management System - IMS).

---

## 1. Define Project Vision

### Tầm nhìn dự án (Project Vision)
Xây dựng một hệ thống phần mềm quản lý kho nội bộ hiện đại, tập trung, giúp tự động hóa và số hóa các quy trình quản lý hàng hóa (nhập, xuất, hoàn trả, kiểm kê). Hệ thống giúp ngăn chặn sai sót dữ liệu, cung cấp cái nhìn tổng quan về tình trạng tồn kho theo thời gian thực và hỗ trợ trích xuất báo cáo nhanh chóng, chính xác.

### Nhận diện Người dùng và Các bên liên quan (Users and Stakeholders)
1. **Quản trị viên (Admin):** 
   - Có toàn quyền kiểm soát hệ thống.
   - Quản lý tài khoản và phân quyền cho nhân viên (MANAGER).
   - Theo dõi nhật ký hoạt động (Audit Logs) của toàn bộ hệ thống để đảm bảo tính minh bạch.
   - Toàn quyền xem và chỉnh sửa dữ liệu Hệ thống (Sản phẩm, Danh mục, Nhà cung cấp).
2. **Quản lý Kho (Manager):**
   - Thực hiện các nghiệp vụ kho hàng ngày: lập phiếu nhập kho, xuất kho, hoàn trả hàng cho nhà cung cấp và kiểm kê điều chỉnh.
   - Xem bảng báo cáo (Dashboard) tổng quan, sơ đồ kho và lịch sử chứng từ để đưa ra quyết định nhập/xuất hàng hóa phù hợp.

---

## 2. Use Case Modeling (Mô hình Use Case)

Dưới đây là các nhóm chức năng chính (Use Cases) của hệ thống:

```mermaid
flowchart LR
    Manager((Quản lý kho))
    Admin((Admin))

    %% Khung giới hạn hệ thống
    subgraph System [Hệ thống Quản lý Kho IMS]
        direction TB
        
        UC_Login([Đăng nhập])
        
        %% Nhóm Tổng quan & Nghiệp vụ
        UC_Dashboard([Xem Báo cáo Tổng quan])
        UC_Map([Xem Sơ đồ Kho])
        UC_Profile([Cập nhật Hồ sơ cá nhân])
        
        UC_Purchase([Tạo Phiếu Nhập Kho])
        UC_ExportTx([Tạo Phiếu Xuất Kho])
        UC_Return([Tạo Phiếu Hoàn Trả])
        UC_Inventory([Thực hiện Kiểm kê Tồn kho])
        
        UC_History([Xem Lịch sử Giao dịch])
        UC_ExportExcel([Xuất file Excel])
        
        %% Nhóm Quản trị (Dữ liệu hệ thống & Phân quyền)
        UC_ProductList([Xem danh sách Sản phẩm])
        UC_ManageCategory([Quản lý Danh mục])
        UC_ManageSupplier([Quản lý Nhà cung cấp])
        UC_ManageProduct([Thêm/Sửa Sản phẩm])
        
        UC_ManageUser([Quản lý Người dùng])
        UC_AuditLog([Xem Nhật ký Hệ thống])
    end

    %% Admin kế thừa toàn bộ quyền của Manager
    Admin ==>|<<generalize>>| Manager

    %% Các quan hệ Include / Extend
    UC_Dashboard -.->|<<include>>| UC_Login
    UC_Purchase -.->|<<include>>| UC_Login
    UC_ExportTx -.->|<<include>>| UC_Login
    UC_Return -.->|<<include>>| UC_Login
    UC_Inventory -.->|<<include>>| UC_Login
    UC_History -.->|<<include>>| UC_Login
    UC_ManageUser -.->|<<include>>| UC_Login

    UC_ExportExcel -.->|<<extend>>| UC_History

    %% Nối Manager với chức năng của Manager
    Manager --- UC_Dashboard
    Manager --- UC_Map
    Manager --- UC_Profile
    Manager --- UC_ProductList
    Manager --- UC_Purchase
    Manager --- UC_ExportTx
    Manager --- UC_Return
    Manager --- UC_Inventory
    Manager --- UC_History

    %% Nối Admin với chức năng độc quyền của Admin
    Admin --- UC_ManageCategory
    Admin --- UC_ManageSupplier
    Admin --- UC_ManageProduct
    Admin --- UC_ManageUser
    Admin --- UC_AuditLog
```

---

## 3. Process Modeling (Mô hình Quy trình)

Quy trình nghiệp vụ lõi: **Xử lý Phiếu Giao Dịch (Nhập/Xuất/Hoàn Trả)**

```mermaid
stateDiagram-v2
    [*] --> KhoiTao: Lập phiếu mới
    KhoiTao --> PENDING: Chờ xử lý
    
    PENDING --> PROCESSING: Đang lấy hàng/Vận chuyển
    PENDING --> CANCELLED: Hủy phiếu
    
    PROCESSING --> COMPLETED: Giao dịch thành công
    PROCESSING --> CANCELLED: Lỗi / Hủy ngang
    
    COMPLETED --> CapNhatTonKho: (Tự động cộng/trừ SP)
    CapNhatTonKho --> [*]
    
    CANCELLED --> [*]: (Không thay đổi tồn kho)
```

---

## 4. Tổng quan kiến trúc

### Trích xuất các yếu tố kiến trúc
- **Tính sẵn sàng (Availability) & Khả năng mở rộng (Scalability):** Hệ thống được module hóa (tách biệt Frontend - Backend) giúp dễ dàng mở rộng.
- **Tính bảo mật (Security):** Phân quyền truy cập cứng (RBAC - Role Based Access Control) và xác thực không trạng thái (Stateless) bằng token.
- **Trải nghiệm người dùng (UX/UI):** Giao diện Single Page Application (SPA) mượt mà, phản hồi tức thời.

### Chọn Stack công nghệ
- **Frontend:** ReactJS (Vite), Axios, Recharts (Vẽ biểu đồ), CSS thuần/Module.
- **Backend:** Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security (JWT).
- **Database:** MySQL 8.
- **Deployment:** Docker & Docker Compose.

### Xác định kiến trúc hệ thống
Hệ thống tuân theo kiến trúc **Client-Server** kết hợp **N-Tier (N-lớp)** ở phía Backend.
1. **Presentation Layer (Frontend):** Ứng dụng React chạy trên trình duyệt.
2. **Controller Layer (Backend API):** Chặn và điều hướng các HTTP Request.
3. **Service Layer (Backend Business):** Xử lý logic nghiệp vụ (cộng trừ tồn kho, tính toán thống kê).
4. **Data Access Layer (Backend Repository):** Giao tiếp trực tiếp với MySQL database.

---

## 5. Thiết kế kiến trúc chi tiết

### Sơ đồ thành phần (Component Diagram)
```mermaid
graph TD
    subgraph Frontend [ReactJS]
        UI[UI Components]
        Router[React Router]
        ApiService[Axios API Client]
        UI --> Router --> ApiService
    end

    subgraph Backend [Spring Boot]
        Filter[JWT Security Filter]
        Controllers[REST Controllers]
        Services[Business Logic Services]
        Repos[JPA Repositories]
        Filter --> Controllers --> Services --> Repos
    end

    subgraph Database
        MySQL[(MySQL Server)]
    end

    ApiService -- "JSON over HTTP" --> Filter
    Repos -- "Hibernate" --> MySQL
```

### Đặc tả API (API Specification)
Một số API cốt lõi:
- `POST /api/auth/login`: Xác thực và trả về JWT Token.
- `GET /api/transactions`: Lấy lịch sử giao dịch (hỗ trợ lọc theo Keyword).
- `POST /api/transactions/purchase`: Tạo phiếu nhập kho mới.
- `POST /api/transactions/sell`: Tạo phiếu xuất kho.
- `PUT /api/transactions/{id}`: Cập nhật trạng thái phiếu.
- `GET /api/dashboard/summary`: Lấy thông tin tổng quan Dashboard.

### Thiết kế cơ sở dữ liệu vật lý (ERD)

Cấu trúc cơ sở dữ liệu quan hệ gồm các bảng chính và mối liên kết:

```mermaid
erDiagram
    USERS ||--o{ TRANSACTIONS : "thực hiện"
    CATEGORIES ||--|{ PRODUCTS : "phân loại"
    SUPPLIERS ||--o{ TRANSACTIONS : "cung cấp / nhận hàng"
    PRODUCTS ||--o{ TRANSACTIONS : "chi tiết phiếu"

    USERS {
        Long id PK
        String email
        String password
        String name
        String phone
        String role
        String avatarUrl
    }
    
    CATEGORIES {
        Long id PK
        String name
    }
    
    SUPPLIERS {
        Long id PK
        String name
        String contactInfo
        String address
    }
    
    PRODUCTS {
        Long id PK
        String sku UK
        String name
        int stockQuantity
        Decimal importPrice
        String unit
        String location
        int minStock
        DateTime expDateTime
        boolean isActive
        Long category_id FK
    }
    
    TRANSACTIONS {
        Long id PK
        int totalProduct
        Decimal totalPrice
        String transactionType
        String transactionStatus
        String note
        DateTime createAt
        Long user_id FK
        Long product_id FK
        Long supplier_id FK
    }
```

### Thiết kế bảo mật
- **Authentication:** Sử dụng JSON Web Token (JWT). Token được cấp khi đăng nhập và đính kèm vào Header `Authorization: Bearer <token>` trong mọi request tiếp theo.
- **Authorization:** Chỉ user có Role `ADMIN` mới được gọi các API truy cập Logs, Quản lý người dùng và Tạo người dùng mới.
- **Data Protection:** Mật khẩu người dùng được băm (Hash) bằng thuật toán `BCrypt` trước khi lưu vào DB.

---

## 6. Thiết kế lớp & Hành vi

### Sơ đồ Lớp (Class Diagram)
Mô tả kiến trúc các Model trong Backend (Java/Spring Boot) và quan hệ liên kết (Associations) giữa chúng:

```mermaid
classDiagram
    direction TB
    
    class User {
        -Long id
        -String email
        -String password
        -String name
        -String phone
        -UserRole role
        -LocalDateTime createAt
    }
    
    class Category {
        -Long id
        -String name
        +List~Product~ products
    }
    
    class Supplier {
        -Long id
        -String name
        -String contactInfo
        -String address
    }
    
    class Product {
        -Long id
        -String sku
        -String name
        -int stockQuantity
        -BigDecimal importPrice
        -String unit
        -String location
        -int minStock
        -Category category
    }
    
    class Transaction {
        -Long id
        -int totalProduct
        -BigDecimal totalPrice
        -TransactionType transactionType
        -TransactionStatus transactionStatus
        -String note
        -LocalDateTime createAt
        -User user
        -Product product
        -Supplier supplier
    }

    Category "1" *-- "*" Product : contains
    User "1" <-- "*" Transaction : created by
    Product "1" <-- "*" Transaction : details
    Supplier "1" <-- "*" Transaction : involves
```

### Sơ đồ trình tự (Sequence Diagram) - Luồng Xuất Kho
```mermaid
sequenceDiagram
    participant User as Nhân viên Kho
    participant FE as React Frontend
    participant API as Transaction Controller
    participant Svc as Transaction Service
    participant DB as MySQL Database

    User->>FE: Điền Form Xuất Kho
    FE->>API: POST /api/transactions/sell
    API->>Svc: Xác thực & Xử lý Xuất
    Svc->>DB: Kiểm tra tồn kho
    DB-->>Svc: Hợp lệ
    Svc->>DB: Lưu Transaction (PENDING)
    Svc->>DB: Cập nhật tồn kho (Nếu Status = COMPLETED)
    DB-->>Svc: Thành công
    Svc-->>API: Success Response
    API-->>FE: HTTP 200 OK
    FE-->>User: Hiển thị thông báo
```

### Mockup giao diện
Các màn hình chính đã được thiết kế:
1. **Layout chung:** Sidebar cố định bên trái (chia 4 nhóm chức năng), nội dung chính hiển thị bên phải.
2. **Bảng báo cáo (Dashboard):** Hiển thị 4 thẻ thông tin tổng quan, danh sách cảnh báo hàng sắp hết và biểu đồ đường Nhập/Xuất.
3. **Quản lý danh sách (Grid):** Thanh tìm kiếm và bộ lọc thời gian đặt trên Table, các nút tính năng Thêm/Sửa/Xuất Excel sắp xếp trực quan.

---

## 7. Lập kế hoạch triển khai

### Chiến lược kiểm thử (Testing Strategy)
- **Unit Testing (Kiểm thử mức đơn vị):** Kiểm thử các tính toán nghiệp vụ lõi ở Backend (cộng trừ tồn kho, tính tổng tiền, bắt lỗi exception nếu tồn kho không đủ).
- **Integration Testing (Kiểm thử tích hợp):** Đảm bảo API Controller gọi đúng Service và ghi/đọc DB chính xác. Thử nghiệm kết nối React <-> Spring Boot.
- **Manual / User Acceptance Testing (UAT):** Kiểm thử thủ công kịch bản thực tế (luồng Nhập -> Xuất -> Trả hàng -> Đổi trạng thái -> Kiểm tra Dashboard).

### Kế hoạch triển khai (Deployment Plan)
Hệ thống sử dụng chiến lược đóng gói **Containerization**:
1. **Môi trường Server:** Yêu cầu máy chủ (Linux/Windows) có cài đặt Docker & Docker Compose.
2. **Tích hợp Services:** 
   - `inventory-db` (MySQL)
   - `inventory-backend` (Spring Boot JAR chạy trên JRE 17)
   - `inventory-frontend` (Bản build Vite chạy trên Nginx)
3. **Thao tác triển khai:**
   - Cấu hình file `.env` (chứa DB URL, credentials, JWT Secret).
   - Chạy lệnh `docker-compose up -d --build`.
   - Mạng nội bộ Docker tự động liên kết các container và hiển thị Frontend ra ngoài.
4. **Bảo trì:** Logs ghi lại tự động. Update từng module bằng cách rebuild độc lập để không làm gián đoạn hệ thống.
