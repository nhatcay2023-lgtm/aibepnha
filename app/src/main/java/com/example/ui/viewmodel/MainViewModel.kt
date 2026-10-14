package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DailyExpenseBar
import com.example.data.model.ExpenseCategorySummary
import com.example.data.model.ExpenseEntity
import com.example.data.model.MealEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.PriceHistoryEntity
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.ShoppingListEntity
import com.example.data.model.UserSettings
import com.example.data.remote.GeminiApiService
import com.example.data.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val screenNumber: Int, val titleVi: String) {
    ONBOARDING_1(1, "Giới thiệu AI Bếp Nhà"),
    ONBOARDING_2(2, "AI đồng hành cùng bạn"),
    ONBOARDING_3(3, "Nhập nhanh - Dễ dàng"),
    AUTH_LOGIN(4, "Đăng nhập"),
    AUTH_REGISTER(5, "Tạo tài khoản mới"),
    AUTH_FORGOT_PASSWORD(6, "Quên mật khẩu"),
    AUTH_RESET_PASSWORD(7, "Đặt lại mật khẩu"),
    AUTH_SUCCESS(8, "Đăng nhập thành công!"),
    HOME(9, "Trang chủ"),
    FEATURES_GRID(10, "Chức năng chính"),
    EXPENSE(12, "Chi tiêu"),
    SETTINGS(13, "Cài đặt"),
    MENU(14, "Thực đơn & Công thức"),
    SHOPPING(15, "Đi chợ")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = AppRepository(db.expenseDao(), db.mealDao(), db.shoppingDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Active Screen
    private val _currentScreen = MutableStateFlow(AppScreen.ONBOARDING_1)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Screen 11: Quick Input Dialog/Modal overlay state
    private val _showQuickInputDialog = MutableStateFlow(false)
    val showQuickInputDialog: StateFlow<Boolean> = _showQuickInputDialog.asStateFlow()

    // Screen picker drawer for fast testing
    private val _showDevDrawer = MutableStateFlow(false)
    val showDevDrawer: StateFlow<Boolean> = _showDevDrawer.asStateFlow()

    // Live Database items
    val meals: StateFlow<List<MealEntity>> = repository.allMeals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shoppingItems: StateFlow<List<ShoppingItemEntity>> = repository.allShoppingItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shoppingLists: StateFlow<List<ShoppingListEntity>> = repository.allShoppingLists
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeShoppingList: StateFlow<ShoppingListEntity?> = repository.activeShoppingList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val pantryItems: StateFlow<List<PantryItemEntity>> = repository.allPantryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val priceHistory: StateFlow<List<PriceHistoryEntity>> = repository.allPriceHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently selected shopping list ID for viewing
    private val _selectedListId = MutableStateFlow<Int?>(null)
    val selectedListId: StateFlow<Int?> = _selectedListId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeListItems: StateFlow<List<ShoppingItemEntity>> = _selectedListId
        .flatMapLatest { id ->
            if (id != null) {
                repository.getShoppingItemsForList(id)
            } else {
                repository.allShoppingItems
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Statistics and charts data
    val categorySummaries: List<ExpenseCategorySummary> = repository.getCategorySummaries()
    val dailyBars: List<DailyExpenseBar> = repository.getDailyBars()

    // User settings
    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    // AI Generation State
    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse: StateFlow<String?> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Toast / Feedback message
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setQuickInputDialogVisible(visible: Boolean) {
        _showQuickInputDialog.value = visible
    }

    fun setDevDrawerVisible(visible: Boolean) {
        _showDevDrawer.value = visible
    }

    fun showFeedback(msg: String) {
        _feedbackMessage.value = msg
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    fun selectShoppingList(listId: Int?) {
        _selectedListId.value = listId
    }

    fun updateSettings(newSettings: UserSettings) {
        _userSettings.value = newSettings
        showFeedback("Đã lưu cài đặt thành công!")
    }

    fun askAiAssistant(prompt: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            try {
                val apiKey = _userSettings.value.geminiApiKey
                val result = GeminiApiService.generateRecipeOrSuggestion(prompt, apiKey)
                _aiResponse.value = result
            } catch (e: Exception) {
                _aiResponse.value = "Không thể kết nối AI: ${e.localizedMessage}"
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun addQuickExpense(category: String, amount: Long, note: String) {
        viewModelScope.launch {
            repository.addExpense(category, amount, note)
            showFeedback("Đã thêm khoản chi $category: $amount đ")
            _showQuickInputDialog.value = false
        }
    }

    fun addQuickMeal(
        name: String,
        portion: String = "4 người",
        category: String = "Món mặn",
        ingredients: String = "",
        recipe: String = "",
        mealTime: String = "Trưa",
        date: String = "13/10/2026",
        estimatedCost: Long = 85000
    ) {
        viewModelScope.launch {
            repository.addMeal(
                name = name,
                portion = portion,
                category = category,
                ingredients = ingredients,
                recipe = recipe,
                mealTime = mealTime,
                date = date,
                estimatedCost = estimatedCost
            )
            showFeedback("Đã thêm món: $name ($mealTime)")
            _showQuickInputDialog.value = false
        }
    }

    fun updateMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.updateMeal(meal)
            showFeedback("Đã cập nhật món: ${meal.name}")
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
            showFeedback("Đã xóa món: ${meal.name}")
        }
    }

    fun toggleMealCooked(id: Int, isCooked: Boolean) {
        viewModelScope.launch {
            repository.toggleMealCooked(id, isCooked)
        }
    }

    fun copyMealToDate(meal: MealEntity, targetDate: String, targetMealTime: String) {
        viewModelScope.launch {
            repository.copyMealToDate(meal, targetDate, targetMealTime)
            showFeedback("Đã sao chép ${meal.name} sang $targetDate ($targetMealTime)")
        }
    }

    fun scaleIngredients(ingredientsStr: String, basePortion: Int, newPortion: Int): String {
        return repository.scaleIngredients(ingredientsStr, basePortion, newPortion)
    }

    fun generateShoppingListFromCurrentMeals() {
        viewModelScope.launch {
            val currentMeals = meals.value
            repository.generateShoppingListFromMeals(currentMeals)
            showFeedback("Đã tạo danh sách đi chợ từ ${currentMeals.size} món ăn!")
        }
    }

    // Smart Shopping Module actions
    fun createSmartShoppingListFromMenu(
        selectedMeals: List<MealEntity>,
        title: String,
        periodLabel: String = "Tuần này",
        dateRange: String = "13/10/2026",
        subtractPantry: Boolean = true,
        budget: Long = 800000
    ) {
        viewModelScope.launch {
            val newListId = repository.generateSmartShoppingListFromMeals(
                meals = selectedMeals,
                title = title,
                periodLabel = periodLabel,
                dateRange = dateRange,
                subtractPantry = subtractPantry,
                budget = budget
            )
            _selectedListId.value = newListId.toInt()
            showFeedback("Đã tạo danh sách '$title' với ${selectedMeals.size} món (đã kiểm tra tủ lạnh)!")
        }
    }

    fun createManualShoppingList(
        title: String,
        budget: Long,
        periodLabel: String = "Hôm nay",
        dateRange: String = "13/10/2026"
    ) {
        viewModelScope.launch {
            val newListId = repository.createManualShoppingList(title, budget, periodLabel, dateRange)
            _selectedListId.value = newListId.toInt()
            showFeedback("Đã tạo danh sách mới: $title")
        }
    }

    fun addShoppingItem(
        listId: Int,
        name: String,
        quantity: Double,
        unit: String,
        category: String,
        estimatedPrice: Long,
        note: String = ""
    ) {
        viewModelScope.launch {
            repository.addShoppingItem(listId, name, quantity, unit, category, estimatedPrice, note)
            showFeedback("Đã thêm '$name' vào danh sách!")
        }
    }

    fun updateShoppingItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.updateShoppingItem(item)
            showFeedback("Đã cập nhật: ${item.name}")
        }
    }

    fun toggleShoppingItemBought(id: Int, isBought: Boolean, actualPrice: Long = 0) {
        viewModelScope.launch {
            repository.toggleShoppingItemBought(id, isBought, actualPrice)
        }
    }

    fun deleteShoppingItem(id: Int) {
        viewModelScope.launch {
            repository.deleteShoppingItem(id)
            showFeedback("Đã xóa món khỏi danh sách")
        }
    }

    fun deleteShoppingList(list: ShoppingListEntity) {
        viewModelScope.launch {
            repository.deleteShoppingList(list)
            if (_selectedListId.value == list.id) {
                _selectedListId.value = null
            }
            showFeedback("Đã xóa danh sách: ${list.title}")
        }
    }

    fun completeShoppingTrip(listId: Int, addToPantry: Boolean, recordExpense: Boolean) {
        viewModelScope.launch {
            val result = repository.completeShoppingTrip(listId, addToPantry, recordExpense)
            val spent = result.first
            val count = result.second
            val formattedSpent = "%,d đ".format(spent).replace(',', '.')
            showFeedback("Hoàn thành đi chợ! Đã mua $count món, tổng chi $formattedSpent (đã cập nhật kho & chi tiêu).")
        }
    }

    fun reuseShoppingList(oldListId: Int) {
        viewModelScope.launch {
            val newListId = repository.reuseShoppingList(oldListId)
            if (newListId > 0) {
                _selectedListId.value = newListId.toInt()
                showFeedback("Đã tạo bản sao danh sách để đi chợ mới!")
            }
        }
    }

    fun addPantryItem(name: String, quantity: Double, unit: String, category: String, note: String = "") {
        viewModelScope.launch {
            repository.addPantryItem(name, quantity, unit, category, note)
            showFeedback("Đã thêm '$name' vào kho nguyên liệu!")
        }
    }

    fun deletePantryItem(item: PantryItemEntity) {
        viewModelScope.launch {
            repository.deletePantryItem(item)
            showFeedback("Đã xóa món khỏi kho")
        }
    }
}
