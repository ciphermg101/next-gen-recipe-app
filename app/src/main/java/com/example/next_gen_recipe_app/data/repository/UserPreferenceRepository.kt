package com.example.next_gen_recipe_app.data.repository

import com.example.next_gen_recipe_app.data.local.dao.UserPreferenceDao
import com.example.next_gen_recipe_app.data.local.entities.UserPreference

class UserPreferenceRepository(private val userPreferenceDao: UserPreferenceDao) {

    // Get the current user preference (dark mode)
    suspend fun getUserPreference(): UserPreference? {
        return userPreferenceDao.getUserPreference()
    }

    // Insert a new user preference
    suspend fun insertUserPreference(userPreference: UserPreference) {
        userPreferenceDao.insert(userPreference)
    }

    // Update the user preference (e.g., dark mode toggle)
    suspend fun updateUserPreference(darkMode: Boolean, lastSyncTimestamp: Long) {
        userPreferenceDao.updateUserPreference(darkMode, lastSyncTimestamp)
    }
}