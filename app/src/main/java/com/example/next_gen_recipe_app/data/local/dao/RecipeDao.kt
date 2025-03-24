package com.example.next_gen_recipe_app.data.local.dao

import androidx.room.*
import com.example.next_gen_recipe_app.data.local.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>  // Changed to Flow for real-time updates

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)  // Optional: Ignore duplicates
    suspend fun insertOrIgnoreRecipes(recipes: List<Recipe>)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY RANDOM() LIMIT :limit")
    fun getRandomRecipes(limit: Int): Flow<List<Recipe>>  // Changed to Flow

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' ORDER BY title")
    fun searchRecipes(query: String): Flow<List<Recipe>>  // Changed to Flow
}
