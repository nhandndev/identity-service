# === STAGE 1: Build file JAR ===
# Sử dụng image Maven chính thức để build code
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Copy file pom.xml và source code vào container
COPY pom.xml .
COPY src ./src

# Build dự án để tạo ra file JAR (bỏ qua chạy thử test để build nhanh hơn)
RUN mvn clean package -DskipTests


# === STAGE 2: Build Image chạy ứng dụng ===
# Sử dụng image JRE tinh gọn chỉ để chạy ứng dụng
FROM maven:3.9.6-eclipse-temurin-21

# Thiết lập thư mục làm việc cho ứng dụng
WORKDIR /app

# Copy file JAR từ stage 'builder' sang stage này
# Lưu ý: Thay đổi 'my-app-1.0.0.jar' thành tên file jar thực tế của bạn
COPY --from=builder /app/target/*.jar app.jar

# Khai báo cổng mà ứng dụng sẽ chạy (VD: 8080 cho Spring Boot)
EXPOSE 8080

# Lệnh để chạy file JAR
ENTRYPOINT ["java", "-jar", "app.jar"]