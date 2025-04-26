package com.example.studentinformationmanagement.ui.manager

import android.content.Context
import android.util.Log
import android.util.Patterns
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
import com.example.studentinformationmanagement.ui.shared.StudentDetailProfile
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ManagerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState: StateFlow<ManagerUiState> = _uiState.asStateFlow()

    var facultyList by mutableStateOf<List<String>>(emptyList())
        private set
    var classList by mutableStateOf<List<String>>(emptyList())
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
        userPhoneNumber: String, navController: NavHostController,
    ) {
        val student = fullStudentList.find { it.studentPhoneNumber == userPhoneNumber }

        student?.let { selectedStudent ->
            navController.navigate(AppScreen.StudentDetailProfile.name)
        } ?: run {
            Log.e("ManagerViewModel", "Student not found with phone number: $userPhoneNumber")
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

    fun onAddStudentButtonClick(
        navController: NavHostController,
        context: Context
    ) {
        val db = Firebase.firestore

        val name = newStudentName.trim()
        val email = newStudentEmail.trim()
        val phone = newStudentPhone.trim()
        val id = newStudentId.trim()
        val studentClass = newStudentClass.trim()
        val birthday = newStudentBirthday.trim()
        val faculty = newStudentFaculty.trim()

        if (validateUserInputs()) {
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
                                            } else {
                                                phoneError = "Phone number is existed."
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "Cannot check existing phone number",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } else {
                                    emailError = "Email is existed."
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Cannot check existing email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        idError = "Student's ID is existed."
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

    fun validateUserInputs(): Boolean {
        var isValid = true

        if (newStudentName.trim().isEmpty()) {
            nameError = "Name is required"
            isValid = false
        } else {
            nameError = ""
        }

        if (newStudentEmail.trim().isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(newStudentEmail.trim()).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = ""
        }

        if (newStudentPhone.trim().isEmpty()) {
            phoneError = "Phone number is required"
            isValid = false
        } else if (newStudentPhone.trim().length != 10) {
            phoneError = "Invalid phone number"
        } else {
            phoneError = ""
        }

        if (newStudentId.trim().isEmpty()) {
            idError = "Student's ID is required"
            isValid = false
        } else {
            idError = ""
        }

        if (newStudentClass.trim().isEmpty()) {
            classError = "Class is required"
            isValid = false
        } else {
            classError = ""
        }

        if (newStudentBirthday.trim().isEmpty()) {
            birthdayError = "Birthday is required"
            isValid = false
        } else {
            birthdayError = ""
        }

        if (newStudentFaculty.trim().isEmpty()) {
            facultyError = "Faculty is required"
            isValid = false
        } else {
            facultyError = ""
        }

        return isValid
    }
}