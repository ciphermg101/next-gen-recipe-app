package com.example.next_gen_recipe_app.ui.navigation

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.next_gen_recipe_app.ui.screens.*
import com.example.next_gen_recipe_app.ui.viewmodel.RecipeViewModel
import com.example.next_gen_recipe_app.data.repository.MLKitHelper

@Composable
fun NavGraph(
    navController: NavHostController,
    mlKitHelper: MLKitHelper,
    recipeViewModel: RecipeViewModel,
    categories: List<String> = listOf("Breakfast", "Lunch", "Dinner", "Dessert"),
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val recipes by recipeViewModel.recipes.collectAsState()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = remember(currentBackStackEntry) {
        currentBackStackEntry?.destination?.route ?: Routes.HomeScreen
    }

    fun navigateSafely(route: String) {
        runCatching {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        }.onFailure { e ->
            Log.e("NavGraph", "Navigation error: ${e.localizedMessage}", e)
        }
    }

    NavHost(navController = navController, startDestination = Routes.HomeScreen) {
        composable(Routes.HomeScreen) {
            HomeScreen(
                recipeViewModel = recipeViewModel,
                onRecipeClick = { recipe -> navigateSafely("${Routes.RecipeDetailScreen}/${recipe}") },
                onNavigate = { route -> navigateSafely(route) },
                toggleDarkMode = onToggleDarkMode,
                isDarkMode = darkMode,
                currentRoute = currentRoute
            )
        }

        composable(
            route = "${Routes.RecipeListScreen}?query={query}",
            arguments = listOf(navArgument("query") { nullable = true })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query").orEmpty()
            RecipeListScreen(
                recipeViewModel = recipeViewModel,
                onRecipeClick = { recipeId -> navigateSafely("${Routes.RecipeDetailScreen}/$recipeId") }
            )
        }

        composable(Routes.CustomRecipeListScreen) {
            CustomRecipeListScreen(
                recipes = recipes,
                onRecipeClick = { recipe -> navigateSafely("${Routes.RecipeDetailScreen}/${recipe.id}") },
                onToggleFavorite = { updatedRecipe ->
                    Log.d("NavGraph", "Favorite toggled for recipe ${updatedRecipe.id}")
                    // Handle favorite toggling, update database or repository here
                }
            )
        }

        composable(
            route = "${Routes.RecipeDetailScreen}/{recipeId}",
            arguments = listOf(navArgument("recipeId") { nullable = true })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
            val selectedRecipe = recipes.find { it.id == recipeId }

            selectedRecipe?.let {
                RecipeDetailScreen(recipe = it)
            } ?: run {
                Log.e("NavGraph", "Recipe with ID $recipeId not found, returning to HomeScreen")
                navigateSafely(Routes.HomeScreen)
            }
        }

        composable(Routes.CategoryGridScreen) {
            CategoryGridScreen(
                categories = categories,
                onCategoryClick = { category ->
                    navigateSafely("${Routes.RecipeListScreen}?query=$category")
                }
            )
        }

        composable(Routes.FoodScanScreen) {
            FoodScanScreen(
                mlKitHelper = mlKitHelper,
                onFoodScanResult = { foodList ->
                    val query = foodList.joinToString(" ").takeIf { it.isNotBlank() }
                    query?.let {
                        navigateSafely("${Routes.RecipeListScreen}?query=$it")
                    } ?: Log.w("NavGraph", "No valid food items recognized to form a query.")
                }
            )
        }
    }
}
