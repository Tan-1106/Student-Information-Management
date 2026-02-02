package com.example.studentinformationmanagement.presentation.ui

import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.presentation.theme.CustomTypography
import com.example.studentinformationmanagement.presentation.theme.PrimaryContent
import com.example.studentinformationmanagement.presentation.theme.SecondaryContent
import com.example.studentinformationmanagement.presentation.uistate.AuthUiState

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: (email: String, password: String, rememberMe: Boolean) -> Unit,
) {
    // Variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isPasswordShowing by remember { mutableStateOf(false) }

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
        Text(
            text = stringResource(R.string.Login_Welcome),
            style = CustomTypography.headlineSmall,
            color = PrimaryContent
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        EmailTextField(
            value = email,
            onValueChange = { email = it },
            label = R.string.Login_EmailLabel,
            leadingIcon = Icons.Filled.Email,
            isError = uiState.errorMessage != null,
        )
        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = R.string.Login_PasswordLabel,
            leadingIcon = Icons.Filled.Password,
            isPasswordShowing = isPasswordShowing,
            onPasswordVisibilityChange = { isPasswordShowing = !isPasswordShowing },
            isError = uiState.errorMessage != null,
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (uiState.errorMessage != null)
            Text(
                text = uiState.errorMessage,
                style = CustomTypography.labelMedium,
                color = Color.Red,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = SecondaryContent,
                    checkedColor = PrimaryContent,
                    checkmarkColor = Color.White,
                )
            )
            Text(
                text = stringResource(R.string.Login_RememberMe),
                style = CustomTypography.bodyMedium,
                color = SecondaryContent
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.Login_ForgotPassword),
                style = CustomTypography.bodyMedium,
                color = PrimaryContent,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onForgotPasswordClick() }
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { onLoginClick(email, password, rememberMe) },
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryContent,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(40.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.Button_SignIn),
                    style = CustomTypography.titleMedium
                )
            }
        }
    }
}

// Components
@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    leadingIcon: ImageVector,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(label),
                style = CustomTypography.labelMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedBorderColor = PrimaryContent,
            unfocusedBorderColor = SecondaryContent,
            focusedLeadingIconColor = PrimaryContent,
            unfocusedLeadingIconColor = SecondaryContent,
            focusedLabelColor = PrimaryContent,
            unfocusedLabelColor = if (value.isEmpty()) Color.Gray else SecondaryContent,
        ),
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = modifier.fillMaxWidth(0.75f)
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
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(label),
                style = CustomTypography.labelMedium
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedBorderColor = PrimaryContent,
            unfocusedBorderColor = SecondaryContent,
            focusedLeadingIconColor = PrimaryContent,
            unfocusedLeadingIconColor = SecondaryContent,
            focusedLabelColor = PrimaryContent,
            unfocusedLabelColor = if (value.isEmpty()) Color.Gray else SecondaryContent,
            unfocusedTrailingIconColor = SecondaryContent,
            focusedTrailingIconColor = PrimaryContent
        ),
        isError = isError,
        singleLine = true,
        modifier = modifier.fillMaxWidth(0.75f)
    )
}

// Preview
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        uiState = AuthUiState(),
        onForgotPasswordClick = {},
        onLoginClick = { _, _, _ -> },
    )
}