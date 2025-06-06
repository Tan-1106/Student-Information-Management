package com.example.studentinformationmanagement.ui.manager

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.manager.Certificate
import com.example.studentinformationmanagement.data.manager.ManagerUiState
import com.example.studentinformationmanagement.data.manager.Student
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.Collator
import java.util.Locale
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
    var certIdList by mutableStateOf<List<String>>(emptyList())
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
                certIdList = students
                    .flatMap { it.studentCertificates }
                    .map { it.certificateId.trim() }
                    .distinct()
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

        fun lastWord(name: String): String {
            return name.trim().split("\\s+".toRegex()).lastOrNull() ?: ""
        }

        filtered = when (sortSelected) {
            "A → Z" -> filtered.sortedWith(compareBy(Collator.getInstance(Locale("vi", "VN"))) { lastWord(it.studentName) })
            "Z → A" -> filtered.sortedWith(compareByDescending(Collator.getInstance(Locale("vi", "VN"))) { lastWord(it.studentName) })
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
    fun onAddStudentFloatingButtonClick(navController: NavController) {
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
    fun clearErrorMessage() {
        nameError = ""
        emailError = ""
        phoneError = ""
        idError = ""
        classError = ""
        birthdayError = ""
        facultyError = ""
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
    // Add a certificate
    fun onAddCertificateFloatingButtonClick(navController: NavHostController) {
        navController.navigate(AppScreen.AddCertificate.name)
    }

    var certificateTitleValue by mutableStateOf("")
        private set
    var certificateCourseNameValue by mutableStateOf("")
        private set
    var certificateIssueDateValue by mutableStateOf("")
        private set
    var certificateOrganizationValue by mutableStateOf("")
        private set
    var certificateIdValue by mutableStateOf("")
        private set
    var certificateExpirationDateValue by mutableStateOf("")
        private set

    var titleError by mutableStateOf("")
        private set
    var courseNameError by mutableStateOf("")
        private set
    var issueDateError by mutableStateOf("")
        private set
    var organizationError by mutableStateOf("")
        private set
    var cIdError by mutableStateOf("")
        private set
    var expirationDateError by mutableStateOf("")
        private set

    fun onCTitleChange(userInput: String) {
        certificateTitleValue = userInput
    }
    fun onCCourseNameChange(userInput: String) {
        certificateCourseNameValue = userInput
    }
    fun onCIssueDateChange(userInput: String) {
        certificateIssueDateValue = userInput
    }
    fun onCOrganizationChange(userInput: String) {
        certificateOrganizationValue = userInput
    }
    fun onCIdChange(userInput: String) {
        certificateIdValue = userInput
    }
    fun onCExpirationDateChange(userInput: String) {
        certificateExpirationDateValue = userInput
    }

    fun clearCertificateInputs() {
        certificateTitleValue = ""
        certificateCourseNameValue = ""
        certificateIssueDateValue = ""
        certificateOrganizationValue = ""
        certificateIdValue = ""
        certificateExpirationDateValue = ""
    }
    fun clearCErrorMessage() {
        titleError = ""
        courseNameError = ""
        issueDateError = ""
        organizationError = ""
        cIdError = ""
        expirationDateError = ""
    }

    fun validateCertificateInputs(
        newTitle: String,
        newCourseName: String,
        newId: String,
        currentId: String = "",
        newOrganization: String,
        newIssueDate: String,
        newExpirationDate: String,
        cIdList: List<String> = certIdList
    ): Boolean {
        var isValid = true

        if (newTitle.isEmpty()) {
            titleError = "Title is required"
            isValid = false
        } else {
            titleError = ""
        }

        if (newCourseName.isEmpty()) {
            courseNameError = "Course name is required"
            isValid = false
        } else {
            courseNameError = ""
        }

        if (newId.isEmpty()) {
            cIdError = "ID is required"
            isValid = false
        } else if (currentId.isNotEmpty() && currentId.equals(newId, ignoreCase = true)) {
            cIdError = ""
        } else if (cIdList.any { it.equals(newId, ignoreCase = true) }) {
            cIdError = "Certificate ID already exists"
            isValid = false
        } else {
            cIdError = ""
        }

        if (newOrganization.isEmpty()) {
            organizationError = "Issuing organization is required"
            isValid = false
        } else {
            organizationError = ""
        }

        if (newIssueDate.isEmpty()) {
            issueDateError = "Issue date is required"
            isValid = false
        } else {
            issueDateError = ""
        }

        if (newExpirationDate.isEmpty()) {
            expirationDateError = "Expiration date is required"
            isValid = false
        } else {
            expirationDateError = ""
        }

        return isValid
    }

    fun updateSelectedStudentInformation() {
        val selectedStudentId = uiState.value.selectedStudent.studentId
        fetchStudentsFromFirestore()

        _uiState.update { currentState ->
            currentState.copy(
                selectedStudent = fullStudentList.find { it.studentId == selectedStudentId } ?: currentState.selectedStudent
            )
        }
    }

    fun updateSelectedCertificateInformation() {
        val selectedCertificateId = uiState.value.selectedCertificate.certificateId
        fetchStudentsFromFirestore()

        val updatedCertificate = fullStudentList
            .flatMap { it.studentCertificates }
            .find { it.certificateId.equals(selectedCertificateId, ignoreCase = true) }

        _uiState.update { currentState ->
            currentState.copy(
                selectedCertificate = updatedCertificate ?: currentState.selectedCertificate
            )
        }
    }

    fun onAddCertificateButtonClick(
        navController: NavHostController,
        context: Context
    ) {
        val title = certificateTitleValue.trim()
        val courseName = certificateCourseNameValue.trim()
        val id = certificateIdValue.trim()
        val organization = certificateOrganizationValue.trim()
        val issueDate = certificateIssueDateValue.trim()
        val expirationDate = certificateExpirationDateValue.trim()

        if (validateCertificateInputs(newTitle = title, newCourseName = courseName, newId = id, newOrganization = organization, newIssueDate = issueDate, newExpirationDate = expirationDate)) {
            val certificate = mapOf(
                "certificateId" to id,
                "certificateTitle" to title,
                "courseName" to courseName,
                "expirationDate" to expirationDate,
                "issueDate" to issueDate,
                "issuingOrganization" to organization
            )

            Firebase.firestore.collection("students")
                .document(_uiState.value.selectedStudent.studentId)
                .update("studentCertificates", FieldValue.arrayUnion(certificate))
                .addOnSuccessListener {
                    updateSelectedStudentInformation()
                    navController.navigateUp()
                    clearCErrorMessage()
                    clearCertificateInputs()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Cannot add certificate", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Certificate detail information
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

    // Delete a certificate
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

    // Edit a certificate
    var certificateToEdit by mutableStateOf<Certificate?>(null)
        private set
    fun onEditCertificateSwipe(
        certificateId: String,
        navController: NavHostController
    ) {
        val matchedCertificate = fullStudentList
            .flatMap { it.studentCertificates }
            .firstOrNull { it.certificateId == certificateId }
        certificateToEdit = matchedCertificate

        navController.navigate(AppScreen.EditCertificate.name)
    }

    fun onEditCertificateSaveClick(
        newTitle: String,
        newCourseName: String,
        newId: String,
        newOrganization: String,
        newIssueDate: String,
        newExpirationDate: String,
        context: Context,
        navController: NavHostController
    ) {
        val certificate = certificateToEdit ?: return
        val db = Firebase.firestore

        val student = fullStudentList.find { student ->
            student.studentCertificates.any { it.certificateId == certificate.certificateId }
        } ?: return

        val updatedCertificate = certificate.copy(
            certificateId = newId,
            certificateTitle = newTitle,
            courseName = newCourseName,
            issuingOrganization = newOrganization,
            issueDate = newIssueDate,
            expirationDate = newExpirationDate
        )

        val updatedCertificates = student.studentCertificates.map {
            if (it.certificateId.equals(certificate.certificateId, ignoreCase = true)) updatedCertificate else it
        }

        db.collection("students")
            .document(student.studentId)
            .update("studentCertificates", updatedCertificates)
            .addOnSuccessListener {
                Toast.makeText(context, "Certificate updated successfully", Toast.LENGTH_SHORT).show()
                fetchStudentsFromFirestore()
                updateSelectedCertificateInformation()
                updateSelectedStudentInformation()
                navController.navigateUp()

            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error updating certificate: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Upload Student CSV
    var studentList: MutableList<Student> = mutableListOf()

    fun uploadStudentsFromCsv(
        uri: Uri,
        navController: NavHostController,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Cannot open file", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                inputStream.use { stream ->
                    val reader = BufferedReader(InputStreamReader(stream))
                    val lines = reader.readLines()

                    val dataLines = if (lines.first().contains("studentName", ignoreCase = true)) {
                        lines.drop(1)
                    } else {
                        lines
                    }

                    studentList.clear()

                    for (line in dataLines) {
                        val tokens = line.split(",").map { it.trim() }

                        if (tokens.size >= 7) {
                            val student = Student(
                                studentName = tokens[0],
                                studentBirthday = tokens[1],
                                studentEmail = tokens[2],
                                studentPhoneNumber = tokens[3],
                                studentId = tokens[4],
                                studentClass = tokens[5],
                                studentFaculty = tokens[6]
                            )
                            studentList.add(student)
                        }
                    }

                    val firestore = Firebase.firestore
                    val collection = firestore.collection("students")
                    for (student in studentList) {
                        collection.document(student.studentId).set(student)
                    }

                    withContext(Dispatchers.Main) {
                        navController.navigateUp()
                        Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }

    // Upload Certificates CSV
    fun uploadCertificatesFromCsv(
        uri: Uri,
        navController: NavHostController,
        context: Context
    ) {
        val studentId = _uiState.value.selectedStudent.studentId

        if (studentId.isBlank()) {
            Toast.makeText(context, "No student selected", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Cannot open file", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                inputStream.use { stream ->
                    val reader = BufferedReader(InputStreamReader(stream))
                    val lines = reader.readLines()

                    val dataLines = if (lines.first().contains("certificateTitle", ignoreCase = true)) {
                        lines.drop(1)
                    } else {
                        lines
                    }

                    val certificateList = mutableListOf<Certificate>()

                    for (line in dataLines) {
                        val tokens = line.split(",").map { it.trim() }

                        if (tokens.size >= 6) {
                            val certificate = Certificate(
                                certificateTitle = tokens[0],
                                courseName = tokens[1],
                                certificateId = tokens[2],
                                issuingOrganization = tokens[3],
                                issueDate = tokens[4],
                                expirationDate = tokens[5]
                            )
                            certificateList.add(certificate)
                        }
                    }

                    val firestore = Firebase.firestore
                    val studentDocRef = firestore.collection("students").document(studentId)

                    studentDocRef.update("studentCertificates", certificateList)
                        .addOnSuccessListener {
                            viewModelScope.launch(Dispatchers.Main) {
                                updateSelectedStudentInformation()
                                updateSelectedCertificateInformation()
                                navController.navigateUp()
                                Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            viewModelScope.launch(Dispatchers.Main) {
                                Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }

    // Export student list
    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportStudentsToCsv(context: Context) {
        val studentList = _uiState.value.studentList
        val fileName = "students_${System.currentTimeMillis()}.csv"
        val csvHeader = "studentName,studentBirthday,studentEmail,studentPhoneNumber,studentId,studentClass,studentFaculty\n"

        val csvBody = buildString {
            studentList.forEach { student ->
                appendLine("${student.studentName},${student.studentBirthday},${student.studentEmail},${student.studentPhoneNumber},${student.studentId},${student.studentClass},${student.studentFaculty}")
            }
        }

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val itemUri = resolver.insert(collection, contentValues)

        itemUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write((csvHeader + csvBody).toByteArray())
                outputStream.flush()
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            Toast.makeText(context, "Exported to Downloads/$fileName", Toast.LENGTH_LONG).show()
        } ?: run {
            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Export student list
    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportCertificatesToCsv(context: Context) {
        val student = _uiState.value.selectedStudent
        val certificateList = student.studentCertificates
        val fileName = "certificates_${student.studentId}_${System.currentTimeMillis()}.csv"
        val csvHeader = "certificateTitle,courseName,certificateId,issuingOrganization,issueDate,expirationDate\n"

        if (certificateList.isEmpty()) {
            Toast.makeText(context, "No certificates to export", Toast.LENGTH_SHORT).show()
            return
        }

        val csvBody = buildString {
            certificateList.forEach { cert ->
                appendLine("${cert.certificateTitle},${cert.courseName},${cert.certificateId},${cert.issuingOrganization},${cert.issueDate},${cert.expirationDate}")
            }
        }

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val itemUri = resolver.insert(collection, contentValues)

        itemUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write((csvHeader + csvBody).toByteArray())
                outputStream.flush()
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            Toast.makeText(context, "Exported to Downloads/$fileName", Toast.LENGTH_LONG).show()
        } ?: run {
            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
        }
    }
}