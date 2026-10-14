# Module Đi Chợ Thông Minh — AI Bếp Nhà (Master Prompt 03)

Triển khai module **Đi chợ thông minh** hoàn chỉnh cho AI Bếp Nhà, kết nối chặt chẽ giữa Thực đơn, Kho nguyên liệu, Ngân sách và Chi tiêu thực tế mà không phá vỡ giao diện Material 3 đã thống nhất.

## User Review & Critical Decisions

> [!IMPORTANT]
> Các quyết định cốt lõi đã được xác nhận qua phản hồi người dùng:
> - **Điểm truy cập điều hướng**: Truy cập màn hình Đi chợ qua lưới "Chức năng chính" (Mục 2 - Đi chợ) và các phím tắt nhanh trên Trang chủ và Thực đơn; bảo toàn tuyệt đối thanh điều hướng 5 tab gốc.
> - **Cập nhật Kho nguyên liệu**: Hiển thị hộp thoại xác nhận hỏi người dùng trước khi cộng dồn hàng đã mua vào Kho nguyên liệu, ngăn ngừa trùng lặp dữ liệu.
> - **Chọn khoảng thời gian khi tạo danh sách từ thực đơn**: Sử dụng bộ chọn khoảng thời gian với các tùy chọn tiện lợi "Hôm nay", "Tuần này", hoặc chọn ngày tùy ý.

---

## 1. Overview & Core Concept

- **Mục tiêu**: Cung cấp công cụ lập danh sách mua sắm thông minh, tự động phân tích các món trong thực đơn, bóc tách nguyên liệu, nhân theo khẩu phần, cộng gộp thông minh và **trừ đi số lượng đã có sẵn trong kho thực phẩm (Tủ lạnh/Pantry)**.
- **Giá trị thực tiễn**: Giúp người nội trợ kiểm soát chi phí mua sắm theo thời gian thực (Giá dự kiến vs. Giá thực tế), tự động ghi nhận vào Sổ thu chi chỉ với một chạm, và lưu trữ lịch sử các lần đi chợ.
- **Nguyên tắc không phá vỡ**: Giữ nguyên toàn bộ 13 màn hình và thanh điều hướng 5 tab hiện hữu; dữ liệu được lưu bền vững qua Room Database.

---

## 2. User Experience & Visual Design

### Luồng trải nghiệm người dùng (User Flows)
1. **Truy cập Đi chợ**: Người dùng mở từ "Chức năng chính" (Grid số 2) hoặc nhấn nút "Đi chợ tuần này" trên Trang chủ / Thực đơn.
2. **Xem tổng quan & Bộ lọc**:
   - 3 thẻ chỉ số tài chính: *Tổng tiền dự kiến*, *Tổng tiền thực tế đã mua*, *Ngân sách còn lại*.
   - Bộ chọn thời gian (Hôm nay / Tuần này / Lịch sử).
3. **Phân nhóm hàng hóa & Thao tác tại chợ**:
   - Các mặt hàng được phân theo 9 nhóm: *Rau củ, Thịt, Cá & hải sản, Trứng & sữa, Gạo & ngũ cốc, Đậu & đồ khô, Gia vị, Đồ dùng gia đình, Khác*.
   - Người dùng chạm để đánh dấu đã mua (Checkbox), nhập giá thực tế tại chợ, thêm/sửa/xóa mặt hàng kèm cảnh báo xác nhận.
4. **Tạo danh sách tự động từ Thực đơn (Core Feature)**:
   - Người dùng bấm "Tạo danh sách từ thực đơn".
   - Hệ thống quét các món ăn trong khoảng thời gian đã chọn, trích xuất định lượng nguyên liệu.
   - So khớp với Kho nguyên liệu (Pantry) để tự động trừ bớt số lượng đã có.
   - Hiển thị bảng duyệt trước (Preview) chi tiết: *Cần dùng* - *Đã có trong kho* = *Cần mua*.
   - Người dùng xác nhận lưu danh sách.
5. **Quản lý Kho nguyên liệu (Pantry)**:
   - Tab phụ hoặc bảng quản lý xem các mặt hàng đang có trong nhà: Tên, Số lượng, Đơn vị, Ngày nhập, Hạn sử dụng, cảnh báo sắp hết/hết hạn.
6. **Ghi nhận chi tiêu một chạm**:
   - Khi hoàn thành mua sắm, nút "Ghi nhận chi tiêu" mở hộp thoại xác nhận số tiền thực tế, ngày mua và danh mục "Đi chợ".
   - Tạo bản ghi chi tiêu thật trong bảng `expenses`, ngăn chặn ghi nhận trùng lặp bằng cờ `isExpenseRecorded`.

