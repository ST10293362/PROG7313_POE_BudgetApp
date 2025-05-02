package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.databinding.ActivityViewExpensesBinding
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import java.text.NumberFormat
import java.util.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import java.time.Instant
import java.time.format.DateTimeFormatter

class ViewExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewExpensesBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityViewExpensesBinding.inflate(layoutInflater)
            setContentView(binding.root)

            initializeViewModels()
            setupRecyclerView()
            observeViewModel()
            loadExpenses()
        } catch (e: Exception) {
            showToast("Error initializing activity: ${e.message}")
            finish()
        }
    }

    private fun initializeViewModels() {
        val db = AppDatabase.getDatabase(applicationContext)
        val expenseRepository = ExpenseRepository(db.expenseDao())
        expenseViewModel = ViewModelProvider(
            this,
            ExpenseViewModelFactory(expenseRepository)
        )[ExpenseViewModel::class.java]
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter { expense ->
            navigateToExpenseDetails(expense.id)
        }

        binding.expensesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewExpensesActivity)
            adapter = expenseAdapter
        }
    }

    private fun observeViewModel() {
        expenseViewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }


        expenseViewModel.expenses.observe(this) { expenses ->
            expenseAdapter.submitList(expenses)
        }
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            try {
                expenseViewModel.loadExpenses()
            } catch (e: Exception) {
                showToast("Error loading expenses: ${e.message}")
            }
        }
    }

    private fun navigateToExpenseDetails(expenseId: UUID) {
        val intent = Intent(this, TransactionDetailsActivity::class.java).apply {
            putExtra("EXPENSE_ID", expenseId.toString())
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    class ExpenseAdapter(
        private val onExpenseClick: (Expense) -> Unit
    ) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
        private var expenses: List<Expense> = emptyList()
        private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
        private val dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy")

        fun submitList(newExpenses: List<Expense>) {
            expenses = newExpenses
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction, parent, false)
            return ExpenseViewHolder(view, onExpenseClick)
        }

        override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
            holder.bind(expenses[position])
        }

        override fun getItemCount(): Int = expenses.size

        class ExpenseViewHolder(
            itemView: View,
            private val onExpenseClick: (Expense) -> Unit
        ) : RecyclerView.ViewHolder(itemView) {
            private val descriptionTextView: TextView =
                itemView.findViewById(R.id.descriptionTextView)
            private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
            private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
            private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)

            fun bind(expense: Expense) {
                descriptionTextView.text = expense.description
                amountTextView.text = formatAmount(expense.amount)
                dateTextView.text = formatDate(expense.startDate)
                categoryTextView.text = expense.categoryId?.toString() ?: "Uncategorized"

                itemView.setOnClickListener { onExpenseClick(expense) }
            }

            private fun formatAmount(amount: Double?): String {
                return if (amount != null) {
                    NumberFormat.getCurrencyInstance(Locale("en", "ZA")).format(amount)
                } else {
                    NumberFormat.getCurrencyInstance(Locale("en", "ZA")).format(0.0)
                }
            }

            private fun formatDate(date: Instant?): String {
                return if (date != null) {
                    DateTimeFormatter.ofPattern("MMM dd, yyyy").format(date)
                } else {
                    "No date"
                }
            }
        }
    }
}