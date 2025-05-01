package com.example.studentinformationmanagement.ui.admin

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studentinformationmanagement.data.shared.LoginHistory
import com.example.studentinformationmanagement.ui.shared.LoginViewModel
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.kanit_regular_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.third_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginHistory(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    LaunchedEffect(Unit) {
        loginViewModel.fetchLoginHistory()
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Login History",
                            fontSize = 35.sp,
                            fontFamily = kanit_bold_font,
                            color = primary_content
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = primary_content,
                                modifier = Modifier.size(40.dp)
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
        if (loginUiState.isLoading) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Loading...", fontSize = 20.sp, fontFamily = kanit_regular_font)
            }
        }
        else if (loginUiState.loginHistoryList.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No historical data available.", fontSize = 20.sp, fontFamily = kanit_regular_font)
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
                        loginUiState.loginHistoryList[index],
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

    val cardColor = if (index % 2 == 0) {
        secondary_content
    } else {
        third_content
    }

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
                model = history.userImageUrl,
                contentDescription = "User Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(30.dp))

            Column {
                Text(
                    text = history.userName,
                    fontFamily = kanit_bold_font
                )
                Text(
                    text = "Phone number: ${history.userPhoneNumber}",
                    fontFamily = kanit_regular_font,
                    fontSize = 12.sp
                )
                Text(
                    text = "Role: ${history.userRole}",
                    fontFamily = kanit_regular_font,
                    fontSize = 12.sp
                )
                Text(
                    text = "Login time: $loginTime",
                    fontFamily = kanit_regular_font,
                    fontSize = 12.sp,
                )
            }
        }
    }
}
