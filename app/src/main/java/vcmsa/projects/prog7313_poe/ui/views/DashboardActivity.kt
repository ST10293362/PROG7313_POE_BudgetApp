package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
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
    private lateinit var viewModel: DashboardViewModel
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

            viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
            setupUI()
            setupObservers()
            loadUserData()
        } catch (e: Exception) {
            showToast("Error initializing dashboard: ${e.message}")
            finish()
        }
    }

    private fun setupUI() {
        // Setup bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    // Already on dashboard
                    true
                }
                R.id.navigation_expenses -> {
                    startActivity(Intent(this, ViewExpensesActivity::class.java))
                    true
                }
                R.id.navigation_add -> {
                    startActivity(Intent(this, AddExpenseActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Setup categories RecyclerView
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

        // Setup search button
        binding.searchButton.setOnClickListener {
            // TODO: Implement search functionality
            Toast.makeText(this, "Search functionality coming soon", Toast.LENGTH_SHORT).show()
        }

        // Setup profile image
        binding.profileImage.setOnClickListener {
            startActivity(Intent(this, CompleteProfileActivity::class.java).apply {
                putExtra("USER_ID", userId)
            })
        }
    }

    private fun setupObservers() {
        viewModel.userData.observe(this) { user ->
            if (user != null) {
                currentSavings = user.monthlyBudget ?: 0.0
                savingsGoal = user.maxGoal ?: 10000.0
                setInitialValues()
                loadCategories()
            }
        }

        viewModel.categories.observe(this) { categories ->
            categoryAdapter.submitList(categories)
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                showToast(it)
            }
        }
    }

    private fun loadUserData() {
        userId?.let { id ->
            lifecycleScope.launch {
                try {
                    viewModel.loadUserData()
                } catch (e: Exception) {
                    showToast("Error loading user data: ${e.message}")
                }
            }
        }
    }

    private fun loadCategories() {
        userId?.let { id ->
            lifecycleScope.launch {
                try {
                    viewModel.loadCategories()
                } catch (e: Exception) {
                    showToast("Error loading categories: ${e.message}")
                }
            }
        }
    }

    private fun setInitialValues() {
        try {
            with(binding) {
                savingsStart.text = currencyFormat.format(currentSavings)
                savingsGoal.text = currencyFormat.format(savingsGoal)
                savingsProgress.progress = calculateProgress(currentSavings, savingsGoal)
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
                savingsProgress.progress = calculateProgress(currentSavings, savingsGoal)
            }
        } catch (e: Exception) {
            showToast("Error updating savings UI: ${e.message}")
        }
    }

    private fun updateUserDataInDb() {
        userId?.let { id ->
            lifecycleScope.launch {
                try {
                    viewModel.updateUserData(
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