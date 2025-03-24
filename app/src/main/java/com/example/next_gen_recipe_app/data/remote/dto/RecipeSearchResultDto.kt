package com.example.next_gen_recipe_app.data.remote.dto

import com.squareup.moshi.Json

/**
 * Data transfer object representing a single search result for a recipe from the Spoonacular API.
 *
 * This DTO is used in the response of the complex search endpoint with addRecipeInformation=true.
 *
 * The fields kept here are essential for displaying the recipe.
 */
data class RecipeSearchResultDto(
    val id: Int?,
    val title: String?,
    @Json(name = "image") val imageUrl: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val sourceUrl: String?,
    val vegetarian: Boolean?,
    val vegan: Boolean?,
    val glutenFree: Boolean?,
    val dairyFree: Boolean?,
    val summary: String?
)
