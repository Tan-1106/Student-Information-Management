package com.example.studentinformationmanagement.ui.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.manager.ManagerUiState
import com.example.studentinformationmanagement.data.manager.Student
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ManagerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState: StateFlow<ManagerUiState> = _uiState.asStateFlow()

    var facultyList by mutableStateOf<List<String>>(emptyList())
        private set
    var classList by mutableStateOf<List<String>>(emptyList())
        private set

    var idList by mutableStateOf<List<String>>(emptyList())
        private set
    var emailList by mutableStateOf<List<String>>(emptyList())
        private set
    var phoneList by mutableStateOf<List<String>>(emptyList())
        private set

    // Fetch Student List
    private var fullStudentList: List<Student> = emptyList()

    init {
        fetchStudentsFromFirestore()
    }

    private fun fetchStudentsFromFirestore() {
        Firebase.firestore.collection("students")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    Log.e("Firestore", "Error fetching students: ${e?.message}")
                    return@addSnapshotListener
                }

                val students = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Student::class.java)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Parsing error: ${e.message}")
                        null
                    }
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        studentList = students
                    )
                }
                // For search feature
                fullStudentList = students
                facultyList = students.map { it.studentFaculty }.distinct().sorted()
                classList = students.map { it.studentClass }.distinct().sorted()

                // For checking duplicate
                idList = students.map { it.studentId }.distinct()
                emailList = students.map { it.studentEmail }.distinct()
                phoneList = students.map { it.studentPhoneNumber }.distinct()
            }
    }

    // Search bar
    var searchInput by mutableStateOf("")
        private set

    fun onStudentSearch(userSearchInput: String) {
        searchInput = userSearchInput

        val keyword = searchInput.trim().lowercase()

        if (keyword.isEmpty()) {
            fetchStudentsFromFirestore()
        } else {
            val filteredList = fullStudentList.filter { student ->
                student.studentName.contains(
                    keyword,
                    ignoreCase = true
                ) || student.studentEmail.contains(keyword, ignoreCase = true) ||
                        student.studentPhoneNumber.contains(
                            keyword,
                            ignoreCase = true
                        ) || student.studentId.contains(keyword, ignoreCase = true) ||
                        student.studentClass.contains(
                            keyword,
                            ignoreCase = true
                        ) || student.studentFaculty.contains(keyword, ignoreCase = true)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    studentList = filteredList
                )
            }
        }
    }

    // Filter feature
    var isShowDialog by mutableStateOf(false)
        private set
    private var sortSelected by mutableStateOf("")
    private var minimumCertificates by mutableStateOf("")
    private var facultySelected by mutableStateOf("")
    private var classSelected by mutableStateOf("")

    fun onSortSelected(userInput: String) {
        sortSelected = userInput
    }

    fun onMinimumCertificatesInput(userInput: String) {
        minimumCertificates = userInput
    }

    fun onFacultyPick(userInput: String) {
        facultySelected = userInput
    }

    fun onClassPick(userInput: String) {
        classSelected = userInput
    }

    fun onFilterClick() {
        isShowDialog = true
    }

    fun onDismissFilterClick() {
        isShowDialog = false
    }

    fun onApplyFilterClick() {
        var filtered = fullStudentList

        val minCert = minimumCertificates.toIntOrNull() ?: 0
        filtered = filtered.filter { it.studentCertificates.size >= minCert }

        if (facultySelected.isNotBlank()) {
            filtered = filtered.filter { it.studentFaculty == facultySelected }
        }

        if (classSelected.isNotBlank()) {
            filtered = filtered.filter { it.studentClass == classSelected }
        }

        filtered = when (sortSelected) {
            "A → Z" -> filtered.sortedBy { it.studentName }
            "Z → A" -> filtered.sortedByDescending { it.studentName }
            else -> filtered
        }

        _uiState.update { currentState ->
            currentState.copy(studentList = filtered)
        }

        isShowDialog = false
    }

    fun onClearFilterClick() {
        sortSelected = ""
        minimumCertificates = ""
        facultySelected = ""
        classSelected = ""

        _uiState.update { currentState ->
            currentState.copy(studentList = fullStudentList)
        }
        isShowDialog = false
    }

    // Student's detail profile
    fun onStudentSeeMoreClicked(
        studentPhoneNumber: String,
        navController: NavHostController,
    ) {
        val student = fullStudentList.find { it.studentPhoneNumber == studentPhoneNumber }
        student?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedStudent = student
                )
            }

            navController.navigate(AppScreen.StudentDetailProfile.name)
        } ?: run {
            Log.e("ManagerViewModel", "Student not found with phone number: $studentPhoneNumber")
        }
    }

    // Add Student
    fun onAddStudentButtonClicked(navController: NavController) {
        navController.navigate(AppScreen.AddStudent.name)
    }

    var newStudentName by mutableStateOf("")
        private set
    var newStudentEmail by mutableStateOf("")
        private set
    var newStudentPhone by mutableStateOf("")
        private set
    var newStudentBirthday by mutableStateOf("")
        private set
    var newStudentId by mutableStateOf("")
        private set
    var newStudentClass by mutableStateOf("")
        private set
    var newStudentFaculty by mutableStateOf("")
        private set

    fun onNewStudentNameChange(userInput: String) {
        newStudentName = userInput
    }

    fun onNewStudentEmailChange(userInput: String) {
        newStudentEmail = userInput
    }

    fun onNewStudentPhoneChange(userInput: String) {
        newStudentPhone = userInput
    }

    fun onNewStudentIdChange(userInput: String) {
        newStudentId = userInput
    }

    fun onNewStudentClassChange(userInput: String) {
        newStudentClass = userInput
    }

    fun onNewStudentBirthdayPick(userInput: String) {
        newStudentBirthday = userInput
    }

    fun onNewStudentFacultyChange(userInput: String) {
        newStudentFaculty = userInput
    }

    fun clearAddStudentInputs() {
        newStudentName = ""
        newStudentEmail = ""
        newStudentPhone = ""
        newStudentId = ""
        newStudentClass = ""
        newStudentBirthday = ""
        newStudentFaculty = ""
    }

    var nameError by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set
    var phoneError by mutableStateOf("")
        private set
    var idError by mutableStateOf("")
        private set
    var classError by mutableStateOf("")
        private set
    var birthdayError by mutableStateOf("")
        private set
    var facultyError by mutableStateOf("")
        private set
    fun clearErrorMessage() {
        nameError = ""
        emailError = ""
        phoneError = ""
        idError = ""
        classError = ""
        birthdayError = ""
        facultyError = ""
    }

    fun onAddStudentButtonClick(
        navController: NavHostController,
        context: Context
    ) {


        val name = newStudentName.trim()
        val email = newStudentEmail.trim()
        val phone = newStudentPhone.trim()
        val id = newStudentId.trim()
        val studentClass = newStudentClass.trim()
        val birthday = newStudentBirthday.trim()
        val faculty = newStudentFaculty.trim()

        if (validateUserInputs(newName = name, newEmail = email, newPhone = phone, newBirthday = birthday, newId = id, newClass = studentClass, newFaculty = faculty)) {
            val db = Firebase.firestore

            db.collection("students")
                .whereIn("studentId", listOf(id))
                .get()
                .addOnSuccessListener { idResult ->
                    if (idResult.isEmpty) {
                        db.collection("students")
                            .whereIn("studentEmail", listOf(email))
                            .get()
                            .addOnSuccessListener { emailResult ->
                                if (emailResult.isEmpty) {
                                    db.collection("students")
                                        .whereIn("studentPhoneNumber", listOf(phone))
                                        .get()
                                        .addOnSuccessListener { phoneResult ->
                                            if (phoneResult.isEmpty) {
                                                val newStudent = Student(
                                                    studentName = name,
                                                    studentBirthday = birthday,
                                                    studentEmail = email,
                                                    studentPhoneNumber = phone,
                                                    studentId = id,
                                                    studentClass = studentClass,
                                                    studentFaculty = faculty
                                                )

                                                db.collection("students")
                                                    .document(id)
                                                    .set(newStudent)
                                                    .addOnSuccessListener {
                                                        clearErrorMessage()
                                                        clearAddStudentInputs()
                                                        navController.navigateUp()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Cannot add student",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "Cannot check existing phone number",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Cannot check existing email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Cannot check existing student's ID",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    fun validateUserInputs(
        newName: String,
        currentEmail: String = "",
        newEmail: String,
        currentPhone: String = "",
        newPhone: String,
        newBirthday: String,
        currentId: String = "",
        newId: String,
        newClass: String,
        newFaculty: String,
        existingEmails: List<String> = emailList,
        existingPhones: List<String> = phoneList,
        existingIds: List<String> = idList
    ): Boolean {
        var isValid = true

        if (newName.isEmpty()) {
            nameError = "Name is required"
            isValid = false
        } else {
            nameError = ""
        }

        if (newEmail.isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail.trim()).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else if (currentEmail != "" && currentEmail == newEmail) {
            phoneError = ""
            isValid = true
        } else if (existingEmails.contains(newEmail)) {
            emailError = "Email already exists"
            isValid = false
        } else {
            emailError = ""
        }

        if (newPhone.isEmpty()) {
            phoneError = "Phone number is required"
            isValid = false
        } else if (newPhone.trim().length != 10) {
            phoneError = "Invalid phone number"
            isValid = false
        } else if (currentPhone != "" && currentPhone == newPhone) {
            phoneError = ""
            isValid = true
        } else if (existingPhones.contains(newPhone)) {
            phoneError = "Phone number already exists"
            isValid = false
        } else {
            phoneError = ""
        }

        if (newBirthday.isEmpty()) {
            birthdayError = "Birthday is required"
            isValid = false
        } else {
            birthdayError = ""
        }

        if (newId.isEmpty()) {
            idError = "ID is required"
            isValid = false
        } else if (currentId != "" && currentId == newId) {
            idError = ""
            isValid = true
        } else if (existingIds.contains(newId)) {
            idError = "Student ID already exists"
            isValid = false
        } else {
            idError = ""
        }

        if (newClass.isEmpty()) {
            classError = "Class is required"
            isValid = false
        } else {
            classError = ""
        }

        if (newFaculty.isEmpty()) {
            facultyError = "Faculty is required"
            isValid = false
        } else {
            facultyError = ""
        }

        return isValid
    }

    // Delete a student
    fun onDeleteStudent(studentId: String) {
        val db = Firebase.firestore

        db.collection("students")
            .document(studentId)
            .delete()
            .addOnSuccessListener {
                Log.d("DeleteStudent", "Successfully deleted student with ID: $studentId")
                fetchStudentsFromFirestore()
            }
            .addOnFailureListener { e ->
                Log.e("DeleteStudent", "Error deleting student: ${e.message}")
            }
    }

    // Edit a student
    var studentToEdit by mutableStateOf<Student?>(null)
        private set
    fun onEditStudentSwipe(
        studentId: String,
        navController: NavHostController
    ) {
        studentToEdit = fullStudentList.find { it.studentId == studentId }
        navController.navigate(AppScreen.EditStudent.name)
    }

    fun onEditStudentSaveClick(
        newName: String,
        newEmail: String,
        newPhone: String,
        newBirthday: String,
        newId: String,
        newClass: String,
        newFaculty: String,
        context: Context,
        navController: NavHostController
    ) {
        val studentToEdit = studentToEdit ?: return

        val db = Firebase.firestore
        val updatedData = mutableMapOf<String, Any>()

        if (newName.isNotEmpty() && newName != studentToEdit.studentName) {
            updatedData["studentName"] = newName
        }

        if (newEmail.isNotEmpty() && newEmail != studentToEdit.studentEmail) {
            updatedData["studentEmail"] = newEmail
        }

        if (newPhone.isNotEmpty() && newPhone != studentToEdit.studentPhoneNumber) {
            updatedData["studentPhoneNumber"] = newPhone
        }

        if (newBirthday.isNotEmpty() && newBirthday != studentToEdit.studentBirthday) {
            updatedData["studentBirthday"] = newBirthday
        }

        if (newId.isNotEmpty() && newId != studentToEdit.studentId) {
            updatedData["studentId"] = newId
        }

        if (newClass.isNotEmpty() && newClass != studentToEdit.studentClass) {
            updatedData["studentClass"] = newClass
        }

        if (newFaculty.isNotEmpty() && newFaculty != studentToEdit.studentFaculty) {
            updatedData["studentFaculty"] = newFaculty
        }

        if (updatedData.isNotEmpty()) {
            db.collection("students")
                .document(studentToEdit.studentId)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Student details updated successfully", Toast.LENGTH_SHORT).show()
                    fetchStudentsFromFirestore()
                    navController.navigateUp()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error updating student: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "No changes detected", Toast.LENGTH_SHORT).show()
        }
    }

    // Change user's image event
    fun updateStudentImage(imageUri: Uri, context: Context, onSuccess: (String) -> Unit) {
        val fileName = "studentImages/${UUID.randomUUID()}.jpg"

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(fileName)

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val db = Firebase.firestore
                    db.collection("students")
                        .document(studentToEdit?.studentId ?: "")
                        .update("studentImageUrl", downloadUri.toString())
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile image updated ", Toast.LENGTH_SHORT).show()
                            onSuccess(downloadUri.toString())
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error updating profile image: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // CERTIFICATE PART
    fun onCertificateSeeMoreClicked(
        certificateId: String,
        navController: NavHostController,
    ) {
        val selectedStudent = _uiState.value.selectedStudent

        val certificate = selectedStudent.studentCertificates.find { it.certificateId == certificateId }
        certificate?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCertificate = certificate
                )
            }

            navController.navigate(AppScreen.CertificateDetail.name)
        }
    }

    fun onDeleteCertificate(
        studentId: String,
        certificateId: String,
        context: Context
    ) {
        val db = Firebase.firestore
        val studentRef = db.collection("students").document(studentId)

        studentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val student = document.toObject(Student::class.java)
                    student?.let {
                        val updatedCertificates = it.studentCertificates.filter { cert ->
                            cert.certificateId != certificateId
                        }

                        studentRef.update("studentCertificates", updatedCertificates)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Certificate deleted", Toast.LENGTH_SHORT).show()
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        selectedStudent = student.copy(studentCertificates = updatedCertificates),
                                        studentList = currentState.studentList.map { s ->
                                            if (s.studentId == studentId) {
                                                s.copy(studentCertificates = updatedCertificates)
                                            } else s
                                        }
                                    )
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed to delete certificate: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(context, "Student not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}