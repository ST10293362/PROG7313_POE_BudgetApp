package vcmsa.projects.prog7313_poe.ui.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import java.time.Instant
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import vcmsa.projects.prog7313_poe.core.models.Expense


class AddExpenseActivity : AppCompatActivity() {
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
    private var imageUris: MutableList<Uri> = mutableListOf()
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var currentPhotoPath: String
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

    inner class PhotoAdapter : BaseAdapter() {
        private val photoNames = mutableListOf<String>()

        fun addPhoto(name: String) {
            photoNames.add(name)
            notifyDataSetChanged()
        }

        fun removePhoto(position: Int) {
            photoNames.removeAt(position)
            imageUris.removeAt(position)
            notifyDataSetChanged()
        }

        override fun getCount(): Int = photoNames.size

        override fun getItem(position: Int): Any = photoNames[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(this@AddExpenseActivity)
                .inflate(R.layout.item_photo, parent, false)

            val photoNameTextView = view.findViewById<TextView>(R.id.photoNameTextView)
            val removeButton = view.findViewById<ImageButton>(R.id.removePhotoButton)

            photoNameTextView.text = photoNames[position]
            removeButton.setOnClickListener {
                removePhoto(position)
            }

            return view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        /**
         * @author ST10326084
         */
        // Set up ViewModel and Repository
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = ExpenseRepository(db.expenseDao())
        val factory = ExpenseViewModelFactory(repository)
        val expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        // Initialize views
        initializeViews()

        // Set up category spinner
        setupCategorySpinner()

        // Set up time pickers
        setupTimePickers()

        // Set up image handling
        setupImageHandling()

        // Set up click listeners
        setupClickListeners()

        // Set up photo list
        photoAdapter = PhotoAdapter()
        photoListView.adapter = photoAdapter
    }

    private fun initializeViews() {
        descriptionEditText = findViewById(R.id.descriptionEditText)
        amountEditText = findViewById(R.id.amountEditText)
        startTimeEditText = findViewById(R.id.startTimeEditText)
        endTimeEditText = findViewById(R.id.endTimeEditText)
        categorySpinner = findViewById(R.id.categorySpinner)
        addCategoryButton = findViewById(R.id.addCategoryButton)
        addPhotoButton = findViewById(R.id.addPhotoButton)
        capturePhotoButton = findViewById(R.id.capturePhotoButton)
        submitButton = findViewById(R.id.submitButton)
        photoListView = findViewById(R.id.photoListView)
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun setupTimePickers() {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        startTimeEditText.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                startTimeEditText.setText(timeFormat.format(calendar.time))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        endTimeEditText.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                endTimeEditText.setText(timeFormat.format(calendar.time))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }
    }

    private fun setupImageHandling() {
        getImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    data?.let {
                        val clipData = it.clipData
                        if (clipData != null) {
                            for (i in 0 until clipData.itemCount) {
                                val imageUri: Uri = clipData.getItemAt(i).uri
                                imageUris.add(imageUri)
                                photoAdapter.addPhoto("Photo ${imageUris.size}")
                            }
                        } else {
                            val imageUri: Uri? = it.data
                            imageUri?.let { uri ->
                                imageUris.add(uri)
                                photoAdapter.addPhoto("Photo ${imageUris.size}")
                            }
                        }
                    }
                }
            }

        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageFile = File(currentPhotoPath)
                    val imageUri =
                        FileProvider.getUriForFile(this, "${packageName}.fileprovider", imageFile)
                    imageUris.add(imageUri)
                    photoAdapter.addPhoto("Photo ${imageUris.size}")
                }
            }
    }

    private fun setupClickListeners() {
        addCategoryButton.setOnClickListener {
            showAddCategoryDialog()
        }

        addPhotoButton.setOnClickListener {
            openGallery()
        }

        capturePhotoButton.setOnClickListener {
            checkPermissions()
        }

        submitButton.setOnClickListener {
            submitExpense()

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)

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
                    (categorySpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            capturePhoto()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required to capture photos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        getImageLauncher.launch(intent)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile: File? = createImageFile()
            if (photoFile != null) {
                currentPhotoPath = photoFile.absolutePath
                val photoURI: Uri =
                    FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePictureLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Error creating file for photo", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
            Log.d("AddExpenseActivity", "Image file created at: ${file.absolutePath}")
            file
        } catch (e: Exception) {
            Log.e("AddExpenseActivity", "Error creating file: ${e.message}")
            null
        }
    }

    /**
     * @author ST10326084
     */
    private fun submitExpense() {
        try {
            val description = descriptionEditText.text.toString().trim()
            val amount = amountEditText.text.toString().toDoubleOrNull()
            val category = categorySpinner.selectedItem.toString()
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()

            if (description.isEmpty() || amount == null || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return
            }

            // TODO: Replace with actual user/account IDs from session
            val userId = UUID.randomUUID()
            val accountId = UUID.randomUUID()
            val categoryId = UUID.randomUUID() // or null if category is not required

            val now = Instant.now()
            val expense = Expense(
                description = description,
                amount = amount,
                startDate = now, // Using current instant for start
                endDate = now,   // Using current instant for end
                userId = userId,
                accountId = accountId,
                categoryId = categoryId
            )

            // Save to DB using ViewModel
            val db = AppDatabase.getDatabase(applicationContext)
            val viewModel = ViewModelProvider(
                this,
                ExpenseViewModelFactory(ExpenseRepository(db.expenseDao()))
            )[ExpenseViewModel::class.java]

            viewModel.addExpense(expense)

            Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show()




        } catch (e: Exception) {
            Toast.makeText(this, "Error saving expense: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }
}
