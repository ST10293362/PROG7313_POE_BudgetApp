package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.data.dao.ExpenseDao

class TransactionDetailsActivity : AppCompatActivity() {
    private lateinit var descriptionTextView: TextView
    private lateinit var amountTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var photosRecyclerView: RecyclerView
    private lateinit var photosAdapter: PhotosAdapter
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)

        // Initialize views
        descriptionTextView = findViewById(R.id.descriptionTextView)
        amountTextView = findViewById(R.id.amountTextView)
        dateTextView = findViewById(R.id.dateTextView)
        categoryTextView = findViewById(R.id.categoryTextView)
        photosRecyclerView = findViewById(R.id.photosRecyclerView)

        // Set up photos recycler view
        photosAdapter = PhotosAdapter()
        photosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photosRecyclerView.adapter = photosAdapter

        // Initialize repositories and services
        val db = AppDatabase.getDatabase(applicationContext)
        expenseRepository = ExpenseRepository(db.expenseDao())
        categoryRepository = CategoryRepository(db.categoryDao())
        authService = AuthService(db.sessionRepository, db.userRepository)

        // Get expense ID from intent
        val expenseId = intent.getStringExtra("expense_id")?.let { UUID.fromString(it) }
        if (expenseId != null) {
            loadExpenseDetails(expenseId)
        } else {
            Toast.makeText(this, "Error: Expense ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadExpenseDetails(expenseId: UUID) {
        lifecycleScope.launch {
            try {
                val expense = expenseRepository.getById(expenseId)
                if (expense != null) {
                    val category = categoryRepository.getById(expense.categoryId)
                    displayExpenseDetails(expense)
                } else {
                    Toast.makeText(this@TransactionDetailsActivity, "Expense not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TransactionDetailsActivity, "Error loading expense: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun displayExpenseDetails(expense: Expense) {
        descriptionTextView.text = expense.description
        amountTextView.text = "R" + String.format("%.2f", expense.amount)
        
        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        val date = Date.from(expense.startDate)
        dateTextView.text = dateFormat.format(date)
        
        // Load category name
        lifecycleScope.launch {
            try {
                val category = categoryRepository.getById(expense.categoryId)
                categoryTextView.text = category?.name ?: "Unknown Category"
            } catch (e: Exception) {
                categoryTextView.text = "Unknown Category"
            }
        }

        // Load photos
        photosAdapter.submitList(expense.photos)
    }

    private fun deleteExpense(expenseId: UUID) {
        lifecycleScope.launch {
            val expense = expenseRepository.getById(expenseId)
            if (expense != null) {
                // Update category total before deleting
                categoryRepository.updateTotalAmount(expense.categoryId, -expense.amount)
                expenseRepository.deleteById(expenseId)
                finish()
            }
        }
    }

    class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {
        private var photos: List<Uri> = emptyList()

        fun submitList(newPhotos: List<Uri>) {
            photos = newPhotos
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_detail, parent, false)
            return PhotoViewHolder(view)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            holder.bind(photos[position])
        }

        override fun getItemCount(): Int = photos.size

        class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)

            fun bind(photoUri: Uri) {
                Glide.with(itemView.context)
                    .load(photoUri)
                    .into(photoImageView)
            }
        }
    }
} 