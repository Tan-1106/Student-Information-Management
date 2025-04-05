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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.third_content

@Composable
fun StudentManagement(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val students = remember {
        listOf(
            "Nguyen Van A",
            "Tran Thi B",
            "Le Van C",
            "Pham Thi D"
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
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
        // List View
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(students.filter {
                it.contains(searchText.text, ignoreCase = true)
            }) { student ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = student,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
