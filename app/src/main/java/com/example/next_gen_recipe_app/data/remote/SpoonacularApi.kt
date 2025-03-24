package com.example.next_gen_recipe_app.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.next_gen_recipe_app.data.remote.dto.RecipeRandomResponse
import com.example.next_gen_recipe_app.data.remote.dto.RecipeSearchResultDto
import com.example.next_gen_recipe_app.data.remote.dto.RecipeSearchResponse

/**
 * Retrofit API interface for accessing the Spoonacular API.
 *
 * Note: The API key is automatically appended to each request by the ApiKeyInterceptor,
 * so there is no need to include it explicitly in the method parameters.
 *
 * Ensure robust error handling is implemented in the calling repository:
 * - Wrap API calls in try/catch blocks.
 * - Check [Response.isSuccessful] and handle errors accordingly.
 */
interface SpoonacularApi {

    /**
     * Returns a set of random recipes.
     *
     * Example URL for testing (Postman):
     * https://api.spoonacular.com/recipes/random?number=4&apiKey=YOUR_API_KEY
     */
    @GET("recipes/random")
    suspend fun randomRecipes(
        @Query("number") number: Int
    ): Response<RecipeRandomResponse>

    /**
     * Searches for recipes that can be made with the specified ingredients.
     *
     * @param ingredients A comma-separated list of ingredients.
     * @param number The number of recipes to return.
     * @param ranking Optional: 1 to maximize used ingredients, 2 to minimize missing ingredients.
     * @param ignorePantry Whether to ignore typical pantry items.
     *
     * Example URL for testing (Postman):
     * https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,flour,sugar&number=2&apiKey=YOUR_API_KEY
     */
    @GET("recipes/findByIngredients")
    suspend fun searchByIngredients(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("ranking") ranking: Int = 1,
        @Query("ignorePantry") ignorePantry: Boolean = true
    ): Response<List<RecipeSearchResultDto>>

    /**
     * Searches for recipes by name using the complexSearch endpoint.
     *
     * @param query The recipe name query.
     * @param number The number of recipes to return.
     * @param addRecipeInformation When true, includes detailed recipe information.
     *
     * Example URL for testing (Postman):
     * https://api.spoonacular.com/recipes/complexSearch?query=chicken&number=10&addRecipeInformation=true&apiKey=YOUR_API_KEY
     */
    @GET("recipes/complexSearch")
    suspend fun searchByName(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true
    ): Response<RecipeSearchResponse>
}
