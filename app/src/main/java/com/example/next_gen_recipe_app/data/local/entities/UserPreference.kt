package com.example.next_gen_recipe_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreference(
    @PrimaryKey val id: Int = 0,
    val darkMode: Boolean = false,
    val lastSyncTimestamp: Long = 0L
)
