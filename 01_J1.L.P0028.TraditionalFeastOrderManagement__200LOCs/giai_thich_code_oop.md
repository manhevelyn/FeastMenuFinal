# 📖 Giải Thích Code + Kiến Thức OOP – LAB211
> 👨‍🏫 Dành cho sinh viên mới bắt đầu học Java | Traditional Feast Order Management

---

# 🎓 OOP LÀ GÌ? – NỀN TẢNG BẮT BUỘC PHẢI BIẾT

**OOP (Object-Oriented Programming)** = Lập trình hướng đối tượng.

Thay vì nghĩ theo từng bước lệnh, ta nghĩ theo **đối tượng thực tế**:

```
Thế giới thực              Java
──────────────────────────────────
Khách hàng           →  class Customer
Thực đơn tiệc        →  class FeastMenu
Đơn đặt tiệc         →  class FeastOrder
Nhân viên thu ngân   →  class FeastController
```

OOP có **4 trụ cột (4 Pillars)**:

| # | Tên | Ý nghĩa đơn giản |
|---|-----|-----------------|
| 1 | **Encapsulation** (Đóng gói) | Giấu dữ liệu bên trong, chỉ cho truy cập qua getter/setter |
| 2 | **Inheritance** (Kế thừa) | Class con dùng lại code của class cha |
| 3 | **Polymorphism** (Đa hình) | Một phương thức, nhiều cách thực hiện khác nhau |
| 4 | **Abstraction** (Trừu tượng) | Ẩn chi tiết phức tạp, chỉ lộ ra những gì cần thiết |

---

# 📁 ENTITY/Customer.java

## 🎓 OOP Concept #1: CLASS và OBJECT

```
Class   = Bản thiết kế (blueprint)
Object  = Sản phẩm được tạo ra từ bản thiết kế
```

```java
// Đây là CLASS – bản thiết kế cho "Khách hàng"
public class Customer implements Serializable {
```

```java
// Đây là OBJECT – một khách hàng CỤ THỂ được tạo ra từ bản thiết kế
Customer kh1 = new Customer("C0001", "Nguyen Van A", "0912345678", "a@gmail.com");
Customer kh2 = new Customer("C0002", "Tran Thi B",  "0987654321", "b@gmail.com");
// kh1 và kh2 đều là Customer, nhưng là 2 object KHÁC NHAU
```

---

## 🎓 OOP Concept #2: ENCAPSULATION (Đóng gói)

> 💡 **Quy tắc vàng**: Field luôn là `private`, chỉ truy cập qua `public` getter/setter

### Tại sao cần đóng gói?

```java
// ❌ KHÔNG đóng gói (nguy hiểm):
public String name;  // ai cũng có thể thay đổi trực tiếp!
customer.name = "";  // tên rỗng → hợp lệ không? Không ai kiểm tra!
customer.name = null; // null → crash NullPointerException!

// ✅ CÓ đóng gói (an toàn):
private String name;  // bên ngoài không thể truy cập trực tiếp

public void setName(String name) {
    // setter là "bảo vệ" – kiểm tra trước khi gán
    if (name != null && name.length() >= 2) {
        this.name = name;
    }
}
```

### Trong Customer.java:

```java
package Entity;
// 📌 Package = "Địa chỉ" của class trong dự án
// Giống như: Phòng Entity trong tòa nhà dự án
// Các class trong cùng package thấy được nhau dễ hơn
```

```java
import java.io.Serializable;
// 📌 IMPORT = "mang về" một công cụ từ thư viện Java
// Serializable nằm trong gói java.io (Input/Output)
// Nếu không import → Java không biết Serializable là gì → lỗi biên dịch
```

```java
public class Customer implements Serializable {
//     ↑              ↑         ↑
//  Ai cũng   Tên class    "Thực hiện" giao diện Serializable
//  dùng được
```

> 🎓 **`implements` là gì?**
> Interface (giao diện) giống như **hợp đồng**: "Tôi cam kết tôi có thể làm điều này".
> `Serializable` = cam kết "Tôi có thể được ghi ra file".
> Đây là nền tảng của **Abstraction** – ẩn chi tiết "ghi như thế nào" sau 1 từ khóa.

```java
    private static final long serialVersionUID = 1L;
//  ↑        ↑       ↑    ↑
//  Chỉ      Thuộc   Không Kiểu số
//  class    class,  thay  nguyên
//  này      không   đổi   64-bit
//           cần obj        (long)
```

> 📌 **Mã phiên bản** – Java dùng để kiểm tra tính tương thích khi đọc file:
> - Viết file lúc Customer có 4 fields
> - Thêm field mới, không đổi `serialVersionUID`
> - Đọc file cũ → **lỗi InvalidClassException**!
> - Giải pháp: luôn khai báo `serialVersionUID = 1L` và tự quản lý

```java
    private final String code;    // Mã KH: C0001, G2345, K9999
//  ↑         ↑
//  Chỉ đọc   Sau khi gán trong constructor, KHÔNG THỂ thay đổi
//  từ ngoài  → đúng vì mã KH là định danh bất biến

    private String name;          // Tên: có thể cập nhật
    private String phoneNumber;   // Số điện thoại: có thể cập nhật
    private String email;         // Email: có thể cập nhật
```

```java
    public Customer(String code, String name, String phoneNumber, String email) {
//  ↑               ↑ Tham số truyền vào khi tạo object
//  Constructor – phương thức đặc biệt, tên GIỐNG tên class
//  Không có kiểu trả về (khác với method thường)
//  Gọi bằng: new Customer(...)

        this.code = code;
//      ↑          ↑
//      field      tham số
//      của object
//
//      "this" = chính đối tượng này
//      Nếu không có this.code thì Java nghĩ cả 2 đều là tham số → không gán được
    }
```

