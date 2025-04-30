package com.example.studentinformationmanagement.ui.admin

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
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
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
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUser(
    adminViewModel: AdminViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current

    val user = adminViewModel.userToEdit
    var updatedImageUrl by remember { mutableStateOf(user?.userImageUrl) }
    var nameValue by remember { mutableStateOf(user?.userName ?: "") }
    var birthdayValue by remember { mutableStateOf(user?.userBirthday ?: "") }
    var emailValue by remember { mutableStateOf(user?.userEmail ?: "") }
    var phoneValue by remember { mutableStateOf(user?.userPhoneNumber ?: "") }
    var roleValue by remember { mutableStateOf(user?.userRole ?: "") }
    var statusValue by remember { mutableStateOf(user?.userStatus ?: "") }

    // Update user's image
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            Toast.makeText(context, "Uploading image...", Toast.LENGTH_LONG).show()

            adminViewModel.updateUserImage(imageUri = imageUri!!, context = context) { newImageUrl ->
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
                title = {}, actions = {}
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (adminViewModel.validateUserInputs(
                                newName = nameValue,
                                newEmail = emailValue,
                                newPhone = phoneValue,
                                newBirthday = birthdayValue,
                                newStatus = statusValue,
                                newRole = roleValue,
                        )) {
                            adminViewModel.onEditUserSaveClick(
                                newName = nameValue,
                                newEmail = emailValue,
                                newPhone = phoneValue,
                                newBirthday = birthdayValue,
                                newStatus = statusValue,
                                newRole = roleValue,
                                context = context
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
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
                    errorMessage = adminViewModel.nameError
                )
                InformationDate(
                    icon = Icons.Default.Cake,
                    label = "Birthday",
                    placeholder = birthdayValue,
                    onDatePick = { birthdayValue = it },
                    errorMessage = adminViewModel.birthdayError
                )
                InformationLine(
                    icon = Icons.Filled.Email,
                    label = "Email",
                    value = emailValue,
                    enable = true,
                    onValueChange = { emailValue = it },
                    errorMessage = adminViewModel.emailError
                )
                InformationLine(
                    icon = Icons.Filled.Phone,
                    label = "Phone",
                    value = phoneValue,
                    enable = true,
                    onValueChange = { phoneValue = it },
                    errorMessage = adminViewModel.phoneError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    )
                )
                InformationSelect(
                    icon = Icons.Filled.Person,
                    label = "Role",
                    options = listOf("Active", "Inactive"),
                    onOptionPick = { roleValue = it },
                    errorMessage = adminViewModel.statusError
                )
                InformationSelect(
                    icon = Icons.Filled.BrokenImage,
                    label = "Status",
                    options = listOf("Manager", "Employee"),
                    onOptionPick = { statusValue = it },
                    errorMessage = adminViewModel.roleError
                )
            }
        }
    }
}