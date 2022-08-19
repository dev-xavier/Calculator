package com.xavier.calculator.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.xavier.calculator.db.ComputeDao

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val computeDao: ComputeDao
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalculatorViewModel(computeDao, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