```java
    // ✅ GETTER – Encapsulation: cho phép ĐỌC nhưng kiểm soát được
    public String getCode() {
        return code;   // trả về giá trị, không cho thay đổi
    }

    // ✅ SETTER – Encapsulation: cho phép THAY ĐỔI nhưng kiểm soát được
    public void setName(String name) {
        this.name = name;   // nếu muốn: thêm validation ở đây
    }

    // ❌ KHÔNG CÓ setCode() vì code là final – không được thay đổi!
```

---

# 📁 ENTITY/FeastMenu.java

## ❓ TẠI SAO DÙNG `BigDecimal` THAY VÌ `double`?

### Vấn đề của `double`:

```java
// Thử tính 0.1 + 0.2 với double:
double a = 0.1;
double b = 0.2;
System.out.println(a + b);  // In ra: 0.30000000000000004  ← SAI!

// Vì sao? Máy tính lưu số thập phân dạng nhị phân (binary):
// 0.1 trong binary = 0.0001100110011... (lặp vô tận)
// → Không lưu chính xác được → sai số tích lũy
```

### BigDecimal giải quyết:

```java
BigDecimal a = new BigDecimal("0.1");
BigDecimal b = new BigDecimal("0.2");
System.out.println(a.add(b));  // In ra: 0.3  ← ĐÚNG!

// BigDecimal lưu số dưới dạng chuỗi chữ số → chính xác tuyệt đối
```

### Hậu quả thực tế nếu dùng double cho tiền:

```java
// Tình huống: KH đặt 100 bàn, giá 150,000.50 VND/bàn
double price = 150000.50;
double total = price * 100;
System.out.println(total);  // 15000049.999999998  ← Thiếu 0.002 VND!

// Với 1000 giao dịch/ngày → sai hàng chục nghìn đồng mỗi ngày!
```

### Code trong FeastMenu.java:

```java
import java.math.BigDecimal;
// 📌 BigDecimal nằm trong package java.math (toán học)
```

```java
public class FeastMenu {
// 📌 Không implements Serializable vì:
// - Thực đơn đọc từ file CSV (văn bản), không phải file .dat (binary)
// - Mỗi lần chạy chương trình → đọc lại từ CSV
// - Không cần lưu trữ trạng thái nhị phân
```

```java
    private final String code;
    private final String name;
    private final BigDecimal price;
    private final String ingredients;
// 📌 TẤT CẢ đều là final → FeastMenu là IMMUTABLE OBJECT
```

## 🎓 OOP Concept: IMMUTABLE OBJECT (Đối tượng bất biến)

```
Immutable = Sau khi tạo ra, KHÔNG THỂ thay đổi nội dung

Ưu điểm:
✅ An toàn khi dùng nhiều nơi – không ai vô tình thay đổi
✅ Dễ debug – giá trị không bao giờ thay đổi bất ngờ
✅ Thread-safe (nếu sau này dùng đa luồng)

Ví dụ Immutable trong Java chuẩn:
- String: "Hello".toUpperCase() → tạo String MỚI, không sửa cái cũ
- Integer, LocalDate, BigDecimal → đều immutable
```

```java
    public FeastMenu(String code, String name, BigDecimal price, String ingredients) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        // Sau đây, KHÔNG CÓ setter nào cả!
        // → Chỉ có thể đọc thực đơn, không thể sửa
    }
```

---

# 📁 ENTITY/FeastOrder.java

## 🎓 OOP Concept: SINGLE RESPONSIBILITY PRINCIPLE (SRP)

> 💡 **Nguyên tắc**: Mỗi class chỉ nên chịu trách nhiệm cho **một việc duy nhất**.

```java
public class FeastOrder implements Serializable {
// FeastOrder chịu trách nhiệm: ĐẠI DIỆN cho một đơn đặt tiệc
// Không đọc file, không in màn hình, không validate → đúng SRP!
```

```java
import java.time.LocalDate;
// 📌 LocalDate – đại diện ngày tháng năm (Java 8+)
// So sánh với java.util.Date cũ:
//
// ❌ java.util.Date (cũ):
//   - Có cả giờ phút giây → thừa cho bài này
//   - Mutable → có thể thay đổi → nguy hiểm
//   - API khó dùng, dễ nhầm
//
// ✅ LocalDate (Java 8+):
//   - Chỉ ngày/tháng/năm → đúng mục đích
//   - Immutable → an toàn
//   - API rõ ràng: isAfter(), isBefore(), plusDays()...
```

```java
    private final int orderId;         // ID đơn: 1, 2, 3, ...
    private final String customerCode; // Mã KH: C0001
    private String menuCode;           // Mã thực đơn: có thể cập nhật
    private int numberOfTables;        // Số bàn: có thể cập nhật
    private LocalDate eventDate;       // Ngày sự kiện: có thể cập nhật
    private BigDecimal menuPrice;      // Giá thực đơn: cập nhật khi đổi menu
```

> 📌 Phân tích thiết kế:
> - `orderId` + `customerCode` = `final` → **định danh** đơn hàng, không đổi
> - Các field còn lại → có setter → cho phép **cập nhật** sau

```java
    public BigDecimal getTotalCost() {
        return menuPrice.multiply(BigDecimal.valueOf(numberOfTables));
//                       ↑                   ↑
//               Phép NHÂN BigDecimal    Chuyển int → BigDecimal
//               (không dùng dấu *)     (không dùng new BigDecimal(int)
//                                       vì có thể có lỗi precision)
    }
```

