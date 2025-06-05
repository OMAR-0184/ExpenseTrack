package com.example.expensetrack.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetrack.data.ExpenseDataBase
import com.example.expensetrack.data.dao.ExpenseDao
import com.example.expensetrack.data.model.ExpenseEntity
import kotlinx.coroutines.launch

class AddExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.insertExpense(expense)
        }
    }
}

class AddExpenseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}