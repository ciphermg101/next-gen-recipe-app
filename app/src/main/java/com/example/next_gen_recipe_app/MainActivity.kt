package com.example.next_gen_recipe_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.next_gen_recipe_app.data.remote.ApiKeyInterceptor
import com.example.next_gen_recipe_app.data.remote.SpoonacularApi
import com.example.next_gen_recipe_app.data.local.AppDatabase
import com.example.next_gen_recipe_app.data.repository.RecipeRepository
import com.example.next_gen_recipe_app.data.repository.MLKitHelper
import com.example.next_gen_recipe_app.data.repository.UserPreferenceRepository
import com.example.next_gen_recipe_app.ui.navigation.NavGraph
import com.example.next_gen_recipe_app.ui.theme.NextGenRecipeAppTheme
import com.example.next_gen_recipe_app.ui.viewmodel.RecipeViewModel
import com.example.next_gen_recipe_app.ui.viewmodel.RecipeViewModelFactory
import com.example.next_gen_recipe_app.ui.viewmodel.UserPreferenceViewModel
import com.example.next_gen_recipe_app.ui.viewmodel.UserPreferenceViewModelFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a Moshi instance with KotlinJsonAdapterFactory for proper JSON conversion.
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        // Build an OkHttpClient with ApiKeyInterceptor to automatically append the API key to every request.
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()

        // Build a Retrofit instance for the Spoonacular API using the custom OkHttpClient and Moshi converter.
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        // Create the Spoonacular API interface.
        val api = retrofit.create(SpoonacularApi::class.java)

        // Get a singleton instance of the Room database.
        val database = AppDatabase.getDatabase(this)

        // Create the repository with the API interface and database.
        val recipeRepository = RecipeRepository(api, database)
        val userPreferenceRepository = UserPreferenceRepository(database.userPreferenceDao())

        // Create ViewModel factories to provide the repositories to the ViewModels.
        val recipeViewModelFactory = RecipeViewModelFactory(recipeRepository)
        val userPreferenceViewModelFactory = UserPreferenceViewModelFactory(userPreferenceRepository)

        setContent {
            // Create the UserPreferenceViewModel to fetch dark mode preference
            val userPreferenceViewModel: UserPreferenceViewModel = viewModel(factory = userPreferenceViewModelFactory)
            val userPreference by userPreferenceViewModel.userPreference

            // If userPreference is null, initialize it with default value (dark mode = false)
            var isDarkMode by remember { mutableStateOf(userPreference?.darkMode ?: false) }

            // Watch for changes in dark mode and update ViewModel
            LaunchedEffect(userPreference) {
                userPreference?.let {
                    isDarkMode = it.darkMode
                }
            }

            // Set the dark mode theme
            NextGenRecipeAppTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                val recipeViewModel: RecipeViewModel = viewModel(factory = recipeViewModelFactory)
                val mlKitHelper = remember { MLKitHelper() }

                // Set up the navigation graph, passing dark mode state and the toggle callback
                NavGraph(
                    navController = navController,
                    mlKitHelper = mlKitHelper,
                    recipeViewModel = recipeViewModel,
                    darkMode = isDarkMode,
                    onToggleDarkMode = {
                        // Toggle dark mode and update it in the UserPreferenceRepository
                        isDarkMode = !isDarkMode
                        userPreferenceViewModel.updateUserPreference(isDarkMode, System.currentTimeMillis())
                    }
                )
            }
        }
    }
}
