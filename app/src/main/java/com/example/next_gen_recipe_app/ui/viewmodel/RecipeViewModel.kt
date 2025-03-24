package com.example.next_gen_recipe_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.next_gen_recipe_app.data.local.entities.Recipe
import com.example.next_gen_recipe_app.data.repository.RecipeRepository
import com.example.next_gen_recipe_app.data.repository.RepositoryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _recipesByIngredients = MutableStateFlow<List<Recipe>>(emptyList())
    val recipesByIngredients: StateFlow<List<Recipe>> = _recipesByIngredients

    private val _randomRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val randomRecipes: StateFlow<List<Recipe>> = _randomRecipes

    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Fetches recipes by name asynchronously.
     */
    fun fetchRecipesByName(query: String) = fetchRecipes(
        fetchFunction = { repository.fetchRecipesByName(query) },
        stateFlow = _recipes
    )

    /**
     * Fetches recipes by ingredients asynchronously.
     */
    fun fetchRecipesByIngredients(ingredients: String) = fetchRecipes(
        fetchFunction = { repository.fetchRecipesByIngredients(ingredients) },
        stateFlow = _recipesByIngredients
    )

    /**
     * Fetches random recipes asynchronously.
     */
    fun fetchRandomRecipes() = fetchRecipes(
        fetchFunction = { repository.fetchRandomRecipes() },
        stateFlow = _randomRecipes
    )

    /**
     * Generic function to fetch recipes and handle repository results.
     */
    private fun fetchRecipes(
        fetchFunction: suspend () -> RepositoryResult<List<Recipe>>,
        stateFlow: MutableStateFlow<List<Recipe>>
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                handleResult(fetchFunction(), stateFlow)
            }
        }
    }

    /**
     * Handles repository results to update state flows efficiently.
     */
    private fun handleResult(
        result: RepositoryResult<List<Recipe>>,
        stateFlow: MutableStateFlow<List<Recipe>>
    ) {
        when (result) {
            is RepositoryResult.Success -> {
                stateFlow.update { result.data }
                _errorMessage.update { null }
            }
            is RepositoryResult.NetworkError -> {
                _errorMessage.update { "Network error. Please try again." }
            }
            is RepositoryResult.ParsingError -> {
                _errorMessage.update { "Parsing error occurred." }
            }
            is RepositoryResult.UnknownError -> {
                _errorMessage.update { "Unknown error. Please try again later." }
            }
        }
    }
}
