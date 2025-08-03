package com.example.studentinformationmanagement.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.SecondaryContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel

@Composable
fun ForgotPassword(
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    // Variables
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }


    // UI
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = stringResource(R.string.AppBrand),
            style = CustomTypography.displayLarge,
            color = PrimaryContent
        )

        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.ForgotPassword),
            style = CustomTypography.headlineSmall,
            color = PrimaryContent
        )
        Spacer(modifier = Modifier.height(20.dp))
        EmailTextField(
            value = email,
            onValueChange = { email = it },
            label = R.string.Login_EmailLabel,
            leadingIcon = Icons.Filled.Email,
            isError = loginViewModel.emailErrorMessage.isNotEmpty(),
            supportingText = {
                Text(
                    text = loginViewModel.emailErrorMessage,
                    style = CustomTypography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                loginViewModel.sendResetEmail(
                    email = email,
                    onSuccess = {
                        loginViewModel.clearErrorMessage()
                        navController.navigateUp()
                        Toast.makeText(context, "Reset email sent", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryContent,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(50.dp)
        ) {
            Text(
                text = stringResource(R.string.Button_SendResetEmail),
                style = CustomTypography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.ForgotPassword_Text1),
                style = CustomTypography.bodyLarge,
                color = SecondaryContent
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.ForgotPassword_SignIn),
                style = CustomTypography.bodyLarge,
                color = PrimaryContent,
                modifier = Modifier
                    .clickable {
                        navController.navigateUp()
                        loginViewModel.clearErrorMessage()
                    }
            )
        }
    }
}