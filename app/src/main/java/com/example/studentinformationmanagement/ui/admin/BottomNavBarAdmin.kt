package com.example.studentinformationmanagement.ui.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.shared.NavItem
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content

@Composable
fun BottomNavBarAdmin(
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    currentRoute: String?,
    onItemClick: (NavItem) -> Unit
) {
    NavigationBar(
        modifier = modifier
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) primary_content else secondary_content
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) primary_content else secondary_content
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = primary_container,
                    selectedIconColor = primary_content,
                    selectedTextColor = primary_content,
                    unselectedIconColor = secondary_content,
                    unselectedTextColor = secondary_content
                )
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)

@Composable
fun BottomNavBarAdminPreview() {
    val previewItems = listOf(
        NavItem("Student", Icons.Default.Home, AppScreen.StudentManagement.name),
        NavItem("User", Icons.Default.AccountCircle, AppScreen.UserManagement.name),
        NavItem("Profile", Icons.Default.Person, AppScreen.UserDetailProfile.name)
    )

    BottomNavBarAdmin(
        items = previewItems,
        currentRoute = AppScreen.StudentManagement.name,
        onItemClick = {}
    )
}