> 🎓 **Tại sao đặt `getTotalCost()` trong Entity?**
> - SRP: FeastOrder biết `menuPrice` và `numberOfTables` → nó **tự tính** được tổng
> - Thay vì: FeastController tính `order.getMenuPrice().multiply(...)`
> - Nguyên tắc OOP: **"Tell, don't ask"** – ra lệnh cho object làm việc của nó

---

# 📁 DATAOBJECT/CustomerRepository.java

## 🎓 OOP Concept: SEPARATION OF CONCERNS (Tách biệt mối quan tâm)

```
Repository = Chỉ lo việc ĐỌC/GHI FILE
Service    = Chỉ lo việc XỬ LÝ LOGIC
Controller = Chỉ lo việc ĐIỀU PHỐI
View       = Chỉ lo việc HIỂN THỊ

→ Mỗi class chỉ làm MỘT VIỆC → dễ sửa, dễ test, dễ hiểu
```

```java
public class CustomerRepository {
// Trách nhiệm DUY NHẤT: đọc/ghi file customers.dat
// Không validate, không sắp xếp, không tìm kiếm

    private static final String FILE_NAME = "customers.dat";
//  ↑                   ↑ ↑
//  Chỉ class này dùng  Cả class dùng chung  Không thể thay đổi
//                      (không cần object)
//
// 📌 Naming convention hằng số: UPPER_SNAKE_CASE
// Đúng:  FILE_NAME, MAX_SIZE, DEFAULT_VALUE
// Sai:   fileName, maxSize, defaultValue (đây là tên biến thường)
```

### PHƯƠNG THỨC `save()` – Ghi file nhị phân

```java
    public void save(ArrayList<Customer> customers) throws IOException {
//               ↑                                    ↑
//         Không trả về gì              Khai báo "tôi có thể ném IOException"
//         (void)                       → người gọi PHẢI xử lý!
```

> 🎓 **Exception (Ngoại lệ) trong OOP**:
> ```
> Khi xảy ra lỗi, Java "ném" (throw) một Exception object
> Người gọi có thể "bắt" (catch) để xử lý, hoặc "ném tiếp" (throws)
>
> Hierarchy:
> Throwable
> ├── Error (lỗi JVM, không xử lý được: OutOfMemoryError)
> └── Exception
>     ├── RuntimeException (không cần khai báo throws)
>     │   └── NullPointerException, NumberFormatException...
>     └── Checked Exception (PHẢI khai báo throws hoặc try-catch)
>         └── IOException ← đây là loại này
> ```

```java
        FileOutputStream fileOutput = null;
        ObjectOutputStream objectOutput = null;
//      📌 Khai báo NGOÀI khối try vì cần dùng trong finally
//      Nếu khai báo trong try → finally không thấy biến này!
```

> 🎓 **Luồng ghi file (Stream chaining)**:
> ```
> Data (Object)
>    ↓
> ObjectOutputStream    ← gói object thành bytes
>    ↓
> FileOutputStream      ← ghi bytes xuống ổ đĩa
>    ↓
> customers.dat (file)
>
> Gọi là "Decorator Pattern": bọc luồng này lên luồng kia để thêm chức năng
> ```

```java
        try {
            fileOutput = new FileOutputStream(FILE_NAME);
//          📌 Mở file để ghi:
//          - File chưa tồn tại → TỰ TẠO MỚI
//          - File đã tồn tại   → GHI ĐÈ (xóa nội dung cũ)
//          - Ổ đĩa đầy, file bị khóa → ném IOException

            objectOutput = new ObjectOutputStream(fileOutput);
//          📌 Bọc FileOutputStream → giờ có thể ghi object

            objectOutput.writeInt(customers.size());
//          📌 Ghi số lượng KH (4 bytes) vào đầu file
//          Khi đọc: đọc số này trước → biết cần đọc bao nhiêu object

            for (Customer customer : customers) {
//          📌 for-each: "với mỗi customer trong customers, làm..."
//          Sạch hơn: for (int i = 0; i < customers.size(); i++)

                objectOutput.writeObject(customer);
//              📌 Chuyển Customer → bytes → ghi vào file
//              Được vì Customer implements Serializable
            }

        } finally {
//          📌 FINALLY: LUÔN chạy dù try thành công hay thất bại
//          Mục đích: ĐẢM BẢO đóng file → tránh rò rỉ tài nguyên

            if (objectOutput != null) {
                objectOutput.close();
//              📌 Đóng ObjectOutputStream → tự đóng FileOutputStream bên trong
            } else if (fileOutput != null) {
                fileOutput.close();
//              📌 Chỉ chạy nếu ObjectOutputStream chưa tạo được
//              nhưng FileOutputStream đã mở rồi
            }
        }
```

### PHƯƠNG THỨC `load()` – Đọc file nhị phân

```java
    public ArrayList<Customer> load() throws IOException, ClassNotFoundException {
//                                                         ↑
//                              Ném thêm ClassNotFoundException
//                              Xảy ra khi đọc object nhưng class đó không tồn tại
```

```java
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<Customer>();
//          📌 Lần đầu chạy chương trình → file chưa có → trả về list rỗng
//          Không ném lỗi, chương trình tiếp tục bình thường
//          Đây là "defensive programming" – phòng thủ trước các tình huống edge case
        }
```

```java
            int numberOfCustomers = objectInput.readInt();
//          📌 Đọc số nguyên 4 bytes đầu tiên (đã ghi bằng writeInt() lúc save)

            for (int index = 0; index < numberOfCustomers; index++) {
                customers.add((Customer) objectInput.readObject());
//                            ↑
//              TYPE CASTING: readObject() trả về kiểu Object (tổng quát)
//              Ta biết đây là Customer → ép kiểu về Customer
//              Nếu ép kiểu sai → ClassCastException (runtime error)
            }
```

