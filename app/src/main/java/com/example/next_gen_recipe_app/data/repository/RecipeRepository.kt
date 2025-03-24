package com.example.next_gen_recipe_app.data.repository

import android.util.Log
import com.example.next_gen_recipe_app.data.local.AppDatabase
import com.example.next_gen_recipe_app.data.local.entities.Recipe
import com.example.next_gen_recipe_app.data.remote.SpoonacularApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

sealed class RepositoryResult<out T> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class NetworkError(val message: String) : RepositoryResult<Nothing>()
    data class ParsingError(val message: String) : RepositoryResult<Nothing>()
    data class UnknownError(val message: String) : RepositoryResult<Nothing>()
}

class RecipeRepository(
    private val api: SpoonacularApi,
    private val database: AppDatabase
) {
    private val recipeDao = database.recipeDao()

    suspend fun fetchRandomRecipes(): RepositoryResult<List<Recipe>> {
        return fetchData(
            apiCall = { api.randomRecipes(number = 4) },
            mapResponse = { body ->
                body?.recipes?.mapNotNull { dto ->
                    Recipe(
                        id = dto.id ?: 0, // Ensure non-null ID
                        title = dto.title ?: "Untitled", // Default title
                        image = dto.imageUrl ?: "",
                        summary = dto.summary ?: "",
                        ingredients = dto.extendedIngredients?.mapNotNull { it.name } ?: emptyList(),
                        steps = dto.instructions?.split("\n")?.mapNotNull { it.trim() } ?: emptyList(),
                        isFavorite = false,
                        readyInMinutes = dto.readyInMinutes ?: 0,
                        servings = dto.servings ?: 1,
                        sourceUrl = dto.sourceUrl ?: ""
                    )
                } ?: emptyList()
            }
        )
    }

    suspend fun fetchRecipesByName(query: String): RepositoryResult<List<Recipe>> {
        return fetchData(
            apiCall = { api.searchByName(query = query, number = 10, addRecipeInformation = true) },
            mapResponse = { body ->
                body?.results?.mapNotNull { searchDto ->
                    Recipe(
                        id = searchDto.id ?: 0,
                        title = searchDto.title ?: "Untitled",
                        image = searchDto.imageUrl ?: "",
                        summary = searchDto.summary ?: "",
                        ingredients = emptyList(),
                        steps = emptyList(),
                        isFavorite = false,
                        readyInMinutes = searchDto.readyInMinutes ?: 0,
                        servings = searchDto.servings ?: 1,
                        sourceUrl = searchDto.sourceUrl ?: ""
                    )
                } ?: emptyList()
            }
        )
    }

    suspend fun fetchRecipesByIngredients(ingredients: String): RepositoryResult<List<Recipe>> {
        return fetchData(
            apiCall = { api.searchByIngredients(ingredients = ingredients, number = 10, ranking = 1, ignorePantry = true) },
            mapResponse = { dtos ->
                dtos?.mapNotNull { dto ->
                    Recipe(
                        id = dto.id ?: 0,
                        title = dto.title ?: "Untitled",
                        image = dto.imageUrl ?: "",
                        summary = dto.summary ?: "",
                        ingredients = emptyList(),
                        steps = emptyList(),
                        isFavorite = false,
                        readyInMinutes = dto.readyInMinutes ?: 0,
                        servings = dto.servings ?: 1,
                        sourceUrl = dto.sourceUrl ?: ""
                    )
                } ?: emptyList()
            }
        )
    }

    private suspend fun <T, R> fetchData(
        apiCall: suspend () -> retrofit2.Response<T>,
        mapResponse: (T?) -> List<R>
    ): RepositoryResult<List<R>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val data = mapResponse(response.body())
                    if (data.isNotEmpty() && data.first() is Recipe) {
                        @Suppress("UNCHECKED_CAST")
                        recipeDao.insertRecipes(data as List<Recipe>)
                    }
                    RepositoryResult.Success(data)
                } else {
                    val errorMsg = "Network error: ${response.code()} ${response.message()}"
                    Log.e("RecipeRepository", errorMsg)
                    RepositoryResult.NetworkError(errorMsg)
                }
            } catch (e: HttpException) {
                val errorMsg = "HTTP exception: ${e.message()}"
                Log.e("RecipeRepository", errorMsg, e)
                RepositoryResult.NetworkError(errorMsg)
            } catch (e: IllegalStateException) {
                val errorMsg = "Illegal state: ${e.localizedMessage}"
                Log.e("RecipeRepository", errorMsg, e)
                RepositoryResult.UnknownError(errorMsg)
            } catch (e: Exception) {
                val errorMsg = "Unknown error: ${e.localizedMessage}"
                Log.e("RecipeRepository", errorMsg, e)
                RepositoryResult.UnknownError(errorMsg)
            }
        }
    }
}
