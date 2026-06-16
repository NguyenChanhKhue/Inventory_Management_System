# Tài Liệu Chức Năng và Flow Hệ Thống Quản Lý Kho (Inventory Management System)

Dựa trên mã nguồn Backend, hệ thống quản lý kho của bạn hiện tại có các chức năng và luồng hoạt động (flow) chính như sau:

## 1. Phân quyền và Xác thực (Authentication & Authorization)
Hệ thống sử dụng bảo mật JWT (JSON Web Token) kết hợp với OTP qua Email.
- **Đăng ký / Đăng nhập:** Người dùng đăng ký tài khoản và đăng nhập để nhận Token.
- **Quên mật khẩu:** Gửi mã xác thực (OTP) 6 số về Email để người dùng tự đổi lại mật khẩu.
- **Phân quyền (Role):** Có 2 quyền chính:
  - `ADMIN`: Quản trị viên cấp cao, được phép thiết lập toàn bộ danh sách Hàng hóa, Danh mục, và Nhà cung cấp.
  - `MANAGER`: Quản lý kho, chỉ tập trung vào việc tạo các giao dịch (Nhập, Xuất, Trả hàng) và xem báo cáo.

## 2. Quản lý Danh mục (Category Management)
- Thêm, sửa, xóa và hiển thị danh sách các nhóm hàng (ví dụ: Điện thoại, Laptop, Phụ kiện...). Mục đích để phân loại và dễ dàng tìm kiếm sản phẩm.

## 3. Quản lý Nhà cung cấp (Supplier Management)
- Quản lý thông tin đối tác cấp hàng (Tên, Số điện thoại/Thông tin liên hệ, Địa chỉ). Điều này rất quan trọng để lưu vết khi nhập hàng (Nhập của ai? Ở đâu?).

## 4. Quản lý Sản phẩm (Product Management)
Hệ thống quản lý thông tin chi tiết từng mặt hàng trong kho, bao gồm:
- **Thông tin cơ bản:** Tên, Giá, Mô tả, Hình ảnh.
- **Mã SKU:** Mã định danh duy nhất cho từng sản phẩm giúp quản lý chính xác.
- **Tồn kho (Stock Quantity):** Quản lý tự động. Người dùng không cần phải tự cộng trừ bằng tay mà số lượng này sẽ tự thay đổi dựa vào các luồng Giao dịch (Transaction) ở dưới.

---

## 5. FLOW CHÍNH CỦA HỆ THỐNG: Luồng Giao Dịch Kho (Transaction Flow)
Đây là "trái tim" của phần mềm. Mọi sự thay đổi về số lượng hàng hóa đều phải thông qua giao dịch để lưu lại lịch sử (Ai làm? Lúc nào? Bao nhiêu?). Hệ thống chia ra 3 luồng chính:

### 5.1. Luồng Nhập hàng (Purchase / Receive Inventory)
- **Flow:** Quản lý chọn Sản phẩm + chọn Nhà Cung Cấp + nhập Số lượng.
- **Hệ thống xử lý:** Tạo ra 1 lịch sử giao dịch loại `PURCHASE`. Sau đó **cộng (+)** số lượng nhập vào tồn kho (`stock_quantity`) của sản phẩm đó.

### 5.2. Luồng Xuất bán hàng (Sell)
- **Flow:** Quản lý chọn Sản phẩm muốn bán + nhập Số lượng.
- **Hệ thống xử lý:** Đầu tiên sẽ kiểm tra xem trong kho còn đủ hàng để bán không (nếu số lượng nhập > tồn kho thì báo lỗi). Nếu hợp lệ, hệ thống tạo ra 1 giao dịch loại `SELL` và **trừ (-)** số lượng bán khỏi tồn kho.

### 5.3. Luồng Hoàn trả (Return)
- **Flow:** Khách hàng mang hàng đến trả lại. Quản lý chọn Sản phẩm + nhập Số lượng trả.
- **Hệ thống xử lý:** Tạo giao dịch loại `RETURN` và **cộng (+)** ngược lại số lượng vào trong tồn kho để có thể bán tiếp.

---

## 6. Báo cáo & Thống kê (Dashboard)
Toàn bộ các luồng giao dịch trên được tổng hợp tại trang Dashboard để người chủ có cái nhìn tổng quan:
- **Thống kê theo biểu đồ (Chart):** Dữ liệu được tính toán theo Ngày/Tháng/Năm.
- **Các thông số theo dõi:** Xem được "Tổng số lần giao dịch", "Số lượng hàng hóa biến động", và "Tổng doanh thu/giá trị (Amount)" sinh ra từ các giao dịch đó.

> **Đánh giá tổng quan:** 
> Flow hệ thống rất chuẩn mực và chặt chẽ đối với một phần mềm Inventory Management. Dữ liệu chạy vòng tròn từ khâu **Thiết lập** (Danh mục, Đối tác, Hàng) ➡️ **Phát sinh biến động** (Nhập/Xuất) ➡️ **Trả về kết quả** (Báo cáo tổng hợp / Tồn kho hiện tại).