---

# 📁 DATAOBJECT/CustomerService.java

## 🎓 OOP Concept: ENCAPSULATION ở cấp độ Class

```java
public class CustomerService {
    private final CustomerRepository repository;  // ẨN repository
    private final ArrayList<Customer> customers;  // ẨN danh sách

// Từ bên ngoài (FeastController), KHÔNG BIẾT:
// - Dữ liệu lưu trong ArrayList hay LinkedList
// - Có cache hay không
// - File được đọc lúc nào
//
// Từ bên ngoài chỉ gọi:
// - customerService.add(...)
// - customerService.findByCode(...)
// - customerService.save()
// → Đây chính là ABSTRACTION: ẩn chi tiết, chỉ lộ giao diện
```

```java
    public CustomerService() {
        repository = new CustomerRepository();
        customers = loadData();
//      📌 NGAY KHI TẠO CustomerService → tự động load dữ liệu
//      FeastController chỉ cần: customerService = new CustomerService();
//      Không cần gọi thêm customerService.load() → tiện hơn, ít lỗi hơn
    }
```

```java
    private ArrayList<Customer> loadData() {
//  ↑
//  private: Đây là chi tiết NỘI BỘ, bên ngoài không cần biết
//  FeastController không gọi được loadData()

        try {
            return repository.load();
        } catch (IOException exception) {
            System.out.println("Cannot load customer data: " + exception.getMessage());
//          📌 exception.getMessage() → lấy thông điệp lỗi từ Exception object
        } catch (ClassNotFoundException exception) {
            System.out.println("Cannot read customer data: incompatible file format.");
        }
        return new ArrayList<Customer>();
//      📌 Nếu có lỗi → không crash, trả về list rỗng
//      Chương trình vẫn chạy được (dù không có dữ liệu cũ)
    }
```

### Phương thức `add()`:

```java
    public boolean add(Customer customer) {
        if (findByCode(customer.getCode()) != null) return false;
//      📌 Kiểm tra trùng mã trước khi thêm
//      return false → thất bại (mã đã tồn tại)
//      FeastController nhận false → không thêm nữa

        customers.add(customer);
        return true;
//      📌 return true → thành công
    }
```

### Thuật toán Selection Sort:

```java
    private void sortByName(ArrayList<Customer> list) {
```

> 🎓 **Selection Sort – Thuật toán sắp xếp chọn**:
> ```
> Ý tưởng: Ở mỗi vị trí, TÌM phần tử NHỎ NHẤT trong phần còn lại
>          rồi ĐỔI CHỖ về vị trí đó.
>
> Ví dụ: ["Charlie", "Alice", "Bob"]
>
> Vòng first=0:
>   So sánh Charlie vs Alice: "Charlie" > "Alice" → đổi
>   → ["Alice", "Charlie", "Bob"]
>   So sánh Alice vs Bob: "Alice" < "Bob" → giữ
>   → ["Alice", "Charlie", "Bob"]
>
> Vòng first=1:
>   So sánh Charlie vs Bob: "Charlie" > "Bob" → đổi
>   → ["Alice", "Bob", "Charlie"]
>
> Kết quả: ["Alice", "Bob", "Charlie"] ✅
>
> Độ phức tạp: O(n²) – với n nhỏ (vài chục KH) là chấp nhận được
> ```

```java
        for (int first = 0; first < list.size() - 1; first++) {
//          📌 first chạy từ 0 đến n-2 (vị trí cần điền)
//          Tại sao đến n-2? Vì phần tử cuối tự động đúng chỗ

            for (int second = first + 1; second < list.size(); second++) {
//              📌 second so sánh với first: từ first+1 đến n-1

                if (list.get(first).getName()
                        .compareToIgnoreCase(list.get(second).getName()) > 0) {
//              📌 compareToIgnoreCase():
//              < 0 → first đứng TRƯỚC second theo alphabet → ổn
//              = 0 → bằng nhau → ổn
//              > 0 → first đứng SAU second → phải ĐỔI CHỖ

                    Customer temporary = list.get(first);  // lưu tạm
                    list.set(first, list.get(second));      // gán second vào first
                    list.set(second, temporary);            // gán tạm vào second
//                  📌 Hoán đổi 3 bước (cần biến tạm)
//                  Giống đổi 2 cốc nước: cần cốc thứ 3!
                }
            }
        }
```

---

# 📁 DATAOBJECT/MenuRepository.java

## Phân tích CSV Parser – Đọc file CSV thực tế

```java
    public List<FeastMenu> load() throws IOException {
//  📌 Trả về List<FeastMenu> (interface), không phải ArrayList<FeastMenu>
//  → ABSTRACTION: người gọi không cần biết bên trong dùng ArrayList
//    (hôm nay dùng ArrayList, mai có thể đổi LinkedList → không ảnh hưởng bên ngoài)
```

```java
        fileScanner = new Scanner(menuFile, "UTF-8");
//      📌 "UTF-8" = bảng mã ký tự hỗ trợ tiếng Việt và ký tự đặc biệt
//      Nếu không chỉ định encoding:
//      - Windows dùng CP1258 (Windows-1258) → đọc sai tiếng Việt!
//      - Linux dùng UTF-8 → đọc đúng
//      → Luôn chỉ định encoding tường minh khi đọc file text
```

