# 📋 Code Review – Traditional Feast Order Management

> **Reviewer**: Antigravity (10 năm kinh nghiệm) | **Đối tượng**: Sinh viên mới học Java LAB211

---

## 🏗️ Kiến trúc tổng thể (Architecture Overview)

Dự án tuân theo mô hình **3 lớp (3-tier layered architecture)** rất rõ ràng:

```
┌──────────────┐
│   Program    │  ← Tầng giao diện (UI): Main, FeastController, ConsoleView
├──────────────┤
│  DataObject  │  ← Tầng nghiệp vụ & truy cập dữ liệu: *Service, *Repository
├──────────────┤
│    Entity    │  ← Tầng thực thể: Customer, FeastMenu, FeastOrder
├──────────────┤
│  Utilities   │  ← Công cụ dùng chung: InputReader, Validator
└──────────────┘
```

---

## ✅ Đánh giá từng file

---

### 📁 `Entity/Customer.java`

```java
public class Customer implements Serializable {
    private final String code;   // code là final → không thể thay đổi sau khi tạo
    private String name;
    private String phoneNumber;
    private String email;
```

**✅ Điểm tốt:**
- `implements Serializable` → cho phép ghi đối tượng ra file `.dat` bằng `ObjectOutputStream`
- `serialVersionUID = 1L` → giúp Java kiểm tra version khi đọc lại file
- `code` là `final` → đúng vì mã khách hàng không bao giờ thay đổi
- Có đầy đủ getter/setter phân biệt rõ cái nào được phép thay đổi

**⚠️ Lưu ý cho sinh viên:**
- `Serializable` là một **marker interface** (interface không có phương thức), chỉ báo cho Java biết rằng object này có thể được tuần tự hóa (ghi ra bytes)
- Nếu thiếu `serialVersionUID`, Java sẽ tự tạo một ID ngẫu nhiên → **nguy cơ lỗi khi load file** sau khi thay đổi class

---

### 📁 `Entity/FeastMenu.java`

```java
private final BigDecimal price;   // BigDecimal thay vì double
```

**✅ Điểm tốt:**
- Dùng `BigDecimal` cho giá tiền → **rất đúng**, tránh sai số dấu phẩy động của `double`
- Tất cả field đều `final` → FeastMenu là **immutable object** (không thay đổi sau khi tạo)
- Không implement `Serializable` → hợp lý vì FeastMenu đọc từ CSV, không cần lưu binary

**⚠️ Lưu ý:**
- `BigDecimal` quan trọng khi xử lý tiền tệ: `0.1 + 0.2` với `double` cho kết quả `0.30000000000000004`, còn `BigDecimal` cho đúng `0.3`

---

### 📁 `Entity/FeastOrder.java`

```java
public BigDecimal getTotalCost() {
    return menuPrice.multiply(BigDecimal.valueOf(numberOfTables));
}
```

**✅ Điểm tốt:**
- `getTotalCost()` tính toán ngay trong entity → **single responsibility** tốt
- Dùng `LocalDate` thay vì `Date` cũ → **modern Java API (Java 8+)**
- `orderId` và `customerCode` là `final` → đúng, không nên thay đổi sau khi tạo

**⚠️ Lưu ý:**
- `LocalDate` là **immutable** và an toàn hơn `java.util.Date` cũ
- `BigDecimal.valueOf(numberOfTables)` thay vì `new BigDecimal(numberOfTables)` → đúng, tránh lỗi precision

---

### 📁 `DataObject/CustomerRepository.java`

```java
public void save(ArrayList<Customer> customers) throws IOException {
    FileOutputStream fileOutput = null;
    ObjectOutputStream objectOutput = null;
    try {
        fileOutput = new FileOutputStream(FILE_NAME);
        objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeInt(customers.size());   // ghi số lượng trước
        for (Customer customer : customers) {
            objectOutput.writeObject(customer);    // ghi từng object
        }
    } finally {
        if (objectOutput != null) objectOutput.close();
        else if (fileOutput != null) fileOutput.close();
    }
}
```

**✅ Điểm tốt:**
- Pattern **try-finally** đảm bảo luôn đóng file dù có lỗi hay không
- Ghi số lượng trước (`writeInt`) → khi đọc lại biết đúng số record cần đọc
- File `customers.dat` → binary format, bảo mật hơn CSV

