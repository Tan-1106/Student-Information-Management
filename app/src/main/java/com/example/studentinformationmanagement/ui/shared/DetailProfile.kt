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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.theme.Pink40
import com.example.studentinformationmanagement.ui.theme.PurpleGrey40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DetailProfile(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Pink40,modifier=Modifier.size(40.dp))
                },
                title = {},
                actions = {
                    Icon(Icons.Outlined.Settings, contentDescription = null, tint = Pink40,modifier=Modifier.size(40.dp))
                }
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Icon(
                    Icons.Outlined.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                    , tint = Pink40
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Nguyen Van A", color = Pink40, fontSize = 25.sp)

                Box(Modifier.padding(vertical = 20.dp)) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,

                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                2.dp, color = Pink40, shape = CircleShape
                            ).size(150.dp),
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
fun InformationLine(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon, contentDescription = null, modifier = Modifier.weight(0.2f),
            tint = Pink40
        )
        Column(modifier = Modifier.weight(0.8f)) {
            Text(label, fontSize =18.sp, fontWeight = FontWeight.Bold , color = Pink40)
            Text(value, modifier = Modifier.padding(vertical = 5.dp), fontSize = 13.sp, color = Pink40)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }
    }
}