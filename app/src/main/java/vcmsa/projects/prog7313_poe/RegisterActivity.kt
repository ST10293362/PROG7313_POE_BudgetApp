package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            registerUser (email, password)
        }
    }

    private fun registerUser (email: String, password: String) {
        loadingIndicator.visibility = ProgressBar.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            loadingIndicator.visibility = ProgressBar.GONE
            if (task.isSuccessful) {
                sendVerificationEmail()
            } else {
                handleRegistrationError(task.exception)
            }
        }
    }

    private fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleRegistrationError(exception: Exception?) {
        val errorMessage = when {
            exception?.message?.contains("email") == true -> "Invalid email format."
            exception?.message?.contains("password") == true -> "Password must be at least 6 characters."
            else -> "Registration failed. Please try again."
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}