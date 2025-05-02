package com.example.studentinformationmanagement.ui.shared

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.manager.Certificate
import com.example.studentinformationmanagement.ui.manager.ManagerViewModel
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.kanit_regular_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.secondary_dark
import com.example.studentinformationmanagement.ui.theme.third_content

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCertificationList(
    managerViewModel: ManagerViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val managerUiState by managerViewModel.uiState.collectAsState()
    val swipeEnable = loginUiState.currentUser?.userRole == "Manager" || loginUiState.currentUser?.userRole == "Admin"

    var showConfirmExportDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var selectedCertificateId by remember { mutableStateOf("") }


    Scaffold(
        containerColor = Color.White,
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
                        text = "STUDENT'S CERTIFICATES",
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                },
                actions = {}
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = secondary_content
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        AsyncImage(
                            model = managerUiState.selectedStudent.studentImageUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.avt_placeholder),
                            error = painterResource(id = R.drawable.avt_error)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = managerUiState.selectedStudent.studentName,
                                fontFamily = kanit_bold_font
                            )
                            Text(
                                text = "ID: ${managerUiState.selectedStudent.studentId}",
                                fontFamily = kanit_regular_font,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Class: ${managerUiState.selectedStudent.studentClass}",
                                fontFamily = kanit_regular_font,
                                fontSize = 12.sp
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
                        text = "List Of Certificates",
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                        fontSize = 24.sp,
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            showConfirmExportDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DownloadForOffline,
                            contentDescription = "Download certificate list",
                            tint = primary_content
                        )
                    }
                    if (swipeEnable) {
                        HelpIcon(
                            message = "Swipe right to edit and left to delete a certificate"
                        )
                    }
                }

                CertificateList(
                    certificateList = managerUiState.selectedStudent.studentCertificates,
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
                        managerViewModel.onDeleteCertificate(managerUiState.selectedStudent.studentId, selectedCertificateId, context)
                        showConfirmDeleteDialog = false
                        selectedCertificateId = ""
                    }
                )
            }

            if (showConfirmExportDialog) {
                ConfirmationBox(
                    title = "Confirm export",
                    message = "Do you want to export and download the certificate list as CSV?",
                    onDismissRequest = {
                        showConfirmExportDialog = false
                    },
                    onConfirmClick = {
                        managerViewModel.exportCertificatesToCsv(context = context)
                        showConfirmExportDialog = false
                    }
                )
            }

            // Add certificate button
            if (swipeEnable) {
                FloatingActionButton(
                    onClick = { managerViewModel.onAddCertificateFloatingButtonClick(navController) },
                    shape = RoundedCornerShape(50),
                    containerColor = primary_content,
                    contentColor = third_content,
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
                certificateTitle = certificate.certificateTitle,
                issueDate = certificate.issueDate,
                certificateId = certificate.certificateId,
                onSeeMoreClicked = {
                    managerViewModel.onCertificateSeeMoreClicked(
                        certificateId = it,
                        navController = navController
                    )
                },
                onEditSwipe = { onEditSwipe(certificate.certificateId) },
                onDeleteSwipe = { onDeleteSwipe(certificate.certificateId) },
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
            .background(color = third_content, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .border(shape = RoundedCornerShape(16.dp), width = 1.dp, color = primary_content)

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
                        fontSize = 20.sp,
                        color = primary_content,
                        fontFamily = kanit_bold_font
                    )
                    Text(
                        text = "Issue date: $issueDate",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontFamily = kanit_regular_font
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "ID: $certificateId",
                            fontSize = 14.sp,
                            fontFamily = kanit_regular_font,
                            color = secondary_dark
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.see_more),
                            fontFamily = kanit_regular_font,
                            color = primary_content,
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
                    fontSize = 20.sp,
                    color = primary_content,
                    fontFamily = kanit_bold_font
                )
                Text(
                    text = "Issue date: $issueDate",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontFamily = kanit_regular_font
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "ID: $certificateId",
                        fontSize = 14.sp,
                        fontFamily = kanit_regular_font,
                        color = secondary_dark
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.see_more),
                        fontFamily = kanit_regular_font,
                        color = primary_content,
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