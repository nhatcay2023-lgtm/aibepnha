package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PantryItemEntity
import com.example.data.model.PriceHistoryEntity
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    // === Shopping Lists ===
    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC")
    fun getAllShoppingLists(): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists WHERE isCompleted = 0 ORDER BY createdAt DESC LIMIT 1")
    fun getActiveShoppingList(): Flow<ShoppingListEntity?>

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    suspend fun getShoppingListById(id: Int): ShoppingListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(list: ShoppingListEntity): Long

    @Update
    suspend fun updateShoppingList(list: ShoppingListEntity)

    @Delete
    suspend fun deleteShoppingList(list: ShoppingListEntity)

    // === Shopping Items ===
    @Query("SELECT * FROM shopping_items ORDER BY isBought ASC, id ASC")
    fun getAllShoppingItems(): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY isBought ASC, id ASC")
    fun getShoppingItemsByListId(listId: Int): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY isBought ASC, id ASC")
    suspend fun getShoppingItemsByListIdSync(listId: Int): List<ShoppingItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ShoppingItemEntity>)

    @Update
    suspend fun updateShoppingItem(item: ShoppingItemEntity)

    @Query("UPDATE shopping_items SET isBought = :isBought WHERE id = :id")
    suspend fun updateBoughtStatus(id: Int, isBought: Boolean)

    @Query("UPDATE shopping_items SET isBought = :isBought, actualPrice = :actualPrice WHERE id = :id")
    suspend fun updateBoughtStatusAndPrice(id: Int, isBought: Boolean, actualPrice: Long)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteItemById(id: Int)

    @Query("DELETE FROM shopping_items WHERE listId = :listId")
    suspend fun deleteItemsByListId(listId: Int)

    @Query("DELETE FROM shopping_items")
    suspend fun clearAll()

    // === Pantry Items ===
    @Query("SELECT * FROM pantry_items ORDER BY id DESC")
    fun getAllPantryItems(): Flow<List<PantryItemEntity>>

    @Query("SELECT * FROM pantry_items")
    suspend fun getAllPantryItemsSync(): List<PantryItemEntity>

    @Query("SELECT * FROM pantry_items WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun findPantryItemByName(name: String): PantryItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryItem(item: PantryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPantryItems(items: List<PantryItemEntity>)

    @Update
    suspend fun updatePantryItem(item: PantryItemEntity)

    @Delete
    suspend fun deletePantryItem(item: PantryItemEntity)

    // === Price History ===
    @Query("SELECT * FROM price_history ORDER BY id DESC")
    fun getAllPriceHistory(): Flow<List<PriceHistoryEntity>>

    @Query("SELECT price FROM price_history WHERE LOWER(name) = LOWER(:name) ORDER BY id DESC LIMIT 1")
    suspend fun getLatestPriceByName(name: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceHistory(item: PriceHistoryEntity): Long
}
