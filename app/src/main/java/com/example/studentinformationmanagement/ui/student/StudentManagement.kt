package com.example.studentinformationmanagement.ui.student

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.uiState.Student
import com.example.studentinformationmanagement.ui.shared.ConfirmationBox
import com.example.studentinformationmanagement.ui.shared.HelpIcon
import com.example.studentinformationmanagement.ui.shared.InformationCard
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.SecondaryContent
import com.example.studentinformationmanagement.ui.theme.ThirdContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@Composable
fun StudentManagement(
    loginViewModel: LoginViewModel,
    managerViewModel: ManagerViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Variables
    val context: Context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val managerUiState by managerViewModel.uiState.collectAsState()
    var selectedStudentId by remember { mutableStateOf("") }
    var searchInput by remember { mutableStateOf("") }
    var showConfirmExportDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    val swipeEnable =
        loginUiState.currentUser?.role == "Manager" || loginUiState.currentUser?.role == "Admin"

    // UI
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = {
                        searchInput = it
                        managerViewModel.onStudentSearch(it)
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.Label_Search),
                            style = CustomTypography.labelLarge,
                            color = PrimaryContent
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = PrimaryContent
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    textStyle = CustomTypography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = PrimaryContent,
                        unfocusedBorderColor = SecondaryContent,
                        unfocusedContainerColor = ThirdContent,

                        focusedTextColor = PrimaryContent,
                        focusedContainerColor = ThirdContent,
                        focusedBorderColor = PrimaryContent,
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showFilterDialog = true }) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = SecondaryContent,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                }
                FilterDialog(
                    onSortSelected = { managerViewModel.onSortSelected(it) },
                    onMinimumCertificatesInput = { managerViewModel.onMinimumCertificatesInput(it) },
                    existingFaculty = managerUiState.facultyList,
                    onFacultyPick = { managerViewModel.onFacultyPick(it) },
                    existingClass = managerUiState.classList,
                    onClassPick = { managerViewModel.onClassPick(it) },
                    showDialog = showFilterDialog,
                    onApplyButtonClick = {
                        managerViewModel.onApplyFilterClick()
                        showFilterDialog = false
                    },
                    onDismissDialog = { showFilterDialog = false },
                    onClearButtonClick = {

                        managerViewModel.onClearFilterClick()
                        showFilterDialog = false
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.Student_StudentList),
                    style = CustomTypography.titleLarge,
                    color = PrimaryContent,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { showConfirmExportDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.DownloadForOffline,
                        contentDescription = "Download student list",
                        tint = PrimaryContent
                    )
                }
                if (swipeEnable) HelpIcon(message = stringResource(R.string.HelpIcon_Student))
            }
            StudentList(
                studentList = managerUiState.studentList,
                managerViewModel = managerViewModel,
                navController = navController,
                onEditSwipe = { studentId ->
                    managerViewModel.onEditStudentSwipe(
                        studentId = studentId,
                        navController = navController
                    )
                },
                onDeleteSwipe = { studentId ->
                    selectedStudentId = studentId
                    showConfirmDeleteDialog = true
                },
                swipeEnable = swipeEnable
            )
        }
        if (showConfirmDeleteDialog) {
            ConfirmationBox(
                title = stringResource(R.string.ConfirmationBox_DeleteStudent),
                message = stringResource(R.string.ConfirmationBox_DeleteStudentContent),
                onDismissRequest = {
                    showConfirmDeleteDialog = false
                    selectedStudentId = ""
                },
                onConfirmClick = {
                    managerViewModel.onDeleteStudent(selectedStudentId)
                    showConfirmDeleteDialog = false
                    selectedStudentId = ""
                }
            )
        }
        if (showConfirmExportDialog) {
            ConfirmationBox(
                title = stringResource(R.string.ConfirmationBox_ExportStudent),
                message = stringResource(R.string.ConfirmationBox_ExportStudentContent),
                onDismissRequest = {
                    showConfirmExportDialog = false
                },
                onConfirmClick = {
                    managerViewModel.exportStudentsToCsv(context = context)
                    showConfirmExportDialog = false
                }
            )
        }
        if (swipeEnable) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppScreen.AddStudent.name)
                },
                shape = RoundedCornerShape(50),
                containerColor = PrimaryContent,
                contentColor = ThirdContent,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Student"
                )
            }
        }
    }
}

// Components
@Composable
fun StudentList(
    studentList: List<Student>,
    navController: NavHostController,
    managerViewModel: ManagerViewModel,
    onEditSwipe: (String) -> Unit,
    onDeleteSwipe: (String) -> Unit,
    swipeEnable: Boolean = true,
) {
    LazyColumn {
        items(studentList.size) { index ->
            val student = studentList[index]
            InformationCard(
                imageUrl = student.imageUrl,
                name = student.name,
                mainInformation = "ID: ${student.id}",
                subInformation = "Class: ${student.stdClass}",
                identificationInformation = student.id,
                onSeeMoreClicked = {
                    managerViewModel.onStudentSeeMoreClicked(
                        studentId = it,
                        navController = navController
                    )
                },
                onEditSwipe = { onEditSwipe(student.id) },
                onDeleteSwipe = { onDeleteSwipe(student.id) },
                swipeEnable = swipeEnable
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onSortSelected: (String) -> Unit = { },
    onMinimumCertificatesInput: (String) -> Unit = { },
    existingFaculty: List<String> = emptyList(),
    onFacultyPick: (String) -> Unit = { },
    existingClass: List<String> = emptyList(),
    onClassPick: (String) -> Unit = { },
    showDialog: Boolean = true,
    onApplyButtonClick: () -> Unit = { },
    onDismissDialog: () -> Unit = { },
    onClearButtonClick: () -> Unit = { }
) {
    val sortOptions = listOf("A → Z", "Z → A")
    var minimumCertificate by remember { mutableStateOf("") }

    if (showDialog) {
        BasicAlertDialog(
            onDismissRequest = onDismissDialog,
            content = {
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 4.dp
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.Filter),
                            style = CustomTypography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryContent,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        InformationSelect(
                            icon = Icons.Filled.Start,
                            label = stringResource(R.string.Filter_SortBy),
                            options = sortOptions,
                            onOptionPick = { onSortSelected(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        InformationLine(
                            icon = Icons.Filled.Numbers,
                            label = stringResource(R.string.Filter_MinCert),
                            value = minimumCertificate,
                            onValueChange = {
                                minimumCertificate = it
                                onMinimumCertificatesInput(minimumCertificate)
                            },
                            enable = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        InformationSelect(
                            icon = Icons.Filled.Apartment,
                            label = stringResource(R.string.Filter_Faculty),
                            options = existingFaculty,
                            onOptionPick = { onFacultyPick(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        InformationSelect(
                            icon = Icons.Filled.MeetingRoom,
                            label = stringResource(R.string.Filter_Class),
                            options = existingClass,
                            onOptionPick = { onClassPick(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = { onDismissDialog() }
                            ) {
                                Text(
                                    text = stringResource(R.string.Button_Cancel),
                                    style = CustomTypography.bodyMedium,
                                    color = PrimaryContent
                                )
                            }
                            TextButton(
                                onClick = {
                                    onClearButtonClick()
                                    onDismissDialog()
                                    minimumCertificate = ""
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.Button_ClearFilter),
                                    style = CustomTypography.bodyMedium,
                                    color = PrimaryContent
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    onApplyButtonClick()
                                    onDismissDialog()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryContent,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.Button_Apply),
                                    style = CustomTypography.bodyMedium,
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
