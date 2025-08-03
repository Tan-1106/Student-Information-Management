package com.example.studentinformationmanagement.ui.student.certificate

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.uiState.Certificate
import com.example.studentinformationmanagement.ui.shared.ConfirmationBox
import com.example.studentinformationmanagement.ui.shared.HelpIcon
import com.example.studentinformationmanagement.ui.shared.SwipeComponent
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.ThirdContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateList(
    managerViewModel: ManagerViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    // Variables
    val context: Context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val managerUiState by managerViewModel.uiState.collectAsState()
    val swipeEnable =
        loginUiState.currentUser?.role == "Manager" || loginUiState.currentUser?.role == "Admin"
    var showConfirmExportDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var selectedCertificateId by remember { mutableStateOf("") }

    // UI
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryContent,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.Certificate_StudentCertificate),
                        style = CustomTypography.headlineMedium,
                        color = PrimaryContent
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ThirdContent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        AsyncImage(
                            model = managerUiState.selectedStudent.imageUrl,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.avt_placeholder),
                            error = painterResource(id = R.drawable.avt_error),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = managerUiState.selectedStudent.name,
                                style = CustomTypography.titleLarge,
                                color = PrimaryContent
                            )
                            Text(
                                text = "ID: ${managerUiState.selectedStudent.id}",
                                style = CustomTypography.bodyMedium,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "Class: ${managerUiState.selectedStudent.stdClass}",
                                style = CustomTypography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.Certificate_CertificateList),
                        style = CustomTypography.titleLarge,
                        color = PrimaryContent,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { showConfirmExportDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DownloadForOffline,
                            contentDescription = "Download certificate list",
                            tint = PrimaryContent
                        )
                    }
                    if (swipeEnable) HelpIcon(message = stringResource(R.string.HelpIcon_UploadCertificate))
                }
                CertificateList(
                    certificateList = managerUiState.selectedStudent.certificates,
                    navController = navController,
                    managerViewModel = managerViewModel,
                    onEditSwipe = { certificateId ->
                        managerViewModel.onEditCertificateSwipe(
                            certificateId = certificateId,
                            navController = navController
                        )

                    },
                    onDeleteSwipe = { certificateId ->
                        selectedCertificateId = certificateId
                        showConfirmDeleteDialog = true
                    },
                    swipeEnable = swipeEnable
                )
            }
            if (showConfirmDeleteDialog) {
                ConfirmationBox(
                    title = "Confirm deletion",
                    message = "Do you want to delete this certificate?",
                    onDismissRequest = {
                        showConfirmDeleteDialog = false
                        selectedCertificateId = ""
                    },
                    onConfirmClick = {
                        managerViewModel.onDeleteCertificate(
                            managerUiState.selectedStudent.id,
                            selectedCertificateId,
                            context
                        )
                        showConfirmDeleteDialog = false
                        selectedCertificateId = ""
                    }
                )
            }
            if (showConfirmExportDialog) {
                ConfirmationBox(
                    title = stringResource(R.string.ConfirmationBox_DeleteCertificate),
                    message = stringResource(R.string.ConfirmationBox_DeleteCertificateContent),
                    onDismissRequest = {
                        showConfirmExportDialog = false
                    },
                    onConfirmClick = {
                        managerViewModel.exportCertificatesToCsv(context = context)
                        showConfirmExportDialog = false
                    }
                )
            }
            if (swipeEnable) {
                FloatingActionButton(
                    onClick = { navController.navigate(AppScreen.AddCertificate.name) },
                    shape = RoundedCornerShape(50),
                    containerColor = PrimaryContent,
                    contentColor = ThirdContent,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Certificate"
                    )
                }
            }
        }
    }
}

// Components
@Composable
fun CertificateList(
    certificateList: List<Certificate>,
    navController: NavHostController,
    managerViewModel: ManagerViewModel,
    onEditSwipe: (String) -> Unit,
    onDeleteSwipe: (String) -> Unit,
    swipeEnable: Boolean = true,
) {
    LazyColumn {
        items(certificateList.size) { index ->
            val certificate = certificateList[index]
            CertificateBox(
                certificateTitle = certificate.title,
                issueDate = certificate.issueDate,
                certificateId = certificate.id,
                onSeeMoreClicked = {
                    managerViewModel.onCertificateSeeMoreClicked(
                        certificateId = it,
                        navController = navController
                    )
                },
                onEditSwipe = { onEditSwipe(certificate.id) },
                onDeleteSwipe = { onDeleteSwipe(certificate.id) },
                swipeEnable = swipeEnable
            )
        }
    }
}

@Composable
fun CertificateBox(
    certificateTitle: String,
    issueDate: String,
    certificateId: String,
    onSeeMoreClicked: (String) -> Unit,
    onEditSwipe: () -> Unit,
    onDeleteSwipe: () -> Unit,
    modifier: Modifier = Modifier,
    swipeEnable: Boolean = true
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .background(color = ThirdContent, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .border(shape = RoundedCornerShape(16.dp), width = 1.dp, color = PrimaryContent)

    ) {
        if (swipeEnable) {
            SwipeComponent(
                onEditSwipe = onEditSwipe,
                onDeleteSwipe = onDeleteSwipe
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Title: $certificateTitle",
                        style = CustomTypography.titleLarge,
                        color = PrimaryContent
                    )
                    Text(
                        text = "ID: $certificateId",
                        style = CustomTypography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Issue date: $issueDate",
                            style = CustomTypography.bodyMedium,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.Button_SeeMore),
                            style = CustomTypography.titleMedium,
                            color = PrimaryContent,
                            modifier = Modifier
                                .clickable {
                                    onSeeMoreClicked(certificateId)
                                }
                        )
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = "Title: $certificateTitle",
                    style = CustomTypography.titleLarge,
                    color = PrimaryContent
                )
                Text(
                    text = "ID: $certificateId",
                    style = CustomTypography.bodyMedium,
                    color = Color.DarkGray
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Issue date: $issueDate",
                        style = CustomTypography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.Button_SeeMore),
                        style = CustomTypography.titleMedium,
                        color = PrimaryContent,
                        modifier = Modifier
                            .clickable {
                                onSeeMoreClicked(certificateId)
                            }
                    )
                }
            }
        }
    }
}