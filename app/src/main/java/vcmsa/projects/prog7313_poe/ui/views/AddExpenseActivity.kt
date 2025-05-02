package vcmsa.projects.prog7313_poe.ui.views

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import java.time.Instant
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.databinding.ActivityAddExpenseBinding
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import vcmsa.projects.prog7313_poe.core.models.Expense
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private var currentPhotoPath: String = ""
    private val imageUris = mutableListOf<Uri>()
    private val photoNames = mutableListOf<String>()
    private lateinit var descriptionEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var addCategoryButton: Button
    private lateinit var addPhotoButton: Button
    private lateinit var capturePhotoButton: Button
    private lateinit var submitButton: Button
    private lateinit var photoListView: ListView
    private lateinit var photoAdapter: PhotoAdapter
    private val categories = mutableListOf(
        "Food & Dining",
        "Transportation",
        "Shopping",
        "Entertainment",
        "Bills & Utilities",
        "Health & Medical",
        "Travel",
        "Education",
        "Personal Care",
        "Other"
    )

    private val REQUEST_CODE_PERMISSIONS = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityAddExpenseBinding.inflate(layoutInflater)
            setContentView(binding.root)

            initializeViewModels()
            setupImageLaunchers()
            setupClickListeners()
            setupCategorySpinner()
            setupTimePickers()
            observeViewModel()
        } catch (e: Exception) {
            showToast("Error initializing activity: ${e.message}")
            finish()
        }
    }

    private fun initializeViewModels() {
        val db = AppDatabase.getDatabase(applicationContext)
        val expenseRepository = ExpenseRepository(db.expenseDao())
        val categoryRepository = CategoryRepository(db.categoryDao())
        val authService = AuthService(applicationContext)
        
        expenseViewModel = ViewModelProvider(
            this,
            ExpenseViewModelFactory(expenseRepository, categoryRepository, authService)
        )[ExpenseViewModel::class.java]
    }

    private fun observeViewModel() {
        expenseViewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }

        expenseViewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.submitButton.isEnabled = !loading
        }

        expenseViewModel.expenseAdded.observe(this) { success ->
            if (success) {
                showToast("Expense added successfully")
                navigateToDashboard()
            }
        }
    }

    private fun setupImageLaunchers() {
        getImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    addImage(uri)
                }
            }
        }

        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                File(currentPhotoPath).let { file ->
                    if (file.exists()) {
                        addImage(Uri.fromFile(file))
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            addPhotoButton.setOnClickListener { openGallery() }
            capturePhotoButton.setOnClickListener { takePicture() }
            submitButton.setOnClickListener { submitExpense() }
            addCategoryButton.setOnClickListener { showAddCategoryDialog() }
        }
    }

    private fun openGallery() {
        if (checkPermissions()) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImageLauncher.launch(intent)
        }
    }

    private fun takePicture() {
        if (checkPermissions()) {
            val photoFile = createImageFile()
            photoFile?.let { file ->
                currentPhotoPath = file.absolutePath
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    file
                )
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
                takePictureLauncher.launch(intent)
            }
        }
    }

    private fun addImage(uri: Uri) {
        imageUris.add(uri)
        photoNames.add("Photo ${photoNames.size + 1}")
        updatePhotoList()
    }

    private fun updatePhotoList() {
        binding.photoListView.adapter = PhotoAdapter()
    }

    private fun submitExpense() {
        val amount = binding.amountEditText.text.toString().toDoubleOrNull()
        val description = binding.descriptionEditText.text.toString()
        val startTime = binding.startTimeEditText.text.toString()
        val endTime = binding.endTimeEditText.text.toString()
        val category = binding.categorySpinner.selectedItem.toString()

        if (amount == null || amount <= 0) {
            showToast("Please enter a valid amount")
            return
        }

        if (description.isBlank()) {
            showToast("Please enter a description")
            return
        }

        lifecycleScope.launch {
            try {
                val expense = Expense(
                    amount = amount,
                    description = description,
                    startDate = parseDateTime(startTime),
                    endDate = if (endTime.isNotBlank()) parseDateTime(endTime) else null,
                    categoryId = UUID.randomUUID(), // TODO: Get actual category ID
                    userId = UUID.randomUUID(), // TODO: Get actual user ID
                    accountId = UUID.randomUUID() // TODO: Get actual account ID
                )
                expenseViewModel.addExpense(expense)
            } catch (e: Exception) {
                showToast("Error submitting expense: ${e.message}")
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE_PERMISSIONS)
            return false
        }
        return true
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun parseDateTime(dateTime: String): Instant {
        // TODO: Implement proper date/time parsing
        return Instant.now()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inner class PhotoAdapter : BaseAdapter() {
        override fun getCount(): Int = photoNames.size
        override fun getItem(position: Int): Any = photoNames[position]
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(this@AddExpenseActivity)
                .inflate(R.layout.item_photo, parent, false)
            view.findViewById<TextView>(R.id.photoNameTextView).text = photoNames[position]
            view.findViewById<ImageButton>(R.id.removePhotoButton).setOnClickListener {
                removePhoto(position)
            }
            return view
        }
    }

    private fun removePhoto(position: Int) {
        if (position in photoNames.indices) {
            photoNames.removeAt(position)
            imageUris.removeAt(position)
            updatePhotoList()
        }
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    private fun setupTimePickers() {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding.startTimeEditText.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                binding.startTimeEditText.setText(timeFormat.format(calendar.time))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        binding.endTimeEditText.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                binding.endTimeEditText.setText(timeFormat.format(calendar.time))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }
    }

    private fun showAddCategoryDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Add New Category")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val newCategory = input.text.toString().trim()
                if (newCategory.isNotEmpty()) {
                    categories.add(newCategory)
                    (binding.categorySpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
    }
}