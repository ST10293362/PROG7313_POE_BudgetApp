package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.ui.views.CategoryDetailsActivity
import androidx.lifecycle.Observer
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.databinding.ActivityDashboardBinding
import vcmsa.projects.prog7313_poe.ui.adapters.CategoryAdapter
import vcmsa.projects.prog7313_poe.ui.viewmodels.CategoryViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.CategoryViewModelFactory
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModelFactory
import java.text.NumberFormat
import java.util.*
import java.util.UUID

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private var userId: UUID? = null
    private var currentSavings = 5000.0
    private var savingsGoal = 10000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Get userId from intent
            userId = intent.getSerializableExtra("USER_ID") as? UUID
            if (userId == null) {
                Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Initialize ViewModels
            val db = AppDatabase.getDatabase(applicationContext)
            val userRepository = UserRepository(db.userDao())
            val categoryRepository = CategoryRepository(db.categoryDao())
            
            val userFactory = UserViewModelFactory(userRepository)
            val categoryFactory = CategoryViewModelFactory(categoryRepository)
            
            userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
            categoryViewModel = ViewModelProvider(this, categoryFactory)[CategoryViewModel::class.java]

            // Set up bottom navigation
            setupBottomNavigation()

            // Set up categories RecyclerView
            setupCategoriesRecyclerView()

            // Load user data
            loadUserData()

            // Set up click listeners
            setupClickListeners()
        } catch (e: Exception) {
            Toast.makeText(this, "Error initializing dashboard: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupCategoriesRecyclerView() {
        categoryAdapter = CategoryAdapter { category ->
            val intent = Intent(this, CategoryDetailsActivity::class.java).apply {
                putExtra("CATEGORY_ID", category.id)
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@DashboardActivity, 2)
            adapter = categoryAdapter
        }

        userId?.let { id ->
            categoryViewModel.getCategoriesByUserId(id).observe(this) { categories ->
                categoryAdapter.submitList(categories)
            }
        }
    }

    private fun loadUserData() {
        try {
            userId?.let { id ->
                userViewModel.getUserById(id)?.let { user ->
                    // Load user's goals and profile data
                    setInitialValues()
                } ?: run {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading user data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        binding.searchButton.setOnClickListener {
            Toast.makeText(this, "Search functionality coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.profileImage.setOnClickListener {
            try {
                val intent = Intent(this, CompleteProfileActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Error opening profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addNewCard.setOnClickListener {
            Toast.makeText(this, "Add new card functionality coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.availableBalanceValue.setOnClickListener {
            Toast.makeText(this, "Card details coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            try {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        // Already on home, do nothing
                        true
                    }
                    R.id.navigation_add_expense -> {
                        val intent = Intent(this, AddExpenseActivity::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                        true
                    }
                    R.id.navigation_transactions -> {
                        val intent = Intent(this, ViewExpensesActivity::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                        true
                    }
                    R.id.navigation_profile -> {
                        val intent = Intent(this, CompleteProfileActivity::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error navigating: ${e.message}", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun setInitialValues() {
        try {
            // Format currency
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

            // Set available balance
            binding.availableBalanceValue.text = currencyFormat.format(250000.0)

            // Set budget values
            binding.budgetValue.text = currencyFormat.format(26000.0)

            // Set savings values
            binding.savingsStart.text = currencyFormat.format(currentSavings)
            binding.savingsGoal.text = currencyFormat.format(savingsGoal)
            
            // Calculate and set progress
            val progress = (currentSavings / savingsGoal * 100).toInt()
            binding.savingsProgress.progress = progress.coerceIn(0, 100)

            // Update current month
            updateCurrentMonth()
        } catch (e: Exception) {
            Toast.makeText(this, "Error setting initial values: ${e.message}", Toast.LENGTH_SHORT).show()
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
        } catch (e: Exception) {
            Toast.makeText(this, "Error updating savings: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateSavingsGoal(newGoal: Double) {
        try {
            savingsGoal = newGoal.coerceAtLeast(0.0)
            updateSavingsUI()
        } catch (e: Exception) {
            Toast.makeText(this, "Error updating savings goal: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSavingsUI() {
        try {
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
            binding.savingsStart.text = currencyFormat.format(currentSavings)
            binding.savingsGoal.text = currencyFormat.format(savingsGoal)
            
            val progress = if (savingsGoal > 0) {
                (currentSavings / savingsGoal * 100).toInt()
            } else {
                0
            }
            binding.savingsProgress.progress = progress.coerceIn(0, 100)
        } catch (e: Exception) {
            Toast.makeText(this, "Error updating savings UI: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}