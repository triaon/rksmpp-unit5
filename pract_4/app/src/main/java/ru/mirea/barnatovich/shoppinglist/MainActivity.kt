package ru.mirea.barnatovich.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.mirea.barnatovich.shoppinglist.ui.DetailsScreen
import ru.mirea.barnatovich.shoppinglist.ui.ListScreen
import ru.mirea.barnatovich.shoppinglist.ui.ShoppingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val vm: ShoppingViewModel = viewModel(factory = ShoppingViewModel.Factory)
                val navController = rememberNavController()
                NavHost(navController, startDestination = "list") {
                    composable("list") {
                        ListScreen(vm, onOpen = { id -> navController.navigate("details/$id") })
                    }
                    composable(
                        "details/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { entry ->
                        val id = entry.arguments?.getLong("id") ?: return@composable
                        DetailsScreen(vm, id, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
