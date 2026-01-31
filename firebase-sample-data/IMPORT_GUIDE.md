# Hướng Dẫn Import Dữ Liệu Mẫu vào Firebase

## Tổng Quan Collections

Project này có **3 collections** trong Firestore:

1. **users** - 6 users (1 Admin, 2 Managers, 2 Employees, 1 Inactive)
2. **students** - 10 sinh viên từ 3 khoa khác nhau với certificates
3. **loginHistory** - 5 records lịch sử đăng nhập

---

## Cách 1: Import Thủ Công qua Firebase Console (Khuyến nghị)

### Bước 1: Import Users Collection

1. Vào [Firebase Console](https://console.firebase.google.com/)
2. Chọn project của bạn
3. Vào **Firestore Database**
4. Click **Start collection**
5. Collection ID: `users`
6. Với mỗi user trong file `users.json`:
   - Document ID = email (ví dụ: `admin@tdtu.edu.vn`)
   - Copy paste các fields từ JSON

**Lưu ý quan trọng**: Document ID phải là **email** của user!

### Bước 2: Import Students Collection

1. Click **Start collection** (hoặc Add collection nếu đã có collection)
2. Collection ID: `students`
3. Với mỗi student trong file `students.json`:
   - Document ID = student ID (ví dụ: `519H0001`)
   - Copy paste các fields từ JSON
   - Field `certificates` là **array** chứa các objects

**Lưu ý**: Document ID phải là **student ID** (519H0001, 520H0001, etc.)!

### Bước 3: Import Login History Collection

1. Click **Start collection**
2. Collection ID: `loginHistory`
3. Với mỗi record trong file `loginHistory.json`:
   - Document ID: **Auto-ID** (để Firebase tự generate)
   - Copy paste các fields từ JSON

---

## Cách 2: Import bằng Firebase CLI (Nâng cao)

### Cài đặt Firebase CLI

```bash
npm install -g firebase-tools
firebase login
```

### Import dữ liệu

Tạo file script `import-data.js`:

```javascript
const admin = require('firebase-admin');
const serviceAccount = require('./path-to-your-service-account-key.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// Import users
const users = require('./users.json');
Object.keys(users).forEach(async (email) => {
  await db.collection('users').doc(email).set(users[email]);
  console.log(`Imported user: ${email}`);
});

// Import students
const students = require('./students.json');
Object.keys(students).forEach(async (id) => {
  await db.collection('students').doc(id).set(students[id]);
  console.log(`Imported student: ${id}`);
});

// Import login history
const loginHistory = require('./loginHistory.json');
loginHistory.forEach(async (record) => {
  await db.collection('loginHistory').add(record);
  console.log('Imported login history record');
});
```

Chạy script:
```bash
node import-data.js
```

---

## Cách 3: Import bằng Firestore Import/Export (Nhanh nhất)

Nếu bạn có file backup Firestore format, dùng lệnh:

```bash
gcloud firestore import gs://[BUCKET_NAME]/[EXPORT_PREFIX]
```

---

## Tạo Authentication Users

**Quan trọng**: Sau khi import collection `users`, bạn cần tạo Firebase Authentication accounts:

1. Vào **Authentication** → **Users** trong Firebase Console
2. Click **Add user** cho mỗi email trong `users.json`:
   - Email: `admin@tdtu.edu.vn`
   - Password: Đặt password tạm (ví dụ: `Admin@123`)
   - Lặp lại cho tất cả users

3. Gửi email reset password cho users:
   - App sẽ tự động gửi khi admin tạo user mới
   - Hoặc click **Reset password** trong Authentication console

---

## Dữ Liệu Mẫu Chi Tiết

### Users (6 accounts)
- **admin@tdtu.edu.vn** - Nguyễn Minh Tuấn (Admin, Active)
- **manager@tdtu.edu.vn** - Trần Thị Hương (Manager, Active)
- **manager2@tdtu.edu.vn** - Lê Văn Hải (Manager, Active)
- **employee@tdtu.edu.vn** - Phạm Thị Mai (Employee, Active)
- **employee2@tdtu.edu.vn** - Hoàng Văn Đức (Employee, Active)
- **inactive@tdtu.edu.vn** - Nguyễn Thị Lan (Employee, Inactive)

### Students (10 sinh viên)
- **519H0001, 519H0002, 519H0003** - Lớp 519H0, Khoa CNTT
- **520H0001, 520H0002** - Lớp 520H0, Khoa CNTT
- **521K0001, 521K0002** - Lớp 521K0, Khoa Kinh tế
- **522M0001, 522M0002** - Lớp 522M0, Khoa Cơ khí

### Certificates
- Một số sinh viên có certificates, một số không
- Certificates có thể có hoặc không có expiration date

---

## Test Login

Sau khi import xong, test login với:
- Email: `admin@tdtu.edu.vn`
- Password: (password bạn đã set trong Authentication)

---

## Lưu Ý

1. **Document IDs quan trọng**:
   - Users: Document ID = email
   - Students: Document ID = student ID
   - LoginHistory: Auto-generated ID

2. **Firestore Rules**: Đảm bảo đã setup security rules như đã hướng dẫn trước đó

3. **Indexes**: Nếu app báo lỗi về indexes, Firebase sẽ tự động suggest tạo index, click vào link để tạo

4. **Images**: URLs trong dữ liệu mẫu là placeholder, bạn có thể thay bằng URLs thật sau
