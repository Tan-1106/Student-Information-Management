package com.example.studentinformationmanagement.ui.user

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.uiState.User
import com.example.studentinformationmanagement.ui.shared.ConfirmationBox
import com.example.studentinformationmanagement.ui.shared.HelpIcon
import com.example.studentinformationmanagement.ui.shared.InformationCard
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.SecondaryContent
import com.example.studentinformationmanagement.ui.theme.ThirdContent
import com.example.studentinformationmanagement.ui.viewModel.AdminViewModel

// DONE
@Composable
fun UserManagement(
    modifier: Modifier = Modifier,
    adminViewModel: AdminViewModel,
    navController: NavHostController
) {
    // Variables
    val context = LocalContext.current
    val adminUiState by adminViewModel.uiState.collectAsState()
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var selectedUserEmail by remember { mutableStateOf("") }
    var searchInput by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

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
                        adminViewModel.onUserSearch(searchInput)
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
                    textStyle = CustomTypography.bodyMedium,
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
                IconButton(
                    onClick = { showFilterDialog = true }
                ) {
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
                    onSortSelected = { adminViewModel.onSortSelected(it) },
                    onRolePick = { adminViewModel.onRoleSelected(it) },
                    onStatusPick = { adminViewModel.onStatusSelected(it) },
                    showDialog = showFilterDialog,
                    onApplyButtonClick = {
                        adminViewModel.onApplyFilterClick()
                        showFilterDialog = false
                    },
                    onDismissDialog = { showFilterDialog = false },
                    onClearButtonClick = {
                        adminViewModel.onClearFilterClick()
                        showFilterDialog = false
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.User_UserList),
                    style = CustomTypography.titleLarge,
                    color = PrimaryContent,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
                )
                Button(
                    onClick = {
                        navController.navigate(AppScreen.LoginHistory.name)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryContent,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.Button_LoginHistory),
                        style = CustomTypography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                HelpIcon(message = stringResource(R.string.HelpIcon_User))
            }
            UserList(
                userList = adminUiState.userList,
                adminViewModel = adminViewModel,
                navController = navController,
                onEditSwipe = { email ->
                    adminViewModel.onEditUser(
                        userEmail = email,
                        navController = navController
                    )
                },
                onDeleteSwipe = { email ->
                    selectedUserEmail = email
                    showConfirmDeleteDialog = true
                }
            )
        }
        if (showConfirmDeleteDialog) {
            ConfirmationBox(
                title = stringResource(R.string.ConfirmationBox_DeleteUser),
                message = stringResource(R.string.ConfirmationBox_DeleteUserContent),
                onDismissRequest = {
                    showConfirmDeleteDialog = false
                    selectedUserEmail = ""
                },
                onConfirmClick = {
                    adminViewModel.onDeleteUser(selectedUserEmail, context)
                    showConfirmDeleteDialog = false
                    selectedUserEmail = ""
                }
            )
        }
        FloatingActionButton(
            onClick = { navController.navigate(AppScreen.AddUser.name) },
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

// Components
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
            InformationCard(
                imageUrl = userList[index].imageUrl,
                name = userList[index].name,
                mainInformation = userList[index].role,
                subInformation = userList[index].status,
                identificationInformation = userList[index].phone,
                onSeeMoreClicked = {
                    adminViewModel.onUserSeeMoreClicked(
                        userPhoneNumber = it,
                        navController = navController
                    )
                },
                onEditSwipe = { onEditSwipe(userList[index].email) },
                onDeleteSwipe = { onDeleteSwipe(userList[index].email) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                        InformationSelect(
                            icon = Icons.Filled.VerifiedUser,
                            label = stringResource(R.string.Filter_Role),
                            options = roleOptions,
                            onOptionPick = { onRolePick(it) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        InformationSelect(
                            icon = Icons.Filled.CheckCircleOutline,
                            label = stringResource(R.string.Filter_Status),
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
                                    text = stringResource(R.string.Button_Cancel),
                                    style = CustomTypography.bodyMedium,
                                    color = PrimaryContent
                                )
                            }

                            TextButton(
                                onClick = {
                                    onClearButtonClick()
                                    onDismissDialog()
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
                                    containerColor = PrimaryContent
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


