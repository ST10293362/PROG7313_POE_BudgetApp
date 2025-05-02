package vcmsa.projects.prog7313_poe.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import vcmsa.projects.prog7313_poe.core.services.AuthService
import java.util.UUID
import kotlinx.coroutines.launch

class CategoryDetailsActivity : AppCompatActivity() {
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseAdapter: ExpenseAdapter
    private var categoryId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)

        categoryId = intent.getSerializableExtra("CATEGORY_ID") as? UUID
        if (categoryId == null) {
            Toast.makeText(this, "Category ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = AppDatabase.getDatabase(applicationContext)
        val expenseRepository = ExpenseRepository(db.expenseDao())
        val categoryRepository = CategoryRepository(db.categoryDao())
        val authService = AuthService(
            applicationContext,
            db.sessionDao(),
            db.userDao()
        )

        expenseViewModel = ViewModelProvider(
            this,
            ExpenseViewModelFactory(expenseRepository, categoryRepository, authService)
        )[ExpenseViewModel::class.java]

        setupRecyclerView()
        loadExpenses()
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter { expense ->
            // Handle expense item click
        }

        findViewById<RecyclerView>(R.id.expensesRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@CategoryDetailsActivity)
            adapter = expenseAdapter
        }
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            try {
                val expenses = expenseViewModel.getExpensesByCategoryId(categoryId!!)
                expenseAdapter.submitList(expenses)
            } catch (e: Exception) {
                Toast.makeText(this@CategoryDetailsActivity, "Error loading expenses: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}