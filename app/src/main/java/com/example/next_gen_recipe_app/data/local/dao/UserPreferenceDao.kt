package com.example.next_gen_recipe_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.next_gen_recipe_app.data.local.entities.UserPreference

@Dao
interface UserPreferenceDao {

    // Insert a new user preference
    @Insert
    suspend fun insert(userPreference: UserPreference)

    // Fetch the user preference (assuming there's only one record)
    @Query("SELECT * FROM user_preferences WHERE id = 0 LIMIT 1")
    suspend fun getUserPreference(): UserPreference?

    // Update the user preference
    @Query("UPDATE user_preferences SET darkMode = :darkMode, lastSyncTimestamp = :lastSyncTimestamp WHERE id = 0")
    suspend fun updateUserPreference(darkMode: Boolean, lastSyncTimestamp: Long)
}