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
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.databinding.ActivityDashboardBinding
import vcmsa.projects.prog7313_poe.ui.adapters.CategoryAdapter
import vcmsa.projects.prog7313_poe.ui.viewmodels.CategoryViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.CategoryViewModelFactory
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModelFactory
import java.text.NumberFormat
import java.util.*
import kotlin.jvm.java

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding.root)

            initializeViewModels()
            setupBottomNavigation()
            setupCategoriesRecyclerView()
            loadUserData()
            setupClickListeners()
            observeViewModels()
        } catch (e: Exception) {
            showToast("Error initializing dashboard: ${e.message}")
            finish()
        }
    }

    private fun initializeViewModels() {
        val db = AppDatabase.getDatabase(applicationContext)
        val userRepository = UserRepository(db.userDao())
        val categoryRepository = CategoryRepository(db.categoryDao())
        authService = AuthService(applicationContext)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository, authService)
        )[UserViewModel::class.java]

        categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(categoryRepository)
        )[CategoryViewModel::class.java]
    }

    private fun observeViewModels() {
        userViewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }

        userViewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        userViewModel.user.observe(this) { user ->
            updateUI(user)
        }

        categoryViewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }

        categoryViewModel.categories.observe(this) { categories ->
            categoryAdapter.submitList(categories)
        }
    }

    private fun setupCategoriesRecyclerView() {
        categoryAdapter = CategoryAdapter { category ->
            startActivity(Intent(this, CategoryDetailsActivity::class.java).apply {
                putExtra("CATEGORY_ID", category.id.toString())
            })
        }
        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@DashboardActivity, 2)
            adapter = categoryAdapter
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                userViewModel.loadCurrentUser()
                categoryViewModel.loadCategories()
            } catch (e: Exception) {
                showToast("Error loading data: ${e.message}")
            }
        }
    }

    private fun updateUI(user: User?) {
        user?.let {
            binding.apply {
                welcomeTextView.text = "Welcome, ${it.name}"
                monthlyBudgetTextView.text = currencyFormat.format(it.monthlyBudget ?: 0.0)
                currentSavingsTextView.text = currencyFormat.format(it.currentBudget ?: 0.0)
                savingsGoalTextView.text = currencyFormat.format(it.maxGoal ?: 0.0)
                
                // Calculate and update savings progress
                val progress = if (it.maxGoal != null && it.maxGoal!! > 0) {
                    ((it.currentBudget ?: 0.0) / it.maxGoal!! * 100).toInt()
                } else {
                    0
                }
                savingsProgress.progress = progress.coerceIn(0, 100)
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {

            profileImage.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, ProfileActivity::class.java))
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            try {
                when (item.itemId) {
                    R.id.navigation_home -> true
                    R.id.navigation_add_expense -> {
                        startActivity(Intent(this, AddExpenseActivity::class.java))
                        true
                    }
                    R.id.navigation_transactions -> {
                        startActivity(Intent(this, ViewExpensesActivity::class.java))
                        true
                    }
                    R.id.navigation_profile -> {
                        startActivity(Intent(this, ProfileActivity::class.java))
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}