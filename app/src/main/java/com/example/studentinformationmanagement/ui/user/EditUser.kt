package com.example.studentinformationmanagement.ui.user

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.viewModel.AdminViewModel
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUser(
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    navController: NavHostController
) {
    // Variables
    val context: Context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val user = adminViewModel.userToEdit
    val adminSelfEdit: Boolean = loginUiState.currentUser?.phone == user?.phone
    var updatedImageUrl by remember { mutableStateOf(user?.imageUrl) }
    var nameValue by remember { mutableStateOf(user?.name ?: "") }
    var birthdayValue by remember { mutableStateOf(user?.birthday ?: "") }
    val emailValue by remember { mutableStateOf(user?.email ?: "") }
    val phoneValue by remember { mutableStateOf(user?.phone ?: "") }
    var roleValue by remember { mutableStateOf(user?.role ?: "") }
    var statusValue by remember { mutableStateOf(user?.status ?: "") }
    val isAdmin = user?.role == "Admin"
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            Toast.makeText(context, "Uploading image...", Toast.LENGTH_LONG).show()

            adminViewModel.updateUserImage(
                imageUri = imageUri!!,
                context = context
            ) { newImageUrl ->
                updatedImageUrl = newImageUrl
                if (adminSelfEdit) {
                    loginViewModel.updateCurrentUserImage(newImageUrl = newImageUrl)
                }
            }
        }
    }

    // UI
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.Edit_EditUser),
                        style = CustomTypography.headlineMedium,
                        color = PrimaryContent
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                            adminViewModel.clearErrorMessage()
                        }
                    ) {
                        Icon(
                            imageVector =  Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryContent,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (adminViewModel
                            .validateUserInputs(
                                newName = nameValue,
                                currentEmail = user?.email ?: "",
                                newEmail = emailValue,
                                currentPhone = user?.phone ?: "",
                                newPhone = phoneValue,
                                newBirthday = birthdayValue,
                                newStatus = statusValue,
                                newRole = roleValue,
                            )
                        ) {
                            adminViewModel
                                .onEditUserSaveClick(
                                newName = nameValue,
                                newEmail = emailValue,
                                newPhone = phoneValue,
                                newBirthday = birthdayValue,
                                newStatus = statusValue,
                                newRole = roleValue,
                                context = context,
                                navController = navController
                            )
                            if (adminSelfEdit) {
                                loginViewModel
                                    .updateCurrentUserInformation(
                                        newName = nameValue,
                                        newEmail = emailValue,
                                        newPhone = phoneValue,
                                        newBirthday =  birthdayValue,
                                        newStatus =  statusValue,
                                        newRole =  roleValue
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = PrimaryContent,
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.Button_Save),
                        style = CustomTypography.titleMedium,
                        color = Color.White
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
                Spacer(modifier = Modifier.height(20.dp))
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
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.Person,
                    label = "Name",
                    value = nameValue,
                    enable = true,
                    onValueChange = { nameValue = it },
                    errorMessage = adminViewModel.nameError
                )
                Spacer(modifier = Modifier.height(20.dp))
                InformationDate(
                    icon = Icons.Default.Cake,
                    label = "Birthday",
                    placeholder = birthdayValue,
                    onDatePick = { birthdayValue = it },
                    errorMessage = adminViewModel.birthdayError
                )
                Spacer(modifier = Modifier.height(20.dp))
                if (isAdmin) {
                    InformationLine(Icons.Filled.Person, "Role", "$roleValue (Cannot be edited)")
                } else {
                    InformationSelect(
                        icon = Icons.Filled.Person,
                        label = "Role",
                        options = listOf("Manager", "Employee"),
                        onOptionPick = { roleValue = it },
                        errorMessage = adminViewModel.roleError
                    )

                }
                Spacer(modifier = Modifier.height(20.dp))
                InformationSelect(
                    icon = Icons.Filled.BrokenImage,
                    label = "Status",
                    options = listOf("Active", "Inactive"),
                    onOptionPick = { statusValue = it },
                    errorMessage = adminViewModel.statusError
                )
            }
        }
    }
}