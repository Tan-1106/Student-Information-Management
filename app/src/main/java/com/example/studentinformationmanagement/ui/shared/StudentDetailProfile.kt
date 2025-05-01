package com.example.studentinformationmanagement.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Numbers
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.manager.ManagerViewModel
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailProfile(
    navController: NavHostController,
    managerViewModel: ManagerViewModel,
    modifier: Modifier = Modifier
) {
    val managerUiState by managerViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        modifier = modifier
            .systemBarsPadding(),
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
                title = {
                    Text(
                        text = "STUDENT'S PROFILE",
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(route = AppScreen.CertificateList.name)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Newspaper,
                            contentDescription = "Certificate management",
                            tint = primary_content
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            // Data Loaded
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .size(100.dp)
                ) {
                    AsyncImage(
                        model = managerUiState.selectedStudent.studentImageUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.avt_placeholder),
                        error = painterResource(id = R.drawable.avt_error)
                    )
                }
                InformationLine(Icons.Filled.Person, "Name", managerUiState.selectedStudent.studentName)
                InformationLine(Icons.Filled.Cake, "Birthday", managerUiState.selectedStudent.studentBirthday)
                InformationLine(Icons.Filled.Email, "Email", managerUiState.selectedStudent.studentEmail)
                InformationLine(Icons.Filled.Phone, "Phone", managerUiState.selectedStudent.studentPhoneNumber)
                InformationLine(Icons.Filled.Numbers, "Id", managerUiState.selectedStudent.studentId)
                InformationLine(Icons.Filled.Class, "Class", managerUiState.selectedStudent.studentClass)
                InformationLine(Icons.Filled.Apartment, "Faculty", managerUiState.selectedStudent.studentFaculty)
                InformationLine(Icons.Filled.Newspaper, "Number of certificates", managerUiState.selectedStudent.studentCertificates.size.toString())
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
