package com.algorithmx.medmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorithmx.medmate.data.DatabaseSeeder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val seeder: DatabaseSeeder
) : ViewModel() {

    init {
        // Launch seeding immediately on app start
        viewModelScope.launch {
            seeder.seedDatabase()
        }
    }
}