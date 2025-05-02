package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModelFactory
import vcmsa.projects.prog7313_poe.core.services.AuthService

class CompleteProfileActivity : AppCompatActivity() {
    private lateinit var accountEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var cardNumberEditText: EditText
    private lateinit var cardTypeSpinner: Spinner
    private lateinit var cvcEditText: EditText
    private lateinit var expiryEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var profileImageView: ImageView
    private lateinit var addProfilePicButton: Button
    private lateinit var userViewModel: UserViewModel
    private lateinit var authService: AuthService

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        // Initialize services and ViewModel
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(db.userDao())
        authService = AuthService(
            applicationContext,
            db.sessionDao(),
            db.userDao()
        )
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        accountEditText = findViewById(R.id.accountEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        cardNumberEditText = findViewById(R.id.cardNumberEditText)
        cardTypeSpinner = findViewById(R.id.cardTypeSpinner)
        cvcEditText = findViewById(R.id.cvcEditText)
        expiryEditText = findViewById(R.id.expiryEditText)
        saveButton = findViewById(R.id.saveButton)
        profileImageView = findViewById(R.id.profileImageView)
        addProfilePicButton = findViewById(R.id.addProfilePicButton)

        // Set up the spinner with card types
        val cardTypes = arrayOf("Savings Account", "Cheque", "Debit")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cardTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cardTypeSpinner.adapter = adapter

        // Set up click listeners
        saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveProfile()
            }
        }

        addProfilePicButton.setOnClickListener {
            openImagePicker()
        }
    }

    private suspend fun saveProfile() {
        try {
            // Validate inputs
            if (accountEditText.text.isEmpty() || phoneNumberEditText.text.isEmpty() ||
                cardNumberEditText.text.isEmpty() || cvcEditText.text.isEmpty() ||
                expiryEditText.text.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return
            }

            // Update user profile completion status
            userViewModel.updateProfileCompletion(true)

            // Navigate to GoalSettingActivity
            startActivity(Intent(this, GoalSettingActivity::class.java))
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving profile: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            imageUri?.let {
                Glide.with(this)
                    .load(it)
                    .transform(CircleCrop())
                    .into(profileImageView)
            }
        }
    }
}