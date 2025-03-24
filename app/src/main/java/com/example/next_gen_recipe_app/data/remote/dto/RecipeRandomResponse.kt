package com.example.next_gen_recipe_app.data.remote.dto

/**
 * Data transfer object representing the response from the Spoonacular API's random recipes endpoint.
 *
 * The JSON response is expected to have a "recipes" field that contains an array of detailed recipe objects.
 */
data class RecipeRandomResponse(
    val recipes: List<RecipeDto> // The list of RecipeDto objects returned from the Spoonacular API
)
