package com.example.studentinformationmanagement.ui.manager

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.data.manager.Student
import com.example.studentinformationmanagement.ui.shared.ConfirmationBox
import com.example.studentinformationmanagement.ui.shared.InformationBox
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.third_content

// Composable: Student Management
@Composable
fun StudentManagement(
    managerViewModel: ManagerViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val managerUiState by managerViewModel.uiState.collectAsState()
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var selectedStudentId by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        // Main Content - Search + Filter + Student list
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
        ) {
            // Search + Filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar
                OutlinedTextField(
                    value = managerViewModel.searchInput,
                    onValueChange = {
                        managerViewModel.onStudentSearch(it)
                    },
                    modifier = Modifier
                        .weight(1f),
                    placeholder = { Text("Search...", fontSize = 16.sp, color = primary_content) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = primary_content
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(fontSize = 16.sp, color = primary_content),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = third_content,
                        focusedContainerColor = primary_container,
                        focusedBorderColor = secondary_content,
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        managerViewModel.onFilterClick()
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = secondary_content,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                }

                FilterDialog(
                    onSortSelected = { managerViewModel.onSortSelected(it) },
                    onMinimumCertificatesInput = { managerViewModel.onMinimumCertificatesInput(it) },
                    existingFaculty = managerViewModel.facultyList,
                    onFacultyPick = { managerViewModel.onFacultyPick(it) },
                    existingClass = managerViewModel.classList,
                    onClassPick = { managerViewModel.onClassPick(it) },
                    showDialog = managerViewModel.isShowDialog,
                    onApplyButtonClick = { managerViewModel.onApplyFilterClick() },
                    onDismissDialog = { managerViewModel.onDismissFilterClick() },
                    onClearButtonClick = { managerViewModel.onClearFilterClick() }
                )
            }

            Text(
                text = "List Student",
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                fontSize = 24.sp,
                fontFamily = kanit_bold_font,
                color = primary_content
            )

            // User list
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
                }
            )
        }

        if (showConfirmDeleteDialog) {
            ConfirmationBox(
                title = "Confirm deletion",
                message = "Do you want to delete this student?",
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

        // Add user button
        FloatingActionButton(
            onClick = { managerViewModel.onAddStudentButtonClicked(navController) },
            shape = RoundedCornerShape(50),
            containerColor = primary_content,
            contentColor = third_content,
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

@Composable
fun StudentList(
    studentList: List<Student>,
    navController: NavHostController,
    managerViewModel: ManagerViewModel,
    onEditSwipe: (String) -> Unit,
    onDeleteSwipe: (String) -> Unit
) {
    LazyColumn {
        items(studentList.size) { index ->
            InformationBox(
                imageUrl = studentList[index].studentImageUrl,
                name = studentList[index].studentName,
                mainInformation = studentList[index].studentId,
                subInformation = studentList[index].studentClass,
                identificationInformation = studentList[index].studentPhoneNumber,
                onSeeMoreClicked = {
                    managerViewModel.onStudentSeeMoreClicked(
                        studentPhoneNumber = it,
                        navController = navController
                    )
                },
                onEditSwipe = { onEditSwipe(studentList[index].studentId) },
                onDeleteSwipe = { onDeleteSwipe(studentList[index].studentId) }
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
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
                            text = "Filter",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = primary_content,
                            fontFamily = kanit_bold_font,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )

                        InformationSelect(
                            icon = Icons.Filled.Start,
                            label = "Sort by: ",
                            options = sortOptions,
                            onOptionPick = { onSortSelected(it) }
                        )

                        InformationLine(
                            icon = Icons.Filled.Numbers,
                            label = "Minimum number of certificates",
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

                        InformationSelect(
                            icon = Icons.Filled.Apartment,
                            label = "Faculty: ",
                            options = existingFaculty,
                            onOptionPick = { onFacultyPick(it) }
                        )

                        InformationSelect(
                            icon = Icons.Filled.MeetingRoom,
                            label = "Class: ",
                            options = existingClass,
                            onOptionPick = { onClassPick(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    onDismissDialog()
                                }
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = primary_content
                                )
                            }

                            TextButton(
                                onClick = {
                                    onClearButtonClick()
                                    onDismissDialog()
                                }
                            ) {
                                Text(
                                    text = "Clear filters",
                                    color = primary_content
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    onApplyButtonClick()
                                    onDismissDialog()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primary_content
                                )
                            ) {
                                Text("Apply")
                            }
                        }
                    }
                }
            }
        )
    }
}
