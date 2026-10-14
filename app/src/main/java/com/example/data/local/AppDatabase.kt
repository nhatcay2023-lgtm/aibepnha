package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ExpenseEntity
import com.example.data.model.MealEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.PriceHistoryEntity
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.ShoppingListEntity

@Database(
    entities = [
        ExpenseEntity::class,
        MealEntity::class,
        ShoppingListEntity::class,
        ShoppingItemEntity::class,
        PantryItemEntity::class,
        PriceHistoryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun mealDao(): MealDao
    abstract fun shoppingDao(): ShoppingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_bep_nha.db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
