# 🎓 Student Information Management System

A comprehensive Android application for managing student records, certificates, and user accounts with role-based access control. Built with modern Android development practices using **Kotlin** and **Jetpack Compose**.

> **Note**: This project was developed as part of the Mobile Application Development course at Ton Duc Thang University (2025) and has been continuously improved to demonstrate professional Android development skills for internship applications.

---

## � Screenshots

[Add screenshots here]

---

## ✨ Key Features

### 🔐 Authentication & Security
- **Firebase Authentication** with email/password
- **Password Reset** via email
- **Remember Me** functionality with encrypted credential storage (EncryptedSharedPreferences)
- **Role-based Access Control** (Admin, Manager, Employee)
- **Account Status Management** (Active/Inactive users)
- **Login History Tracking** with timestamps

### 👥 User Management (Admin Only)
- **Create Users** with automatic password reset email
- **Edit User Information** (name, email, phone, birthday, role, status)
- **Delete Users** (with Admin protection)
- **View User Profiles** with detailed information
- **Search & Filter Users** by name, email, phone, role, or status
- **Sort Users** alphabetically (A→Z, Z→A) with Vietnamese collation support
- **Profile Picture Upload** to Firebase Storage

### 🎓 Student Management (Admin & Manager)
- **Add Students** with validation (ID, email, phone uniqueness)
- **Edit Student Information** (name, email, phone, birthday, class, faculty)
- **Delete Students** with confirmation
- **View Student Profiles** with certificate list
- **Advanced Search** by name, email, phone, ID, class, or faculty
- **Multi-criteria Filtering**:
  - Filter by faculty
  - Filter by class
  - Filter by minimum number of certificates
  - Sort alphabetically with Vietnamese support
- **Bulk Import** students from CSV files
- **Export** student list to CSV (Downloads folder)
- **Profile Picture Management** with Firebase Storage

### 🏅 Certificate Management (Admin & Manager)
- **Add Certificates** to student records
- **Edit Certificate Details** (title, course, ID, organization, dates)
- **Delete Certificates** from student profiles
- **View Certificate Details** with full information
- **Certificate Validation** (unique ID per student)
- **Bulk Import** certificates from CSV files
- **Export** certificate list to CSV per student
- **Expiration Date Tracking** (optional)

### 📊 Additional Features
- **Real-time Data Sync** with Firestore listeners
- **Swipe Actions** for quick edit/delete operations
- **Input Validation** with error messages
- **Toast Notifications** for user feedback
- **Responsive UI** with Material Design 3
- **Vietnamese Language Support** for sorting and collation
- **Image Caching** with Coil library
- **Offline-first Architecture** considerations

---

## 🏗️ Technical Architecture

### Architecture Pattern
- **MVVM (Model-View-ViewModel)** for clean separation of concerns
- **Unidirectional Data Flow** with StateFlow
- **Repository Pattern** (implicit via ViewModels)

### Tech Stack

#### Core Technologies
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (100% declarative UI)
- **Min SDK**: 29 (Android 10)
- **Target SDK**: 35

#### Firebase Services
- **Firebase Authentication** - User authentication
- **Cloud Firestore** - Real-time NoSQL database
- **Firebase Storage** - Image storage and retrieval
- **Firebase BOM**: 34.8.0

#### Jetpack Libraries
- **Compose BOM**: 2026.01.01
- **Material3**: 1.4.0 - Modern Material Design
- **Navigation Compose**: 2.9.7 - Type-safe navigation
- **Lifecycle ViewModel**: 2.10.0 - State management
- **Activity Compose**: 1.12.3

#### Additional Libraries
- **Coil**: 2.7.0 - Image loading and caching
- **Swipe**: 1.3.0 - Swipe gesture actions
- **EncryptedSharedPreferences** - Secure credential storage
- **BCrypt**: 0.9.0 - Password hashing

---

## 📂 Project Structure

```
app/src/main/java/com/example/studentinformationmanagement/
├── MainActivity.kt                 # Single Activity entry point
├── AppScreen.kt                    # Navigation graph setup
├── data/
│   ├── shared/                     # Shared data models
│   └── uiState/                    # UI state classes
│       ├── LoginUiState.kt
│       ├── AdminUiState.kt
│       └── ManagerUiState.kt
└── ui/
    ├── auth/                       # Authentication screens
    ├── home/                       # Role-based home screens
    ├── student/                    # Student management
    │   └── certificate/            # Certificate sub-feature
    ├── user/                       # User management
    ├── shared/                     # Reusable components
    ├── theme/                      # App theming
    └── viewModel/                  # Business logic
        ├── LoginViewModel.kt
        ├── AdminViewModel.kt
        └── ManagerViewModel.kt
```

---

## 🔑 User Roles & Permissions

| Feature | Admin | Manager | Employee |
|---------|-------|---------|----------|
| **Student Management** | ✅ Full CRUD | ✅ Full CRUD | 👁️ View Only |
| **Certificate Management** | ✅ Full CRUD | ✅ Full CRUD | 👁️ View Only |
| **User Management** | ✅ Full CRUD | 👁️ View Only | 👁️ View Only |
| **Login History** | ✅ View All | ❌ No Access | ❌ No Access |
| **Profile Picture Update** | ✅ Yes | ✅ Yes | ✅ Yes |
| **CSV Import/Export** | ✅ Yes | ✅ Yes | ❌ No |

