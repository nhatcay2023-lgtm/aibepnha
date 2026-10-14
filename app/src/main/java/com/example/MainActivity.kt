package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MealEntity
import com.example.ui.components.AiAssistantDialog
import com.example.ui.components.DishDetailDialog
import com.example.ui.components.QuickInputBottomSheet
import com.example.ui.components.ScreenSwitcherDrawer
import com.example.ui.screens.AuthSuccessScreen
import com.example.ui.screens.ExpenseScreen
import com.example.ui.screens.FeaturesGridScreen
import com.example.ui.screens.ForgotPasswordScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.OnboardingScreen1
import com.example.ui.screens.OnboardingScreen2
import com.example.ui.screens.OnboardingScreen3
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.ResetPasswordScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShoppingScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                AiBepNhaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AiBepNhaApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val showQuickInputDialog by viewModel.showQuickInputDialog.collectAsStateWithLifecycle()
    val showDevDrawer by viewModel.showDevDrawer.collectAsStateWithLifecycle()
    val meals by viewModel.meals.collectAsStateWithLifecycle()
    val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
    val aiResponse by viewModel.aiResponse.collectAsStateWithLifecycle()
    val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()
    val feedbackMessage by viewModel.feedbackMessage.collectAsStateWithLifecycle()

    var selectedDishForDetail by remember { mutableStateOf<MealEntity?>(null) }
    var showAiChatDialog by remember { mutableStateOf(false) }
    var initialAiPrompt by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearFeedback()
        }
    }

    // Predictive / Compose BackHandler
    BackHandler(enabled = true) {
        if (showQuickInputDialog) {
            viewModel.setQuickInputDialogVisible(false)
        } else if (showDevDrawer) {
            viewModel.setDevDrawerVisible(false)
        } else {
            when (currentScreen) {
                AppScreen.ONBOARDING_2 -> viewModel.navigateTo(AppScreen.ONBOARDING_1)
                AppScreen.ONBOARDING_3 -> viewModel.navigateTo(AppScreen.ONBOARDING_2)
                AppScreen.AUTH_LOGIN -> viewModel.navigateTo(AppScreen.ONBOARDING_3)
                AppScreen.AUTH_REGISTER,
                AppScreen.AUTH_FORGOT_PASSWORD,
                AppScreen.AUTH_RESET_PASSWORD -> viewModel.navigateTo(AppScreen.AUTH_LOGIN)
                AppScreen.AUTH_SUCCESS -> viewModel.navigateTo(AppScreen.HOME)
                AppScreen.FEATURES_GRID,
                AppScreen.EXPENSE,
                AppScreen.SETTINGS,
                AppScreen.MENU,
                AppScreen.SHOPPING -> viewModel.navigateTo(AppScreen.HOME)
                AppScreen.HOME,
                AppScreen.ONBOARDING_1 -> {
                    // Root screen
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Screen Renderer (Exact 1:1 for each of the 13 screens)
            when (currentScreen) {
                // 1. Màn hình giới thiệu AI BẾP NHÀ
                AppScreen.ONBOARDING_1 -> {
                    OnboardingScreen1(
                        onNext = { viewModel.navigateTo(AppScreen.ONBOARDING_2) }
                    )
                }

                // 2. Màn hình giới thiệu trợ lý AI
                AppScreen.ONBOARDING_2 -> {
                    OnboardingScreen2(
                        onNext = { viewModel.navigateTo(AppScreen.ONBOARDING_3) }
                    )
                }

                // 3. Màn hình giới thiệu nhập nhanh và tính năng
                AppScreen.ONBOARDING_3 -> {
                    OnboardingScreen3(
                        onStart = { viewModel.navigateTo(AppScreen.AUTH_LOGIN) }
                    )
                }

                // 4. Màn hình đăng nhập
                AppScreen.AUTH_LOGIN -> {
                    LoginScreen(
                        onLoginSuccess = { viewModel.navigateTo(AppScreen.AUTH_SUCCESS) },
                        onNavigateToRegister = { viewModel.navigateTo(AppScreen.AUTH_REGISTER) },
                        onForgotPassword = { viewModel.navigateTo(AppScreen.AUTH_FORGOT_PASSWORD) }
                    )
                }

                // 5. Màn hình đăng ký
                AppScreen.AUTH_REGISTER -> {
                    RegisterScreen(
                        onRegisterSuccess = { viewModel.navigateTo(AppScreen.AUTH_SUCCESS) },
                        onBackToLogin = { viewModel.navigateTo(AppScreen.AUTH_LOGIN) }
                    )
                }

                // 6. Màn hình quên mật khẩu
                AppScreen.AUTH_FORGOT_PASSWORD -> {
                    ForgotPasswordScreen(
                        onSubmitEmail = { viewModel.navigateTo(AppScreen.AUTH_RESET_PASSWORD) },
                        onBackToLogin = { viewModel.navigateTo(AppScreen.AUTH_LOGIN) }
                    )
                }

                // 7. Màn hình đặt lại mật khẩu
                AppScreen.AUTH_RESET_PASSWORD -> {
                    ResetPasswordScreen(
                        onResetSuccess = { viewModel.navigateTo(AppScreen.AUTH_SUCCESS) },
                        onBackToLogin = { viewModel.navigateTo(AppScreen.AUTH_LOGIN) }
                    )
                }

                // 8. Màn hình đăng nhập thành công
                AppScreen.AUTH_SUCCESS -> {
                    AuthSuccessScreen(
                        onEnterApp = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                // 9. Màn hình trang chủ
                AppScreen.HOME -> {
                    HomeScreen(
                        meals = meals,
                        onNavigate = { viewModel.navigateTo(it) },
                        onOpenQuickInput = { viewModel.setQuickInputDialogVisible(true) },
                        onOpenDevDrawer = { viewModel.setDevDrawerVisible(true) },
                        onDishClick = { selectedDishForDetail = it }
                    )
                }

                // 10. Màn hình chức năng chính
                AppScreen.FEATURES_GRID -> {
                    FeaturesGridScreen(
                        onNavigate = { viewModel.navigateTo(it) },
                        onOpenQuickInput = { viewModel.setQuickInputDialogVisible(true) }
                    )
                }

                // 12. Màn hình chi tiêu
                AppScreen.EXPENSE -> {
                    ExpenseScreen(
                        categorySummaries = viewModel.categorySummaries,
                        dailyBars = viewModel.dailyBars,
                        onNavigate = { viewModel.navigateTo(it) },
                        onOpenQuickInput = { viewModel.setQuickInputDialogVisible(true) }
                    )
                }

                // 13. Màn hình cài đặt
                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        userSettings = userSettings,
                        onSaveSettings = { viewModel.updateSettings(it) },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }

                // 14. Màn hình thực đơn & công thức
                AppScreen.MENU -> {
                    val shoppingItems by viewModel.shoppingItems.collectAsStateWithLifecycle()
                    MenuScreen(
                        meals = meals,
                        shoppingItems = shoppingItems,
                        aiResponse = aiResponse,
                        isAiLoading = isAiLoading,
                        onNavigate = { viewModel.navigateTo(it) },
                        onOpenQuickInput = { viewModel.setQuickInputDialogVisible(true) },
                        onOpenDevDrawer = { viewModel.setDevDrawerVisible(true) },
                        onToggleMealCooked = { id, currentStatus ->
                            viewModel.toggleMealCooked(id, currentStatus)
                        },
                        onAddMeal = { name, portion, category, ingredients, recipe, mealTime, date, cost ->
                            viewModel.addQuickMeal(
                                name = name,
                                portion = portion,
                                category = category,
                                ingredients = ingredients,
                                recipe = recipe,
                                mealTime = mealTime,
                                date = date,
                                estimatedCost = cost
                            )
                        },
                        onDeleteMeal = { meal ->
                            viewModel.deleteMeal(meal)
                        },
                        onCopyMeal = { meal, targetDate, targetMealTime ->
                            viewModel.copyMealToDate(meal, targetDate, targetMealTime)
                        },
                        onScaleIngredients = { ingredients, basePortion, newPortion ->
                            viewModel.scaleIngredients(ingredients, basePortion, newPortion)
                        },
                        onGenerateShoppingList = {
                            viewModel.generateShoppingListFromCurrentMeals()
                        },
                        onToggleShoppingItem = { id, isBought ->
                            viewModel.toggleShoppingItemBought(id, isBought)
                        },
                        onDeleteShoppingItem = { id ->
                            viewModel.deleteShoppingItem(id)
                        },
                        onAskAi = { prompt ->
                            viewModel.askAiAssistant(prompt)
                        }
                    )
                }

                // 15. Màn hình đi chợ thông minh (Master Prompt 03)
                AppScreen.SHOPPING -> {
                    val activeList by viewModel.activeShoppingList.collectAsStateWithLifecycle()
                    val allLists by viewModel.shoppingLists.collectAsStateWithLifecycle()
                    val activeItems by viewModel.activeListItems.collectAsStateWithLifecycle()
                    val pantryItems by viewModel.pantryItems.collectAsStateWithLifecycle()

                    ShoppingScreen(
                        currentList = activeList,
                        allLists = allLists,
                        items = activeItems,
                        meals = meals,
                        pantryItems = pantryItems,
                        onNavigate = { viewModel.navigateTo(it) },
                        onOpenQuickInput = { viewModel.setQuickInputDialogVisible(true) },
                        onSelectList = { viewModel.selectShoppingList(it) },
                        onCreateFromMenu = { selectedMeals, title, period, dateRange, subtractPantry, budget ->
                            viewModel.createSmartShoppingListFromMenu(selectedMeals, title, period, dateRange, subtractPantry, budget)
                        },
                        onCreateManualList = { title, budget, period, dateRange ->
                            viewModel.createManualShoppingList(title, budget, period, dateRange)
                        },
                        onAddItem = { listId, name, qty, unit, cat, estPrice, note ->
                            viewModel.addShoppingItem(listId, name, qty, unit, cat, estPrice, note)
                        },
                        onUpdateItem = { viewModel.updateShoppingItem(it) },
                        onDeleteItem = { viewModel.deleteShoppingItem(it) },
                        onToggleBought = { id, isBought, actualPrice ->
                            viewModel.toggleShoppingItemBought(id, isBought, actualPrice)
                        },
                        onCompleteTrip = { listId, addToPantry, recordExpense ->
                            viewModel.completeShoppingTrip(listId, addToPantry, recordExpense)
                        },
                        onReuseList = { viewModel.reuseShoppingList(it) },
                        onDeleteList = { viewModel.deleteShoppingList(it) },
                        onAddPantryItem = { name, qty, unit, cat ->
                            viewModel.addPantryItem(name, qty, unit, cat)
                        }
                    )
                }
            }

            // 11. Màn hình nhập nhanh dạng hộp thoại (Screen 11 - Modal Overlay)
            if (showQuickInputDialog) {
                // Dimmed background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.55f))
                        .clickable { viewModel.setQuickInputDialogVisible(false) }
                )

                QuickInputBottomSheet(
                    onDismiss = { viewModel.setQuickInputDialogVisible(false) },
                    onVoiceSelect = {
                        viewModel.showFeedback("🎤 Đang ghi nhận giọng nói: 'Bữa trưa hôm nay ăn cá kho gừng'")
                        viewModel.setQuickInputDialogVisible(false)
                    },
                    onTextQuickAdd = { text ->
                        viewModel.addQuickExpense("Ăn uống", 50000, text)
                    },
                    onChatAiSelect = {
                        viewModel.setQuickInputDialogVisible(false)
                        initialAiPrompt = "Gợi ý cho tôi thực đơn bữa tối tiết kiệm dưới 100.000 đ cho 4 người"
                        showAiChatDialog = true
                    },
                    onManualAddSelect = {
                        viewModel.addQuickMeal("Thịt kho tàu", "500g thịt")
                    }
                )
            }

            // Interactive Dish Detail Dialog
            selectedDishForDetail?.let { meal ->
                DishDetailDialog(
                    meal = meal,
                    onAskAiToAdjust = { prompt ->
                        initialAiPrompt = prompt
                        viewModel.askAiAssistant(prompt)
                        showAiChatDialog = true
                    },
                    onDismiss = { selectedDishForDetail = null }
                )
            }

            // Interactive AI Assistant Dialog (Gemini 3.5 Flash)
            if (showAiChatDialog) {
                AiAssistantDialog(
                    initialPrompt = initialAiPrompt,
                    aiResponse = aiResponse,
                    isLoading = isAiLoading,
                    onSendQuery = { query ->
                        viewModel.askAiAssistant(query)
                    },
                    onDismiss = { showAiChatDialog = false }
                )
            }

            // Screen Switcher Drawer for 1:1 image testing
            AnimatedVisibility(
                visible = showDevDrawer,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.45f))
                        .clickable { viewModel.setDevDrawerVisible(false) }
                ) {
                    ScreenSwitcherDrawer(
                        currentScreen = currentScreen,
                        onSelectScreen = { screen ->
                            viewModel.navigateTo(screen)
                            if (screen.screenNumber == 11) {
                                viewModel.setQuickInputDialogVisible(true)
                            }
                        },
                        onClose = { viewModel.setDevDrawerVisible(false) },
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
        }
    }
}