```java
        boolean firstLine = true;
        while (fileScanner.hasNextLine()) {
//          📌 hasNextLine() → còn dòng để đọc không? (true/false)

            String line = fileScanner.nextLine();
//          📌 Đọc một dòng, bỏ ký tự xuống dòng

            if (firstLine) {
                line = line.replace("\uFEFF", "");
//              📌 BOM (Byte Order Mark) = ký tự KHÔNG HIỂN THỊ ở đầu file UTF-8
//              Khi mở bằng Notepad và lưu UTF-8 → tự thêm BOM
//              \uFEFF = mã Unicode của BOM
//              Nếu không xóa: "code" đầu tiên = "\uFEFFM001" → không match!

                firstLine = false;
                if (line.toLowerCase().startsWith("code,")) {
                    continue;  // bỏ qua dòng tiêu đề
//                  📌 continue = bỏ vòng lặp hiện tại, sang vòng tiếp theo
//                  (khác break = thoát toàn bộ vòng lặp)
                }
            }
```

```java
                String priceText = fields.get(2).replaceAll("[^0-9.]", "");
//              📌 Regex: [^0-9.] = ký tự KHÔNG phải (^) chữ số (0-9) hoặc dấu chấm (.)
//              replaceAll → thay tất cả ký tự khớp bằng "" (xóa đi)
//
//              Ví dụ: "1,500,000 VND" → xóa dấu phẩy, khoảng trắng, "VND"
//                   → "1500000"
//
//              Ví dụ: "1.500.000" → xóa dấu chấm thừa... cẩn thận!
//                   → "1.500.000" → giữ nguyên nếu chỉ có dấu chấm (.)
```

### CSV Parser tùy chỉnh:

```java
    private List<String> parseCsvLine(String line) {
//  📌 Tại sao không dùng line.split(",") đơn giản?
//  Vì CSV có thể chứa dấu phẩy TRONG ngoặc kép:
//  M001,Gà Hầm,"Gà, táo tàu, kỷ tử",1500000
//              ↑ dấu phẩy trong ngoặc → KHÔNG phải phân cách cột!
//  split(",") sẽ tách sai! Cần parser thông minh hơn
```

```java
        StringBuilder field = new StringBuilder();
//      📌 StringBuilder: dùng để XÂY DỰNG chuỗi từng ký tự một
//
//      Tại sao không dùng String field = ""?
//      String + String trong vòng lặp = TẠO ĐỐI TƯỢNG MỚI mỗi lần
//      StringBuilder.append() = THÊM VÀO đối tượng hiện có → nhanh hơn nhiều!

        boolean quoted = false;
//      📌 Đang trong chuỗi có ngoặc kép không?
//      false = bình thường | true = trong "..."
```

```java
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
//          📌 line.charAt(index) = lấy ký tự tại vị trí index (0-based)
//          Ví dụ: "Hello".charAt(0) = 'H'
//                 "Hello".charAt(4) = 'o'
```

```java
            if (character == '"') {
//              📌 Gặp dấu ngoặc kép "

                if (quoted && index + 1 < line.length() && line.charAt(index + 1) == '"') {
//                  📌 Đang trong quoted VÀ ký tự tiếp theo cũng là " → "" = escaped quote
//                  Trong CSV: "" bên trong ngoặc = một dấu " thực sự
//                  Ví dụ: "He said ""Hello""" → He said "Hello"

                    field.append('"');  // thêm dấu " vào field
                    index++;           // bỏ qua ký tự " tiếp theo

                } else {
                    quoted = !quoted;  // bật/tắt chế độ quoted
//                  📌 ! = NOT: true→false, false→true
                }

            } else if (character == ',' && !quoted) {
//              📌 Dấu phẩy VÀ không trong ngoặc → kết thúc một cột

                fields.add(field.toString());
//              📌 Chuyển StringBuilder → String và thêm vào danh sách cột

                field.setLength(0);
//              📌 Reset StringBuilder về rỗng (nhanh hơn tạo mới)

            } else {
                field.append(character);
//              📌 Ký tự bình thường → tích lũy vào cột hiện tại
            }
        }
        fields.add(field.toString());  // thêm cột cuối (không có dấu phẩy sau)
        return fields;
```

---

# 📁 DATAOBJECT/OrderService.java

```java
    public FeastOrder create(...) {
        if (isDuplicate(0, customerCode, menuCode, eventDate)) return null;
//      📌 Truyền 0 vì đây là TẠO MỚI (không có ID để loại trừ)
//      Không có đơn hàng nào có ID = 0 → so sánh với tất cả

        int nextId = 1;
        for (FeastOrder existingOrder : orders) {
            if (existingOrder.getOrderId() >= nextId)
                nextId = existingOrder.getOrderId() + 1;
        }
//      📌 Tìm ID tiếp theo = Max(ID hiện có) + 1
//
//      Tại sao không đơn giản là orders.size() + 1?
//      Vì: giả sử có đơn ID = 1, 2, 5 (ID 3,4 bị xóa)
//      orders.size() + 1 = 3 + 1 = 4 → nhưng ID 4 đã từng tồn tại!
//      Max + 1 = 5 + 1 = 6 → đảm bảo LUÔN MỚI và TĂNG DẦN
```

```java
    public boolean isDuplicate(int excludedOrderId, String customerCode,
                               String menuCode, LocalDate eventDate) {
//  📌 THIẾT KẾ THÔNG MINH: dùng được cho cả 2 trường hợp:
//
//  Tạo mới:  isDuplicate(0, ...)       → excludedOrderId=0 (không loại trừ ai)
//  Update:   isDuplicate(orderId, ...) → loại trừ chính đơn đang sửa
//
//  → Code tái sử dụng, không viết 2 hàm riêng biệt
//  → Đây là nguyên tắc DRY: Don't Repeat Yourself

        for (FeastOrder order : orders) {
            if (order.getOrderId() != excludedOrderId
//              📌 != excludedOrderId → bỏ qua đơn đang update

                    && order.getCustomerCode().equalsIgnoreCase(customerCode)
//                  📌 Cùng khách hàng (không phân biệt hoa/thường)

                    && order.getMenuCode().equalsIgnoreCase(menuCode)
//                  📌 Cùng thực đơn

                    && order.getEventDate().equals(eventDate))
//                  📌 Cùng ngày tổ chức (LocalDate.equals() so sánh chính xác)

                return true;    // Tìm thấy trùng lặp!
        }
        return false;           // Không trùng
    }
```

