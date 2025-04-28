package vcmsa.projects.prog7313_poe.ui.views

import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.prog7313_poe.R

class PersonalInfoActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var saveButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        // Initialize views
        backButton = findViewById(R.id.backButton)
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        phoneInput = findViewById(R.id.phoneInput)
        saveButton = findViewById(R.id.saveButton)

        // Set up click listeners
        setupClickListeners()

        // Load existing user data
        loadUserData()
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        // TODO: Load user data from SharedPreferences or database
        nameInput.setText("John Doe")
        emailInput.setText("john.doe@example.com")
        phoneInput.setText("+27 123 456 7890")
    }

    private fun saveUserData() {
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        val phone = phoneInput.text.toString()

        // TODO: Save user data to SharedPreferences or database
        // For now, just show a toast
        android.widget.Toast.makeText(this, "Changes saved successfully", android.widget.Toast.LENGTH_SHORT).show()
    }
} 