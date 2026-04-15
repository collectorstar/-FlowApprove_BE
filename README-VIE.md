# FlowApprove BE

Backend cho dự án FlowApprove, được xây dựng bằng Spring Boot với cấu trúc Maven multi-module.

## Mục Lục

1. [Tổng Quan](#tổng-quan)
2. [Tình Trạng Hiện Tại](#tình-trạng-hiện-tại)
3. [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
4. [Cấu Trúc Module](#cấu-trúc-module)
5. [Yêu Cầu Môi Trường](#yêu-cầu-môi-trường)
6. [Chạy Dependency Local](#chạy-dependency-local)
7. [Chạy Test](#chạy-test)
8. [Chạy API](#chạy-api)
9. [Chạy DB Migrator](#chạy-db-migrator)
10. [Lưu Ý Về Cấu Hình](#lưu-ý-về-cấu-hình)
11. [Phạm Vi API Hiện Có](#phạm-vi-api-hiện-có)
12. [Giới Hạn Hiện Tại](#giới-hạn-hiện-tại)
13. [Hướng Phát Triển Tiếp Theo](#hướng-phát-triển-tiếp-theo)

## Tổng Quan

Repository này chứa ứng dụng backend của FlowApprove.

Hiện tại backend vẫn đang ở giai đoạn xây dựng nền tảng. Trọng tâm hiện nay là hoàn thiện kiến trúc cơ sở, ranh giới module, security, luồng migration, và wiring API cơ bản trước khi đưa vào các tính năng nghiệp vụ thật và các entity hoàn chỉnh.

## Tình Trạng Hiện Tại

- Repository đã được chia thành nhiều module theo trách nhiệm.
- Đã có skeleton API cho một số nhóm chức năng ban đầu.
- Đã bật security dựa trên JWT cho `/api/v1/**`.
- Đã có module migrator riêng cho database.
- Đã có các file migration nền tảng cho PostgreSQL.
- Đã có integration test cho security và luồng migration.
- Chưa triển khai các tính năng nghiệp vụ thực tế.
- Vẫn chưa có đầy đủ entity hoặc domain model cho toàn hệ thống.

## Công Nghệ Sử Dụng

- Java 21
- Spring Boot 3
- Maven Wrapper
- Spring Security Resource Server
- Flyway
- PostgreSQL
- Redis
- Testcontainers

## Cấu Trúc Module

```text
flowapprove-shared          Kiểu dùng chung, value object, contract dùng chung
flowapprove-domain          Tầng domain
flowapprove-application     Use case, command/query handler, port
flowapprove-infrastructure  Persistence, redis, tenant context, security adapter
flowapprove-api             Ứng dụng REST API
flowapprove-db-migrator     Chạy migration cho public schema và tenant schema
```

## Yêu Cầu Môi Trường

- JDK 21
- Docker Desktop hoặc Docker Engine

Không cần cài Maven toàn cục vì repository đã có sẵn `mvnw` và `mvnw.cmd`.

Kiểm tra nhanh:

```bash
java -version
docker version
```

## Chạy Dependency Local

Khởi động PostgreSQL và Redis:

```bash
docker compose up -d
```

Service mặc định:
- PostgreSQL: `localhost:55432`
- Redis: `localhost:56379`

## Chạy Test

Windows:

```bash
.\mvnw.cmd test
```

macOS/Linux:

```bash
./mvnw test
```

Lưu ý:
- Một số integration test dùng Testcontainers nên cần Docker đang chạy.
- Test hiện là điểm kiểm tra chính để xác nhận backend build được và wiring nền tảng đang hoạt động đúng.

## Chạy API

Windows:

```bash
.\mvnw.cmd -pl flowapprove-api spring-boot:run
```

macOS/Linux:

```bash
./mvnw -pl flowapprove-api spring-boot:run
```

Hành vi mặc định của `flowapprove-api`:
- Database: PostgreSQL
- Actuator expose: `health`, `info`
- Redis: cấu hình qua biến môi trường

## Chạy DB Migrator

Windows:

```bash
.\mvnw.cmd -pl flowapprove-db-migrator spring-boot:run
```

macOS/Linux:

```bash
./mvnw -pl flowapprove-db-migrator spring-boot:run
```

Kết nối PostgreSQL mặc định:
- URL: `jdbc:postgresql://localhost:55432/flowapprove`
- Username: `postgres`
- Password: `postgres`

Có thể override bằng các biến môi trường:

```text
FLOWAPPROVE_DB_URL
FLOWAPPROVE_DB_USERNAME
FLOWAPPROVE_DB_PASSWORD
FLOWAPPROVE_DB_ACTION
FLOWAPPROVE_DB_TENANT
```

## Lưu Ý Về Cấu Hình

### API Application

`flowapprove-api` hiện mặc định dùng PostgreSQL, đồng bộ với Docker Compose và luồng migration.

### Security

- `/actuator/health` được mở public.
- `/api/v1/**` yêu cầu JWT hợp lệ.
- Secret hiện tại chỉ phù hợp cho local development, chưa phải cấu hình production.

### Database Migration

`flowapprove-db-migrator` quản lý migration cho:
- `public` schema
- các tenant schema

## Phạm Vi API Hiện Có

Backend hiện vẫn đang ở mức skeleton, nhưng đã có các nhóm endpoint sau:

- `POST /api/v1/organizations`
- `POST /api/v1/workflow-definitions`
- `POST /api/v1/workflow-definitions/{workflowDefinitionId}/publish`
- `POST /api/v1/workflow-requests`
- `GET /api/v1/workflow-requests/{workflowRequestId}`
- `GET /api/v1/approvals/pending`

Các endpoint này hiện chủ yếu phục vụ việc dựng nền tảng kỹ thuật và xác nhận wiring giữa các tầng. Chúng chưa phản ánh đầy đủ bộ tính năng hoàn chỉnh của sản phẩm.

## Giới Hạn Hiện Tại

- Vẫn thiếu các entity đầy đủ và business flow thực tế.
- Contract API chưa được chốt hoàn chỉnh.
- Cấu hình runtime giữa API application và luồng migration PostgreSQL chưa được thống nhất hoàn toàn.
- Trạng thái hiện tại phù hợp cho việc dựng nền tảng, chưa phù hợp cho production.

## Hướng Phát Triển Tiếp Theo

- Bổ sung entity và domain model theo các use case thực tế.
- Hoàn thiện contract API để frontend tích hợp thật.
- Thống nhất cấu hình runtime cho môi trường local và development.
- Mở rộng test từ mức kiểm tra wiring sang mức kiểm tra use case thực tế.
