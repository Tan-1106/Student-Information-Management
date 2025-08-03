package com.example.studentinformationmanagement.ui.student

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailProfile(
    navController: NavHostController,
    managerViewModel: ManagerViewModel,
    modifier: Modifier = Modifier
) {
    // Variables
    val managerUiState by managerViewModel.uiState.collectAsState()

    // UI
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.Profile_Student),
                        style = CustomTypography.headlineMedium,
                        color = PrimaryContent
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryContent,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(route = AppScreen.CertificateList.name) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Newspaper,
                            contentDescription = "Certificate management",
                            tint = PrimaryContent
                        )
                    }
                }
            )
        },
        modifier = modifier.systemBarsPadding()
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .size(100.dp)
                ) {
                    AsyncImage(
                        model = managerUiState.selectedStudent.imageUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.avt_placeholder),
                        error = painterResource(id = R.drawable.avt_error)
                    )
                }

                InformationLine(Icons.Filled.Person, "Name", managerUiState.selectedStudent.name)
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    Icons.Filled.Cake,
                    "Birthday",
                    managerUiState.selectedStudent.birthday
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(Icons.Filled.Email, "Email", managerUiState.selectedStudent.email)
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(Icons.Filled.Phone, "Phone", managerUiState.selectedStudent.phone)
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(Icons.Filled.Numbers, "Id", managerUiState.selectedStudent.id)
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.Class,
                    label = "Class",
                    value = managerUiState.selectedStudent.stdClass
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon =  Icons.Filled.Apartment,
                    label = "Faculty",
                    value =  managerUiState.selectedStudent.faculty
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon =  Icons.Filled.Newspaper,
                    label = "Number of certificates",
                    value = managerUiState.selectedStudent.certificates.size.toString()
                )
            }
        }
    }
}
