# Identity Service

Đây là dự án backend xây dựng hệ thống quản lý danh tính (Identity Service) cung cấp các chức năng xác thực (Authentication) và phân quyền (Authorization).

Dự án áp dụng mô hình phối hợp giữa Con người (Lập trình viên) và Trí tuệ nhân tạo (AI) nhằm tối ưu hóa hiệu suất và chất lượng mã nguồn theo quy trình khép kín:

Giai đoạn Hoạch định (Planning): AI chịu trách nhiệm phác thảo và khởi tạo tài liệu kế hoạch (Plan file). Lập trình viên đóng vai trò cố vấn, hỗ trợ định hướng nghiệp vụ và trực tiếp phê duyệt (Review) kế hoạch cuối cùng.

Giai đoạn Triển khai (Implementation): Lập trình viên trực tiếp hiện thực hóa mã nguồn (Implement code) cốt lõi của hệ thống dựa trên kế hoạch đã thống nhất để đảm bảo tính tối ưu và bảo mật.

Giai đoạn Kiểm thử (Testing): AI tự động hóa việc viết các kịch bản kiểm thử đơn vị (Unit Test bằng JUnit/Mockito). Lập trình viên tiến hành đánh giá độ bao phủ (Code Coverage) và nghiệm thu chất lượng test case.

## 1. Nghiệm thu các yêu cầu của dự án

Dựa trên mã nguồn hiện tại, dưới đây là kết quả kiểm tra các yêu cầu bạn đã đề ra:

| Yêu cầu | Trạng thái | Ghi chú / Dẫn chứng trong code |
| --- | :---: | --- |
| **Java 8+ (21)** | ✅ | Cấu hình `<java.version>21</java.version>` trong `pom.xml`. |
| **Spring Framework** | ✅ | Sử dụng Spring Boot, Spring Data JPA và Spring Security. |
| **Spring Boot (Bean, DI, IoC, Annotations)** | ✅ | Có đầy đủ qua các annotation như `@RestController`, `@Service`, `@Configuration`. |
| **Spring Data (JPA)** | ✅ | Dùng thư viện `spring-boot-starter-data-jpa` và kế thừa `JpaRepository`. |
| **Spring Security (Filter chain)** | ✅ | Sử dụng `spring-boot-starter-security` và `oauth2-resource-server` để xử lý xác thực JWT. |
| **API, REST API** | ✅ | Cung cấp RESTful API thông qua các lớp trong package `controller` (`AuthController`, `UserController`). |
| **JUnit** | ✅ | Có thư viện `spring-boot-starter-test`, có viết test ở `src/test/java`. |
| **Database: PostgreSQL** | ✅ | Có driver `postgresql` trong `pom.xml` và kết nối jdbc PostgreSQL ở `application.yml`. |
| **MVC model** | ✅ | Tuân thủ mô hình MVC (Controller nhận request, Entity/Model lưu dữ liệu). |
| **Layered architecture** | ✅ | Cấu trúc thư mục chia layer rõ ràng: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception`. |
| **Git/GitHub** | ✅ | Thư mục dự án đã được khởi tạo Git (`.git` exists). |
| **Tools (IntelliJ, DBeaver, Postman)** | ✅ | Đây là các công cụ môi trường bên ngoài, hoàn toàn phù hợp để dùng cho dự án này. |
| **Swagger / OpenAPI** | ❌ | **Chưa có** thư viện `springdoc-openapi` trong `pom.xml`. Bạn cần bổ sung thêm nếu muốn generate document API tự động. |

---

## 2. Dự án này làm những gì?

**Identity Service** đóng vai trò là một dịch vụ trung tâm trong hệ thống Microservices (hoặc Monolithic) để quản lý người dùng và cấp quyền. Các chức năng chính bao gồm:
- **Đăng ký / Quản lý tài khoản**: Thêm mới người dùng, lấy thông tin người dùng (`Users.java`).
- **Xác thực (Authentication)**: Cho phép người dùng đăng nhập và trả về một JWT (JSON Web Token) để truy cập các tài nguyên.
- **Phân quyền (Authorization)**: Quản lý các vai trò (`Roles.java`) để kiểm soát xem người dùng có được phép thực hiện một hành động cụ thể hay không.
- **Đăng xuất / Thu hồi token**: Quản lý các token đã bị vô hiệu hóa (`InvalidatedTokens.java`) để ngăn chặn việc tái sử dụng token khi người dùng đã đăng xuất.

## 3. Luồng hoạt động (Flow)

Dự án áp dụng kiến trúc **Layered Architecture** (Kiến trúc phân tầng) chuẩn mực:

1. **Client (Postman/Trình duyệt)** gửi HTTP Request (ví dụ: `POST /identity-service/auth/login`).
2. **Controller Layer (`controller`):**
   - Nhận request từ Client.
   - Hứng dữ liệu đầu vào qua các DTO (`request dto`).
   - Validate dữ liệu đầu vào (ví dụ kiểm tra null, độ dài chuỗi).
   - Gọi xuống Service Layer.
3. **Service Layer (`service`):**
   - Chứa **Business Logic** (Luồng nghiệp vụ cốt lõi).
   - Xử lý việc kiểm tra mật khẩu, tạo token JWT, map dữ liệu từ DTO sang Entity bằng MapStruct/ModelMapper.
   - Gọi xuống Repository Layer để thao tác với database.
4. **Repository Layer (`repository`):**
   - Kế thừa `JpaRepository` của Spring Data JPA.
   - Dịch các hàm (như `findByUsername`) thành các câu lệnh SQL tự động để truy vấn Database PostgreSQL.
5. **Database (PostgreSQL):**
   - Xử lý lưu trữ/truy xuất dữ liệu và trả kết quả ngược lại theo luồng trên (Repo -> Service -> Controller -> Client).
6. **Exception Handling (`exception`):**
   - Nếu có lỗi xảy ra ở bất kỳ tầng nào (Sai mật khẩu, User không tồn tại, Validation fail), Global Exception Handler sẽ gom lỗi lại và trả về chuẩn JSON Error response cho Client.

## 4. Bảo mật (Security) trong dự án

- **Xác thực bằng JWT (JSON Web Token):** Sử dụng `spring-boot-starter-security-oauth2-resource-server` để tự động parse và validate JWT thông qua một `signerKey` bí mật được khai báo trong `application.yml`.
- **Mã hóa mật khẩu:** Thông thường Spring Security sẽ sử dụng `BCryptPasswordEncoder` để băm mật khẩu trước khi lưu xuống Database, đảm bảo database bị lộ thì mật khẩu gốc cũng không bị lộ.
- **Chặn truy cập trái phép:** Thiết lập Security Filter Chain để quyết định endpoint nào được phép public (như đăng ký, đăng nhập) và endpoint nào yêu cầu phải có token hợp lệ.
- **Blacklist Token (Vô hiệu hóa token):** Bằng entity `InvalidatedTokens`, hệ thống có thể lưu lại ID của các JWT đã được người dùng chủ động đăng xuất trước khi hết hạn, qua đó ngăn chặn các cuộc tấn công dùng lại token cũ (Replay Attack).