**⚠️ Điểm cần cải thiện:**
- Java 7+ có `try-with-resources` gọn hơn:
  ```java
  // Cách hiện tại (Java 6 style, vẫn hoạt động tốt):
  finally { if (objectOutput != null) objectOutput.close(); }
  
  // Cách hiện đại hơn (Java 7+):
  try (ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
      // ...
  }
  ```
  > Với bài LAB211 không dùng try-with-resources là **không bị trừ điểm**, code vẫn đúng

**Giải thích luồng `save/load`:**
```
save():  ArrayList<Customer> → ObjectOutputStream → file .dat (binary)
load():  file .dat → ObjectInputStream → ArrayList<Customer>
```

---

### 📁 `DataObject/CustomerService.java`

```java
private void sortByName(ArrayList<Customer> list) {
    for (int first = 0; first < list.size() - 1; first++) {
        for (int second = first + 1; second < list.size(); second++) {
            if (list.get(first).getName().compareToIgnoreCase(list.get(second).getName()) > 0) {
                Customer temporary = list.get(first);
                list.set(first, list.get(second));
                list.set(second, temporary);
            }
        }
    }
}
```

**✅ Điểm tốt:**
- Tự cài thuật toán **Selection Sort** thay vì dùng `Collections.sort()` → phù hợp yêu cầu LAB211 (tự implement sort)
- `compareToIgnoreCase` → sắp xếp không phân biệt hoa thường
- `getSortedCustomers()` trả về **bản sao** (`new ArrayList<>(customers)`) → không làm hỏng dữ liệu gốc ✅

**Giải thích Selection Sort:**
```
[Charlie, Alice, Bob]
→ Vòng first=0: so sánh Charlie với Alice → đổi → [Alice, Charlie, Bob]
→ Vòng first=0: so sánh Alice với Bob → giữ nguyên → [Alice, Charlie, Bob]
→ Vòng first=1: so sánh Charlie với Bob → đổi → [Alice, Bob, Charlie]
Kết quả: [Alice, Bob, Charlie]
```

---

### 📁 `DataObject/MenuRepository.java`

```java
private List<String> parseCsvLine(String line) {
    boolean quoted = false;
    // Xử lý trường hợp có dấu phẩy trong ngoặc kép: "A,B" → 1 field
}
```

**✅ Điểm xuất sắc:**
- **Tự viết CSV parser** xử lý quoted fields → rất tốt, đây là edge case phổ biến
- Xử lý BOM (`\uFEFF`) ở đầu file UTF-8 → code "battle-tested", xử lý thực tế
- Loại bỏ ký tự không phải số trong price: `replaceAll("[^0-9.]", "")` → linh hoạt với format giá
- Skip header line (`code,` ...) → đọc CSV thực tế

---

### 📁 `DataObject/MenuService.java`

```java
public List<FeastMenu> getSortedMenus() {
    // Sort by price ascending (Selection Sort)
}

public boolean isEmpty() {
    return menus.isEmpty();
}
```

**✅ Điểm tốt:**
- Sort theo giá tăng dần → theo yêu cầu đề bài
- `isEmpty()` cho phép Controller kiểm tra trước khi place order

---

### 📁 `DataObject/OrderService.java`

```java
public FeastOrder create(...) {
    if (isDuplicate(0, customerCode, menuCode, eventDate)) return null;  // kiểm tra trùng
    int nextId = 1;
    for (FeastOrder existingOrder : orders) {
        if (existingOrder.getOrderId() >= nextId) nextId = existingOrder.getOrderId() + 1;
    }
    // ...
}

public boolean isDuplicate(int excludedOrderId, String customerCode, String menuCode, LocalDate eventDate) {
    // excludedOrderId dùng cho UPDATE: bỏ qua chính đơn hàng đang sửa
}
```

**✅ Điểm xuất sắc:**
- `isDuplicate` với `excludedOrderId` → dùng được cho cả **tạo mới** (pass `0`) và **update** (pass id hiện tại) → **thiết kế tái sử dụng tốt**
- Auto-increment ID từ max hiện có → không bị trùng dù xóa bỏ record

---

### 📁 `Utilities/Validator.java`

```java
private static final Pattern CUSTOMER_CODE =
    Pattern.compile("^[CGK]\\d{4}$", Pattern.CASE_INSENSITIVE);

private static final Pattern VIETNAMESE_PHONE =
    Pattern.compile("^(03[2-9]|05[2689]|07[06-9]|08[1-9]|09[0-9])\\d{7}$");
```

**✅ Điểm tốt:**
- Dùng **Regex Pattern** compile sẵn → hiệu quả hơn compile mỗi lần gọi
- `final class` với `private Validator()` → **Utility class pattern** đúng chuẩn, không thể instantiate
- Phone pattern chính xác theo đầu số Việt Nam (Viettel, Mobifone, Vinaphone, ...)