---

# 📁 UTILITIES/Validator.java

## 🎓 OOP Concept: UTILITY CLASS PATTERN

```java
public final class Validator {
//       ↑
//  final class = KHÔNG THỂ kế thừa (extend)
//  Tại sao? Vì Validator là công cụ tiện ích, không có logic kế thừa

    private Validator() {}
//  ↑
//  Constructor PRIVATE = KHÔNG THỂ tạo object: new Validator() → lỗi!
//  Tại sao? Vì không cần object, tất cả method đều là static
```

> 🎓 **Utility Class là gì?**
> ```java
> // Cách KHÔNG ĐÚNG (phải tạo object vô nghĩa):
> Validator v = new Validator();
> v.isEmail("abc@gmail.com");  // thừa!
>
> // Cách ĐÚNG (static method, gọi thẳng qua tên class):
> Validator.isEmail("abc@gmail.com");  // gọn gàng!
>
> // Ví dụ Utility Class trong Java chuẩn:
> Math.sqrt(16);      // Math là Utility Class
> Collections.sort(); // Collections là Utility Class
> Arrays.fill();      // Arrays là Utility Class
> ```

```java
    private static final Pattern CUSTOMER_CODE =
            Pattern.compile("^[CGK]\\d{4}$", Pattern.CASE_INSENSITIVE);
//  📌 Pattern.compile() = biên dịch Regex sẵn (1 lần duy nhất)
//  Sau đó tái sử dụng nhiều lần mà không cần biên dịch lại
//
//  So sánh:
//  ❌ Kém hiệu quả (compile mỗi lần gọi):
//     "C0001".matches("^[CGK]\\d{4}$")  ← compile mỗi lần!
//
//  ✅ Hiệu quả hơn (compile 1 lần):
//     CUSTOMER_CODE.matcher("C0001").matches()
```

```java
//  Giải thích Regex từng ký tự:
//
//  ^         = bắt đầu chuỗi (phải bắt đầu từ đây)
//  [CGK]     = tập ký tự: chấp nhận C, G, hoặc K (1 ký tự)
//  \\d       = digit = chữ số (0-9) | \\ vì Java String cần escape backslash
//  {4}       = lặp đúng 4 lần
//  $         = kết thúc chuỗi (phải kết thúc ở đây)
//
//  CASE_INSENSITIVE = [CGK] chấp nhận cả c, g, k
//
//  Hợp lệ:   "C0001" "G9999" "k1234"
//  Sai:      "A0001" (A không trong [CGK])
//            "C123"  (chỉ 3 chữ số, cần 4)
//            "C12345"(5 chữ số, thừa)
//            "C0001X"(có ký tự thừa sau 4 số)
```

```java
    private static final Pattern VIETNAMESE_PHONE =
            Pattern.compile("^(03[2-9]|05[2689]|07[06-9]|08[1-9]|09[0-9])\\d{7}$");
//
//  Giải thích:
//  (...)     = nhóm (group)
//  |         = HOẶC (OR)
//  03[2-9]   = 03 rồi một chữ số từ 2 đến 9: 032, 033, ..., 039
//  05[2689]  = 05 rồi một trong {2,6,8,9}: 052, 056, 058, 059
//  07[06-9]  = 07 rồi {0,6,7,8,9}: 070, 076, 077, 078, 079
//  08[1-9]   = 08 rồi 1-9: 081, ..., 089
//  09[0-9]   = 09 rồi 0-9: 090, ..., 099
//  \\d{7}    = 7 chữ số tiếp theo
//  Tổng: 3 + 7 = 10 chữ số ✅
```

```java
    public static boolean isName(String value) {
        int length = value == null ? 0 : value.trim().length();
//      📌 TOÁN TỬ BA NGÔI: điều_kiện ? giá_trị_nếu_đúng : giá_trị_nếu_sai
//
//      Tương đương:
//      int length;
//      if (value == null) {
//          length = 0;
//      } else {
//          length = value.trim().length();
//      }
//
//      Dùng để phòng NullPointerException: nếu value=null thì length=0
//      → isName(null) → false (hợp lý)

        return length >= 2 && length <= 25;
//      📌 && = AND: cả hai điều kiện phải đúng
//      Tên hợp lệ: từ 2 đến 25 ký tự (sau khi trim)
    }
```

---

# 📁 UTILITIES/InputReader.java

```java
public static final DateTimeFormatter DATE_FORMAT =
    DateTimeFormatter.ofPattern("dd/MM/uuuu");
// 📌 public static final → CÓ THỂ dùng từ class khác:
//    InputReader.DATE_FORMAT → ConsoleView cũng dùng để format ngày khi hiển thị
//
// Tại sao uuuu thay vì yyyy?
// yyyy = năm theo era (có thể là 2026 BC)
// uuuu = năm lịch proleptic Gregorian (dương lịch chuẩn, an toàn hơn)
```

