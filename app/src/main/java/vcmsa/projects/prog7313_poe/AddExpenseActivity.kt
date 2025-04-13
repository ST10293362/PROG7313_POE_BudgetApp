package vcmsa.projects.prog7313_poe

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var dateEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var addPhotoButton: Button
    private lateinit var submitButton: Button
    private lateinit var imageView: ImageView
    private var imageUris: MutableList<Uri> = mutableListOf()

    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        dateEditText = findViewById(R.id.dateEditText)
        startTimeEditText = findViewById(R.id.startTimeEditText)
        endTimeEditText = findViewById(R.id.endTimeEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        categoryEditText = findViewById(R.id.categoryEditText)
        addPhotoButton = findViewById(R.id.addPhotoButton)
        submitButton = findViewById(R.id.submitButton)
        imageView = findViewById(R.id.imageView)

        // Automatically set the current date and time
        val calendar = Calendar.getInstance()
        dateEditText.setText(String.format("%02d/%02d/%04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)))
        startTimeEditText.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)))
        endTimeEditText.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)))

        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.let {
                    val clipData = it.clipData
                    if (clipData != null) {
                        // Multiple images selected
                        for (i in 0 until clipData.itemCount) {
                            val imageUri: Uri = clipData.getItemAt(i).uri
                            imageUris.add(imageUri)
                        }
                    } else {
                        // Single image selected
                        val imageUri: Uri? = it.data
                        imageUri?.let { uri -> imageUris.add(uri) }
                    }
                    updateImageView()
                }
            }
        }

        dateEditText.setOnClickListener {
            showDatePicker()
        }

        startTimeEditText.setOnClickListener {
            showTimePicker(true)
        }

        endTimeEditText.setOnClickListener {
            showTimePicker(false)
        }

        addPhotoButton.setOnClickListener {
            openGallery()
        }

        submitButton.setOnClickListener {
            submitExpense()
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
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            if (isStartTime) {
                startTimeEditText.setText(time)
            } else {
                endTimeEditText.setText(time)
            }
        }, hour, minute , true).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Allow multiple images to be selected
        }
        getImageLauncher.launch(intent)
    }

    private fun updateImageView() {
        if (imageUris.isNotEmpty()) {
            imageView.setImageURI(imageUris[0]) // Display the first image for simplicity
            imageView.visibility = ImageView.VISIBLE
        } else {
            imageView.visibility = ImageView.GONE
        }
    }

    private fun submitExpense() {
        val date = dateEditText.text.toString()
        val startTime = startTimeEditText.text.toString()
        val endTime = endTimeEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val category = categoryEditText.text.toString()

        if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Here you can add code to save the expense to your database, including handling multiple images

        Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}