### Thiết kế giao diện (Material 3 Consistency)
- **Màu sắc**: Màu xanh lá chủ đạo (`#16A34A`, `#22C55E`), bề mặt xanh ngọc nhẹ (`#F0FDF4`), thẻ trắng bo góc 16.dp, viền nhạt thanh thoát.
- **Phân nhóm màu danh mục**:
  - Rau củ: Xanh lá mạ (`#DCFCE7`)
  - Thịt: Đỏ cam nhạt (`#FEE2E2`)
  - Cá & hải sản: Xanh biển nhạt (`#E0F2FE`)
  - Trứng & sữa: Vàng kem (`#FEF3C7`)
  - Gia vị: Tím nhạt (`#F3E8FF`)
- **Tương tác**: Kích thước vùng bấm tối thiểu 48.dp, phản hồi xúc giác nhẹ nhàng, hiển thị badge số lượng đã mua/chưa mua trực quan.

---

## 3. Key Product Decisions & Trade-Offs

- **Quyết định 1: Cơ chế cộng gộp và quy đổi đơn vị an toàn**:
  - *Phương pháp*: Chỉ cộng gộp khi cùng danh tính nguyên liệu và cùng loại đơn vị đo lường (ví dụ: `g` với `g`, `kg` với `kg`, `quả` với `quả`). Nếu đơn vị không tương thích (ví dụ `gam` và `mớ`), hệ thống giữ thành 2 dòng riêng biệt để người dùng quyết định, tránh tính toán sai lệch thực tế.
- **Quyết định 2: Quản lý Kho nguyên liệu và Trừ kho**:
  - *Phương pháp*: Kho lưu trữ trong bảng Room `pantry_items`. Khi mua sắm xong, hệ thống hỏi người dùng qua hộp thoại xác nhận trước khi cập nhật số lượng vào kho, tránh việc tự động tăng kho ngoài ý muốn.
- **Quyết định 3: Chống trùng lặp giao dịch Chi tiêu**:
  - *Phương pháp*: Bản ghi `shopping_lists` lưu trường `expenseId: Int?` và `isExpenseRecorded: Boolean`. Khi đã ghi nhận, nút chuyển sang trạng thái "Đã ghi nhận chi tiêu" kèm mã liên kết. Nếu chỉnh sửa giá thực tế sau khi đã ghi nhận, hệ thống hiển thị tùy chọn cập nhật khoản chi tương ứng.

---

## 4. Technical Architecture & Data Strategy

```
┌─────────────────────────────────────────────────────────────┐
│                       UI LAYER                              │
│  FeaturesGridScreen (Item 2) ──► ShoppingScreen            │
│  HomeScreen / MenuScreen     ──► (Overview, Category Tabs,  │
│                                  Items, Dialogs)            │
│                                       │                     │
│  ┌─────────────────────────┐          │                     │
│  │ ShoppingPreviewDialog   │◄─────────┤                     │
│  │ PantryManagerDialog     │          │                     │
│  │ RecordExpenseDialog     │          │                     │
│  └─────────────────────────┘          │                     │
└───────────────────────────────────────┼─────────────────────┘
                                        ▼
┌─────────────────────────────────────────────────────────────┐
│                    VIEWMODEL LAYER                          │
│                      MainViewModel                          │
│    - shoppingListsState, currentShoppingList, shoppingItems │
│    - pantryItemsState, lowPantryItems                       │
│    - autoGenerateShoppingListFromMenu(startDate, endDate)   │
│    - toggleItemBought(), updateActualPrice(), recordExpense │
└─────────────────────────────────────────────────────────────┘
                                        ▼
┌─────────────────────────────────────────────────────────────┐
│                   REPOSITORY LAYER                          │
│                      AppRepository                          │
│    - calculateNetIngredients(meals, pantryItems)            │
│    - createShoppingListFromPreview()                        │
│    - recordShoppingExpense()                                │
│    - updatePantryFromPurchasedItems()                       │
└─────────────────────────────────────────────────────────────┘
                                        ▼
┌─────────────────────────────────────────────────────────────┐
│                   LOCAL DATABASE (Room v3)                  │
│  AppDatabase (ai_bep_nha.db)                                │
│  ├── meals                                                  │
│  ├── expenses                                               │
│  ├── shopping_lists (id, title, dateRange, est, act, expId) │
│  ├── shopping_items (id, listId, name, category, qty, unit) │
│  └── pantry_items (id, name, quantity, unit, expiryDate)    │
└─────────────────────────────────────────────────────────────┘
```

