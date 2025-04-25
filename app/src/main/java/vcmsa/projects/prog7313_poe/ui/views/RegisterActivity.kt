package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color // Import the correct Color class
import com.google.firebase.auth.FirebaseAuth
import vcmsa.projects.prog7313_poe.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var passwordStrengthTextView: TextView
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        userNameEditText = findViewById(R.id.userNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        passwordStrengthTextView = findViewById(R.id.passwordStrengthTextView)
        registerButton = findViewById(R.id.registerButton)

        auth = FirebaseAuth.getInstance()

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkPasswordStrength(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val userName = userNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (!isValidEmail(email)) {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password != confirmPassword) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                loadingIndicator.visibility = ProgressBar.VISIBLE

                auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    loadingIndicator.visibility = ProgressBar.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CompleteProfileActivity::class.java)
                        intent.putExtra("FIRST_NAME", firstName)
                        intent.putExtra("LAST_NAME", lastName)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                    .addOnFailureListener { e ->
                        loadingIndicator.visibility = ProgressBar.GONE
                        Toast.makeText (this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPasswordStrength(password: String) {
        val strength = when {
            password.length < 6 -> {
                passwordStrengthTextView.setTextColor(Color.RED) // Use Color.RED from android.graphics.Color
                "Weak"
            }
            password.length < 10 -> {
                passwordStrengthTextView.setTextColor(Color.YELLOW) // Use Color.YELLOW from android.graphics.Color
                "Medium"
            }
            else -> {
                passwordStrengthTextView.setTextColor(Color.GREEN) // Use Color.GREEN from android.graphics.Color
                "Strong"
            }
        }
        passwordStrengthTextView.text = "Password Strength: $strength"
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}