package com.example.studentinformationmanagement.ui.user

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.uiState.LoginHistory
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.SecondaryContent
import com.example.studentinformationmanagement.ui.theme.ThirdContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginHistory(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    // Variables
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    LaunchedEffect(Unit) {
        loginViewModel.fetchLoginHistory()
    }

    // UI
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.Login_History),
                            style = CustomTypography.headlineMedium,
                            color = PrimaryContent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = PrimaryContent,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        onClick = {
                            navController.navigateUp()
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        if (loginUiState.loginHistoryList.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.Login_History_Empty),
                    style = CustomTypography.bodyMedium
                )
            }
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                items(loginUiState.loginHistoryList.size) { index ->
                    LoginHistoryItem(
                        loginViewModel = loginViewModel,
                        history = loginUiState.loginHistoryList[index],
                        index = index
                    )
                }
            }
        }
    }
}


@Composable
fun LoginHistoryItem(
    loginViewModel: LoginViewModel,
    history: LoginHistory,
    index: Int
) {
    val loginTime = loginViewModel.formatTimestamp(history.loginTime)
    val cardColor = if (index % 2 == 0) SecondaryContent else ThirdContent


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = history.imageUrl,
                contentDescription = "User Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(30.dp))

            Column {
                Text(
                    text = history.name,
                    style = CustomTypography.titleLarge
                )
                Text(
                    text = "Email: ${history.email}",
                    style = CustomTypography.bodyMedium
                )
                Text(
                    text = "Role: ${history.role}",
                    style = CustomTypography.bodyMedium
                )
                Text(
                    text = "Login time: $loginTime",
                    style = CustomTypography.bodyMedium
                )
            }
        }
    }
}
