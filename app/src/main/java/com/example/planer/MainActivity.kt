package com.example.planer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.planer.data.repository.TaskRepository
import com.example.planer.data.repository.CategoryRepository
import com.example.planer.data.repository.UserRepository
import com.example.planer.ui.screens.*
import com.example.planer.ui.theme.PlanerTheme

class MainActivity : ComponentActivity() {

    private lateinit var taskRepository: TaskRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ініціалізація репозиторіїв
        taskRepository = TaskRepository(this)
        categoryRepository = CategoryRepository(this)
        userRepository = UserRepository(this)

        setContent {
            PlanerTheme {
                PlanerApp(
                    taskRepository = taskRepository,
                    categoryRepository = categoryRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}

@Composable
fun PlanerApp(
    taskRepository: TaskRepository,
    categoryRepository: CategoryRepository,
    userRepository: UserRepository
) {
    val navController = rememberNavController()

    // Початкове завантаження даних з сервера
    LaunchedEffect(Unit) {
        taskRepository.refreshFromServer()
        categoryRepository.refreshCategoriesFromServer()
        userRepository.refreshUserFromServer()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("list") },
                    icon = { Text("📋") },
                    label = { Text("Задачі") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("add") },
                    icon = { Text("➕") },
                    label = { Text("Додати") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Text("👤") },
                    label = { Text("Профіль") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("list") {
                TaskListScreen(
                    navController = navController,
                    taskRepository = taskRepository
                )
            }

            composable("detail/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                TaskDetailScreen(
                    taskId = taskId,
                    navController = navController,
                    taskRepository = taskRepository
                )
            }

            composable("add") {
                AddTaskScreen(
                    navController = navController,
                    taskRepository = taskRepository
                )
            }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    userRepository = userRepository
                )
            }
        }
    }
}