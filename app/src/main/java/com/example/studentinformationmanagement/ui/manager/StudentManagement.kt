package com.example.studentinformationmanagement.ui.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.data.shared.SampleData
import com.example.studentinformationmanagement.ui.shared.StudentList
import com.example.studentinformationmanagement.ui.shared.UserList
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.third_content

//@Composable
//fun StudentManagement(modifier: Modifier = Modifier) {
//    var searchText by remember { mutableStateOf(TextFieldValue("")) }
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .background(Color.White)
//    ) {
//        // Search + Filter
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = searchText,
//                onValueChange = { searchText = it },
//                modifier = Modifier
//                    .weight(1f),
//                placeholder = { Text("Tìm sinh viên...", fontSize = 16.sp, color = primary_content) },
//                leadingIcon = {
//                    Icon(Icons.Default.Search, contentDescription = "Search", tint = primary_content)
//                },
//                shape = RoundedCornerShape(16.dp),
//                textStyle = TextStyle(fontSize = 16.sp, color = primary_content),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedContainerColor = third_content,
//                    focusedContainerColor = primary_container,
//                    focusedBorderColor = secondary_content,
//                ),
//                singleLine = true
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            IconButton(onClick = { /* Mở bộ lọc */ }) {
//                Box(
//                    modifier = Modifier
//                        .background(
//                            color = secondary_content, // Màu nền tròn
//                            shape = RoundedCornerShape(50) // Đảm bảo nền có hình tròn
//                        )
//                        .padding(8.dp) // Padding để không bị ép icon vào cạnh
//                ) {
//                    Icon(
//                        Icons.Default.FilterList,
//                        contentDescription = "Filter",
//                        tint = Color.White // Màu của icon
//                    )
//                }
//            }
//        }
//        Text(
//            text = "List Student",
//            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
//            fontSize = 24.sp,
//            fontFamily = kanit_bold_font,
//            color = primary_content
//        )
//        UserList(SampleData.sampleUserList)
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Button(
//                onClick = { /* Hành động thêm sinh viên */ },
//                shape = RoundedCornerShape(16.dp),
//                modifier = Modifier
//                    .padding(vertical = 16.dp)
//                    .fillMaxWidth(0.6f) // Làm cho nút rộng vừa phải
//            ) {
//                Text(
//                    text = "Add Student",
//                    fontSize = 18.sp,
//                    fontFamily = kanit_bold_font,
//                    color = Color.White
//                )
//            }
//        }
//    }
//}
@Composable
fun StudentManagement(
    modifier: Modifier = Modifier,
    managerViewModel: ManagerViewModel = viewModel(),
    navController: NavHostController
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        // Nội dung chính, bao gồm Search + Filter và danh sách sinh viên
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp) // Để tránh che khuất nút add
        ) {
            // Search + Filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f),
                    placeholder = { Text("Tìm sinh viên...", fontSize = 16.sp, color = primary_content) },
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
                IconButton(onClick = { /* Mở bộ lọc */ }) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = secondary_content, // Màu nền tròn
                                shape = RoundedCornerShape(50) // Đảm bảo nền có hình tròn
                            )
                            .padding(8.dp) // Padding để không bị ép icon vào cạnh
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White // Màu của icon
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
            StudentList(SampleData.exampleStudentList)
        }

        // Add Student Button
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Đặt nút ở dưới cùng giữa màn hình
        ) {
            Button(
                onClick = { managerViewModel.onAddButtonClicked(navController) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f), // Làm cho nút rộng vừa phải
                colors = ButtonDefaults.buttonColors(
                    containerColor = third_content, // Màu nền của nút
                    contentColor = primary_content// Màu của văn bản trên nút
                )
            ) {
                Text(
                    text = "Add Student",
                    fontSize = 18.sp,
                    fontFamily = kanit_bold_font,
                )
            }
        }
    }
}
