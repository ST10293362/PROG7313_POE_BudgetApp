package vcmsa.projects.prog7313_poe.ui.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author ST10326084
 */
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.ExpenseViewModelFactory
import vcmsa.projects.prog7313_poe.core.models.Expense

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var addPhotoButton: Button
    private lateinit var capturePhotoButton: Button
    private lateinit var submitButton: Button
    private lateinit var imageView: ImageView
    private var imageUris: MutableList<Uri> = mutableListOf()
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var currentPhotoPath: String

    private val REQUEST_CODE_PERMISSIONS = 1001

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

        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        categoryEditText = findViewById(R.id.categoryEditText)
        addPhotoButton = findViewById(R.id.addPhotoButton)
        capturePhotoButton = findViewById(R.id.capturePhotoButton)
        submitButton = findViewById(R.id.submitButton)
        imageView = findViewById(R.id.imageView)

        val calendar = Calendar.getInstance()
        dateEditText.setText(String.format("%02d/%02d/%04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)))
        timeEditText.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)))

        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.let {
                    val clipData = it.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            val imageUri: Uri = clipData.getItemAt(i).uri
                            imageUris.add(imageUri)
                        }
                    } else {
                        val imageUri: Uri? = it.data
                        imageUri?.let { uri -> imageUris.add(uri) }
                    }
                    updateImageView()
                }
            }
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageFile = File(currentPhotoPath)
                val imageUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", imageFile)
                imageUris.add(imageUri)
                updateImageView()
            }
        }

        dateEditText.setOnClickListener {
            showDatePicker()
        }

        timeEditText.setOnClickListener {
            showTimePicker(true)
        }

        addPhotoButton.setOnClickListener {
            openGallery()
        }

        capturePhotoButton.setOnClickListener {
            checkPermissions()
        }

        submitButton.setOnClickListener {
            submitExpense()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
        } else {
            capturePhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto()
            } else {
                Toast.makeText(this, "Camera permission is required to capture photos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            dateEditText.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day).show()
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            timeEditText.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, true).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            intent.setType("image/*")
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
                val photoURI: Uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
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

    private fun updateImageView() {
        if (imageUris.isNotEmpty()) {
            imageView.setImageURI(imageUris.last())
        }
    }
    
    /**
     * @author ST10326084
     */
    private fun submitExpense() {
        val description = descriptionEditText.text.toString().trim()
        val category = categoryEditText.text.toString().trim()
        val amount = 0.0 // You can replace this with a field later
        val vendor = "Unknown Vendor" // Replace if you add vendor input
        val date = Date()

        // TODO: Replace with actual user/account/category IDs
        val userId = UUID.randomUUID()
        val accountId = UUID.randomUUID()

        val expense = Expense(
            detail = description,
            vendor = vendor,
            amount = amount,
            dateOfExpense = date,
            idAuthor = userId,
            idAccount = accountId,
            idCategory = null
        )

        // Save to DB using ViewModel
        val db = AppDatabase.getDatabase(applicationContext)
        val viewModel = ViewModelProvider(
            this,
            ExpenseViewModelFactory(ExpenseRepository(db.expenseDao()))
        )[ExpenseViewModel::class.java]

        viewModel.addExpense(expense)

        Toast.makeText(this, "Expense saved to database!", Toast.LENGTH_SHORT).show()
    }

}
