package com.binarybricks.iwt.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.binarybricks.iwt.data.repository.FakeWorkoutRepository
import com.binarybricks.iwt.ui.screens.home.HomeViewModel

/**
 * Factory for creating ViewModels without Hilt dependency injection
 * This is a temporary solution until we fully set up Hilt
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // HomeViewModel
        initializer {
            HomeViewModel(
                workoutRepository = FakeWorkoutRepository()
            )
        }
    }
}