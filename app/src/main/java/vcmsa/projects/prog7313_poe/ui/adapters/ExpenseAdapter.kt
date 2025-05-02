package vcmsa.projects.prog7313_poe.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.models.Expense
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseAdapter(
    private val onExpenseClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view, onExpenseClick)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(
        itemView: View,
        private val onExpenseClick: (Expense) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.expenseTitle)
        private val amountTextView: TextView = itemView.findViewById(R.id.expenseAmount)
        private val dateTextView: TextView = itemView.findViewById(R.id.expenseDate)
        private val categoryTextView: TextView = itemView.findViewById(R.id.expenseCategory)

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        fun bind(expense: Expense) {
            titleTextView.text = expense.title
            amountTextView.text = String.format("$%.2f", expense.amount)
            dateTextView.text = dateFormat.format(expense.date)
            categoryTextView.text = expense.categoryName

            itemView.setOnClickListener { onExpenseClick(expense) }
        }
    }

    private class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
} 