```java
    public int readPositiveInt(String prompt) {
        while (true) {
//      📌 Vòng lặp vô hạn – dừng khi return

            String input = readString(prompt);
            try {
                int number = Integer.parseInt(input);
//              📌 Integer.parseInt("123") → 123 (int)
//              Integer.parseInt("abc") → ném NumberFormatException!
//              Integer.parseInt("") → ném NumberFormatException!
//              Integer.parseInt("99999999999") → ném NumberFormatException! (quá lớn)

                if (number > 0) return number;
//              📌 Chỉ trả về khi số DƯƠNG
//              Số 0 hoặc âm → không return → tiếp tục vòng lặp

            } catch (NumberFormatException exception) {
//              📌 Bắt lỗi khi parseInt thất bại
//              Không làm gì trong catch → tiếp tục xuống println
            }
            System.out.println("Please enter an integer greater than zero.");
//          📌 Chỉ hiển thị thông báo khi nhập SAI
//          Khi nhập đúng → đã return rồi, không chạy đến đây
        }
    }
```

```java
    public Integer readOptionalPositiveInt(String prompt) {
//           ↑
//  Integer (chữ hoa) thay vì int (chữ thường)
//  Vì sao? int là kiểu nguyên thủy (primitive) → KHÔNG THỂ null
//           Integer là wrapper class → CÓ THỂ null
//
//  Khi user bỏ trống → trả về null → báo hiệu "không thay đổi gì"
//  Nếu dùng int, làm sao phân biệt "bỏ trống" với "nhập số 0"?

        while (true) {
            String input = readString(prompt);
            if (input.isEmpty()) return null;  // bỏ trống → null = "giữ nguyên"
```

> 🎓 **Primitive vs Wrapper Class**:
> ```
> Primitive    Wrapper     Đặc điểm Wrapper
> int      →  Integer     Có thể null, có methods
> double   →  Double      toIntValue(), compareTo()...
> boolean  →  Boolean     Dùng trong Collection<Integer>
> char     →  Character   (Collection không chấp nhận primitive)
> ```

---

# 📁 PROGRAM/ConsoleView.java

## 🎓 OOP Concept: SINGLE RESPONSIBILITY – Chỉ hiển thị!

```java
public class ConsoleView {
// ConsoleView KHÔNG:
// - Đọc input từ bàn phím (đó là việc của InputReader)
// - Xử lý logic (đó là việc của Service)
// - Đọc/ghi file (đó là việc của Repository)
//
// ConsoleView CHỈ: IN DỮ LIỆU RA MÀN HÌNH
```

```java
    private final NumberFormat moneyFormat = NumberFormat.getIntegerInstance(Locale.US);
//  📌 NumberFormat: định dạng số theo quy ước vùng
//  Locale.US → kiểu Mỹ: dùng dấu phẩy phân cách nghìn
//  1500000 → "1,500,000"
//
//  Tại sao dùng US locale? Vì Việt Nam không có NumberFormat chuẩn trong Java
//  Locale.US cho định dạng đẹp nhất (dấu phẩy nghìn)
```

```java
    public void showCustomers(List<Customer> customers) {
//                            ↑
//  Nhận vào List (interface), không phải ArrayList (implementation)
//  → Linh hoạt: có thể truyền ArrayList, LinkedList, hoặc bất kỳ List nào
//  → Đây là nguyên tắc: "Program to interface, not implementation"

        System.out.printf(
            "%-7s | %-25s | %-10s | %-30s%n",
            "Code", "Customer Name", "Phone", "Email");
//      📌 printf format string:
//      %    → bắt đầu format
//      -    → căn TRÁI (mặc định là căn phải)
//      7    → chiều rộng tối thiểu 7 ký tự (thừa thì padding bằng space)
//      s    → kiểu chuỗi (String)
//      %n   → xuống dòng (platform-independent, tốt hơn \n trên Windows)
//
//      Ví dụ kết quả:
//      Code    | Customer Name             | Phone      | Email
//      C0001   | Nguyen Van A              | 0912345678 | a@gmail.com
```

---

# 📁 PROGRAM/FeastController.java

## 🎓 OOP Concept: CONTROLLER PATTERN

```java
public class FeastController {
// Controller = "Bộ não" điều phối:
// - NHẬN input từ InputReader
// - GỌI Service để xử lý
// - GỬI kết quả cho ConsoleView để hiển thị
//
// Controller KHÔNG tự đọc file, KHÔNG tự in màn hình
// → Tách biệt rõ ràng các mối quan tâm (Separation of Concerns)
```

```java
    private final InputReader input;     // Đọc bàn phím
    private final ConsoleView view;      // In màn hình
    private final CustomerService customerService;  // Logic KH
    private final MenuService menuService;          // Logic menu
    private final OrderService orderService;        // Logic đơn hàng
    private boolean changed;             // Cờ theo dõi thay đổi
```

```java
    public void run() {
        boolean running = true;
        while (running) {
//          📌 Vòng lặp chính: hiển thị menu → đọc lựa chọn → xử lý → lặp lại

            view.showMainMenu();
            int choice = input.readPositiveInt("Select an option: ");

            switch (choice) {
//              📌 switch-case: phân nhánh theo giá trị nguyên
//              Thay thế cho nhiều if-else if liên tiếp

                case 1: registerCustomers(); break;
//                      ↑                   ↑
//              Gọi method      Thoát khỏi switch (nếu không có break → chạy case tiếp!)

                case 9:
                    running = !confirmQuit();
//                  📌 confirmQuit() trả về true nếu nên thoát
//                  !true = false → running = false → vòng lặp dừng
                    break;

                default:
                    System.out.println("Please select an option from 1 to 9.");
//                  📌 default: chạy khi không khớp case nào
//                  Xử lý input ngoài phạm vi 1-9
            }
        }
        System.out.println("Goodbye!");
//      📌 Chỉ in khi vòng lặp kết thúc (running = false)
    }
```

