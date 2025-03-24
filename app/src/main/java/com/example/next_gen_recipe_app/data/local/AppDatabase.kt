package com.example.next_gen_recipe_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.next_gen_recipe_app.data.local.dao.RecipeDao
import com.example.next_gen_recipe_app.data.local.dao.UserPreferenceDao
import com.example.next_gen_recipe_app.data.local.entities.Recipe
import com.example.next_gen_recipe_app.data.local.entities.UserPreference
import com.example.next_gen_recipe_app.data.local.converters.ListTypeConverter

@Database(entities = [Recipe::class, UserPreference::class], version = 1, exportSchema = false)
@TypeConverters(ListTypeConverter::class) // Apply the TypeConverter to the entire database
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun userPreferenceDao(): UserPreferenceDao // Add UserPreferenceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                )
                    .fallbackToDestructiveMigration() // Handle migrations gracefully
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
