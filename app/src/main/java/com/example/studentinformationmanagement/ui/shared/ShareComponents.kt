package com.example.studentinformationmanagement.ui.shared


import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.admin.User
import com.example.studentinformationmanagement.data.manager.Student
import com.example.studentinformationmanagement.data.shared.SampleData
import com.example.studentinformationmanagement.ui.admin.AdminViewModel
import com.example.studentinformationmanagement.ui.manager.ManagerViewModel
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.kanit_regular_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.secondary_dark
import com.example.studentinformationmanagement.ui.theme.third_content
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProfile(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    Scaffold(modifier = modifier.systemBarsPadding(), topBar = {
        TopAppBar(navigationIcon = {
            IconButton(
                content = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = primary_content,
                        modifier = Modifier.size(40.dp)
                    )
                },
                onClick = {}
            )
        }, title = {}, actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = primary_content,
                    modifier = Modifier.size(40.dp)
                )
            }
        })
    }, bottomBar = bottomBar) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Nguyen Van A",
                    color = primary_content,
                    fontSize = 25.sp,
                    fontFamily = kanit_bold_font
                )

                Box(Modifier.padding(vertical = 20.dp)) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,

                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                2.dp, color = secondary_dark, shape = CircleShape
                            )
                            .size(150.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
                InformationLine(Icons.Filled.Person, "Name", "Username")
                InformationLine(Icons.Filled.Cake, "Birthday", "18.3.2004")
                InformationLine(Icons.Filled.Email, "Email", "abc@gmail.com")
                InformationLine(Icons.Filled.Phone, "Phone", "Username")
                InformationLine(Icons.Filled.BrokenImage, "Status", "Username")
            }
        }
    }
}

@Composable
fun InformationLine(
    icon: ImageVector,
    label: String,
    value: String,
    enable: Boolean = false,
    onValueChange: (String) -> Unit = {},
    placeholder: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.weight(0.2f),
            tint = primary_content
        )
        Column(modifier = Modifier.weight(0.8f)) {
            Text(
                label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primary_content,
                fontFamily = kanit_bold_font
            )
            BasicTextField(
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,  // Placeholder text
                                style = TextStyle(
                                    color = primary_dark,
                                    fontSize = 14.sp,
                                    fontFamily = kanit_regular_font
                                )
                            )
                        }
                        innerTextField()  // Hiển thị TextField thực sự
                    }
                },
                value = value,

                onValueChange = onValueChange,
                enabled = enable,
                textStyle = TextStyle(
                    color = primary_dark,
                    fontSize = 14.sp, fontFamily = kanit_regular_font
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
            )

            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
fun UserList(
    userList: List<User>,
    viewModel: AdminViewModel = viewModel()
) {
    LazyColumn {
        items(userList.size) { index ->
            InformationBox(
                imageUrl = userList[index].userImageUrl,
                name = userList[index].userName,
                roleOrStuId = userList[index].userRole,
                stateOrClass = userList[index].userState,
                phoneNumber = userList[index].userPhoneNumber,
                onSeeMoreClicked = { viewModel.onUserSeeMoreClicked(it) },
                onEditButtonClicked = { viewModel.onUserEditClicked(it) }
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

@Composable
fun InformationBox(
    imageUrl: String,
    name: String,
    roleOrStuId: String,
    stateOrClass: String,
    phoneNumber: String,
    onEditButtonClicked: (String) -> Unit,
    onSeeMoreClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(color = third_content, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .border(shape = RoundedCornerShape(16.dp), width = 1.dp, color = primary_content)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
        ) {
//             Avatar Image
            AsyncImage(
                model = imageUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.avt_placeholder),
                error = painterResource(id = R.drawable.avt_error)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    color = primary_content,
                    fontFamily = kanit_bold_font
                )
                Text(
                    text = roleOrStuId,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontFamily = kanit_regular_font
                )
                if (stateOrClass == "Normal") {
                    Text(
                        text = stateOrClass,
                        fontSize = 14.sp,
                        fontFamily = kanit_regular_font,
                        color = primary_content
                    )
                } else if (stateOrClass == "Locked") {
                    Text(
                        text = stateOrClass,
                        fontSize = 14.sp,
                        fontFamily = kanit_regular_font,
                        color = secondary_dark
                    )
                } else {
                    Text(
                        text = stateOrClass,
                        fontSize = 14.sp,
                        fontFamily = kanit_regular_font,
                        color = secondary_content
                    )
                }

            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = { onEditButtonClicked(phoneNumber) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                    )
                }
                Text(
                    text = stringResource(R.string.see_more),
                    fontFamily = kanit_regular_font,
                    color = primary_content,
                    modifier = Modifier
                        .clickable {
                            onSeeMoreClicked(phoneNumber)
                        }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun InformationDate(icon: ImageVector, label: String) {
    val context = LocalContext.current

    // State lưu ngày đã chọn
    var selectedDate by remember { mutableStateOf("") }

    // Lấy ngày hiện tại làm mặc định
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Tạo dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, y: Int, m: Int, d: Int ->
            selectedDate = String.format("%02d/%02d/%04d", d, m + 1, y)
        },
        year, month, day
    )

    // UI: TextField hiển thị ngày
    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                datePickerDialog.show()
            },
        label = { Text("Chọn ngày") },
        enabled = false, // Không cho người dùng gõ trực tiếp
        readOnly = true
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationSelect(icon: ImageVector, label: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Choose 1 option") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.weight(0.2f),
            tint = primary_content
        )
        Column(modifier = Modifier.weight(0.8f)) {
            Text(
                label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primary_content,
                fontFamily = kanit_bold_font
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                BasicTextField(
                    value = selectedOption,
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .padding(bottom = 8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}
@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ComponentPreview() {
    UserList(SampleData.sampleUserList)
}
