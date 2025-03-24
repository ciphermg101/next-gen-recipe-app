package com.example.next_gen_recipe_app.data.remote.dto

/**
 * Data transfer object representing an ingredient in a recipe from the Spoonacular API.
 *
 * This DTO is used within the RecipeDto to detail the extendedIngredients.
 */
data class IngredientDto(
    val id: Int?,
    val name: String? // Nullable as Spoonacular might return null for name
)
