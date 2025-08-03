package com.example.studentinformationmanagement.ui.user

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailProfile(
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    // Variables
    val context: Context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val user = loginUiState.currentUser
    var updatedImageUrl by remember { mutableStateOf(user?.imageUrl) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            Toast.makeText(context, "Uploading image...", Toast.LENGTH_LONG).show()

            loginViewModel.updateProfileImage(
                imageUri = imageUri!!,
                context = context
            ) { newImageUrl ->
                updatedImageUrl = newImageUrl
            }
        }
    }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.Profile_YourProfile),
                        style = CustomTypography.headlineMedium,
                        color = PrimaryContent
                    )
                }, actions = {
                    IconButton(
                        onClick = {
                            loginViewModel.onLogOutButtonClicked()
                            navController.navigate(AppScreen.Login.name) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector =  Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = PrimaryContent,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(start = 50.dp)
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
                InformationLine(Icons.Filled.Person, "Name", user?.name ?: "")
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Cake, "Birthday", user?.birthday ?: "")
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Email, "Email", user?.email ?: "")
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Phone, "Phone", user?.phone ?: "")
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Person, "Role", user?.role ?: "")
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.BrokenImage, "Status", user?.status ?: "")
            }
        }
    }
}
