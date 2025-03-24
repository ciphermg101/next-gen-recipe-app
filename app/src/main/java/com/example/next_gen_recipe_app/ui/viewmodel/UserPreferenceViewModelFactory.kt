package com.example.next_gen_recipe_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.next_gen_recipe_app.data.repository.UserPreferenceRepository

class UserPreferenceViewModelFactory(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModelProvider.Factory {

    // This method will create an instance of UserPreferenceViewModel and inject the repository
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferenceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserPreferenceViewModel(userPreferenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
