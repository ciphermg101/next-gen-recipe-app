package com.example.next_gen_recipe_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.next_gen_recipe_app.data.local.entities.Recipe
import com.example.next_gen_recipe_app.ui.components.RecipeCard
import com.example.next_gen_recipe_app.ui.viewmodel.RecipeViewModel

enum class SearchType { NAME, INGREDIENTS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    recipeViewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit,  // Only passing recipeId
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf(SearchType.NAME) }

    val recipes by recipeViewModel.recipes.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()
    val isLoading = recipes.isEmpty() && errorMessage == null

    Scaffold(topBar = { TopAppBar(title = { Text("Next-Gen Recipe App") }) }) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding).padding(16.dp)
        ) {
            SearchSection(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                searchType = searchType,
                onSearchTypeChange = { searchType = it },
                onSearch = {
                    if (searchQuery.isNotBlank()) {
                        when (searchType) {
                            SearchType.NAME -> recipeViewModel.fetchRecipesByName(searchQuery)
                            SearchType.INGREDIENTS -> recipeViewModel.fetchRecipesByIngredients(searchQuery)
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            RecipeContent(isLoading, errorMessage, recipes, onRecipeClick)
        }
    }
}

@Composable
private fun SearchSection(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchType: SearchType,
    onSearchTypeChange: (SearchType) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        label = { Text("Search recipes") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SearchToggleButton("By Name", searchType == SearchType.NAME) { onSearchTypeChange(SearchType.NAME) }
        SearchToggleButton("By Ingredients", searchType == SearchType.INGREDIENTS) { onSearchTypeChange(SearchType.INGREDIENTS) }
        Button(onClick = onSearch) { Text("Search") }
    }
}

@Composable
private fun SearchToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    ) { Text(text) }
}

@Composable
private fun RecipeContent(
    isLoading: Boolean,
    errorMessage: String?,
    recipes: List<Recipe>,
    onRecipeClick: (Int) -> Unit  // Passing only recipeId here
) {
    when {
        isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        errorMessage != null -> Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(8.dp)
        )
        recipes.isEmpty() -> Text("No recipes found.", modifier = Modifier.padding(16.dp))
        else -> LazyColumn {
            items(recipes) { recipe ->
                RecipeCard(
                    recipeId = recipe.id,
                    recipeTitle = recipe.title,
                    recipeImage = recipe.image,
                    recipeSummary = recipe.summary,
                    onClick = onRecipeClick
                )
            }
        }
    }
}
