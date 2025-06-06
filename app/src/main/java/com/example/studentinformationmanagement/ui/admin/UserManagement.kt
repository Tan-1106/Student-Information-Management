package com.example.studentinformationmanagement.ui.admin

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.admin.User
import com.example.studentinformationmanagement.ui.shared.ConfirmationBox
import com.example.studentinformationmanagement.ui.shared.HelpIcon
import com.example.studentinformationmanagement.ui.shared.InformationBox
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.third_content

@Composable
fun UserManagement(
    modifier: Modifier = Modifier,
    adminViewModel: AdminViewModel,
    navController: NavHostController
) {
    val adminUiState by adminViewModel.uiState.collectAsState()
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var selectedUserPhoneNumber by remember { mutableStateOf("") }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar
                OutlinedTextField(
                    value = adminViewModel.searchInput,
                    onValueChange = {
                        adminViewModel.onUserSearch(it)
                    },
                    modifier = Modifier.weight(1f),
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
                IconButton(
                    onClick = {
                        adminViewModel.onFilterClick()
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .background(secondary_content, RoundedCornerShape(50))
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
                    onSortSelected = { adminViewModel.onSortSelected(it) },
                    onRolePick = { adminViewModel.onRoleSelected(it) },
                    onStatusPick = { adminViewModel.onStatusSelected(it) },
                    showDialog = adminViewModel.isShowDialog,
                    onApplyButtonClick = { adminViewModel.onApplyFilterClick() },
                    onDismissDialog = { adminViewModel.onDismissFilterClick() },
                    onClearButtonClick = { adminViewModel.onClearFilterClick() }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "List Users",
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                    fontSize = 24.sp,
                    fontFamily = kanit_bold_font,
                    color = primary_content
                )
                Button(
                    onClick = {
                        navController.navigate(AppScreen.LoginHistory.name)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary_content,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "View login history",
                        fontFamily = kanit_bold_font
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                HelpIcon(
                    message = "Swipe right to edit and left to delete a user"
                )
            }

            // User list
            UserList(
                userList = adminUiState.userList,
                adminViewModel = adminViewModel,
                navController = navController,
                onEditSwipe = { phoneNumber ->
                    adminViewModel.onEditUserSwipe(
                        userPhoneNumber = phoneNumber,
                        navController = navController
                    )
                },
                onDeleteSwipe = { phoneNumber ->
                    selectedUserPhoneNumber = phoneNumber
                    showConfirmDeleteDialog = true
                }
            )
        }

        if (showConfirmDeleteDialog) {
            ConfirmationBox(
                title = "Confirm deletion",
                message = "Do you want to delete this user?",
                onDismissRequest = {
                    showConfirmDeleteDialog = false
                    selectedUserPhoneNumber = ""
                },
                onConfirmClick = {
                    adminViewModel.onDeleteUser(selectedUserPhoneNumber)
                    showConfirmDeleteDialog = false
                    selectedUserPhoneNumber = ""
                }
            )
        }

        // Add user button
        FloatingActionButton(
            onClick = { adminViewModel.onAddUserButtonClicked(navController) },
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

// Composable: User list
@Composable
fun UserList(
    userList: List<User>,
    adminViewModel: AdminViewModel,
    navController: NavHostController,
    onEditSwipe: (String) -> Unit,
    onDeleteSwipe: (String) -> Unit
) {
    LazyColumn {
        items(userList.size) { index ->
            InformationBox(
                imageUrl = userList[index].userImageUrl,
                name = userList[index].userName,
                mainInformation = userList[index].userRole,
                subInformation = userList[index].userStatus,
                identificationInformation = userList[index].userPhoneNumber,
                onSeeMoreClicked = {
                    adminViewModel.onUserSeeMoreClicked(
                        userPhoneNumber = it,
                        navController = navController
                    )
                },
                onEditSwipe = { onEditSwipe(userList[index].userPhoneNumber) },
                onDeleteSwipe = { onDeleteSwipe(userList[index].userPhoneNumber) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FilterDialog(
    onSortSelected: (String) -> Unit = { },
    onRolePick: (String) -> Unit = { },
    onStatusPick: (String) -> Unit = { },
    showDialog: Boolean = true,
    onApplyButtonClick: () -> Unit = { },
    onDismissDialog: () -> Unit = { },
    onClearButtonClick: () -> Unit = { }
) {
    val sortOptions = listOf("A → Z", "Z → A")
    val roleOptions = listOf("Admin", "Manager", "Employee")
    val statusOptions = listOf("Active", "Inactive")

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

                        InformationSelect(
                            icon = Icons.Filled.VerifiedUser,
                            label = "Role: ",
                            options = roleOptions,
                            onOptionPick = { onRolePick(it) }
                        )

                        InformationSelect(
                            icon = Icons.Filled.CheckCircleOutline,
                            label = "Status: ",
                            options = statusOptions,
                            onOptionPick = { onStatusPick(it) }
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