**Giải thích Regex Customer Code:**
```
^[CGK]  → bắt đầu bằng C, G, hoặc K
\\d{4}  → tiếp theo là đúng 4 chữ số
$       → kết thúc
CASE_INSENSITIVE → c1234 cũng hợp lệ như C1234
```

---

### 📁 `Utilities/InputReader.java`

```java
public int readPositiveInt(String prompt) {
    while (true) {
        String input = readString(prompt);
        try {
            int number = Integer.parseInt(input);
            if (number > 0) return number;
        } catch (NumberFormatException exception) {
            // tiếp tục vòng lặp
        }
        System.out.println("Please enter an integer greater than zero.");
    }
}
```

**✅ Điểm tốt:**
- Vòng lặp vô hạn + try-catch → **không crash khi user nhập sai**
- `readFutureDate(prompt, optional)` với flag `optional` → tái sử dụng cho cả nhập mới lẫn update
- `DATE_FORMAT` là `public static final` → dùng chung ở `ConsoleView` tránh trùng lặp

---

### 📁 `Program/ConsoleView.java`

```java
System.out.printf("%-7s | %-25s | %-10s | %-30s%n", "Code", "Customer Name", "Phone", "Email");
```

**✅ Điểm tốt:**
- `printf` với format string → căn lề đẹp
- `NumberFormat.getIntegerInstance(Locale.US)` → hiển thị tiền có dấu phẩy: `1,500,000`
- **Tách biệt** hiển thị ra `ConsoleView` → `FeastController` không có `System.out.println` nào liên quan đến format

---

### 📁 `Program/FeastController.java`

```java
private boolean confirmQuit() {
    if (!changed) return true;                                   // không có thay đổi → thoát luôn
    if (input.readYesNo("...Save before quitting? (Y/N): "))
        return saveData();                                       // lưu thành công mới thoát
    return input.readYesNo("Discard unsaved changes...? (Y/N): ");
}
```

**✅ Điểm xuất sắc:**
- `changed` flag theo dõi dữ liệu chưa lưu → nhắc user khi thoát
- `confirmQuit()` xử lý 3 tình huống: không đổi / lưu / bỏ qua
- Tách các phương thức nhỏ (`readNewCustomerCode`, `readExistingMenuCode`, ...) → **Clean Code**

---

## 🐛 Các lỗi / điểm cần cải thiện

| # | Vị trí | Mức độ | Mô tả |
|---|--------|--------|-------|
| 1 | `FeastController.java` dòng 30 | ⚠️ Nhỏ | `menuService = new MenuService("feastMenu.csv")` - tên file khác với file thực là `FeastMenu.csv` (chữ hoa F). Trên Windows không sao, nhưng Linux/Mac sẽ lỗi |
| 2 | `CustomerRepository.java` dòng 29 | ⚠️ Nhỏ | `else if (fileOutput != null)` nên là `if (fileOutput != null)` vì nếu `objectOutput != null` thì khi đóng `objectOutput` cũng tự đóng `fileOutput` bên trong |
| 3 | `FeastController.java` dòng 183 | ℹ️ Rất nhỏ | Input `choice > 2` không có xử lý rõ ràng, chỉ hiện thông báo → OK với đề bài |
| 4 | `FeastMenu.java` | ℹ️ Gợi ý | Nên thêm `toString()` để debug dễ hơn |

---

## 🎯 Kết luận tổng thể

| Tiêu chí | Điểm |
|----------|------|
| Đúng yêu cầu chức năng (9 menu) | ✅ **Đủ cả 9 chức năng** |
| Kiến trúc phân lớp | ✅ Rõ ràng, chuẩn mực |
| Validation đầy đủ | ✅ Code, phone, email, date |
| Xử lý file (đọc/ghi) | ✅ Đúng, có xử lý lỗi |
| Tránh trùng lặp dữ liệu | ✅ isDuplicate() tốt |
| Tự cài Sort (không dùng Collections.sort) | ✅ Selection Sort |
| Xử lý unsaved changes khi Quit | ✅ changed flag |
| Code sạch, đặt tên tốt | ✅ Rất tốt |

> **Điểm ước lượng: 9/10** 🌟
> Trừ 1 điểm nhỏ do tiềm ẩn lỗi tên file case-sensitive.
> Đây là code chất lượng cao, vượt xa mức trung bình của sinh viên LAB211!
