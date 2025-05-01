package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.databinding.ActivityDashboardBinding
import vcmsa.projects.prog7313_poe.ui.adapters.CategoryAdapter
import vcmsa.projects.prog7313_poe.ui.viewmodels.*
import java.text.NumberFormat
import java.util.*
import vcmsa.projects.prog7313_poe.core.models.User


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private var userId: UUID? = null
    private var currentSavings: Double = 5000.0
    private var savingsGoal: Double = 10000.0
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            userId = intent.getSerializableExtra("USER_ID") as? UUID
            if (userId == null) {
                showToast("Error: User ID not found")
                finish()
                return
            }

            binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding.root)

            initializeViewModels()

            // This guarantees user is inserted before loading data
            lifecycleScope.launch {
                insertGuestUserIfNeeded()    // suspend block
                loadUserData()               // run only after insert finishes
            }

            setupBottomNavigation()
            setupCategoriesRecyclerView()
            loadUserData()
            setupClickListeners()
        } catch (e: Exception) {
            showToast("Error initializing dashboard: ${e.message}")
            finish()
        }
    }

    private suspend fun insertGuestUserIfNeeded() {
        val isGuest = intent.getBooleanExtra("IS_GUEST", false)

        if (isGuest && userId != null) {
            val existing = userViewModel.getUserById(userId!!)
            if (existing == null) {
                val guest = User(
                    id = userId!!,
                    username = "guest",
                    password = "",
                    passwordSalt = "",
                    name = "Guest",
                    surname = "",
                    dateOfBirth = null,
                    cellNumber = null,
                    emailAddress = "guest-${userId}@demo.com",
                    minGoal = 500.0,
                    maxGoal = 8000.0,
                    imageUri = null,
                    goalsSet = true,
                    profileCompleted = true,
                    monthlyBudget = 3000.0,
                    currentBudget = 3000.0,
                    budgetLastReset = System.currentTimeMillis()
                )
                userViewModel.insertUser(guest)
            }
        }
    }




    private fun initializeViewModels() {
        val db = AppDatabase.getDatabase(applicationContext)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(UserRepository(db.userDao())))[UserViewModel::class.java]
        categoryViewModel = ViewModelProvider(this, CategoryViewModelFactory(CategoryRepository(db.categoryDao())))[CategoryViewModel::class.java]
    }

    private fun setupCategoriesRecyclerView() {
        categoryAdapter = CategoryAdapter { category ->
            startActivity(Intent(this, CategoryDetailsActivity::class.java).apply {
                putExtra("CATEGORY_ID", category.id)
                putExtra("USER_ID", userId)
            })
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@DashboardActivity, 2)
            adapter = categoryAdapter
        }

        loadCategories()
    }

    private fun loadCategories() {
        userId?.let { id ->
            lifecycleScope.launch {
                try {
                    val categories = categoryViewModel.getCategoriesByUserId(id)
                    categoryAdapter.submitList(categories)
                } catch (e: Exception) {
                    showToast("Error loading categories: ${e.message}")
                }
            }
        }
    }

    private fun loadUserData() {
        userId?.let { id ->
            lifecycleScope.launch {
                try {
                    val user = userViewModel.getUserById(id)
                    user?.let {
                        currentSavings = it.monthlyBudget ?: 0.0
                        savingsGoal = it.maxGoal ?: 10000.0
                        setInitialValues()
                    } ?: run {
                        showToast("User data not found")
                        finish()
                    }
                } catch (e: Exception) {
                    showToast("Error loading user data: ${e.message}")
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            searchButton.setOnClickListener { showToast("Search functionality coming soon") }
            profileImage.setOnClickListener { navigateToProfile() }
            addNewCard.setOnClickListener { showToast("Add new card functionality coming soon") }
            availableBalanceValue.setOnClickListener { showToast("Card details coming soon") }
        }
    }

    private fun navigateToProfile() {
        try {
            startActivity(Intent(this, CompleteProfileActivity::class.java).apply {
                putExtra("USER_ID", userId)
            })
        } catch (e: Exception) {
            showToast("Error opening profile: ${e.message}")
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            try {
                when (item.itemId) {
                    R.id.navigation_home -> true
                    R.id.navigation_add_expense -> {
                        navigateTo(AddExpenseActivity::class.java)
                        true
                    }
                    R.id.navigation_transactions -> {
                        navigateTo(ViewExpensesActivity::class.java)
                        true
                    }
                    R.id.navigation_profile -> {
                        navigateTo(CompleteProfileActivity::class.java)
                        true
                    }
                    else -> false
                }
            } catch (e: Exception) {
                showToast("Error navigating: ${e.message}")
                false
            }
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass).apply {
            putExtra("USER_ID", userId)
        })
    }

    private fun setInitialValues() {
        try {
            with(binding) {
                availableBalanceValue.text = currencyFormat.format(250000.0)
                budgetValue.text = currencyFormat.format(26000.0)
                savingsStart.text = currencyFormat.format(currentSavings)
                savingsGoal.text = currencyFormat.format(savingsGoal)
                var savingsGoalFinal = savingsGoal.text.toString().toInt().toDouble()
                savingsProgress.progress = calculateProgress(currentSavings, savingsGoalFinal)
            }
            updateCurrentMonth()
        } catch (e: Exception) {
            showToast("Error setting initial values: ${e.message}")
        }
    }

    private fun calculateProgress(current: Double, goal: Double): Int {
        return if (goal > 0) {
            ((current / goal) * 100.0).toInt().coerceIn(0, 100)
        } else {
            0
        }
    }

    private fun updateCurrentMonth() {
        try {
            val calendar = Calendar.getInstance()
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
            val year = calendar.get(Calendar.YEAR)
            binding.budgetLabel.text = "Budget for $month $year"
        } catch (e: Exception) {
            binding.budgetLabel.text = "Budget"
        }
    }

    fun updateSavings(newSavings: Double) {
        try {
            currentSavings = newSavings.coerceAtLeast(0.0)
            updateSavingsUI()
            updateUserDataInDb()
        } catch (e: Exception) {
            showToast("Error updating savings: ${e.message}")
        }
    }

    fun updateSavingsGoal(newGoal: Double) {
        try {
            savingsGoal = newGoal.coerceAtLeast(0.0)
            updateSavingsUI()
            updateUserDataInDb()
        } catch (e: Exception) {
            showToast("Error updating savings goal: ${e.message}")
        }
    }

    private fun updateSavingsUI() {
        try {
            with(binding) {
                savingsStart.text = currencyFormat.format(currentSavings)
                savingsGoal.text = currencyFormat.format(savingsGoal)

                var savingsGoalFinal = savingsGoal.text.toString().toInt().toDouble()
                savingsProgress.progress = calculateProgress(currentSavings, savingsGoalFinal)
            }
        } catch (e: Exception) {
            showToast("Error updating savings UI: ${e.message}")
        }
    }

    private fun updateUserDataInDb() {
        userId?.let { id ->
            lifecycleScope.launch {
                try {
                    userViewModel.updateUserGoalsAndBudget(
                        userId = id,
                        minGoal = currentSavings,
                        maxGoal = savingsGoal,
                        monthlyBudget = currentSavings,
                        goalsSet = true
                    )
                } catch (e: Exception) {
                    showToast("Error updating user data: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}