package com.app.figmamobiletest.design.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.figmamobiletest.repository.DashboardRepository

class DashboardActivityModelFactory constructor(private val repository: DashboardRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DashboardActivityViewModel::class.java)) {
            DashboardActivityViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}