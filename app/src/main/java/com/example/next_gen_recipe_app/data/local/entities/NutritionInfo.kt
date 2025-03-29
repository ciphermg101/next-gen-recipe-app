package com.example.next_gen_recipe_app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "nutrition_info",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NutritionInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeId: Int,
    val calories: Double,
    val protein: Double,
    val carbohydrates: Double,
    val fat: Double,
    val sugar: Double,
    val fiber: Double
)