### Chức năng đăng ký khách hàng:

```java
    private void registerCustomers() {
        do {
//          📌 do-while: giống while nhưng CHẠY ÍT NHẤT 1 LẦN trước khi kiểm tra điều kiện
//
//          So sánh:
//          while(điều_kiện) { ... }  → kiểm tra TRƯỚC, có thể không chạy lần nào
//          do { ... } while(điều_kiện)  → chạy TRƯỚC, kiểm tra SAU → ít nhất 1 lần

            String code = readNewCustomerCode();
            String name = readName("Customer name: ", false);
//                                                     ↑
//                                                  false = bắt buộc nhập
//                                                  (không cho để trống)
            customerService.add(new Customer(code, name, phone, email));
            changed = true;

        } while (input.readYesNo("Register another customer? (Y/N): "));
//                ↑
//  Hỏi có tiếp tục không: Y → true → lặp lại | N → false → dừng
    }
```

### Logic kiểm tra thoát chương trình:

```java
    private boolean confirmQuit() {
//  📌 3 tình huống:
//
//  1. Không có thay đổi → thoát ngay
        if (!changed) return true;

//  2. Có thay đổi, muốn lưu
        if (input.readYesNo("There are unsaved changes. Save before quitting? (Y/N): "))
            return saveData();
//          ↑ saveData() trả về true nếu lưu thành công → thoát
//            saveData() trả về false nếu lưu thất bại → không thoát!

//  3. Có thay đổi, không muốn lưu → hỏi xác nhận bỏ qua
        return input.readYesNo("Discard unsaved changes and quit? (Y/N): ");
//          Y → true → thoát
//          N → false → không thoát
    }
```

---

# 📁 PROGRAM/Main.java

```java
public class Main {
    public static void main(String[] args) {
//  📌 main() = điểm khởi đầu của mọi chương trình Java
//  JVM (Java Virtual Machine) tìm chính xác chữ ký:
//  public static void main(String[] args)
//  Thiếu bất kỳ từ nào → JVM không nhận ra → không chạy được!
//
//  public  → JVM (bên ngoài) có thể gọi
//  static  → gọi được mà không cần tạo object (JVM chưa có object nào)
//  void    → không trả về gì
//  String[] args → tham số dòng lệnh (ít dùng trong ứng dụng console đơn giản)

        new FeastController().run();
//      ↑                      ↑
//  Tạo FeastController   Gọi phương thức run()
//  (load dữ liệu từ file) (bắt đầu vòng lặp chính)
//
//  Có thể viết thành 2 dòng cho rõ hơn:
//  FeastController controller = new FeastController();
//  controller.run();
    }
}
```

---

# 📊 TỔNG KẾT: OOP TRONG DỰ ÁN NÀY

## 4 Trụ cột OOP – Ví dụ cụ thể trong code

### 1️⃣ ENCAPSULATION (Đóng gói)
```java
// Tất cả field là private, chỉ truy cập qua getter/setter
private String name;
public String getName() { return name; }
public void setName(String name) { this.name = name; }

// FeastMenu: tất cả final → chỉ đọc, không ghi
private final BigDecimal price;
// Không có setPrice() → không thể thay đổi giá từ bên ngoài
```

### 2️⃣ ABSTRACTION (Trừu tượng)
```java
// List (interface) thay vì ArrayList (cụ thể)
public List<FeastMenu> load() { ... }
// Người gọi chỉ biết "trả về danh sách", không cần biết ArrayList hay LinkedList

// CustomerService ẩn chi tiết đọc file:
customerService.add(customer);  // Đơn giản! Không cần biết bên trong làm gì
// Bên trong: kiểm tra trùng, thêm vào ArrayList, đánh dấu changed...
```

### 3️⃣ INHERITANCE (Kế thừa) ← ít dùng trong dự án này
```java
// implements Serializable là dạng kế thừa interface
public class Customer implements Serializable { }
// Customer "kế thừa" hành vi có thể serialize từ interface Serializable
```

### 4️⃣ POLYMORPHISM (Đa hình)
```java
// Method overloading: cùng tên, khác tham số
public LocalDate readFutureDate(String prompt, boolean optional)
// → gọi với optional=true hoặc false → hành vi khác nhau

// Interface polymorphism:
List<FeastMenu> menus; // biến kiểu List
menus = new ArrayList<>(); // thực chất là ArrayList → đa hình!
```

---

## 📝 CÁC KEYWORD JAVA QUAN TRỌNG

| Keyword | Ý nghĩa | Ví dụ trong code |
|---------|---------|-----------------|
| `private` | Chỉ dùng trong class này | Tất cả field của Entity |
| `public` | Mọi nơi đều dùng được | Getter, setter, constructor |
| `final` (field) | Không thay đổi sau khi gán | `code`, `orderId` |
| `final` (class) | Không thể kế thừa | `Validator` |
| `static` | Thuộc về class, không cần object | `Validator.isEmail()` |
| `static final` | Hằng số class | `FILE_NAME`, `DATE_FORMAT` |
| `this` | Chính đối tượng này | `this.code = code` |
| `new` | Tạo object mới | `new Customer(...)` |
| `null` | Không có object | Khi không tìm thấy |
| `return` | Trả về giá trị và thoát method | `return customer` |
| `throws` | Khai báo có thể ném ngoại lệ | `throws IOException` |
| `implements` | Thực hiện interface | `implements Serializable` |
| `continue` | Bỏ qua vòng lặp hiện tại | Skip dòng header CSV |
| `break` | Thoát khỏi vòng lặp/switch | `break` trong switch-case |
