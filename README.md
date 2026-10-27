# 🎯 STUDYQUEST - ỨNG DỤNG ANDROID VƯỢT QUA TRÌ TRỆ HỌC TẬP



---

## 📱 CẤU TRÚC DỰ ÁN

Dự án bao gồm 2 phiên bản hoàn chỉnh:

1. **`studyquest-app/`**: **Ứng dụng Mobile Web App & PWA (Chạy thử ngay lập tức)**
   - Trải nghiệm tương tác 100% các tính năng trên mọi trình duyệt hoặc cài đặt trực tiếp lên điện thoại Android (PWA / Chrome Add to Home Screen).
   - Có khung giả lập điện thoại thực tế, tích hợp hiệu ứng âm thanh (Web Audio API), pháo hoa chúc mừng (Confetti), lưu trữ dữ liệu tự động (`localStorage`).
2. **`studyquest-android-native/`**: **Dự án Native Android Studio (Kotlin + Jetpack Compose)**
   - Dự án chuẩn cấu trúc Gradle hiện đại (`build.gradle.kts`, Version Catalog `libs.versions.toml`).
   - Sử dụng **Jetpack Compose + Material 3 + StateFlow + ViewModel**, sẵn sàng mở trực tiếp trong Android Studio để build file APK cài đặt lên máy thật hoặc máy ảo Android.

---

## 🌟 CÁC TÍNH NĂNG CỐT LÕI ĐÃ HIỆN THỰC

### 1. ⏱️ Quy tắc 5 Phút (Vượt Qua Trì Trệ)
- **Tâm lý học (Gollwitzer, 1999):** Hạ thấp ngưỡng bắt đầu đến mức gần như không thể từ chối (chỉ cần mở file và viết 1 dòng).
- Đồng hồ đếm ngược vòng tròn 5 phút (`05:00` ➔ `00:00`) với lời nhắc: *"Bạn chỉ cần làm trong 5 phút, hết giờ bạn hoàn toàn có quyền dừng lại mà không có tội gì."*
- **Lựa chọn linh hoạt, không ép buộc:**
  - `🔴 Nghỉ ngơi (Không có EXP)`: Được quyền dừng lại nếu không muốn tiếp tục.
  - `🔥 Làm tiếp (Vào 25m Pomodoro • Nhận 50 EXP)`: Khi đã vào guồng, chuyển ngay sang phiên tập trung sâu.

### 2. 📌 Hôm Nay (Top 3 Focus Tasks)
- Giới hạn tối đa **3 việc quan trọng nhất ngày** để giải phóng áp lực nhận thức cho sinh viên.
- Thẻ task hiển thị: môn học, thời lượng (25m), điểm thưởng (+50 EXP), nút bấm bắt đầu tập trung ngay.
- Checkbox hoàn thành kèm hiệu ứng âm thanh và pháo hoa chúc mừng.

### 3. 🧩 Bẻ Nhỏ Bài Tập Lớn & Bảng Kanban (Micro-Breakdown)
- Theo kỹ thuật Pomodoro (Cirillo, 2018): Thay vì để một bài tập mơ hồ treo trên đầu gây lo lắng (ví dụ: *"Tiểu luận 8 trang"*), công cụ tự động phân rã thành các thẻ 25 phút cụ thể.
- Bảng Kanban 3 cột: **Cần làm**, **Đang làm**, **Hoàn thành**.

### 4. 🪙 Hệ Thống Gamification & Quỹ Giải Trí "Không Tội Lỗi"
- Mỗi task xong nhận ngay **+50 EXP**.
- Cứ tích lũy đủ 300 EXP thăng 1 cấp độ: *Tân binh ➔ Thợ săn Deadline (Lv. 2) ➔ Chiến thần tập trung ➔ Bậc thầy năng suất*.
- **Quy đổi thời gian giải trí:** Tỷ lệ 100 EXP = 20 phút giải trí (5 EXP = 1 phút).
- **Quầy đổi giải trí "Không tội lỗi" (Shop):**
  - 🎮 Chơi 1 trận game / 20 phút (100 EXP)
  - 🎬 Xem 1 tập phim anime / 25 phút (125 EXP)
  - ☕ Lướt MXH / Đi cà phê 15 phút (75 EXP)
  - 🎵 Nghe nhạc lofi thư giãn / 10 phút (50 EXP)
- Khi đổi thưởng: Mở **Vé Giải Trí Không Tội Lỗi (Guilt-Free Pass)** với đồng hồ đếm ngược giờ giải trí chính đáng, xóa tan cảm giác áy náy!

### 5. 👥 Góc Đồng Hành (Study Buddy Accountability)
- Giám sát chéo và tạo cam kết cùng bạn học (`@Minh - 🟢 Đã hoàn thành 2/3 task`).
- Nút **`[ 📢 Gửi Ping ]`** với các mẫu tin nhắn khích lệ, nhắc nhở tức thì.
- Bot StudyQuest tự động thông báo và cổ vũ.

---

## 🚀 HƯỚNG DẪN TRẢI NGHIỆM ỨNG DỤNG

### Cách 1: Chạy thử tương tác ngay trên máy (Đang chạy sẵn)
Máy chủ cục bộ đang chạy tại cổng 3000. Bạn chỉ cần mở trình duyệt và truy cập:
👉 **`http://localhost:3000/`**

*(Nếu máy chủ tắt, bạn có thể khởi động lại bằng cách chạy lệnh:)*
```powershell
powershell -ExecutionPolicy Bypass -File "d:\Projects\StudyQuest (Android)\studyquest-app\serve.ps1"
```

### Cách 2: Mở & Build Native Android Studio
1. Mở **Android Studio**.
2. Chọn **Open** ➔ chọn thư mục: `D:\Projects\StudyQuest (Android)\studyquest-android-native`.
3. Chờ Gradle đồng bộ (Sync Project with Gradle Files).
4. Nhấn nút **Run (Shift + F10)** để cài đặt lên thiết bị thật hoặc máy ảo Android Emulator.
