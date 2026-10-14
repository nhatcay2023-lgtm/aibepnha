package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseEntity>)

    @Query("DELETE FROM expenses")
    suspend fun clearAll()

    @Query("SELECT SUM(amount) FROM expenses WHERE date = :date")
    fun getDailyTotal(date: String): Flow<Long?>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpenses(): Flow<Long?>
}
