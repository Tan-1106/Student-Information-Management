package com.example.studentinformationmanagement.ui.shared


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.shared.NavItem
import com.example.studentinformationmanagement.data.uiState.CurrentUser
import com.example.studentinformationmanagement.ui.theme.CustomBlack
import com.example.studentinformationmanagement.ui.theme.CustomGray
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContainer
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.SecondaryContent
import com.example.studentinformationmanagement.ui.theme.ThirdContent
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Components For Information Show
@Composable
fun InformationLine(
    icon: ImageVector,
    label: String,
    value: String,
    enable: Boolean = false,
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    errorMessage: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryContent,
                modifier = Modifier.weight(0.2f)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = CustomTypography.titleLarge,
                    color = PrimaryContent,
                )
                BasicTextField(
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = CustomTypography.labelLarge,
                                    color = CustomGray
                                )
                            }
                            innerTextField()
                        }
                    },
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enable,
                    textStyle = CustomTypography.bodyMedium.copy(color = CustomBlack),
                    singleLine = true,
                    keyboardOptions = keyboardOptions,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = if (errorMessage.isEmpty()) Color.Gray else Color.Red)
            }
        }
        if (errorMessage != "") {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = errorMessage,
                    style = CustomTypography.bodyMedium,
                    color = Color.Red,
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
    onDatePick: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String = "",
    canSelectFuture: Boolean = false
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val selectableDates = if (canSelectFuture) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = true
        }
    } else {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis < Clock.systemUTC().millis()
            }
        }
    }
    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates
    )
    var openSheet by remember { mutableStateOf(false) }
    var birthday by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryContent,
                modifier = Modifier.weight(0.2f),
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = CustomTypography.titleLarge,
                    color = PrimaryContent,
                )
                Text(
                    text = birthday.ifEmpty { placeholder },
                    style = if (birthday.isEmpty()) CustomTypography.labelLarge else CustomTypography.bodyMedium,
                    color = if (birthday.isEmpty()) CustomGray else CustomBlack,
                    modifier = Modifier
                        .fillMaxWidth()
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
                                    Text(
                                        text = stringResource(R.string.Button_Cancel),
                                        style = CustomTypography.bodyMedium
                                    )
                                }
                                TextButton(onClick = {
                                    birthday = datePickerState.selectedDateMillis?.let {
                                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                                    }?.format(formatter) ?: ""
                                    openSheet = false
                                    onDatePick(birthday)
                                }) {
                                    Text(
                                        text = stringResource(R.string.Button_Select),
                                        style = CustomTypography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                Divider(color = if (errorMessage.isEmpty()) Color.Gray else Color.Red)
            }
        }
        if (errorMessage != "") {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = errorMessage,
                    style = CustomTypography.bodyMedium,
                    color = Color.Red,
                )
            }
        }
    }
}

@Composable
fun InformationSelect(
    icon: ImageVector,
    label: String,
    options: List<String>,
    onOptionPick: (String) -> Unit,
    errorMessage: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Choose 1 option") }
    var optionColor by remember { mutableStateOf(Color.Gray) }
    var optionStyle by remember { mutableStateOf(CustomTypography.labelLarge) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryContent,
                modifier = Modifier.weight(0.2f),
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = CustomTypography.titleLarge,
                    color = PrimaryContent,
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 35.dp)
                        .clickable {
                            expanded = true
                        }
                ) {
                    Text(
                        text = selectedOption,
                        style = optionStyle,
                        color = optionColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    style = CustomTypography.bodyMedium,
                                    color = CustomBlack
                                )
                            },
                            onClick = {
                                selectedOption = option
                                expanded = false
                                onOptionPick(selectedOption)
                                optionColor = CustomBlack
                                optionStyle = CustomTypography.bodyMedium
                            }
                        )
                    }
                }
                Divider(color = if (errorMessage.isEmpty()) Color.Gray else Color.Red)
            }
        }
        if (errorMessage != "") {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = errorMessage,
                    style = CustomTypography.bodyMedium,
                    color = Color.Red,
                )
            }
        }
    }
}

// Composable: User's detail profile for admin
@Composable
fun DetailProfile(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit) = {},
    user: CurrentUser
) {
    Scaffold(
        containerColor = Color.White,
        topBar = topBar,
        modifier = modifier.systemBarsPadding()
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .size(100.dp)
                ) {
                    AsyncImage(
                        model = user.imageUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.avt_placeholder),
                        error = painterResource(id = R.drawable.avt_error)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Person, "Name", user.name)
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Cake, "Birthday", user.birthday)
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Email, "Email", user.email)
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Phone, "Phone", user.phone)
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.Person, "Role", user.role)
                Spacer(modifier = Modifier.height(20.dp))
                InformationLine(Icons.Filled.BrokenImage, "Status", user.status)
            }
        }
    }
}

