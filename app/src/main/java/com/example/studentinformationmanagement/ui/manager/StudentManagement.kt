package com.example.studentinformationmanagement.ui.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.data.manager.Student
import com.example.studentinformationmanagement.ui.shared.InformationBox
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.third_content

// Composable: Student Management
@Composable
fun StudentManagement(
    modifier: Modifier = Modifier,
    managerViewModel: ManagerViewModel = viewModel(),
    navController: NavHostController
) {
    val managerUiState by managerViewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

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
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        /* TODO: Xử lý sự kiện tìm kiếm */

                    },
                    modifier = Modifier
                        .weight(1f),
                    placeholder = { Text("Search...", fontSize = 16.sp, color = primary_content) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = primary_content)
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
                IconButton(onClick = {
                    /* TODO: Xây dựng và xử lý sự kiện bộ lọc */
                }) {
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
            }

            Text(
                text = "List Student",
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                fontSize = 24.sp,
                fontFamily = kanit_bold_font,
                color = primary_content
            )

            // User list
            StudentList(managerUiState.userList)
        }

    // Add Student Button
        FloatingActionButton(
            onClick = { managerViewModel.onAddButtonClicked(navController) },
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
    viewModel: ManagerViewModel = viewModel()
) {
    LazyColumn {
        items(studentList.size) { index ->
            InformationBox(
                imageUrl = studentList[index].studentImageUrl,
                name = studentList[index].studentName,
                roleOrStuId = studentList[index].studentId,
                stateOrClass = studentList[index].studentClass,
                phoneNumber = studentList[index].studentPhoneNumber,
                onSeeMoreClicked = { viewModel.onUserSeeMoreClicked(it) },
                onEditButtonClicked = { viewModel.onUserEditClicked(it) }
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun StudentManagementPreview() {
    val fakeScreenNavController = rememberNavController()
    Scaffold { innerPadding ->
        StudentManagement(
            modifier = Modifier.padding(innerPadding),
            navController = fakeScreenNavController
        )
    }
}