### Các Entity Mới và Cập Nhật (Room Database)
1. **`ShoppingListEntity`**:
   - `id: Int` (PrimaryKey, autoGenerate)
   - `title: String` ("Đi chợ tuần 42 - Tháng 10")
   - `dateRange: String` ("12/10 - 18/10/2026")
   - `budget: Long` (Ngân sách đặt ra, mặc định 1.200.000 đ)
   - `estimatedTotal: Long`
   - `actualTotal: Long`
   - `isCompleted: Boolean`
   - `isExpenseRecorded: Boolean`
   - `expenseId: Int?` (Liên kết với bảng `expenses`)
   - `createdAt: Long`

2. **`ShoppingItemEntity`** (Mở rộng):
   - `id: Int` (PrimaryKey, autoGenerate)
   - `listId: Int` (Khóa ngoại logic tới `ShoppingListEntity`)
   - `name: String`
   - `category: String` (Rau củ, Thịt, Cá và hải sản, v.v.)
   - `quantity: Double`
   - `unit: String`
   - `estimatedPrice: Long`
   - `actualPrice: Long`
   - `isBought: Boolean`
   - `note: String`
   - `originalNeededQty: Double` (Số lượng cần theo thực đơn)
   - `pantryDeductedQty: Double` (Số lượng đã trừ từ kho)

3. **`PantryItemEntity`**:
   - `id: Int` (PrimaryKey, autoGenerate)
   - `name: String`
   - `quantity: Double`
   - `unit: String`
   - `category: String`
   - `importDate: String`
   - `expiryDate: String`
   - `note: String`
   - `updatedAt: Long`

---

## 5. Implementation Steps & Verification Plan

### Bước 1: Cấu trúc Dữ liệu & Room Database (v3)
- Bổ sung `PantryItemEntity`, `ShoppingListEntity`, mở rộng `ShoppingItemEntity` trong `Entities.kt`.
- Tạo `PantryDao` và cập nhật `ShoppingDao`.
- Nâng cấp `AppDatabase` lên version 3 với `fallbackToDestructiveMigration(true)`.

### Bước 2: Logic Nghiệp vụ trong `AppRepository` & `MainViewModel`
- Viết thuật toán phân tích nguyên liệu:
  - Bóc tách chuỗi nguyên liệu từ các `MealEntity` được chọn.
  - Áp dụng hệ số khẩu phần (portion scaling).
  - So khớp chuẩn hóa tên nguyên liệu (case-insensitive, bỏ dấu khoảng trắng).
  - Trừ số lượng kho tương ứng từ `pantry_items`.
- Triển khai chức năng ghi nhận chi tiêu không trùng lặp (tạo `ExpenseEntity` danh mục "Đi chợ" kèm cờ `isExpenseRecorded`).
- Triển khai cập nhật kho theo xác nhận của người dùng.

### Bước 3: Xây dựng Giao diện Màn hình Đi chợ (`ShoppingScreen.kt`)
- **Header**: Tiêu đề "Đi chợ", mô tả phụ, bộ chọn thời gian, nút lịch sử danh sách, nút Kho nguyên liệu.
- **3 Thẻ Tổng quan**: Dự kiến, Thực tế, Ngân sách còn lại.
- **Danh sách mặt hàng theo 9 nhóm**: Cho phép thu gọn/mở rộng từng nhóm, checkbox đánh dấu đã mua, nhập giá thực tế tức thì, thêm/sửa/xóa từng dòng.
- **Hộp thoại xem trước khi tạo từ thực đơn** (`AutoGenerateShoppingDialog`).
- **Hộp thoại Kho nguyên liệu** (`PantryManagerDialog`).
- **Hộp thoại Ghi nhận chi tiêu** (`RecordExpenseDialog`).
- **Hộp thoại Lịch sử đi chợ** (`ShoppingHistoryDialog`).

### Bước 4: Tích hợp Điều hướng & Phím tắt
- Thêm `AppScreen.SHOPPING` vào `AppScreen` enum trong `MainViewModel`.
- Kết nối mục 2 "Đi chợ" trong `FeaturesGridScreen.kt` tới `AppScreen.SHOPPING`.
- Cập nhật nút "Tạo danh sách đi chợ" trên `MenuScreen.kt` để chuyển tiếp mượt mà sang `ShoppingScreen`.
- Hỗ trợ `BackHandler` quay về màn hình trước đó chuẩn xác.

### Bước 5: Kiểm thử Toàn diện (Unit Tests & Robolectric)
- Chạy unit tests:
  - Thuật toán bóc tách và cộng gộp nguyên liệu cùng đơn vị.
  - Kiểm tra việc trừ số lượng nguyên liệu có sẵn trong kho.
  - Kiểm tra tính toán ngân sách và tổng tiền dự kiến/thực tế.
  - Kiểm tra logic chống trùng lặp giao dịch chi tiêu.
- Chạy `gradle :app:testDebugUnitTest` và `compile_applet` để xác nhận thành công 100%.
