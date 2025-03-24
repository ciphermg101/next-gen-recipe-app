package com.example.next_gen_recipe_app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.next_gen_recipe_app.ui.components.RecipeCardImage
import com.example.next_gen_recipe_app.ui.navigation.Routes
import com.example.next_gen_recipe_app.ui.viewmodel.RecipeViewModel
import com.example.next_gen_recipe_app.data.local.entities.Recipe

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    recipeViewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit,  // Pass only recipeId now
    toggleDarkMode: () -> Unit,
    isDarkMode: Boolean,
    onNavigate: (String) -> Unit,
    currentRoute: String  // Track current route for navigation bar selection
) {
    // Collect state for random recipes and error messages.
    val randomRecipes by recipeViewModel.randomRecipes.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()

    val isLoading = randomRecipes.isEmpty() && errorMessage == null

    // Fetch random recipes only once when the screen is first displayed.
    LaunchedEffect(key1 = recipeViewModel) {
        if (randomRecipes.isEmpty()) {
            recipeViewModel.fetchRandomRecipes()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Next-Gen Recipe App") },
                actions = {
                    IconButton(onClick = toggleDarkMode) {
                        if (isDarkMode) {
                            Icon(Icons.Filled.LightMode, contentDescription = "Switch to Light Mode")
                        } else {
                            Icon(Icons.Filled.DarkMode, contentDescription = "Switch to Dark Mode")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigate(Routes.FoodScanScreen) }) {
                Icon(Icons.Filled.Camera, contentDescription = "Scan Food")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Routes.RecipeListScreen,
                    onClick = { onNavigate(Routes.RecipeListScreen) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "View Recipes") },
                    label = { Text("Recipes") }
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.CategoryGridScreen,
                    onClick = { onNavigate(Routes.CategoryGridScreen) },
                    icon = { Icon(Icons.Filled.GridOn, contentDescription = "View Categories") },
                    label = { Text("Categories") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Random Recipes",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Error loading recipes.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                randomRecipes.isEmpty() -> {
                    Text("No recipes found.", modifier = Modifier.padding(16.dp))
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(randomRecipes.take(4)) { recipe ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { onRecipeClick(recipe.id) } // Pass only recipeId
                                    .padding(4.dp)
                            ) {
                                RecipeCardImage(recipeId = recipe.id, recipeImage = recipe.image, modifier = Modifier.size(150.dp)) // Only pass necessary data
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = recipe.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
