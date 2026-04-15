package com.example.planer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planer.data.repository.UserRepository

@Composable
fun ProfileScreen(
    navController: NavController,
    userRepository: UserRepository
) {
    var user by remember { mutableStateOf(com.example.planer.User(
        id = "current_user",
        name = "Завантаження...",
        email = "",
        createdAt = ""
    )) }

    LaunchedEffect(Unit) {
        userRepository.getUser().collect { userFromDb ->
            user = userFromDb ?: user
        }
        userRepository.refreshUserFromServer()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = "← Назад",
            modifier = Modifier.clickable { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "👤 ${user.name}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "📧 ${user.email}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "📅 Зареєстрований: ${user.createdAt}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (user.isPremium) {
                    Text("⭐ Premium користувач")
                }
            }
        }
    }
}