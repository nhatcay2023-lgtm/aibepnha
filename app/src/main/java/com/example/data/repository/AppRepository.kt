package com.example.data.repository

import com.example.data.local.ExpenseDao
import com.example.data.local.MealDao
import com.example.data.local.ShoppingDao
import com.example.data.model.DailyExpenseBar
import com.example.data.model.ExpenseCategorySummary
import com.example.data.model.ExpenseEntity
import com.example.data.model.MealEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.PriceHistoryEntity
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.ShoppingListEntity
import kotlinx.coroutines.flow.Flow
import java.util.regex.Pattern

class AppRepository(
    private val expenseDao: ExpenseDao,
    private val mealDao: MealDao,
    private val shoppingDao: ShoppingDao
) {
    val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    val allMeals: Flow<List<MealEntity>> = mealDao.getAllMeals()
    val allShoppingItems: Flow<List<ShoppingItemEntity>> = shoppingDao.getAllShoppingItems()
    val allShoppingLists: Flow<List<ShoppingListEntity>> = shoppingDao.getAllShoppingLists()
    val activeShoppingList: Flow<ShoppingListEntity?> = shoppingDao.getActiveShoppingList()
    val allPantryItems: Flow<List<PantryItemEntity>> = shoppingDao.getAllPantryItems()
    val allPriceHistory: Flow<List<PriceHistoryEntity>> = shoppingDao.getAllPriceHistory()

    fun getShoppingItemsForList(listId: Int): Flow<List<ShoppingItemEntity>> {
        return shoppingDao.getShoppingItemsByListId(listId)
    }

    suspend fun addExpense(category: String, amount: Long, note: String = "") {
        expenseDao.insertExpense(
            ExpenseEntity(
                category = category,
                amount = amount,
                date = "13/10/2026",
                dayOfWeek = "T3",
                note = note
            )
        )
    }

    suspend fun addMeal(
        name: String,
        portion: String = "4 người",
        category: String = "Món mặn",
        ingredients: String = "",
        recipe: String = "",
        mealTime: String = "Trưa",
        date: String = "13/10/2026",
        estimatedCost: Long = 85000,
        prepTimeMinutes: Int = 15,
        cookTimeMinutes: Int = 30
    ): Long {
        return mealDao.insertMeal(
            MealEntity(
                name = name,
                portion = portion,
                category = category,
                portionBase = 4,
                ingredients = ingredients,
                recipe = recipe,
                mealTime = mealTime,
                date = date,
                estimatedCost = estimatedCost,
                prepTimeMinutes = prepTimeMinutes,
                cookTimeMinutes = cookTimeMinutes,
                isCooked = false
            )
        )
    }

    suspend fun updateMeal(meal: MealEntity) {
        mealDao.updateMeal(meal)
    }

    suspend fun deleteMeal(meal: MealEntity) {
        mealDao.deleteMeal(meal)
    }

    suspend fun deleteMealById(id: Int) {
        mealDao.deleteMealById(id)
    }

    suspend fun toggleMealCooked(id: Int, currentStatus: Boolean) {
        mealDao.updateCookedStatus(id, !currentStatus)
    }

    suspend fun copyMealToDate(meal: MealEntity, targetDate: String, targetMealTime: String) {
        mealDao.insertMeal(
            meal.copy(
                id = 0,
                date = targetDate,
                mealTime = targetMealTime,
                isCooked = false
            )
        )
    }

    // Ingredient Scaling: Scale quantity linearly based on portion change
    fun scaleIngredients(ingredientsStr: String, basePortion: Int, newPortion: Int): String {
        if (basePortion <= 0 || newPortion <= 0 || ingredientsStr.isBlank()) return ingredientsStr
        val ratio = newPortion.toDouble() / basePortion.toDouble()

        val items = ingredientsStr.split(",").map { it.trim() }
        val scaledItems = items.map { item ->
            val regex = Pattern.compile("(\\d+(\\.\\d+)?)\\s*([a-zA-Zà-ỹÀ-Ỹ]+)?(.*)")
            val matcher = regex.matcher(item)
            if (matcher.find()) {
                val numStr = matcher.group(1) ?: ""
                val unit = matcher.group(3) ?: ""
                val rest = matcher.group(4) ?: ""
                val num = numStr.toDoubleOrNull()
                if (num != null) {
                    val scaledNum = num * ratio
                    val formattedNum = if (scaledNum % 1.0 == 0.0) {
                        scaledNum.toInt().toString()
                    } else {
                        "%.1f".format(scaledNum).replace(',', '.')
                    }
                    "$formattedNum$unit$rest".trim()
                } else {
                    item
                }
            } else {
                item
            }
        }
        return scaledItems.joinToString(", ")
    }

    // Intelligent ingredient categorization
    fun categorizeIngredient(name: String): String {
        val lower = name.lowercase().trim()
        val meatKeywords = listOf(
            "thịt", "cá", "sườn", "giò", "chả", "tôm", "cua", "mực", "gà", "vịt",
            "bò", "heo", "lợn", "mỡ", "lươn", "trứng", "ba chỉ", "tai heo", "hải sản", "bạch tuộc"
        )
        val vegKeywords = listOf(
            "rau", "cải", "bắp cải", "cà chua", "dứa", "thơm", "giá", "giá đỗ", "đậu bắp",
            "bí", "bầu", "mướp", "khổ qua", "gừng", "tỏi", "hành", "chanh", "ớt", "ngò",
            "mùi", "thì là", "xà lách", "dưa chuột", "khoai", "cà rốt", "su hào", "nấm"
        )
        val dryKeywords = listOf(
            "mắm", "nước mắm", "muối", "đường", "tiêu", "hạt nêm", "mì chính", "bột ngọt",
            "dầu ăn", "xì dầu", "giấm", "gạo", "bún", "miến", "phở", "tiêu sọ", "bột mì",
            "tương", "bột bắp", "vừng", "mè", "rượu nấu", "ngũ vị hương"
        )

        return when {
            meatKeywords.any { lower.contains(it) } -> "Thịt, cá, hải sản"
            vegKeywords.any { lower.contains(it) } -> "Rau củ, trái cây"
            dryKeywords.any { lower.contains(it) } -> "Gia vị, đồ khô"
            else -> "Khác"
        }
    }

    // Realistic market price estimation
    fun estimateMarketPrice(name: String, quantity: Double, unit: String): Long {
        val lower = name.lowercase()
        return when {
            lower.contains("thịt ba chỉ") -> (quantity * 130).toLong().coerceAtLeast(35000)
            lower.contains("thịt bò") -> (quantity * 250).toLong().coerceAtLeast(60000)
            lower.contains("thịt gà") -> (quantity * 90).toLong().coerceAtLeast(40000)
            lower.contains("cá quả") || lower.contains("cá lóc") -> (quantity * 95).toLong().coerceAtLeast(45000)
            lower.contains("trứng cút") -> (quantity * 1200).toLong().coerceAtLeast(15000)
            lower.contains("trứng gà") || lower.contains("trứng") -> (quantity * 3500).toLong().coerceAtLeast(10000)
            lower.contains("bắp cải") -> 20000L
            lower.contains("cà chua") -> 15000L
            lower.contains("giá đỗ") -> 10000L
            lower.contains("đậu bắp") -> 10000L
            lower.contains("dứa") -> 12000L
            lower.contains("gừng") || lower.contains("hành") || lower.contains("tỏi") || lower.contains("ớt") -> 8000L
            lower.contains("nước mắm") -> 35000L
            lower.contains("dầu ăn") -> 45000L
            lower.contains("hạt nêm") -> 25000L
            lower.contains("đường") || lower.contains("muối") -> 12000L
            else -> (25000L * quantity.coerceAtLeast(1.0)).toLong().coerceAtMost(100000L)
        }
    }

    // Generate Shopping List from Meals with Pantry Subtraction & Ingredient Aggregation
    suspend fun generateSmartShoppingListFromMeals(
        meals: List<MealEntity>,
        title: String,
        periodLabel: String = "Tuần này",
        dateRange: String = "13/10/2026",
        subtractPantry: Boolean = true,
        budget: Long = 800000
    ): Long {
        // 1. Create or insert Shopping List
        val listEntity = ShoppingListEntity(
            title = title,
            periodLabel = periodLabel,
            dateRange = dateRange,
            budget = budget,
            isCompleted = false
        )
        val listId = shoppingDao.insertShoppingList(listEntity).toInt()

        // 2. Fetch existing pantry items if subtraction enabled
        val pantryItems = if (subtractPantry) shoppingDao.getAllPantryItemsSync() else emptyList()

        // 3. Aggregate ingredients across all meals
        // Key: cleanKey -> Triple(totalQty, unit, sourceDishes)
        val aggregated = mutableMapOf<String, Triple<Double, String, MutableList<String>>>()

        for (meal in meals) {
            val parts = meal.ingredients.split(",").map { it.trim() }
            for (part in parts) {
                if (part.isBlank()) continue
                val regex = Pattern.compile("(\\d+(\\.\\d+)?)\\s*([a-zA-Zà-ỹÀ-Ỹ]+)?\\s*(.*)")
                val matcher = regex.matcher(part)
                if (matcher.find()) {
                    val num = matcher.group(1)?.toDoubleOrNull() ?: 1.0
                    val unit = matcher.group(3)?.trim() ?: "phần"
                    val name = matcher.group(4)?.trim() ?: part
                    val cleanKey = name.lowercase()

                    val existing = aggregated[cleanKey]
                    if (existing != null && existing.second.equals(unit, ignoreCase = true)) {
                        existing.third.add(meal.name)
                        aggregated[cleanKey] = Triple(existing.first + num, unit, existing.third)
                    } else {
                        aggregated[cleanKey] = Triple(num, unit, mutableListOf(meal.name))
                    }
                } else {
                    val cleanKey = part.lowercase()
                    val existing = aggregated[cleanKey]
                    if (existing != null) {
                        existing.third.add(meal.name)
                        aggregated[cleanKey] = Triple(existing.first + 1.0, "món", existing.third)
                    } else {
                        aggregated[cleanKey] = Triple(1.0, "món", mutableListOf(meal.name))
                    }
                }
            }
        }

        // 4. Construct Shopping Items with Pantry Deduction
        val itemsToInsert = aggregated.map { (cleanName, triple) ->
            val origQty = triple.first
            val unit = triple.second
            val dishes = triple.third.distinct().joinToString(", ")
            val category = categorizeIngredient(cleanName)

            // Check if pantry has this item
            val matchingPantry = pantryItems.find { it.name.lowercase().contains(cleanName) || cleanName.contains(it.name.lowercase()) }
            var neededQty = origQty
            var itemNote = ""

            if (matchingPantry != null) {
                if (matchingPantry.quantity >= origQty && matchingPantry.unit.equals(unit, ignoreCase = true)) {
                    itemNote = "Kho đã có ${matchingPantry.quantity} ${matchingPantry.unit} (đủ)"
                    neededQty = 0.0
                } else if (matchingPantry.unit.equals(unit, ignoreCase = true)) {
                    neededQty = (origQty - matchingPantry.quantity).coerceAtLeast(0.0)
                    itemNote = "Đã trừ ${matchingPantry.quantity} ${matchingPantry.unit} từ tủ lạnh"
                } else {
                    itemNote = "Tủ lạnh có: ${matchingPantry.quantity} ${matchingPantry.unit}"
                }
            }

            val estPrice = if (neededQty > 0) estimateMarketPrice(cleanName, neededQty, unit) else 0L

            ShoppingItemEntity(
                listId = listId,
                name = cleanName.replaceFirstChar { it.uppercase() },
                quantity = if (neededQty > 0) neededQty else origQty,
                unit = unit,
                category = category,
                estimatedPrice = estPrice,
                actualPrice = 0,
                isBought = neededQty == 0.0, // Pre-marked bought if already sufficient in pantry!
                weekLabel = periodLabel,
                sourceMealName = dishes,
                note = itemNote
            )
        }

        shoppingDao.insertAll(itemsToInsert)
        return listId.toLong()
    }

    // Maintain backwards compatibility for older calls
    suspend fun generateShoppingListFromMeals(meals: List<MealEntity>, weekLabel: String = "Tuần này") {
        generateSmartShoppingListFromMeals(
            meals = meals,
            title = "Đi chợ $weekLabel",
            periodLabel = weekLabel,
            dateRange = "13/10/2026",
            subtractPantry = true,
            budget = 800000
        )
    }

    suspend fun createManualShoppingList(title: String, budget: Long, periodLabel: String, dateRange: String): Long {
        val list = ShoppingListEntity(
            title = title,
            budget = budget,
            periodLabel = periodLabel,
            dateRange = dateRange,
            isCompleted = false
        )
        return shoppingDao.insertShoppingList(list)
    }

    suspend fun addShoppingItem(
        listId: Int,
        name: String,
        quantity: Double,
        unit: String,
        category: String,
        estimatedPrice: Long,
        note: String = ""
    ) {
        val item = ShoppingItemEntity(
            listId = listId,
            name = name,
            quantity = quantity,
            unit = unit,
            category = category,
            estimatedPrice = estimatedPrice,
            actualPrice = 0,
            isBought = false,
            note = note
        )
        shoppingDao.insertShoppingItem(item)
    }

    suspend fun updateShoppingItem(item: ShoppingItemEntity) {
        shoppingDao.updateShoppingItem(item)
    }

    suspend fun toggleShoppingItemBought(id: Int, isBought: Boolean, actualPrice: Long = 0) {
        if (actualPrice > 0) {
            shoppingDao.updateBoughtStatusAndPrice(id, isBought, actualPrice)
        } else {
            shoppingDao.updateBoughtStatus(id, isBought)
        }
    }

    suspend fun deleteShoppingItem(id: Int) {
        shoppingDao.deleteItemById(id)
    }

    suspend fun deleteShoppingList(list: ShoppingListEntity) {
        shoppingDao.deleteItemsByListId(list.id)
        shoppingDao.deleteShoppingList(list)
    }

    // Complete Shopping Trip: Update pantry, record expense, mark list done
    suspend fun completeShoppingTrip(
        listId: Int,
        addToPantry: Boolean = true,
        recordExpense: Boolean = true
    ): Pair<Long, Int> {
        val list = shoppingDao.getShoppingListById(listId) ?: return Pair(0L, 0)
        val items = shoppingDao.getShoppingItemsByListIdSync(listId)

        val boughtItems = items.filter { it.isBought }
        val totalActualSpent = boughtItems.sumOf { if (it.actualPrice > 0) it.actualPrice else it.estimatedPrice }

        // 1. Add bought items to pantry if requested
        if (addToPantry) {
            for (item in boughtItems) {
                val existing = shoppingDao.findPantryItemByName(item.name)
                if (existing != null && existing.unit.equals(item.unit, ignoreCase = true)) {
                    shoppingDao.updatePantryItem(
                        existing.copy(
                            quantity = existing.quantity + item.quantity,
                            addedDate = "13/10/2026"
                        )
                    )
                } else {
                    shoppingDao.insertPantryItem(
                        PantryItemEntity(
                            name = item.name,
                            quantity = item.quantity,
                            unit = item.unit,
                            category = item.category,
                            note = "Mua từ: ${list.title}",
                            addedDate = "13/10/2026"
                        )
                    )
                }

                // Record price history
                val priceToRecord = if (item.actualPrice > 0) item.actualPrice else item.estimatedPrice
                if (priceToRecord > 0) {
                    shoppingDao.insertPriceHistory(
                        PriceHistoryEntity(
                            name = item.name,
                            unit = item.unit,
                            price = priceToRecord,
                            recordedDate = "13/10/2026"
                        )
                    )
                }
            }
        }

        // 2. Record Expense
        if (recordExpense && totalActualSpent > 0) {
            expenseDao.insertExpense(
                ExpenseEntity(
                    category = "Đi chợ",
                    amount = totalActualSpent,
                    date = "13/10/2026",
                    dayOfWeek = "T3",
                    note = "Chi đi chợ: ${list.title} (${boughtItems.size} món)"
                )
            )
        }

        // 3. Mark list completed
        shoppingDao.updateShoppingList(
            list.copy(
                isCompleted = true,
                completedDate = "13/10/2026",
                note = "Đã mua ${boughtItems.size}/${items.size} món. Tổng chi: $totalActualSpent đ"
            )
        )

        return Pair(totalActualSpent, boughtItems.size)
    }

    // Reuse completed list for a new trip
    suspend fun reuseShoppingList(oldListId: Int): Long {
        val oldList = shoppingDao.getShoppingListById(oldListId) ?: return 0L
        val oldItems = shoppingDao.getShoppingItemsByListIdSync(oldListId)

        val newList = ShoppingListEntity(
            title = "${oldList.title} (Dùng lại)",
            periodLabel = "Hôm nay",
            dateRange = "13/10/2026",
            budget = oldList.budget,
            isCompleted = false
        )
        val newListId = shoppingDao.insertShoppingList(newList).toInt()

        val newItems = oldItems.map {
            it.copy(
                id = 0,
                listId = newListId,
                isBought = false,
                actualPrice = 0
            )
        }
        shoppingDao.insertAll(newItems)
        return newListId.toLong()
    }

    // Pantry Management
    suspend fun addPantryItem(name: String, quantity: Double, unit: String, category: String, note: String = "") {
        shoppingDao.insertPantryItem(
            PantryItemEntity(
                name = name,
                quantity = quantity,
                unit = unit,
                category = category,
                note = note
            )
        )
    }

    suspend fun deletePantryItem(item: PantryItemEntity) {
        shoppingDao.deletePantryItem(item)
    }

    // Initial Database Seeding
    suspend fun seedInitialDataIfEmpty() {
        val initialMeals = listOf(
            MealEntity(
                id = 1,
                name = "Cá kho gừng",
                portion = "4 người",
                category = "Món mặn",
                portionBase = 4,
                ingredients = "400g cá quả, 1 củ gừng, 3 thìa nước mắm, 1 củ hành, 1 quả ớt",
                recipe = "1. Ướp cá với gừng thái sợi, nước mắm ngon và tiêu sọ 20 phút.\n2. Thắng đường vàng cánh gián làm nước màu.\n3. Cho cá vào đảo săn hai mặt rồi đổ nước xâm xấp.\n4. Đun nhỏ lửa riu riu 30 phút cho cá đậm đà keo sánh.",
                prepTimeMinutes = 15,
                cookTimeMinutes = 35,
                estimatedCost = 85000,
                date = "13/10/2026",
                mealTime = "Trưa",
                isCooked = false
            ),
            MealEntity(
                id = 2,
                name = "Bắp cải luộc",
                portion = "4 người",
                category = "Món rau",
                portionBase = 4,
                ingredients = "600g bắp cải, 1 nhánh gừng, 1 thìa muối",
                recipe = "1. Bắp cải rửa sạch, cắt miếng vừa ăn.\n2. Đun sôi nước với nhánh gừng đập dập và chút muối.\n3. Thả bắp cải vào luộc 4-5 phút cho chín tới giòn ngọt.\n4. Vớt ra đĩa, nước luộc làm canh thanh mát.",
                prepTimeMinutes = 10,
                cookTimeMinutes = 10,
                estimatedCost = 25000,
                date = "13/10/2026",
                mealTime = "Trưa",
                isCooked = true
            ),
            MealEntity(
                id = 3,
                name = "Thịt kho trứng cút",
                portion = "4 người",
                category = "Món mặn",
                portionBase = 4,
                ingredients = "500g thịt ba chỉ, 10 quả trứng cút, 3 thìa nước mắm, 1 củ hành tím",
                recipe = "1. Luộc chín trứng cút, bóc sạch vỏ.\n2. Thịt ba chỉ thái miếng vuông vừa ăn, ướp hành tím băm và mắm tiêu.\n3. Xào săn thịt, thêm nước dừa xiêm đun sôi.\n4. Thả trứng cút vào kho nhỏ lửa 40 phút cho thịt mềm béo ngậy.",
                prepTimeMinutes = 20,
                cookTimeMinutes = 45,
                estimatedCost = 110000,
                date = "13/10/2026",
                mealTime = "Tối",
                isCooked = false
            ),
            MealEntity(
                id = 4,
                name = "Canh chua cá lóc",
                portion = "4 người",
                category = "Món canh",
                portionBase = 4,
                ingredients = "350g cá lóc, 2 quả cà chua, 50g dứa, 100g giá đỗ, 50g đậu bắp",
                recipe = "1. Rán sơ cá lóc cho săn.\n2. Xào thơm cà chua và dứa.\n3. Đun sôi nước dùng me chua, thả cá vào đun 10 phút.\n4. Thêm đậu bắp, giá đỗ, nêm mắm thơm và ngò gai.",
                prepTimeMinutes = 15,
                cookTimeMinutes = 20,
                estimatedCost = 75000,
                date = "13/10/2026",
                mealTime = "Tối",
                isCooked = false
            )
        )
        mealDao.insertAll(initialMeals)

        val initialExpenses = listOf(
            ExpenseEntity(category = "Ăn uống", amount = 450000, date = "10/10/2026", dayOfWeek = "T6", note = "Bữa tối gia đình cuối tuần"),
            ExpenseEntity(category = "Đi chợ", amount = 200000, date = "07/10/2026", dayOfWeek = "T3", note = "Thịt, cá, rau củ đầu tuần"),
            ExpenseEntity(category = "Sinh hoạt", amount = 120000, date = "08/10/2026", dayOfWeek = "T4", note = "Gia vị, dầu ăn & nước giặt"),
            ExpenseEntity(category = "Cá nhân", amount = 80000, date = "09/10/2026", dayOfWeek = "T5", note = "Cà phê sáng & đồ dùng"),
            ExpenseEntity(category = "Đi lại", amount = 40000, date = "06/10/2026", dayOfWeek = "T2", note = "Xăng xe đi làm"),
            ExpenseEntity(category = "Khác", amount = 30000, date = "11/10/2026", dayOfWeek = "T7", note = "Phí gửi xe & vặt vãnh")
        )
        expenseDao.insertAll(initialExpenses)

        // Seed Pantry items
        val initialPantry = listOf(
            PantryItemEntity(name = "Gừng", quantity = 1.0, unit = "củ", category = "Rau củ, trái cây", note = "Rổ gia vị"),
            PantryItemEntity(name = "Nước mắm", quantity = 1.0, unit = "chai", category = "Gia vị, đồ khô", note = "Kệ gia vị"),
            PantryItemEntity(name = "Đường", quantity = 500.0, unit = "g", category = "Gia vị, đồ khô", note = "Hũ gia vị"),
            PantryItemEntity(name = "Muối", quantity = 1.0, unit = "gói", category = "Gia vị, đồ khô", note = "Tủ bếp"),
            PantryItemEntity(name = "Hành tím", quantity = 2.0, unit = "củ", category = "Rau củ, trái cây", note = "Rổ hành tỏi")
        )
        shoppingDao.insertAllPantryItems(initialPantry)

        // Seed initial smart shopping list with pantry deduction
        generateSmartShoppingListFromMeals(
            meals = initialMeals,
            title = "Đi chợ Tuần 42 (13/10 - 18/10/2026)",
            periodLabel = "Tuần này",
            dateRange = "12/10 - 18/10/2026",
            subtractPantry = true,
            budget = 800000
        )
    }

    fun getCategorySummaries(): List<ExpenseCategorySummary> {
        return listOf(
            ExpenseCategorySummary("Ăn uống", 450000, 48.9f, 0xFF3B82F6),
            ExpenseCategorySummary("Đi chợ", 200000, 21.7f, 0xFF10B981),
            ExpenseCategorySummary("Sinh hoạt", 120000, 13.0f, 0xFFF59E0B),
            ExpenseCategorySummary("Cá nhân", 80000, 8.7f, 0xFF8B5CF6),
            ExpenseCategorySummary("Đi lại", 40000, 4.3f, 0xFFEF4444),
            ExpenseCategorySummary("Khác", 30000, 3.4f, 0xFF64748B)
        )
    }

    fun getDailyBars(): List<DailyExpenseBar> {
        return listOf(
            DailyExpenseBar("T2", 110),
            DailyExpenseBar("T3", 95),
            DailyExpenseBar("T4", 120),
            DailyExpenseBar("T5", 130),
            DailyExpenseBar("T6", 180),
            DailyExpenseBar("T7", 160),
            DailyExpenseBar("CN", 125)
        )
    }
}
