package vcmsa.projects.prog7313_poe.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class ViewExpensesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_expenses)

        recyclerView = findViewById(R.id.transactionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter()
        recyclerView.adapter = adapter

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = ExpenseRepository(db.expenseDao())
        val factory = ExpenseViewModelFactory(repository)
        val expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        expenseViewModel.expenses.observe(this) { expenses ->
            adapter.submitList(expenses)
        }
    }

    class TransactionAdapter : ListAdapter<Expense, TransactionAdapter.TransactionViewHolder>(ExpenseDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction, parent, false)
            return TransactionViewHolder(view)
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val icon: ImageView = itemView.findViewById(R.id.transactionIcon)
            private val title: TextView = itemView.findViewById(R.id.transactionTitle)
            private val amount: TextView = itemView.findViewById(R.id.transactionAmount)
            private val time: TextView = itemView.findViewById(R.id.transactionTime)
            private val card: View = itemView

            fun bind(expense: Expense) {
                icon.setImageResource(R.drawable.ic_transactions)
                title.text = expense.description
                amount.text = "R" + String.format("%.2f", expense.amount)

                val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val date = Date.from(expense.startDate)
                time.text = dateFormat.format(date)

                if (expense.description.contains("Deposit", ignoreCase = true)) {
                    card.setBackgroundColor(itemView.context.getColor(R.color.deposit_green))
                } else {
                    card.setBackgroundColor(itemView.context.getColor(android.R.color.white))
                }
            }
        }
    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean =
            oldItem == newItem
    }
}