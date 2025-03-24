package com.example.next_gen_recipe_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.next_gen_recipe_app.data.local.converters.ListTypeConverter

@Entity(tableName = "recipes")
@TypeConverters(ListTypeConverter::class) // Apply TypeConverter
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val isFavorite: Boolean,
    val readyInMinutes: Int?,
    val servings: Int?,
    val sourceUrl: String?
)
