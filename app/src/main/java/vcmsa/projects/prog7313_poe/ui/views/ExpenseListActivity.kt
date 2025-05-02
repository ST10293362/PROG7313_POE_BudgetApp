package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.data.repos.SessionRepository
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory

class ExpenseListActivity : AppCompatActivity() {
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        // Initialize repositories and services
        val db = AppDatabase.getDatabase(applicationContext)
        val expenseRepository = ExpenseRepository(db.expenseDao())
        val categoryRepository = CategoryRepository(db.categoryDao())
        val userRepository = UserRepository(db.userDao())
        val sessionRepository = SessionRepository(db.sessionDao(), db.userDao())
        val authService = AuthService(sessionRepository, userRepository)

        // Initialize ViewModel
        val factory = ExpenseViewModelFactory(expenseRepository, categoryRepository, authService)
        expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        // Setup RecyclerView
        expenseAdapter = ExpenseAdapter { expense ->
            val intent = Intent(this, TransactionDetailsActivity::class.java)
            intent.putExtra("expense_id", expense.id.toString())
            startActivity(intent)
        }

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.expensesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = expenseAdapter

        // Observe expenses
        expenseViewModel.expenses.observe(this) { expenses ->
            expenseAdapter.submitList(expenses)
        }

        // Setup FAB
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddExpense).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }
} 