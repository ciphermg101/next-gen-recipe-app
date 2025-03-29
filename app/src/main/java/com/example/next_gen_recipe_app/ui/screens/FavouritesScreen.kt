package com.example.next_gen_recipe_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.next_gen_recipe_app.ui.components.RecipeCard
import com.example.next_gen_recipe_app.ui.viewmodel.RecipeViewModel

@Composable
fun CustomRecipeListScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteRecipes by viewModel.favoriteRecipes.collectAsState()

    if (favoriteRecipes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No favorite recipes yet!",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(modifier = modifier.padding(8.dp)) {
            items(favoriteRecipes, key = { it.id }) { recipe ->
                RecipeCard(
                    recipeId = recipe.id,
                    recipeTitle = recipe.title,
                    recipeImage = recipe.image,
                    recipeSummary = recipe.summary,
                    isFavorite = recipe.isFavorite,
                    onClick = { onRecipeClick(recipe.id) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) }
                )
            }
        }
    }
}
