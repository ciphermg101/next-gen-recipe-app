package com.example.next_gen_recipe_app.data.remote.dto

import com.squareup.moshi.Json

/**
 * Data transfer object representing detailed information about a recipe from the Spoonacular API.
 *
 * This DTO is used when fetching detailed recipe information via the /recipes/{id}/information endpoint.
 * It includes extended fields such as instructions and a list of ingredients.
 */
data class RecipeDto(
    val id: Int,
    val title: String,
    @Json(name = "image") val imageUrl: String,
    @Json(name = "summary") val summary: String,
    @Json(name = "instructions") val instructions: String?,
    @Json(name = "extendedIngredients") val extendedIngredients: List<IngredientDto>?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val sourceUrl: String?,
    val vegetarian: Boolean?,
    val vegan: Boolean?,
    val glutenFree: Boolean?,
    val dairyFree: Boolean?,
    val pricePerServing: Double?
)
