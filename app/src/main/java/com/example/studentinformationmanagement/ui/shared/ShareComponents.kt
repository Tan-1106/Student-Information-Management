package com.example.studentinformationmanagement.ui.shared


import android.widget.Toast
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
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
import com.example.studentinformationmanagement.data.shared.CurrentUser
import com.example.studentinformationmanagement.data.shared.SampleData
import com.example.studentinformationmanagement.ui.admin.AdminViewModel
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.kanit_regular_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.secondary_dark
import com.example.studentinformationmanagement.ui.theme.third_content
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// Composable: User's detail profile
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProfile(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit) = {},
    user: CurrentUser
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
            // Data Loaded — Hiển thị thông tin chi tiết
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .size(100.dp)
                ) {
                    AsyncImage(
                        model = user.userImageUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.avt_placeholder),
                        error = painterResource(id = R.drawable.avt_error)
                    )
                }
                InformationLine(
                    Icons.Filled.Person,
                    "Name",
                    user.userName,

                    errorMessage = "Please enter your name"
                )
                InformationLine(Icons.Filled.Cake, "Birthday", user.userBirthday)
                InformationLine(Icons.Filled.Email, "Email", user.userEmail)
                InformationLine(Icons.Filled.Phone, "Phone", user.userPhoneNumber)
                InformationLine(Icons.Filled.Person, "Role", user.userRole)
                InformationLine(Icons.Filled.BrokenImage, "Status", user.userStatus)
            }
        }
    }
}

// Composable: Information line in user's detail information
@Composable
fun InformationLine(
    icon: ImageVector,
    label: String,
    value: String,
    enable: Boolean = false,
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    errorMessage: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.weight(0.2f),
            tint = primary_content
        )
        Column(modifier = Modifier.weight(0.8f)) {
            Text(
                label, fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primary_content,
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
                                text = placeholder,
                                style = TextStyle(
                                    color = primary_dark,
                                    fontSize = 14.sp,
                                    fontFamily = kanit_regular_font
                                )
                            )
                        }
                        innerTextField()
                    }
                },
                value = value,
                onValueChange = onValueChange,
                enabled = enable,
                textStyle = TextStyle(
                    color = primary_dark,
                    fontSize = 14.sp,
                    fontFamily = kanit_regular_font
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
            )
            if (errorMessage != "") {
                Divider(color = Color.Red)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        errorMessage,
                        style = TextStyle(color = Color.Red),
                    )
                }
            } else {
                Divider()
            }
        }
    }
}

// Composable: Divide between information line
@Composable
fun Divider(color: Color = Color.Gray) {
//    Spacer(modifier = Modifier.height(5.dp))
    HorizontalDivider(
        color = color,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}


// Composable: User list
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

// Composable: User information box for user list
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
    var context = LocalContext.current
    Box(
        modifier = modifier
            .padding(16.dp)
            .background(color = third_content, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .border(shape = RoundedCornerShape(16.dp), width = 1.dp, color = primary_content)

    ) {
        SwipeComponent(
            onSwipeLeft = { Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show() },
            onSwipeRight = { Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show() },
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                // Avatar Image
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationDate(
    icon: ImageVector,
    label: String,
    placeholder: String,
    onDatePick: (String) -> Unit,
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
                                onDatePick(birthday)
                            }) {
                                Text("Chọn")
                            }
                        }
                    }
                }
            }
            Divider()
        }
    }
}

@Composable
fun InformationSelect(
    icon: ImageVector,
    label: String,
    options: List<String>,
    onOptionPick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Choose 1 option") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.weight(0.2f),
            tint = primary_content
        )
        Column(modifier = Modifier.weight(0.8f)) {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primary_content,
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
                            onOptionPick(selectedOption)
                        }
                    )
                }
            }
            Divider()
        }
    }
}

@Composable
fun SwipeComponent(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    content: @Composable () -> Unit
) {
    var startAction =
        SwipeAction(
            icon = {
                Box(contentAlignment = Alignment.TopCenter) {
                    Icon(Icons.Outlined.Edit, contentDescription = null)
                }
            },
            onSwipe = onSwipeLeft,
            background = Color.Gray,
        )
    var endAction =
        SwipeAction(
            icon = {
                Box(contentAlignment = Alignment.TopCenter) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                }
            },
            onSwipe = onSwipeRight,
            background = Color.Red,
        )
    SwipeableActionsBox(

        startActions = listOf(startAction),
        endActions = listOf(endAction),
        swipeThreshold = 100.dp,
        backgroundUntilSwipeThreshold = Color.White,
    ) {
        content()
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
        user = CurrentUser(
            "",
            "Nguyễn Văn A",
            "01/01/2004",
            "nguyenvana@gmail.com",
            "0123456789",
            "Manager",
            "Enable"
        )
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
