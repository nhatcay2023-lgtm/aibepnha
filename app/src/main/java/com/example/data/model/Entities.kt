package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String, // Ăn uống, Đi chợ, Sinh hoạt, Cá nhân, Đi lại, Khác
    val amount: Long,
    val date: String,
    val dayOfWeek: String, // T2, T3, T4, T5, T6, T7, CN
    val note: String = ""
)

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val portion: String = "4 người",
    val category: String = "Món mặn",
    val portionBase: Int = 4,
    val ingredients: String = "", // e.g. "400g cá, 1 củ gừng, 2 thìa nước mắm"
    val recipe: String = "",
    val prepTimeMinutes: Int = 15,
    val cookTimeMinutes: Int = 30,
    val estimatedCost: Long = 85000,
    val date: String = "13/10/2026",
    val mealTime: String = "Trưa", // Sáng, Trưa, Tối
    val isCooked: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "Đi chợ Tuần này",
    val periodLabel: String = "Tuần này", // "Hôm nay", "Tuần này", "Tùy chọn"
    val dateRange: String = "13/10/2026",
    val budget: Long = 800000,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedDate: String = "",
    val note: String = ""
)

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listId: Int = 1,
    val name: String,
    val quantity: Double,
    val unit: String = "g", // g, kg, quả, củ, thìa, mớ, gói, hộp, chai
    val category: String = "Rau củ, trái cây", // "Thịt, cá, hải sản", "Rau củ, trái cây", "Gia vị, đồ khô", "Khác"
    val estimatedPrice: Long = 0,
    val actualPrice: Long = 0,
    val isBought: Boolean = false,
    val weekLabel: String = "Tuần này",
    val sourceMealName: String = "",
    val note: String = ""
)

@Entity(tableName = "pantry_items")
data class PantryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Double,
    val unit: String = "g",
    val category: String = "Gia vị, đồ khô",
    val note: String = "",
    val addedDate: String = "13/10/2026"
)

@Entity(tableName = "price_history")
data class PriceHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val unit: String,
    val price: Long,
    val recordedDate: String = "13/10/2026"
)

data class ExpenseCategorySummary(
    val category: String,
    val amount: Long,
    val percentage: Float,
    val colorHex: Long
)

data class DailyExpenseBar(
    val dayLabel: String,
    val amountInK: Int
)

data class UserSettings(
    val familyMembers: Int = 4,
    val monthlyBudget: Long = 6000000,
    val groceryBudget: Long = 800000,
    val dietaryNote: String = "Thanh đạm, ít dầu mỡ, chuẩn vị Bắc",
    val geminiApiKey: String = "",
    val currency: String = "VND" // VND or TWD
)

