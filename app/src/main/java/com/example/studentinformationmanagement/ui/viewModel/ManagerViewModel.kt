package com.example.studentinformationmanagement.ui.viewModel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.uiState.Certificate
import com.example.studentinformationmanagement.data.uiState.ManagerUiState
import com.example.studentinformationmanagement.data.uiState.Student
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

    // Fetch Student List
    private var fullStudentList: List<Student> = emptyList()
    private var facultyList by mutableStateOf<List<String>>(emptyList())
    private var classList by mutableStateOf<List<String>>(emptyList())
    private var idList by mutableStateOf<List<String>>(emptyList())
    private var emailList by mutableStateOf<List<String>>(emptyList())
    private var phoneList by mutableStateOf<List<String>>(emptyList())
    private var certIdList by mutableStateOf<List<String>>(emptyList())

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
                fullStudentList = students
                _uiState.update {
                    it.copy(
                        studentList = students,
                        facultyList = facultyList,
                        classList = classList
                    )
                }

                // For search feature
                facultyList = students.map { it.faculty }.distinct().sorted()
                classList = students.map { it.stdClass }.distinct().sorted()
                idList = students.map { it.id }.distinct()
                emailList = students.map { it.email }.distinct()
                phoneList = students.map { it.phone }.distinct()
                certIdList = students
                    .flatMap { it.certificates }
                    .map { it.id.trim() }
                    .distinct()
            }
    }

    // STUDENT
    // Search Bar
    fun onStudentSearch(userSearchInput: String) {
        val keyword = userSearchInput.trim().lowercase()
        if (keyword.isEmpty()) {
            fetchStudentsFromFirestore()
        } else {
            val filteredList = fullStudentList.filter { student ->
                student.name.contains(
                    keyword,
                    ignoreCase = true
                ) || student.email.contains(keyword, ignoreCase = true) ||
                        student.phone.contains(
                            keyword,
                            ignoreCase = true
                        ) || student.id.contains(keyword, ignoreCase = true) ||
                        student.stdClass.contains(
                            keyword,
                            ignoreCase = true
                        ) || student.faculty.contains(keyword, ignoreCase = true)
            }
            _uiState.update { it.copy(studentList = filteredList) }
        }
    }

    // Filter Feature
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

    fun onApplyFilterClick() {
        var filtered = fullStudentList
        val minCert = minimumCertificates.toIntOrNull() ?: 0
        filtered = filtered.filter { it.certificates.size >= minCert }

        if (facultySelected.isNotBlank()) {
            filtered = filtered.filter { it.faculty == facultySelected }
        }
        if (classSelected.isNotBlank()) {
            filtered = filtered.filter { it.stdClass == classSelected }
        }

        fun lastWord(name: String): String {
            return name.trim().split("\\s+".toRegex()).lastOrNull() ?: ""
        }

        filtered = when (sortSelected) {
            "A → Z" -> filtered.sortedWith(
                compareBy(
                    Collator.getInstance(
                        Locale(
                            "vi",
                            "VN"
                        )
                    )
                ) { lastWord(it.name) })

            "Z → A" -> filtered.sortedWith(
                compareByDescending(
                    Collator.getInstance(
                        Locale(
                            "vi",
                            "VN"
                        )
                    )
                ) { lastWord(it.name) })

            else -> filtered
        }
        _uiState.update { it.copy(studentList = filtered) }
    }

    fun onClearFilterClick() {
        sortSelected = ""
        minimumCertificates = ""
        facultySelected = ""
        classSelected = ""

        _uiState.update { it.copy(studentList = fullStudentList) }
    }

    // Detail Profile
    fun onStudentSeeMoreClicked(
        studentId: String,
        navController: NavHostController,
    ) {
        val student = fullStudentList.find { it.id == studentId }
        student?.let {
            _uiState.update { it.copy(selectedStudent = student) }
            navController.navigate(AppScreen.StudentDetailProfile.name)
        } ?: run {
            Log.e("ManagerViewModel", "Student not found with phone number: $studentId")
        }
    }

    // Add Student
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
        name: String,
        email: String,
        phone: String,
        birthday: String,
        id: String,
        stdClass: String,
        faculty: String,
        navController: NavHostController,
        onSuccess: () -> Unit,
        context: Context
    ) {
        val newStdName = name.trim()
        val newStdEmail = email.trim()
        val newStdPhone = phone.trim()
        val newStdId = id.trim()
        val newStdClass = stdClass.trim()
        val newStdBirthday = birthday.trim()
        val newStdFaculty = faculty.trim()

        if (validateUserInputs(
                newName = newStdName,
                newEmail = newStdEmail,
                newPhone = newStdPhone,
                newBirthday = newStdBirthday,
                newId = newStdId,
                newClass = newStdClass,
                newFaculty = newStdFaculty
            )
        ) {
            val db = Firebase.firestore

            db.collection("students")
                .whereIn("id", listOf(newStdId))
                .get()
                .addOnSuccessListener { idResult ->
                    if (idResult.isEmpty) {
                        db.collection("students")
                            .whereIn("email", listOf(newStdEmail))
                            .get()
                            .addOnSuccessListener { emailResult ->
                                if (emailResult.isEmpty) {
                                    db.collection("students")
                                        .whereIn("phone", listOf(newStdPhone))
                                        .get()
                                        .addOnSuccessListener { phoneResult ->
                                            if (phoneResult.isEmpty) {
                                                val newStudent = Student(
                                                    name = newStdName,
                                                    birthday = newStdBirthday,
                                                    email = newStdEmail,
                                                    phone = newStdPhone,
                                                    id = newStdId,
                                                    stdClass = newStdClass,
                                                    faculty = newStdFaculty
                                                )

                                                db.collection("students")
                                                    .document(newStdId)
                                                    .set(newStudent)
                                                    .addOnSuccessListener {
                                                        clearErrorMessage()
                                                        onSuccess()
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

    // Delete Student
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

    // Edit Student
    var studentToEdit by mutableStateOf<Student?>(null)
        private set

    fun onEditStudentSwipe(
        studentId: String,
        navController: NavHostController
    ) {
        studentToEdit = fullStudentList.find { it.id == studentId }
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

        if (newName.isNotEmpty() && newName != studentToEdit.name) {
            updatedData["name"] = newName
        }
        if (newEmail.isNotEmpty() && newEmail != studentToEdit.email) {
            updatedData["email"] = newEmail
        }
        if (newPhone.isNotEmpty() && newPhone != studentToEdit.phone) {
            updatedData["phone"] = newPhone
        }
        if (newBirthday.isNotEmpty() && newBirthday != studentToEdit.birthday) {
            updatedData["birthday"] = newBirthday
        }
        if (newId.isNotEmpty() && newId != studentToEdit.id) {
            updatedData["id"] = newId
        }
        if (newClass.isNotEmpty() && newClass != studentToEdit.stdClass) {
            updatedData["stdClass"] = newClass
        }
        if (newFaculty.isNotEmpty() && newFaculty != studentToEdit.faculty) {
            updatedData["faculty"] = newFaculty
        }

        if (updatedData.isNotEmpty()) {
            db.collection("students")
                .document(studentToEdit.id)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Student details updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    fetchStudentsFromFirestore()
                    navController.navigateUp()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Error updating student: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(context, "No changes detected", Toast.LENGTH_SHORT).show()
        }
    }

    // Change Image
    fun updateStudentImage(imageUri: Uri, context: Context, onSuccess: (String) -> Unit) {
        val fileName = "studentImages/${UUID.randomUUID()}.jpg"
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(fileName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val db = Firebase.firestore
                    db.collection("students")
                        .document(studentToEdit?.id ?: "")
                        .update("imageUrl", downloadUri.toString())
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile image updated ", Toast.LENGTH_SHORT)
                                .show()
                            onSuccess(downloadUri.toString())
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error updating profile image: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    // Upload Student CSV
    private var studentList: MutableList<Student> = mutableListOf()
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
                                name = tokens[0],
                                birthday = tokens[1],
                                email = tokens[2],
                                phone = tokens[3],
                                id = tokens[4],
                                stdClass = tokens[5],
                                faculty = tokens[6]
                            )
                            studentList.add(student)
                        }
                    }

                    val firestore = Firebase.firestore
                    val collection = firestore.collection("students")
                    for (student in studentList) {
                        collection.document(student.id).set(student)
                    }

                    withContext(Dispatchers.Main) {
                        navController.navigateUp()
                        Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                e.printStackTrace()
            }
        }
    }

    // Export Student List
    fun exportStudentsToCsv(context: Context) {
        val studentList = _uiState.value.studentList
        val fileName = "students_${System.currentTimeMillis()}.csv"
        val csvHeader =
            "studentName,studentBirthday,studentEmail,studentPhoneNumber,studentId,studentClass,studentFaculty\n"

        val csvBody = buildString {
            studentList.forEach { student ->
                appendLine("${student.name},${student.birthday},${student.email},${student.phone},${student.id},${student.stdClass},${student.faculty}")
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

    // CERTIFICATE
    // Add
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

    private fun updateSelectedStudentInformation() {
        val selectedStudentId = uiState.value.selectedStudent.id
        fetchStudentsFromFirestore()

        _uiState.update { currentState ->
            currentState.copy(
                selectedStudent = fullStudentList.find { it.id == selectedStudentId }
                    ?: currentState.selectedStudent
            )
        }
    }

    private fun updateSelectedCertificateInformation() {
        val selectedCertificateId = uiState.value.selectedCertificate.id
        fetchStudentsFromFirestore()

        val updatedCertificate = fullStudentList
            .flatMap { it.certificates }
            .find { it.id.equals(selectedCertificateId, ignoreCase = true) }

        _uiState.update { currentState ->
            currentState.copy(
                selectedCertificate = updatedCertificate ?: currentState.selectedCertificate
            )
        }
    }

    fun onAddCertificateButtonClick(
        titleInput: String,
        courseNameInput: String,
        idInput: String,
        organizationInput: String,
        issueDateInput: String,
        expirationDateInput: String,
        navController: NavHostController,
        context: Context
    ) {
        val title = titleInput.trim()
        val courseName = courseNameInput.trim()
        val id = idInput.trim()
        val organization = organizationInput.trim()
        val issueDate = issueDateInput.trim()
        val expirationDate = expirationDateInput.trim()

        if (validateCertificateInputs(
                newTitle = title,
                newCourseName = courseName,
                newId = id,
                newOrganization = organization,
                newIssueDate = issueDate,
                newExpirationDate = expirationDate
            )
        ) {
            val certificate = mapOf(
                "id" to id,
                "title" to title,
                "courseName" to courseName,
                "expirationDate" to expirationDate,
                "issueDate" to issueDate,
                "issuingOrganization" to organization
            )

            Firebase.firestore.collection("students")
                .document(_uiState.value.selectedStudent.id)
                .update("certificates", FieldValue.arrayUnion(certificate))
                .addOnSuccessListener {
                    updateSelectedStudentInformation()
                    navController.navigateUp()
                    clearCErrorMessage()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Cannot add certificate", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Detail Information
    fun onCertificateSeeMoreClicked(
        certificateId: String,
        navController: NavHostController,
    ) {
        val selectedStudent = _uiState.value.selectedStudent

        val certificate = selectedStudent.certificates.find { it.id == certificateId }
        certificate?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCertificate = certificate
                )
            }
            navController.navigate(AppScreen.CertificateDetail.name)
        }
    }

    // Delete
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
                        val updatedCertificates = it.certificates.filter { cert ->
                            cert.id != certificateId
                        }

                        studentRef.update("certificates", updatedCertificates)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Certificate deleted", Toast.LENGTH_SHORT)
                                    .show()
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        selectedStudent = student.copy(certificates = updatedCertificates),
                                        studentList = currentState.studentList.map { s ->
                                            if (s.id == studentId) {
                                                s.copy(certificates = updatedCertificates)
                                            } else s
                                        }
                                    )
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "Failed to delete certificate: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
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

    // Edit Certificate
    var certificateToEdit by mutableStateOf<Certificate?>(null)
        private set

    fun onEditCertificateSwipe(
        certificateId: String,
        navController: NavHostController
    ) {
        val matchedCertificate = fullStudentList
            .flatMap { it.certificates }
            .firstOrNull { it.id == certificateId }
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
            student.certificates.any { it.id == certificate.id }
        } ?: return

        val updatedCertificate = certificate.copy(
            id = newId,
            title = newTitle,
            courseName = newCourseName,
            issuingOrganization = newOrganization,
            issueDate = newIssueDate,
            expirationDate = newExpirationDate
        )

        val updatedCertificates = student.certificates.map {
            if (it.id.equals(certificate.id, ignoreCase = true)) updatedCertificate else it
        }

        db.collection("students")
            .document(student.id)
            .update("certificates", updatedCertificates)
            .addOnSuccessListener {
                Toast.makeText(context, "Certificate updated successfully", Toast.LENGTH_SHORT)
                    .show()
                fetchStudentsFromFirestore()
                updateSelectedCertificateInformation()
                updateSelectedStudentInformation()
                navController.navigateUp()

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error updating certificate: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Upload Certificates CSV
    fun uploadCertificatesFromCsv(
        uri: Uri,
        navController: NavHostController,
        context: Context
    ) {
        val studentId = _uiState.value.selectedStudent.id

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

                    val dataLines = if (lines.first().contains("title", ignoreCase = true)) {
                        lines.drop(1)
                    } else {
                        lines
                    }

                    val certificateList = mutableListOf<Certificate>()

                    for (line in dataLines) {
                        val tokens = line.split(",").map { it.trim() }

                        if (tokens.size >= 6) {
                            val certificate = Certificate(
                                title = tokens[0],
                                courseName = tokens[1],
                                id = tokens[2],
                                issuingOrganization = tokens[3],
                                issueDate = tokens[4],
                                expirationDate = tokens[5]
                            )
                            certificateList.add(certificate)
                        }
                    }

                    val firestore = Firebase.firestore
                    val studentDocRef = firestore.collection("students").document(studentId)

                    studentDocRef.update("certificates", certificateList)
                        .addOnSuccessListener {
                            viewModelScope.launch(Dispatchers.Main) {
                                updateSelectedStudentInformation()
                                updateSelectedCertificateInformation()
                                navController.navigateUp()
                                Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        .addOnFailureListener { e ->
                            viewModelScope.launch(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Upload failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                e.printStackTrace()
            }
        }
    }

    // Export Certificate List
    fun exportCertificatesToCsv(context: Context) {
        val student = _uiState.value.selectedStudent
        val certificateList = student.certificates
        val fileName = "certificates_${student.id}_${System.currentTimeMillis()}.csv"
        val csvHeader =
            "certificateTitle,courseName,certificateId,issuingOrganization,issueDate,expirationDate\n"

        if (certificateList.isEmpty()) {
            Toast.makeText(context, "No certificates to export", Toast.LENGTH_SHORT).show()
            return
        }

        val csvBody = buildString {
            certificateList.forEach { cert ->
                appendLine("${cert.title},${cert.courseName},${cert.id},${cert.issuingOrganization},${cert.issueDate},${cert.expirationDate}")
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