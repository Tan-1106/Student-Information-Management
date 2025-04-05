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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentinformationmanagement.R

@Composable
fun LoginScreen(
    // Gọi ViewModel của trang để sử dụng
    loginViewModel: LoginViewModel = viewModel()
) {
    // Gọi UiState của trang để sử dụng (Optional)
    // val loginUiState by loginViewModel.uiState.collectAsState()

    /* Giao diện của LoginScreen được viết tại đây
     * Và được xử lý bằng các hàm được gọi từ loginViewModel
     **/
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
            color = colorResource(R.color.primary_content)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.login_welcome),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily(
                Font(R.font.kanit_regular)
            ),
            color = colorResource(R.color.primary_content)
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
    }
}

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
                focusedBorderColor = colorResource(R.color.primary_content),
                unfocusedBorderColor = colorResource(R.color.secondary_content),
                focusedLeadingIconColor = colorResource(R.color.primary_content),
                unfocusedLeadingIconColor = colorResource(R.color.secondary_content),
                focusedLabelColor = colorResource(R.color.primary_content),
                unfocusedLabelColor = Color.Gray,
            ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.7f)
    )
}

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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors (
            focusedTextColor = Color.Black,
            focusedBorderColor = colorResource(R.color.primary_content),
            unfocusedBorderColor = colorResource(R.color.secondary_content),
            focusedLeadingIconColor = colorResource(R.color.primary_content),
            unfocusedLeadingIconColor = colorResource(R.color.secondary_content),
            focusedLabelColor = colorResource(R.color.primary_content),
            unfocusedLabelColor = Color.Gray,
            unfocusedTrailingIconColor = colorResource(R.color.secondary_content),
            focusedTrailingIconColor = colorResource(R.color.primary_content)
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.7f)
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}