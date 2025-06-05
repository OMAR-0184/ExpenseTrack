package com.example.expensetrack.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "income")
    val income: Double = 0.0,

    @ColumnInfo(name = "expense")
    val expense: Double = 0.0,

    @ColumnInfo(name = "date")
    val date: String, // Storing as String for simplicity, consider using a Long for timestamps if complex date operations are needed.

    @ColumnInfo(name = "category")
    val category: String // Added category to ExpenseEntity as it's used in AddExpense
)