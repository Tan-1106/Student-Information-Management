# 🎓 Student Information Management

An Android application for managing student records, built with **Kotlin** and **Jetpack Compose**, using **Firebase** for authentication and real-time data handling.

This project was originally developed as a team assignment in the *Mobile Application Development* course at **Ton Duc Thang University (TDTU)** in 2025.  
Although it was designed as a group project, I was the primary developer and took full responsibility for the architecture, feature implementation, and interface design. After the course ended, I continued improving and refining the project to enhance its usability, code structure, and visual presentation.

> 📌 This project demonstrates my practical experience with:
> - Firebase Authentication & Firestore integration  
> - Role-based access control  
> - Modern Android UI using Jetpack Compose  
> - MVVM architecture and clean code practices  
>
> ✅ It is included in my CV to support internship applications in Android development.

---

## 🚀 Features

### 🔐 Authentication
- Sign in using email and password
- Password reset via email
- Admin-created accounts receive a reset password email from Firebase

### 👥 Role-based Access Control

| Role      | Student Management | Certificate Management | User Management | Avatar Update |
|-----------|--------------------|-------------------------|-----------------|---------------|
| **Admin**   | ✅ Full control     | ✅ Full control          | ✅ Add/Edit/Delete users | ✅ |
| **Manager** | ✅ Add/Edit/Delete | ✅ Add/Edit/Delete       | ❌               | ✅ |
| **Employee**| ❌ View only       | ❌ View only             | ❌               | ✅ |

---

## 📋 Student Management

- View student list with filter and sorting
- View detailes, add, edit, delete students
- Upload and download student list (xlsx)

## 🏅 Certificate Management

- Manage certificates for individual students
- View details, add, edit, delete certificates
- Upload and download certificate lists (xlsx)

## 👨‍💼 User Management (Admin only)

- Create users with specific roles (Admin, Manager, Employee)
- Edit or delete existing users
- Send reset password email for account setup

---

## 🎨 UI/UX Highlights

- Built with Jetpack Compose for a modern, declarative UI
- Clean layout, intuitive navigation, and responsive design
- Focused on ease of use for different user roles (Admin, Manager, Employee)

---

## 🛠️ Built With

- 💻 **Kotlin** + **Jetpack Compose**
- 🔥 **Firebase Authentication** + **Firestore**
- ☁️ Firebase Storage (for future file uploads/downloads)
- 🧩 **MVVM Architecture**
- 🛠️ Gradle Build System