---

## 🗄️ Database Schema

### Firestore Collections

#### `users` Collection
```javascript
{
  "email": "admin@tdtu.edu.vn",  // Document ID
  "name": "Nguyễn Minh Tuấn",
  "birthday": "15/03/1985",
  "phone": "0901234567",
  "role": "Admin",               // Admin | Manager | Employee
  "status": "Active",            // Active | Inactive
  "imageUrl": "https://..."
}
```

#### `students` Collection
```javascript
{
  "id": "519H0001",              // Document ID
  "name": "Nguyễn Văn An",
  "birthday": "15/05/2001",
  "email": "519h0001@student.tdtu.edu.vn",
  "phone": "0911111111",
  "stdClass": "519H0",
  "faculty": "Công nghệ Thông tin",
  "imageUrl": "https://...",
  "certificates": [              // Array of Certificate objects
    {
      "id": "CERT-JAVA-001",
      "title": "Java Programming Certificate",
      "courseName": "Advanced Java Development",
      "issuingOrganization": "Oracle University",
      "issueDate": "15/06/2023",
      "expirationDate": "15/06/2026"
    }
  ]
}
```

#### `loginHistory` Collection
```javascript
{
  "loginTime": 1738310400000,    // Timestamp (Long)
  "email": "admin@tdtu.edu.vn",
  "name": "Nguyễn Minh Tuấn",
  "role": "Admin",
  "imageUrl": "https://..."
}
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 29+
- Firebase project with Authentication, Firestore, and Storage enabled

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/student-information-management.git
   cd student-information-management
   ```

2. **Setup Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app with package name: `com.example.studentinformationmanagement`
   - Download `google-services.json` and place it in `app/` directory
   - Enable Email/Password authentication
   - Create Firestore database
   - Enable Firebase Storage

3. **Configure Firestore Security Rules**
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{userId} {
         allow read: if request.auth != null;
         allow write: if request.auth != null && 
                        (request.auth.token.email == userId || 
                         get(/databases/$(database)/documents/users/$(request.auth.token.email)).data.role == 'Admin');
       }
       
       match /students/{studentId} {
         allow read: if request.auth != null;
         allow write: if request.auth != null && 
                        get(/databases/$(database)/documents/users/$(request.auth.token.email)).data.role in ['Admin', 'Manager'];
       }
       
       match /loginHistory/{historyId} {
         allow read: if request.auth != null;
         allow create: if request.auth != null;
       }
     }
   }
   ```

4. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

### Initial Setup

1. **Create Admin User**
   - Go to Firebase Console → Authentication → Add User
   - Create user with email: `admin@tdtu.edu.vn`
   - Go to Firestore → Create `users` collection
   - Add document with ID: `admin@tdtu.edu.vn`
   - Set fields: `name`, `birthday`, `phone`, `role: "Admin"`, `status: "Active"`

2. **Import Sample Data** (Optional)
   - Sample data files are available in `firebase-sample-data/` directory
   - Follow instructions in `firebase-sample-data/IMPORT_GUIDE.md`

---

## 📝 CSV Import/Export Format

### Students CSV Format
```csv
studentName,studentBirthday,studentEmail,studentPhoneNumber,studentId,studentClass,studentFaculty
Nguyễn Văn An,15/05/2001,519h0001@student.tdtu.edu.vn,0911111111,519H0001,519H0,Công nghệ Thông tin
```

### Certificates CSV Format
```csv
certificateTitle,courseName,certificateId,issuingOrganization,issueDate,expirationDate
Java Programming,Advanced Java,CERT-001,Oracle,15/06/2023,15/06/2026
```

---

## 🎯 Key Learning Outcomes

This project demonstrates proficiency in:

- ✅ **Modern Android Development** with Jetpack Compose
- ✅ **MVVM Architecture** and state management
- ✅ **Firebase Integration** (Auth, Firestore, Storage)
- ✅ **Material Design 3** implementation
- ✅ **Complex UI/UX** with navigation and gestures
- ✅ **Data Validation** and error handling
- ✅ **File I/O Operations** (CSV import/export)
- ✅ **Security Best Practices** (encrypted storage, role-based access)
- ✅ **Real-time Data Synchronization**
- ✅ **Clean Code Principles** and project organization

---

## 🔮 Future Enhancements

- [ ] Offline mode with local caching
- [ ] Push notifications for important updates
- [ ] Advanced analytics dashboard
- [ ] PDF export for certificates
- [ ] Multi-language support (English/Vietnamese)
- [ ] Dark mode theme
- [ ] Biometric authentication
- [ ] Student attendance tracking
- [ ] Grade management system

---

## 📄 License

This project is developed for educational purposes as part of university coursework and portfolio demonstration.

---

## 👤 Author

**[Your Name]**
- Email: [your.email@example.com]
- LinkedIn: [Your LinkedIn Profile]
- GitHub: [@yourusername](https://github.com/yourusername)

---

## 🙏 Acknowledgments

- Ton Duc Thang University - Mobile Application Development Course
- Firebase for backend services
- Android Jetpack Compose team for excellent documentation
- Open source community for libraries and resources

---

**⭐ If you find this project helpful, please consider giving it a star!**
