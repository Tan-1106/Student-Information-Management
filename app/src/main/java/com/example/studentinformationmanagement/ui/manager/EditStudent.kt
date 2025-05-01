package com.example.studentinformationmanagement.ui.manager

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudent(
    managerViewModel: ManagerViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current

    val student = managerViewModel.studentToEdit
    var updatedImageUrl by remember { mutableStateOf(student?.studentImageUrl) }
    var nameValue by remember { mutableStateOf(student?.studentName ?: "") }
    var birthdayValue by remember { mutableStateOf(student?.studentBirthday ?: "") }
    var emailValue by remember { mutableStateOf(student?.studentEmail ?: "") }
    var phoneValue by remember { mutableStateOf(student?.studentPhoneNumber ?: "") }
    var idValue by remember { mutableStateOf(student?.studentId ?: "") }
    var classValue by remember { mutableStateOf(student?.studentClass ?: "") }
    var facultyValue by remember { mutableStateOf(student?.studentFaculty ?: "") }

    // Update student's image
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            Toast.makeText(context, "Uploading image...", Toast.LENGTH_LONG).show()

            managerViewModel.updateStudentImage(imageUri = imageUri!!, context = context) { newImageUrl ->
                updatedImageUrl = newImageUrl
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            managerViewModel.clearErrorMessage()
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = primary_content,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = "EDIT STUDENT",
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                }, actions = {}
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (managerViewModel.validateUserInputs(
                            newName = nameValue,
                            currentEmail = student?.studentEmail ?: "",
                            newEmail = emailValue,
                            currentPhone = student?.studentPhoneNumber ?: "",
                            newPhone = phoneValue,
                            newBirthday = birthdayValue,
                            currentId = student?.studentId ?: "",
                            newId = idValue,
                            newClass = classValue,
                            newFaculty = facultyValue
                        )) {
                            managerViewModel.onEditStudentSaveClick(
                                newName = nameValue,
                                newEmail = emailValue,
                                newPhone = phoneValue,
                                newBirthday = birthdayValue,
                                newId = idValue,
                                newClass = classValue,
                                newFaculty = facultyValue,
                                context = context,
                                navController = navController
                            )
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = secondary_content,
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "Save",
                        style = TextStyle(
                            color = primary_dark,
                            fontFamily = kanit_bold_font
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 50.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                    ) {
                        AsyncImage(
                            model = updatedImageUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.avt_placeholder),
                            error = painterResource(id = R.drawable.avt_error)
                        )
                    }
                    IconButton(
                        onClick = {
                            launcher.launch("image/*")
                        },
                        modifier = Modifier.padding(top = 60.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Change image"
                        )
                    }
                }

                InformationLine(
                    icon = Icons.Filled.Person,
                    label = "Name",
                    value = nameValue,
                    enable = true,
                    onValueChange = { nameValue = it },
                    errorMessage = managerViewModel.nameError
                )

                InformationDate(
                    icon = Icons.Filled.Cake,
                    label = "Birthday",
                    placeholder = birthdayValue,
                    onDatePick = { birthdayValue = it },
                    errorMessage = managerViewModel.birthdayError
                )

                InformationLine(
                    icon = Icons.Filled.Email,
                    label = "Email",
                    value = emailValue,
                    enable = true,
                    onValueChange = { emailValue = it },
                    errorMessage = managerViewModel.emailError
                )

                InformationLine(
                    icon = Icons.Filled.Phone,
                    label = "Phone",
                    value = phoneValue,
                    enable = true,
                    onValueChange = { phoneValue = it },
                    errorMessage = managerViewModel.phoneError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    )
                )

                // Fixed - Can't change student's ID
                InformationLine(
                    icon = Icons.Filled.FilterCenterFocus,
                    label = "Student ID",
                    value = "$idValue (Cannot be edited)",
                    enable = false,
                    onValueChange = { idValue = it },
                    errorMessage = managerViewModel.idError
                )

                InformationLine(
                    icon = Icons.Filled.Book,
                    label = "Class",
                    value = classValue,
                    enable = true,
                    onValueChange = { classValue = it },
                    errorMessage = managerViewModel.classError
                )

                InformationLine(
                    icon = Icons.Filled.AccountBalance,
                    label = "Faculty",
                    value = facultyValue,
                    enable = true,
                    onValueChange = { facultyValue = it },
                    errorMessage = managerViewModel.facultyError
                )
            }
        }
    }
}
