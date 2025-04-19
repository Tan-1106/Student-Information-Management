package com.example.studentinformationmanagement.ui.shared


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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// Composable: Thông tin chi tiết của người dùng
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProfile(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    user: User?
) {
    Scaffold(
        containerColor = Color.White,
        modifier = modifier
            .systemBarsPadding(),
        topBar = topBar,
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            if (user == null) {
                // Loading State
                androidx.compose.material3.CircularProgressIndicator(
                    color = primary_content,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                // Data Loaded — Hiển thị thông tin chi tiết
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.padding(vertical = 20.dp)) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(2.dp, color = secondary_dark, shape = CircleShape)
                                .size(150.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    InformationLine(Icons.Filled.Person, "Name", user.userName)
                    InformationLine(Icons.Filled.Cake, "Birthday", user.userBirthday)
                    InformationLine(Icons.Filled.Email, "Email", user.userEmail)
                    InformationLine(Icons.Filled.Phone, "Phone", user.userPhoneNumber)
                    InformationLine(Icons.Filled.Person, "Role", user.userRole)
                    InformationLine(Icons.Filled.BrokenImage, "Status", user.userStatus)

                }
            }
        }
    }
}

// Composable: Dòng thông tin
// Sử dụng bởi: DetailProfile
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


// Composable: Danh sách người dùng
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
                stateOrClass = userList[index].userStatus,
                phoneNumber = userList[index].userPhoneNumber,
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
        modifier = modifier
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
                if (stateOrClass == "Active") {
                    Text(
                        text = stateOrClass,
                        fontSize = 14.sp,
                        fontFamily = kanit_regular_font,
                        color = primary_content
                    )
                } else if (stateOrClass == "Inactive") {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationDate(
    icon: ImageVector,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis < Clock.systemUTC().millis()
        }
    })
    var openSheet by remember { mutableStateOf(false) }
    var birthday by remember { mutableStateOf("") }
    Row(
        modifier = modifier
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
            Text(
                text = if (birthday == "") placeholder else birthday,
                color = primary_dark,
                fontSize = 14.sp, fontFamily = kanit_regular_font,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable(onClick = { openSheet = true }),

                )
            if (openSheet) {
                ModalBottomSheet(
                    onDismissRequest = { openSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                ) {
                    Column(modifier = Modifier.systemBarsPadding()) {

                        DatePicker(state = datePickerState)
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                openSheet = false
                            }) {
                                Text("Hủy")
                            }
                            TextButton(onClick = {
                                birthday = datePickerState.selectedDateMillis?.let {
                                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                }?.format(formatter) ?: ""
                                openSheet = false
                            }) {
                                Text("Chọn")
                            }
                        }
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

            TextButton(onClick = { expanded = true }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(selectedOption, fontFamily = kanit_regular_font, color = primary_dark)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
            DropdownMenu(
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
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

// Preview
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DetailProfilePreview() {
    DetailProfile(
        user = User("", "Nguyễn Văn A", "01/01/2004", "nguyenvana@gmail.com", "0123456789", "Manager", "Enable")
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun UserListPreview() {
    UserList(SampleData.sampleUserList)
}
