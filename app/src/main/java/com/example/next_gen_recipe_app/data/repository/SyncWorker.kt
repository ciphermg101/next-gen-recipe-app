package com.example.next_gen_recipe_app.data.repository

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.next_gen_recipe_app.data.local.AppDatabase
import com.example.next_gen_recipe_app.data.remote.SpoonacularApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val api: SpoonacularApi,
    private val database: AppDatabase
) : CoroutineWorker(appContext, workerParams) {

    private val repository = RecipeRepository(api, database)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Fetch random recipes using the dedicated random recipes endpoint.
            val result = repository.fetchRandomRecipes()

            return@withContext when (result) {
                is RepositoryResult.Success -> {
                    Log.d("SyncWorker", "Successfully synced ${result.data.size} random recipes")
                    Result.success()
                }
                is RepositoryResult.NetworkError -> {
                    Log.e("SyncWorker", "Network error during sync: ${result.message}")
                    Result.retry()
                }
                is RepositoryResult.ParsingError -> {
                    Log.e("SyncWorker", "Parsing error during sync: ${result.message}")
                    Result.failure() // Fail the work if parsing fails.
                }
                is RepositoryResult.UnknownError -> {
                    Log.e("SyncWorker", "Unknown error during sync: ${result.message}")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Exception during sync", e)
            Result.retry()
        }
    }
}