// Composable: User information box for user list
@Composable
fun InformationCard(
    imageUrl: String,
    name: String,
    mainInformation: String,
    subInformation: String,
    identificationInformation: String,
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
                            style = CustomTypography.titleLarge,
                            color = PrimaryContent
                        )
                        Text(
                            text = mainInformation,
                            style = CustomTypography.bodyMedium,
                            color = Color.DarkGray
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            when (subInformation) {
                                "Active" -> Text(
                                    text = subInformation,
                                    style = CustomTypography.bodyMedium,
                                    color = PrimaryContent
                                )

                                "InActive" -> Text(
                                    text = subInformation,
                                    style = CustomTypography.bodyMedium,
                                    color = CustomGray
                                )

                                else -> Text(
                                    text = subInformation,
                                    style = CustomTypography.bodyMedium,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(R.string.Button_SeeMore),
                                style = CustomTypography.titleMedium,
                                color = PrimaryContent,
                                modifier = Modifier
                                    .clickable {
                                        onSeeMoreClicked(identificationInformation)
                                    }
                            )
                        }
                    }
                }
            }
        } else {
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
                        style = CustomTypography.titleLarge,
                        color = PrimaryContent
                    )
                    Text(
                        text = mainInformation,
                        style = CustomTypography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        when (subInformation) {
                            "Active" -> Text(
                                text = subInformation,
                                style = CustomTypography.bodyMedium,
                                color = PrimaryContent
                            )

                            "InActive" -> Text(
                                text = subInformation,
                                style = CustomTypography.bodyMedium,
                                color = CustomGray
                            )

                            else -> Text(
                                text = subInformation,
                                style = CustomTypography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.Button_SeeMore),
                            style = CustomTypography.titleMedium,
                            color = PrimaryContent,
                            modifier = Modifier
                                .clickable {
                                    onSeeMoreClicked(identificationInformation)
                                }
                        )
                    }
                }
            }
        }
    }
}

// Composable: Divide between information line
@Composable
fun Divider(color: Color = Color.Gray) {
    HorizontalDivider(
        color = color,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}


@Composable
fun SwipeComponent(
    onEditSwipe: () -> Unit,
    onDeleteSwipe: () -> Unit,
    content: @Composable () -> Unit
) {
    val startAction =
        SwipeAction(
            icon = {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null
                    )
                }
            },
            onSwipe = onEditSwipe,
            background = Color.Gray,
        )
    val endAction =
        SwipeAction(
            icon = {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null
                    )
                }
            },
            onSwipe = onDeleteSwipe,
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

@Composable
fun ConfirmationBox(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = CustomTypography.titleLarge
            )
        },
        text = {
            Text(
                text = message,
                style = CustomTypography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
            ) {
                Text(
                    text = "Confirm",
                    style = CustomTypography.titleMedium,
                    color = Color.Red
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = "Cancel",
                    style = CustomTypography.titleMedium
                )
            }
        }
    )
}


@Composable
fun HelpIcon(message: String) {
    var showTooltip by remember { mutableStateOf(false) }

    Box {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
            tint = PrimaryContent,
            contentDescription = "Help",
            modifier = Modifier
                .size(24.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            showTooltip = true
                            try {
                                awaitRelease()
                            } finally {
                                showTooltip = false
                            }
                        }
                    )
                }
        )

        if (showTooltip) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = IntOffset(0, -100),
                onDismissRequest = { showTooltip = false }
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .shadow(4.dp)
                ) {
                    Text(
                        text = message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
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

@Composable
fun BottomNavBarAdmin(
    items: List<NavItem>,
    currentRoute: String?,
    onItemClick: (NavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        items.forEach { item ->
            val selected = (currentRoute == item.route)
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) PrimaryContent else SecondaryContent
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) PrimaryContent else SecondaryContent
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = PrimaryContainer,
                    selectedIconColor = PrimaryContent,
                    selectedTextColor = PrimaryContent,
                    unselectedIconColor = SecondaryContent,
                    unselectedTextColor = SecondaryContent
                )
            )
        }
    }
}

@Composable
fun BottomNavBarManager(
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    currentRoute: String?,
    onItemClick: (NavItem) -> Unit
) {
    NavigationBar(
        modifier = modifier
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) PrimaryContent else SecondaryContent
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) PrimaryContent else SecondaryContent
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = PrimaryContainer,
                    selectedIconColor = PrimaryContent,
                    selectedTextColor = PrimaryContent,
                    unselectedIconColor = SecondaryContent,
                    unselectedTextColor = SecondaryContent
                )
            )
        }
    }
}