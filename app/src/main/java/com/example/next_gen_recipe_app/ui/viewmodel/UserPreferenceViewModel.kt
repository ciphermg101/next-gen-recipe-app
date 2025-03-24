package com.example.next_gen_recipe_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.next_gen_recipe_app.data.repository.UserPreferenceRepository
import com.example.next_gen_recipe_app.data.local.entities.UserPreference
import kotlinx.coroutines.launch

class UserPreferenceViewModel(private val userPreferenceRepository: UserPreferenceRepository) : ViewModel() {

    // Hold the user preference in a mutable state for Compose UI layer
    private val _userPreference = mutableStateOf<UserPreference?>(null)
    val userPreference: State<UserPreference?> get() = _userPreference

    // To get the user preferences (e.g., dark mode)
    fun getUserPreference() {
        viewModelScope.launch {
            val userPreference = userPreferenceRepository.getUserPreference()
            _userPreference.value = userPreference
        }
    }

    // To update the user preferences (e.g., dark mode toggle)
    fun updateUserPreference(darkMode: Boolean, lastSyncTimestamp: Long) {
        viewModelScope.launch {
            userPreferenceRepository.updateUserPreference(darkMode, lastSyncTimestamp)
            // Optionally update local state after the update
            _userPreference.value = _userPreference.value?.copy(darkMode = darkMode, lastSyncTimestamp = lastSyncTimestamp)
        }
    }
}
