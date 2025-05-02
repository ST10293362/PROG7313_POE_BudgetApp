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
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.databinding.ActivityTransactionDetailsBinding
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.core.data.repos.SessionRepository
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import android.widget.ProgressBar
import android.widget.Button
import android.util.Log
import android.app.AlertDialog
import android.app.Dialog
import android.widget.ImageButton
import java.time.Instant

class TransactionDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionDetailsBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private var expenseId: UUID? = null
    private var expense: Expense? = null
    private lateinit var descriptionTextView: TextView
    private lateinit var amountTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var photosRecyclerView: RecyclerView
    private lateinit var photosAdapter: PhotosAdapter
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var authService: AuthService
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingOverlay: View
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button
    private lateinit var deleteButton: Button
    private var photoUris: List<Uri> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityTransactionDetailsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            expenseId = UUID.fromString(intent.getStringExtra("EXPENSE_ID"))
            initializeViewModels()
            initializeViews()
            setupClickListeners()
            observeViewModel()
            loadExpenseDetails()
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
        
        // Initialize repositories and auth service
        categoryRepository = CategoryRepository(db.categoryDao())
        sessionRepository = SessionRepository(db.sessionDao(), db.userDao())
        userRepository = UserRepository(db.userDao())
        authService = AuthService(applicationContext)
    }

    private fun observeViewModel() {
        expenseViewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }

        expenseViewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            deleteButton.setOnClickListener { showDeleteConfirmationDialog() }
        }
    }

    private fun loadExpenseDetails() {
        expenseId?.let { id ->
            lifecycleScope.launch {
                try {
                    expenseViewModel.loadExpenses()
                } catch (e: Exception) {
                    showToast("Error loading expense details: ${e.message}")
                }
            }
        } ?: run {
            showToast("Invalid expense ID")
            finish()
        }
    }

    private fun updateUI(expense: Expense) {
        binding.apply {
            descriptionTextView.text = expense.description
            amountTextView.text = formatAmount(expense.amount)
            dateTextView.text = formatDate(expense.startDate)
            categoryTextView.text = expense.categoryId?.toString() ?: "Uncategorized"
            
            // Update photos if available
            expense.photos?.let { photoList: List<String> ->
                if (photoList.isNotEmpty()) {
                    photoUris = photoList.map { Uri.parse(it) }
                    setupRecyclerView()
                }
            }
        }
    }

    private fun deleteExpense() {
        expense?.let { expenseToDelete ->
            lifecycleScope.launch {
                try {
                    expenseViewModel.deleteExpense(expenseToDelete)
                    showToast("Expense deleted successfully")
                    finish()
                } catch (e: Exception) {
                    showToast("Error deleting expense: ${e.message}")
                }
            }
        }
    }

    private fun editExpense() {
        expense?.let { expenseToEdit ->
            val intent = Intent(this, AddExpenseActivity::class.java).apply {
                putExtra("EXPENSE_ID", expenseToEdit.id.toString())
            }
            startActivity(intent)
        }
    }

    private fun formatAmount(amount: Double?): String {
        return if (amount != null) {
            String.format("R%.2f", amount)
        } else {
            "R0.00"
        }
    }

    private fun formatDate(date: Instant?): String {
        return if (date != null) {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
            dateFormat.format(Date(date.toEpochMilli()))
        } else {
            "No date"
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initializeViews() {
        descriptionTextView = binding.descriptionTextView
        amountTextView = binding.amountTextView
        dateTextView = binding.dateTextView
        categoryTextView = binding.categoryTextView
        photosRecyclerView = binding.photosRecyclerView
        progressBar = binding.progressBar
        loadingOverlay = binding.loadingOverlay
        errorTextView = binding.errorTextView
        retryButton = binding.retryButton
        deleteButton = binding.deleteButton
    }

    private fun setupRecyclerView() {
        photosAdapter = PhotosAdapter()
        photosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photosRecyclerView.adapter = photosAdapter
        photosAdapter.submitList(photoUris)
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        loadingOverlay.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        loadingOverlay.visibility = View.GONE
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE
        retryButton.setOnClickListener {
            expenseId?.let { loadExpenseDetails() }
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Delete") { _, _ ->
                deleteExpense()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPhotoPreview(photoUri: Uri) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_photo_preview)
        
        val imageView = dialog.findViewById<ImageView>(R.id.previewImageView)
        val closeButton = dialog.findViewById<ImageButton>(R.id.closeButton)
        
        Glide.with(this)
            .load(photoUri)
            .into(imageView)
        
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }

    private fun handlePhotoError(error: Exception) {
        Log.e("PhotoHandling", "Error handling photo", error)
        Toast.makeText(this, "Error handling photo: ${error.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up any resources
        photosAdapter.submitList(emptyList())
        Glide.with(this).clear(photosRecyclerView)
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