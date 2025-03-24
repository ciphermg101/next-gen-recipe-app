package com.example.next_gen_recipe_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import com.example.next_gen_recipe_app.data.local.entities.Recipe
import com.example.next_gen_recipe_app.ui.components.RecipeCard

@Composable
fun CustomRecipeListScreen(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(recipes) { recipe ->
            // Wrap RecipeCard and IconToggleButton in a Row to align them properly
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Pass individual properties of the recipe to RecipeCard
                RecipeCard(
                    recipeId = recipe.id,  // Passing only the recipeId
                    recipeTitle = recipe.title,
                    recipeImage = recipe.image,  // Assuming 'image' is the correct property name
                    recipeSummary = recipe.summary,
                    onClick = { onRecipeClick(recipe) }
                )

                // Add the Favorite IconToggleButton to toggle the favorite state
                IconToggleButton(
                    checked = recipe.isFavorite,
                    onCheckedChange = { isFavorite ->
                        // Toggle the favorite state in the parent composable
                        onToggleFavorite(recipe.copy(isFavorite = isFavorite))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite"
                    )
                }
            }
        }
    }
}
