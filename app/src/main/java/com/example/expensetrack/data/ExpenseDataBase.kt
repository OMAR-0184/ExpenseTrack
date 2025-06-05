package com.example.expensetrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetrack.data.dao.ExpenseDao
import com.example.expensetrack.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = false)
abstract class ExpenseDataBase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        @Volatile
        private var INSTANCE: ExpenseDataBase? = null

        fun getDatabase(context: Context): ExpenseDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDataBase::class.java,
                    DATABASE_NAME
                )
                    // We no longer need to add the callback if it only contains initial data.
                    // If you have other database initialization logic, keep the callback and remove only the data insertion.
                    // .addCallback(ExpenseDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Remove the ExpenseDatabaseCallback class entirely if its only purpose was to pre-populate data.
        // If you need it for other onCreate/onOpen logic, keep the class but clear the CoroutineScope block.

        /*
        private class ExpenseDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Removed the data population logic
                // CoroutineScope(Dispatchers.IO).launch {
                //     INSTANCE?.expenseDao()?.apply {
                //         val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                //         insertExpense(ExpenseEntity(title = "Groceries", income = 0.0, expense = 1500.0, date = sdf.format(Date()), category = "Food"))
                //         insertExpense(ExpenseEntity(title = "Salary", income = 50000.0, expense = 0.0, date = sdf.format(Date()), category = "Income"))
                //         insertExpense(ExpenseEntity(title = "Electricity Bill", income = 0.0, expense = 2200.0, date = sdf.format(Date()), category = "Bills"))
                //         insertExpense(ExpenseEntity(title = "Freelance", income = 8000.0, expense = 0.0, date = sdf.format(Date()), category = "Income"))
                //         insertExpense(ExpenseEntity(title = "Internet Bill", income = 0.0, expense = 1000.0, date = sdf.format(Date()), category = "Bills"))
                //     }
                // }
            }
        }
        */
    }
}