package com.xavier.calculator.api

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.xavier.calculator.db.AppDatabase
import com.xavier.calculator.db.ComputeDao
import com.xavier.calculator.ui.ViewModelFactory

object Injection {

    private fun provideComputeDao(context: Context): ComputeDao {
        return AppDatabase.getInstance(context).computeDao()
    }

    fun provideViewModelFactory(
        context: Context,
        owner: SavedStateRegistryOwner
    ): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideComputeDao(context))
    }
}