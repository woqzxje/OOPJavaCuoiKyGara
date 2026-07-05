# Hệ Thống Quản Lý Gara Ô Tô (Garage Management System)

## Giới Thiệu
Dự án Quản Lý Gara Ô Tô là một ứng dụng phần mềm được xây dựng bằng ngôn ngữ Java nhằm mục đích số hóa và tối ưu hóa quy trình quản lý tại các xưởng sửa chữa, bảo dưỡng ô tô. Hệ thống cung cấp các giải pháp toàn diện từ việc tiếp nhận phương tiện, quản lý nhân sự (thợ máy, khách hàng), quản lý kho linh kiện, dịch vụ sửa chữa cho đến việc lập hóa đơn và theo dõi đơn hàng.

## Kiến Trúc Hệ Thống
Dự án được thiết kế theo mô hình **MVC (Model - View - Controller)**, đảm bảo sự tách biệt rõ ràng giữa logic nghiệp vụ, giao diện người dùng và tầng điều khiển. Điều này giúp mã nguồn dễ dàng bảo trì, mở rộng và kiểm thử:
- **Model**: Chứa các lớp thực thể (Person, Mechanic, Owner, Vehicle, RepairOrder, Invoice, v.v.) đại diện cho dữ liệu và các quy tắc nghiệp vụ. Áp dụng chặt chẽ 4 tính chất của Lập trình hướng đối tượng (OOP).
- **View**: Chịu trách nhiệm hiển thị thông tin và tương tác với người dùng.
- **Controller**: Đóng vai trò cầu nối, tiếp nhận các yêu cầu từ View, tương tác với Model và Cơ sở dữ liệu (Database).

## Tính Năng Cốt Lõi
- **Quản lý Nhân sự & Khách hàng**: Quản lý thông tin chủ xe (Owner) và thợ máy (Mechanic) dựa trên cấu trúc kế thừa từ lớp Person cơ sở.
- **Quản lý Phương tiện**: Lưu trữ và truy xuất thông tin chi tiết về các xe ô tô (Vehicle) được đưa vào xưởng.
- **Quản lý Dịch vụ & Linh kiện**: Hệ thống danh mục đa dạng bao gồm các Hạng mục (HangMuc) chuyên biệt như Dịch vụ (DichVu), và Linh kiện (LinhKien).
- **Xử lý Đơn sửa chữa**: Tạo lập và quản lý các Đơn sửa chữa (RepairOrder) cùng với các chi tiết sửa chữa (RepairOrderDetail).
- **Lập Hóa đơn**: Tính toán chi phí tự động và xuất hóa đơn (Invoice) thanh toán cho khách hàng một cách chính xác.

## Công Nghệ Sử Dụng
- **Ngôn ngữ lập trình**: Java
- **Quản lý dự án & Build tool**: Apache Maven
- **Cơ sở dữ liệu**: MySQL (thông qua `mysql-connector-j` 9.7.0)
- **Kiến trúc phần mềm**: MVC (Model-View-Controller)
- **Phương pháp luận**: Lập trình hướng đối tượng (OOP)

## Nguyên Tắc Hướng Đối Tượng (OOP) Áp Dụng
Dự án thể hiện rõ nét 4 nguyên lý thiết kế hướng đối tượng:
1. **Tính Đóng gói (Encapsulation)**: Các thuộc tính của mọi lớp thực thể đều được bảo vệ bằng phạm vi truy cập `private` hoặc `protected`. Dữ liệu chỉ được thay đổi hoặc truy xuất thông qua các phương thức `getter` và `setter` hợp lệ, đảm bảo tính toàn vẹn.
2. **Tính Kế thừa (Inheritance)**: Thiết kế các phân cấp lớp hợp lý. Các lớp `Mechanic` và `Owner` kế thừa từ lớp cha `Person`, tái sử dụng các thuộc tính chung (như tên, số điện thoại, địa chỉ) và chỉ định nghĩa thêm các đặc tả riêng. Lớp `DichVu` và `LinhKien` kế thừa từ `HangMuc`.
3. **Tính Đa hình (Polymorphism)**: Hỗ trợ nạp chồng (Overloading) phương thức và ghi đè (Overriding) các phương thức tiêu chuẩn như `toString()`. Khả năng đa hình cũng được sử dụng trong việc tính toán chi phí linh hoạt tùy thuộc vào loại đối tượng dịch vụ hay linh kiện.
4. **Tính Trừu tượng (Abstraction)**: Ẩn đi những xử lý nghiệp vụ phức tạp. Người dùng hoặc các thành phần khác của hệ thống chỉ cần tương tác thông qua các public interface hoặc phương thức được định nghĩa sẵn. Ngoài ra, ngoại lệ tùy chỉnh (`DuplicateMaException`) được tạo ra để trừu tượng hóa và xử lý lỗi hệ thống một cách tinh gọn.

## Cài Đặt Và Chạy Ứng Dụng
### 1. Yêu cầu hệ thống
- Java Development Kit (JDK) 21 trở lên.
- Apache Maven được cài đặt trong biến môi trường.
- Hệ quản trị cơ sở dữ liệu MySQL Server.

### 2. Các bước triển khai
- Thiết lập cơ sở dữ liệu MySQL và cập nhật thông tin kết nối (URL, Username, Password) trong tệp cấu hình kết nối (`DBConnection` thuộc package controller).
- Mở terminal hoặc command prompt tại thư mục gốc của dự án (`QuanLyGara`).
- Thực thi lệnh Maven để biên dịch và build dự án:
  ```bash
  mvn clean install
  ```
- Chạy chương trình trực tiếp qua Maven:
  ```bash
  mvn exec:java -Dexec.mainClass="com.mycompany.quanlygara.ConsoleView"
  ```

## Cấu Trúc Thư Mục
```text
QuanLyGara/
├── pom.xml                                     # Tệp cấu hình Maven, quản lý thư viện và dependencies
├── src/
│   └── main/java/com/mycompany/quanlygara/
│       ├── controller/                         # Controller: Xử lý logic nghiệp vụ, giao tiếp DB (DBConnection)
│       ├── model/                              # Model: Các lớp định nghĩa dữ liệu và quy tắc nghiệp vụ
│       └── view/                               # View: Các lớp quản lý giao diện, nhập xuất dữ liệu
└── quangara_class_diagram.html                 # Biểu đồ thiết kế lớp (UML Class Diagram)
```
