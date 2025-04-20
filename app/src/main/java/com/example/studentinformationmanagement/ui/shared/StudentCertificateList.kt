package com.example.studentinformationmanagement.ui.shared

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun StudentCertificationList() {
    Scaffold { paddingValue ->
        Box(modifier = Modifier.padding(paddingValue)) {

        }
    }
}

@Composable
fun SwipeActionItem(
    text: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val edit = SwipeAction(
        icon = { Text("✏️") },
        background = Color(0xFFFFEB3B),
        onSwipe = onEdit
    )
    val delete = SwipeAction(
        icon = { Text("🗑️") },
        background = Color(0xFFF44336),
        onSwipe = onDelete
    )

    SwipeableActionsBox(
        startActions = listOf(edit),
        endActions = listOf(delete),
    ) {
            Text(text)
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewSwipe() {
    var context= LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        SwipeActionItem(
            text = "Item 1",
            onEdit = { Toast.makeText(context,"edit", Toast.LENGTH_SHORT) },
            onDelete = {  Toast.makeText(context,"delete", Toast.LENGTH_SHORT) }
        )
    }
}