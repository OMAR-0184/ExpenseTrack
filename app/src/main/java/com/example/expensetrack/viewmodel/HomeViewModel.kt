// HomeViewModel.kt
package com.example.expensetrack.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope // Import viewModelScope
import com.example.expensetrack.data.ExpenseDataBase
import com.example.expensetrack.data.dao.ExpenseDao
import com.example.expensetrack.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch // Import launch

class HomeViewModel(private val dao: ExpenseDao) : ViewModel() {

    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()

    fun getBalance(expenses: List<ExpenseEntity>): String {
        val totalIncome = expenses.sumOf { it.income }
        val totalExpense = expenses.sumOf { it.expense }
        return String.format("%.2f", totalIncome - totalExpense)
    }

    fun getTotalExpense(expenses: List<ExpenseEntity>): String {
        return String.format("%.2f", expenses.sumOf { it.expense })
    }

    fun getTransactions(expenses: List<ExpenseEntity>): Int {
        return expenses.size
    }

    // --- NEW FUNCTION FOR DELETION ---
    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
        }
    }
    // --- END NEW FUNCTION ---
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}