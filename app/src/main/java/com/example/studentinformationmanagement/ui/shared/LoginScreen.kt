package com.example.studentinformationmanagement.ui.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.app_brand),
            fontSize = 40.sp,
            fontFamily = FontFamily(
                Font(R.font.kanit_bold)
            ),
            color = primary_content
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.login_welcome),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily(
                Font(R.font.kanit_regular)
            ),
            color = primary_content
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.login_image),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        UsernameTextField(
            value = loginViewModel.userUsernameInput,
            onValueChange = { loginViewModel.onUsernameChange(it) },
            label = R.string.username_label,
            leadingIcon = Icons.Filled.Phone
        )
        Spacer(modifier = Modifier.height(20.dp))
        PasswordTextField(
            value = loginViewModel.userPasswordInput,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            label = R.string.password_label,
            leadingIcon = Icons.Filled.Password,
            isPasswordShowing = loginViewModel.isPasswordShowing,
            onPasswordVisibilityChange = { loginViewModel.onPasswordVisibilityChange() }
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                loginViewModel.onLoginButtonClicked(context, navController)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_content,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(50.dp)
        ) {
            Text(
                text = stringResource(R.string.login_button),
                fontFamily = FontFamily(Font(R.font.kanit_regular))
            )
        }
    }
}

// Phone number
@Composable
fun UsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(label),
                fontFamily = FontFamily(
                    Font(R.font.kanit_regular)
                )
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors (
                focusedTextColor = Color.Black,
                focusedBorderColor = primary_content,
                unfocusedBorderColor = secondary_content,
                focusedLeadingIconColor = primary_content,
                unfocusedLeadingIconColor = secondary_content,
                focusedLabelColor = primary_content,
                unfocusedLabelColor = Color.Gray,
            ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth(0.75f)
    )
}

// Password
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    leadingIcon: ImageVector,
    isPasswordShowing: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(label),
                fontFamily = FontFamily(
                    Font(R.font.kanit_regular)
                )
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibilityChange() }) {
                Icon(
                    imageVector = if (isPasswordShowing) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isPasswordShowing) "Hide password" else "Show password"
                )
            }
        },
        visualTransformation = if (isPasswordShowing) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors (
            focusedTextColor = Color.Black,
            focusedBorderColor = primary_content,
            unfocusedBorderColor = secondary_content,
            focusedLeadingIconColor = primary_content,
            unfocusedLeadingIconColor = secondary_content,
            focusedLabelColor = primary_content,
            unfocusedLabelColor = Color.Gray,
            unfocusedTrailingIconColor = secondary_content,
            focusedTrailingIconColor = primary_content
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.75f)
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    val fakeNavController = rememberNavController()
    LoginScreen(
        loginViewModel = viewModel(),
        navController = fakeNavController
    )
}