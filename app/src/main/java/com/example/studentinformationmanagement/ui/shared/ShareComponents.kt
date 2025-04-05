package com.example.studentinformationmanagement.ui.shared


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.kanit_regular_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.secondary_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DetailProfile(
    bottomBar: @Composable ()->Unit={},
    modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.systemBarsPadding(), topBar = {
        TopAppBar(navigationIcon = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = primary_content,
                modifier = Modifier.size(40.dp)
            )
        }, title = {}, actions = {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = null,
                tint = primary_content,
                modifier = Modifier.size(40.dp)
            )
        })
    }
    ,bottomBar=bottomBar) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Nguyen Van A",
                    color =primary_content,
                    fontSize = 25.sp,
                    fontFamily = kanit_bold_font
                )

                Box(Modifier.padding(vertical = 20.dp)) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,

                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                2.dp, color = secondary_dark, shape = CircleShape
                            )
                            .size(150.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
                InformationLine(Icons.Filled.Person, "Name", "Username")
                InformationLine(Icons.Filled.Cake, "Birhday", "18.3.2004")
                InformationLine(Icons.Filled.Email, "Email", "abc@gmail.com")
                InformationLine(Icons.Filled.Phone, "Phone", "Username")
                InformationLine(Icons.Filled.BrokenImage, "Status", "Username")
            }
        }
    }
}

@Composable
fun InformationLine(icon: ImageVector, label: String, value: String, enable: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon, contentDescription = null, modifier = Modifier.weight(0.2f), tint = primary_content
        )
        Column(modifier = Modifier.weight(0.8f)) {
            Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primary_content,
                fontFamily = kanit_bold_font)
            BasicTextField(
                value = value,
                onValueChange = {},
                enabled = enable,
                textStyle = TextStyle(
                    color = primary_dark,
                    fontSize = 14.sp
                    , fontFamily = kanit_regular_font
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
            )

            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationLine(icon: ImageVector, label: String, options: List<String>, enable: Boolean = false) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Choose 1 option") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon, contentDescription = null, modifier = Modifier.weight(0.2f), tint = primary_content
        )
        Column(modifier = Modifier.weight(0.8f)) {
            Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primary_content,
                fontFamily = kanit_bold_font)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                BasicTextField(
                    value = selectedOption,
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .padding(bottom = 8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}