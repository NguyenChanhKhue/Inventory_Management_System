1. Vai trò của từng file trong JWT/Security

### SecurityConfig.java

Đây là file cấu hình tổng.
Nó nói với Spring:
/api/auth/** thì cho đi qua không cần login.
mọi API khác phải đăng nhập.
không dùng session, tức là stateless.
trước khi Spring xử lý auth chuẩn, hãy chạy AuthFilter trước.
nếu chưa đăng nhập mà vào API protected thì trả 401.
nếu đã đăng nhập nhưng không đủ quyền thì trả 403.
JwtUtils.java

Đây là file làm việc với JWT.
Nhiệm vụ:
tạo token khi login thành công
đọc email từ token
kiểm tra token còn hạn không
kiểm tra token có đúng user không

### AuthFilter.java

- Đây là file quan trọng nhất trong flow request.
- Mỗi request đi vào server, filter này chạy 1 lần.
- Nó làm các bước:
- lấy token từ header Authorization
- nếu header có dạng Bearer abcxyz... thì cắt lấy phần token
- dùng JwtUtils để đọc email trong token
- load user từ DB bằng email đó
- validate token- 
- nếu hợp lệ thì set thông tin đăng nhập vào SecurityContextHolder
- Sau bước này, Spring hiểu: user này đã đăng nhập.

### CustomUserDetailService.java

- File này dùng để lấy user từ DB theo email.
- Spring Security cần UserDetails, nên nó không dùng trực tiếp entity User, mà bọc qua AuthUser.
- AuthUser.java
- Đây là object mà Spring Security hiểu.
- Nó cung cấp:
- username = email
- password
- authorities = role của user, ví dụ ADMIN, MANAGER
- CustomAuthenticationEntryPoint.java

Xử lý trường hợp chưa đăng nhập mà đòi vào API protected.
Trả 401 Unauthorized.

### CustomAccessDenialHandler.java

Xử lý trường hợp đã đăng nhập rồi nhưng không đủ quyền.
Trả 403 Forbidden.
2. Riêng hàm getTokenFromRequest() đang làm gì

### Trong AuthFilter.java (line 59):

private String getTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
        return token.substring(7);
    }
    return null;
}
Ý nghĩa thực tế:

request gửi lên có header:
Authorization: Bearer eyJhbGciOi...
request.getHeader("Authorization") lấy cả chuỗi đó.
startsWith("Bearer ") kiểm tra đúng chuẩn bearer token.
substring(7) cắt bỏ chữ "Bearer " để lấy phần JWT thật:
eyJhbGciOi...
Nếu header không có hoặc sai format, hàm trả null.

3. Flow thực tế từ đầu đến cuối

Giả sử có user:

email: admin@gmail.com
password: 123456
role: ADMIN
User muốn gọi API:

GET /api/user/all
API này có:
UserController.java (line 27)

@PreAuthorize("hasAuthority('ADMIN')")
@GetMapping("/all")
public ResponseEntity<Response> getAllUser() {
    return ResponseEntity.ok(userService.getAllUsers());
}
Nghĩa là:

phải đăng nhập
và phải có quyền ADMIN
Bước 1: login

Client gọi:

POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@gmail.com",
  "password": "123456"
}
Vì /api/auth/login nằm trong /api/auth/**, nên SecurityConfig.java (line 41) cho đi qua không cần token.

Sau đó vào:
AuthController.java (line 28)
rồi vào:
UserServiceImpl.java (line 63)

Trong loginUser():

tìm user theo email
so sánh password người dùng nhập với password đã mã hóa trong DB
nếu đúng thì gọi:
String token = jwtUtils.generateToken(user.getEmail());
Token trả về cho client, ví dụ:

{
  "status": 200,
  "message": "User Logged in Successfully",
  "role": "ADMIN",
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "expirationTime": "6 month"
}
Bước 2: gọi API protected với token

Client lấy token đó và gọi:

GET /api/user/all
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9....
Bước 3: request đi vào AuthFilter

Trong AuthFilter.java:

getTokenFromRequest(request) lấy token từ header
jwtUtils.getUsernameFromToken(token) đọc email từ token
ở dự án bạn, email nằm trong subject
customUserDetailService.loadUserByUsername(email) lấy user từ DB
jwtUtils.isTokeValid(token, userDetails) kiểm tra:
email trong token có khớp user DB không
token có hết hạn không
Nếu mọi thứ đúng:

SecurityContextHolder.getContext().setAuthentication(authenticationToken);
Dòng này cực kỳ quan trọng.

Nó nghĩa là:

Spring Security ghi nhận request hiện tại là đã được xác thực
principal hiện tại là user đó
role hiện tại là role lấy từ DB
Bước 4: Spring kiểm tra quyền

Sau khi filter chạy xong, request vào controller.

Tại @PreAuthorize("hasAuthority('ADMIN')"), Spring nhìn vào Authentication trong SecurityContext.

Role này đến từ:
AuthUser.java (line 23)

return List.of(new SimpleGrantedAuthority(user.getRole().name()));
Nếu user có role ADMIN, request được đi tiếp vào:

userService.getAllUsers()
Nếu không phải ADMIN, Spring chặn lại và trả 403.

4. 3 tình huống thực tế dễ nhớ

Trường hợp A: không gửi token
Request:

GET /api/user/all
Điều gì xảy ra:

getTokenFromRequest() trả null
AuthFilter không set Authentication
Spring thấy endpoint này cần authenticated
gọi CustomAuthenticationEntryPoint
trả 401 Unauthorized
Trường hợp B: có token hợp lệ nhưng role không đủ
Ví dụ user là MANAGER, nhưng gọi API cần ADMIN.

Request:

GET /api/user/all
Authorization: Bearer <token-cua-manager>
Điều gì xảy ra:

AuthFilter xác thực thành công
SecurityContext có user rồi
nhưng tới @PreAuthorize("hasAuthority('ADMIN')") thì fail
gọi CustomAccessDenialHandler
trả 403 Forbidden
Trường hợp C: có token hợp lệ và đúng role
Request:

GET /api/user/all
Authorization: Bearer <token-cua-admin>
Điều gì xảy ra:

AuthFilter xác thực thành công
SecurityContext có user
@PreAuthorize("hasAuthority('ADMIN')") pass
controller chạy bình thường
trả danh sách user
5. Một sơ đồ ngắn để bạn nhớ

Client login
-> /api/auth/login
-> UserServiceImpl kiểm tra email/password
-> JwtUtils tạo token
-> trả token cho client

Client gọi API protected với Bearer token
-> AuthFilter đọc Authorization header
-> lấy token
-> JwtUtils đọc email từ token
-> CustomUserDetailService load user từ DB
-> JwtUtils validate token
-> set Authentication vào SecurityContext
-> Spring kiểm tra authenticated
-> Spring kiểm tra role qua @PreAuthorize
-> cho vào controller hoặc trả 401/403
6. Điểm rất đáng chú ý trong dự án của bạn

Dự án của bạn có AuthenticationManager bean, nhưng flow login hiện tại không dùng nó. Login đang là:

tự tìm user trong DB
tự so password
tự generate JWT
Tức là security flow của bạn là:

login: custom bằng service
authorize request: chuẩn Spring Security qua filter + security context