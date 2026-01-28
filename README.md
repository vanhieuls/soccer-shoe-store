# Soccer Shoe Store - Source Code Documentation

## 📋 Giới thiệu

Đây là dự án **Soccer Shoe Store** - Hệ thống quản lý cửa hàng bán giày bóng đá được xây dựng bằng **Spring Boot** với kiến trúc RESTful API. Dự án bao gồm các chức năng quản lý sản phẩm, đơn hàng, người dùng, thanh toán và nhiều tính năng khác.

## 🌐 Live Demo

- **🛍️ Giao diện người dùng (Customer)**: [https://vnhi-store.vercel.app](https://vnhi-store.vercel.app)
- **👨‍💼 Giao diện quản trị (Admin)**: [https://admin-sport-store.vercel.app](https://admin-sport-store.vercel.app)

## 🖼️ Giao diện hệ thống

### 🎯 Giao diện Admin

#### Dashboard Admin
<img width="915" height="436" alt="image" src="https://github.com/user-attachments/assets/48e780bc-4507-480c-9cc5-70fc8b5fee48" />

*Dashboard thống kê tổng quan doanh thu, đơn hàng theo tháng năm 2025*

#### Biểu đồ doanh thu tích lũy
<img width="915" height="464" alt="image" src="https://github.com/user-attachments/assets/8ae5b2ee-2679-4ba1-829b-58d7cb132ead" />

*Biểu đồ doanh thu tích lũy theo từng tháng trong năm 2025*

#### Quản lý đơn hàng
<img width="915" height="437" alt="image" src="https://github.com/user-attachments/assets/7ca4c553-f133-4e9c-a919-3e73a206c204" />

*Giao diện quản lý và theo dõi trạng thái đơn hàng*

#### Chi tiết đơn hàng
<img width="915" height="461" alt="image" src="https://github.com/user-attachments/assets/7fad4404-089b-4698-a2e6-49004e238941" />

*Thông tin chi ti���t đơn hàng bao gồm thông tin khách hàng, sản phẩm và thanh toán*

#### Quản lý sản phẩm
<img width="915" height="464" alt="image" src="https://github.com/user-attachments/assets/d7e7178b-0ae0-4254-9bbf-e291f2b9208f" />

*Giao diện quản lý sản phẩm với các tính năng sắp xếp, tìm kiếm và thao tác CRUD*

#### Quản lý người dùng
<img width="915" height="465" alt="image" src="https://github.com/user-attachments/assets/d4646674-8aa2-48af-8098-0a5b0949d2db" />

*Giao diện quản lý người dùng với thông tin tài khoản, trạng thái và ngày sinh*

#### Quản lý Admin & Nhân viên
<img width="915" height="460" alt="image" src="https://github.com/user-attachments/assets/f015569d-c5b2-4850-bafd-0340593ca35c" />

*Giao diện quản lý tài khoản quản trị viên và nhân viên hệ thống*

#### Quản lý Bộ Sưu Tập
<img width="915" height="434" alt="image" src="https://github.com/user-attachments/assets/c570a2bb-081c-43b8-a831-c06a59c4456e" />

*Giao diện quản lý các bộ sưu tập sản phẩm như SELECTAL VICTORY PACK, AUDACITY PACK, FUTSAL BOOT, PRISM PACK*

#### Quản lý Voucher
<img width="915" height="435" alt="image" src="https://github.com/user-attachments/assets/e4f2fab6-f7a1-4572-843a-836978f8b5a6" />

*Giao diện quản lý voucher giảm giá với thông tin mã, phần trăm giảm, điểm yêu cầu, thời gian và trạng thái*

#### Chỉnh sửa Voucher
<img width="915" height="434" alt="image" src="https://github.com/user-attachments/assets/52878a32-fe27-4eb4-a26a-535fad7f74d8" />

*Form chỉnh sửa thông tin voucher bao gồm mã, giá trị giảm, giới hạn sử dụng, điểm yêu cầu và ngày hết hạn*

#### Cập nhật trạng thái đơn hàng
<img width="915" height="466" alt="image" src="https://github.com/user-attachments/assets/65f925c1-d27b-4e5a-b0d0-aa5c2d581ab5" />

*Modal cập nhật trạng thái đơn hàng với các tùy chọn trạng thái giao hàng (SHIPPING, DELIVERED, CANCELLED)*

### 🛒 Giao diện Người dùng (Customer)

#### Trang chủ
<img width="915" height="465" alt="image" src="https://github.com/user-attachments/assets/8713a799-0e58-48a1-b168-f69f7ae6df7a" />

*Trang chủ với banner quảng cáo giày đá bóng sân cỏ nhân tạo từ các thương hiệu Nike, Adidas, Puma, Mizuno, Joma, Lotto, Zocker*

#### Trang sản phẩm
<img width="915" height="437" alt="image" src="https://github.com/user-attachments/assets/166d4be8-3433-4f01-a3af-b26def1b98cf" />

*Trang danh sách sản phẩm với bộ lọc theo danh mục, giá, thương hiệu, khuyến mãi và đánh giá. Hiển thị 12 sản phẩm với giá và nút thêm vào giỏ*

#### Chi tiết sản phẩm
<img width="915" height="462" alt="image" src="https://github.com/user-attachments/assets/38a56f38-93aa-4f85-93af-5af5b4021a39" />

*Trang chi tiết sản phẩm ADIDAS PREDATOR 25 LEAGUE với hình ảnh, giá 2.000đ, chọn kích thước (39-50), số lượng, nút thêm giỏ hàng và mua ngay, có nút chia sẻ mạng xã hội*

#### Giỏ hàng
<img width="915" height="436" alt="image" src="https://github.com/user-attachments/assets/d85bb53a-d03e-438d-bd7e-ee7abdd5bdd9" />

*Giỏ hàng hiển thị sản phẩm đã chọn, số lượng, giá, tạm tính, phí vận chuyển (20.000đ), tổng cộng (22.000đ) và nút tiến hành thanh toán*

#### Quản lý đơn hàng của khách
<img width="1877" height="992" alt="Screenshot 2026-01-28 111442" src="https://github.com/user-attachments/assets/24964afd-d0e3-4365-ab62-62db28a31367" />


*Trang quản lý đơn hàng cá nhân với các tab: Chờ xác nhận, Chờ xác nhận hủy, Đã xác nhận, Đang xử lý, Đang giao, Lịch sử đơn hàng. Popup chi tiết đơn hàng hiển thị mã đơn, ngày đặt, phí vận chuyển, ghi chú, sản phẩm và tổng cộng*

#### Thanh toán
<img width="915" height="508" alt="image" src="https://github.com/user-attachments/assets/f53774c8-89b1-4aed-860d-af7901a4f787" />

*Trang thanh toán với các bước: Trang chủ → Giỏ hàng → Thanh toán → Xác nhận thanh toán. Hiển thị tóm tắt đơn hàng, địa chỉ giao hàng, voucher khả dụng (SPRING2025, SUMMER2208, MPB2025), phương thức thanh toán và tổng tiền 22.000đ*

#### Thanh toán PayPal
<img width="915" height="491" alt="image" src="https://github.com/user-attachments/assets/beceb715-ce19-4aeb-b81c-6ba60ad6b85c" />

*Trang thanh toán PayPal sandbox với địa chỉ giao hàng tới Hiếu Nguyễn, TP HCM. Hiển thị số tiền $0.88, các phương thức thanh toán: PayPal balance, Visa ****8578, và nút "Continue to Review Order"*

#### Thông tin cá nhân
<img width="915" height="433" alt="image" src="https://github.com/user-attachments/assets/38af36d2-e361-426a-a108-e4a8e9bd9de6" />

*Trang thông tin cá nhân hiển thị avatar, tên "Hieu Van", email vanhieuls7@gmail.com, nút "Chỉnh sửa thông tin". Các thông tin chi tiết: Tên đăng nhập (hieu@1234), Họ và tên (Hieu Van), Giới tính (Nam), Ngày sinh, Email (vanhieuls7@gmail.com), Số điện thoại (0123456789), Địa chỉ (Đặc Khu Ủy Sơn)*

#### Chia sẻ sản phẩm lên Facebook
<img width="915" height="485" alt="image" src="https://github.com/user-attachments/assets/f2b46c11-2d7a-4040-966c-55610f8bd501" />

*Modal chia sẻ sản phẩm ADIDAS F50 PRO TF - JH7664 - CAM/XANH giá 2.000.000đ lên Facebook. Hiển thị hình ảnh sản phẩm, caption "Chúng ơi, bạn đánh giá gì thế?", nút "Thêm vào bài viết của của bạn" và nút "Tiếp" màu xanh*

#### Voucher của người dùng
<img width="915" height="465" alt="image" src="https://github.com/user-attachments/assets/1135b980-a589-4661-ac2b-50da25996f43" />

*Trang "Voucher Của Bạn" với tiêu đề "Đổi điểm tích lũy để nhận ưu đãi hấp dẫn". Card "Voucher của bạn" hiển thị điểm tích lũy: 128 điểm. Có 2 tab: "Voucher Có Thể Đổi" (trống) và "Voucher Của Tôi" hiển thị SUMMER2025 (giảm 40%, từ 500.000đ, NSX: 2025-12-30, HSD: 2025-08-01, còn hạn 0/15) và SUMMER2208 (giảm 20%, từ 500.000đ, số hữu)*

## 🏗️ Cấu trúc thư mục

```
src/
├── main/
│   ├── java/com/dailycodework/shopping_cart/
│   │   ├── Configuration/      # Cấu hình ứng dụng
│   │   ├── Controller/          # REST API Controllers
│   │   ├── DTO/                 # Data Transfer Objects
│   │   ├── Data/                # Dữ liệu khởi tạo
│   │   ├── Entity/              # JPA Entities
│   │   ├── Enum/                # Enumerations
│   │   ├── Exception/           # Exception Handling
│   │   ├── Helper/              # Các class hỗ trợ
│   │   ├── Mapper/              # MapStruct Mappers
│   │   ├── Repository/          # JPA Repositories
│   │   ├── Service/             # Business Logic
│   │   ├── Validation/          # Custom Validators
│   │   └── ShoppingCartApplication.java
│   └── resources/
│       ├── templates/           # HTML Templates
│       ├── static/              # Static resources
│       └── application.properties
└── test/                        # Unit Tests
```

## 📦 Chi tiết các package

### 1. **Configuration**
Chứa các file cấu hình Spring Boot:
- **ApplicationConfig.java**: Cấu hình chung cho ứng dụng, khởi tạo admin user
- **WebConfig.java**: Cấu hình CORS, interceptors
- **SecurityConfig**: Cấu hình bảo mật JWT

### 2. **Controller**
REST API endpoints:
- **AuthenticationController**: Đăng nhập, đăng ký, đăng xuất
- **CategoryController**: Quản lý danh mục sản phẩm
- **ProductController**: Quản lý sản phẩm
- **OrderController**: Quản lý đơn hàng
- **UserController**: Quản lý người dùng
- **VoucherController**: Quản lý voucher giảm giá
- **AddressController**: Quản lý địa chỉ giao hàng
- **ImageController**: Upload và quản lý hình ảnh
- **CollectionController**: Quản lý bộ sưu tập sản phẩm
- **TestController**: Test thanh toán PayPal

### 3. **DTO (Data Transfer Objects)**
```
DTO/
├── Dto/                 # DTOs chung
├── Request/             # Request DTOs
└── Response/            # Response DTOs
    └── ApiResponse.java # Cấu trúc response chuẩn
```

**ApiResponse Structure:**
```java
{
    "code": 100,
    "message": "Success",
    "result": { ... }
}
```

### 4. **Entity**
JPA Entities ánh xạ với database:
- **User**: Thông tin người dùng
- **Product**: Sản phẩm
- **Category**: Danh mục
- **Order**: Đơn hàng
- **OrderItem**: Chi tiết đơn hàng
- **Cart**: Giỏ hàng
- **Voucher**: Phiếu giảm giá
- **Address**: Địa chỉ
- **Review**: Đánh giá sản phẩm
- **Role**: Vai trò người dùng
- **Collection**: Bộ sưu tập sản phẩm

### 5. **Enum**
Các hằng số enum:

#### **Roles.java**
```java
public enum Roles {
    ROLE_ADMIN,   // Quản trị viên
    ROLE_USER,    // Khách hàng
    ROLE_STAFF    // Nhân viên
}
```

#### **OderStatus.java**
```java
public enum OderStatus {
    PENDING,           // Chờ xử lý
    CONFIRMED,         // Đã xác nhận
    PROCESSING,        // Đang xử lý
    SHIPPING,          // Đang giao hàng
    DELIVERED,         // Đã giao
    CANCELLED,         // Đã hủy
    CANCEL_REQUESTED   // Yêu cầu hủy
}
```

#### **PaymentStatus.java**
```java
public enum PaymentStatus {
    UNPAID,   // Chưa thanh toán
    PAID,     // Đã thanh toán
    FAILED    // Thất bại
}
```

### 6. **Exception**
Xử lý exception tập trung:
- **ErrorCode.java**: Định nghĩa các mã lỗi
- **GlobalExceptionHandler**: Xử lý exception toàn cục
- **AppException**: Custom exception class

**Error Codes Sample:**
```java
PRODUCT_NOT_EXIST (100,"Product not exist", HttpStatus.NOT_FOUND)
USER_NOT_FOUND (101,"User not found", HttpStatus.NOT_FOUND)
VOUCHER_EXPIRED(129,"Voucher expired", HttpStatus.BAD_REQUEST)
ACCOUNT_LOCKED(150,"Account is locked", HttpStatus.UNAUTHORIZED)
```

### 7. **Helper**
Các class tiện ích:
- **ProfanityFilter**: Lọc từ ngữ nhạy cảm trong review/comment
  - Chặn các từ tục tĩu (tiếng Việt & tiếng Anh)
  - Chặn link đến các trang thương mại điện tử khác
  - Chặn link rút gọn và spam
- **JwtUtil**: Xử lý JWT token
- **EmailService**: Gửi email

### 8. **Mapper**
MapStruct mappers để chuyển đổi giữa Entity và DTO:
- **UserMapper**: User ↔ UserRequest/UserResponse
- **ProductMapper**: Product ↔ ProductDTO
- **OrderMapper**: Order ↔ OrderDTO
- **ReviewMapper**: Review ↔ ReviewDTO
- **VoucherMapper**: Voucher ↔ VoucherDTO

### 9. **Repository**
JPA Repositories:
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    long countNewUsersByCreatedAtBetween(String startDate, String endDate);
    long countUsersIsActive();
}
```

### 10. **Service**
Business logic layer:
```
Service/
├── Interface/           # Service interfaces
│   ├── IUser
│   ├── IProduct
│   ├── IOrder
│   ├── IVoucher
│   ├── IAddress
│   └── ...
└── ImpInterface/        # Service implementations
    ├── ImpUser
    ├── ImpProduct
    ├── ImpOrder
    ├── ImpPayment       # PayPal integration
    ├── ImpVoucher
    └── ...
```

### 11. **Validation**
Custom validators:
- Email validation
- Phone number validation
- Password strength validation
- Date validation

## 🔐 Authentication & Authorization

### JWT Token Flow
1. User đăng nhập → Nhận Access Token & Refresh Token
2. Mỗi request gửi kèm `Authorization: Bearer {token}`
3. Token được validate qua Security Filter
4. Phân quyền dựa trên Role (ADMIN/USER/STAFF)

### Security Endpoints
```
POST /auth/login       - Đăng nhập
POST /auth/signup      - Đăng ký
POST /auth/logout      - Đăng xuất
PUT  /auth/resend-email - Gửi lại email xác thực
```

## 💳 Payment Integration

### PayPal Integration
- **ImpPayment.java**: Service xử lý thanh toán PayPal
- Support methods: PayPal, Credit Card
- Flow: Create Payment → Execute Payment → Verify
- Sandbox mode cho development
- Chuyển đổi tỷ giá VND sang USD tự động

### Payment Endpoints
```
GET  /payments/           - Payment form
POST /payments/create     - Tạo payment
GET  /payments/success    - Payment success callback
GET  /payments/cancel     - Payment cancel callback
GET  /payments/error      - Payment error callback
```

### Payment Methods
- **COD (Cash on Delivery)**: Thanh toán khi nhận hàng
- **PAYOS**: Cổng thanh toán trực tuyến
- **PayPal**: Thanh toán quốc tế

## 📊 Features

### 1. Quản lý sản phẩm
- CRUD sản phẩm
- Upload nhiều ảnh
- Quản lý danh mục
- Tìm kiếm, lọc, sắp xếp sản phẩm
- Quản lý kích thước và số lượng tồn kho
- Quản lý thương hiệu (Adidas, Nike, Puma, Mizuno, Joma, Lotto, Zocker)
- Chia sẻ sản phẩm lên Facebook

### 2. Quản lý đơn hàng
- Tạo đơn hàng
- Theo dõi trạng thái (Pending → Confirmed → Processing → Shipping → Delivered)
- Cập nhật trạng thái đơn
- Hủy đơn hàng / Yêu cầu hủy đơn
- Xem lịch sử đơn hàng
- Thống kê đơn hàng theo trạng thái
- Tìm kiếm đơn hàng theo mã, ngày

### 3. Hệ thống voucher
- Tạo mã giảm giá (%, số tiền cố định)
- Áp dụng voucher cho đơn hàng
- Kiểm tra hợp lệ (thời hạn, số lượng, điều kiện)
- Quản lý số lượng sử dụng
- Voucher theo người dùng
- **Đổi điểm tích lũy lấy voucher**
- Hiển thị voucher có thể đổi và voucher đã sở hữu
- Theo dõi số lượng voucher đã dùng/tổng số

### 4. Đánh giá sản phẩm
- Đánh giá 1-5 sao
- Comment review
- Lọc từ ngữ nhạy cảm tự động
- Chặn link spam

### 5. Quản lý người dùng
- Quản lý khách hàng
- Quản lý Admin & Staff
- Khóa/Mở khóa tài khoản
- Xác thực email
- Phân quyền theo Role
- Tìm kiếm người dùng
- Hệ thống điểm tích lũy

### 6. Quản lý Bộ Sưu Tập
- Tạo và quản lý các bộ sưu tập sản phẩm
- Thêm/Xóa sản phẩm khỏi bộ sưu tập
- Hiển thị mô tả bộ sưu tập
- Các bộ sưu tập: SELECTAL VICTORY PACK, AUDACITY PACK, FUTSAL BOOT, PRISM PACK

### 7. Thống kê & Báo cáo
- Dashboard doanh thu
- Biểu đồ theo tháng/năm
- Sản phẩm bán chạy
- Thống kê khách hàng mới
- Doanh thu tích lũy

### 8. Social Integration
- Chia sẻ sản phẩm lên Facebook
- Share sản phẩm trên LinkedIn, Twitter
- Tích h��p các nút social share

## 🛠️ Technologies

- **Framework**: Spring Boot 3.x
- **Database**: MySQL/PostgreSQL
- **Cache**: Redis
- **Security**: Spring Security + JWT
- **ORM**: Hibernate/JPA
- **Mapping**: MapStruct
- **Payment**: PayPal SDK
- **Email**: JavaMailSender
- **Validation**: Hibernate Validator
- **Cloud Storage**: Cloudinary (Image upload)
- **Documentation**: Swagger/OpenAPI
- **Deployment**: Vercel (Frontend), Railway/Render (Backend)

## 🚀 Getting Started

### Prerequisites
```bash
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Redis Server
- Node.js 18+ (for frontend)
```

### Configuration
Cấu hình trong `application.properties`:
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/soccer_store
spring.datasource.username=root
spring.datasource.password=your_password

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# PayPal
paypal.mode=sandbox
paypal.client.id=your-client-id
paypal.client.secret=your-client-secret

# Redis
spring.redis.host=localhost
spring.redis.port=6379

# Cloudinary
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret
```

### Run Application
```bash
# Clone repository
git clone https://github.com/vanhieuls/soccer-shoe-store.git

# Navigate to project
cd soccer-shoe-store

# Install dependencies
mvn clean install

# Run application
mvn spring-boot:run
```

Application chạy tại: `http://localhost:8080`

**Last Updated**: January 2026
