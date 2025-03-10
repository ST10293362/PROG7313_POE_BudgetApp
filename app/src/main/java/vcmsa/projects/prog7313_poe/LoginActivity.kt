package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.registerTextView)

        // Set up the login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            loginUser (email, password)
        }


        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser (email: String, password: String) {

        loadingIndicator.visibility = ProgressBar.VISIBLE


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                loadingIndicator.visibility = ProgressBar.GONE

                if (task.isSuccessful) {

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {

                    handleLoginError(task.exception)
                }
            }
    }

    private fun handleLoginError(exception: Exception?) {
        val errorMessage = when {
            exception?.message?.contains("no user record") == true -> "No account found with this email."
            exception?.message?.contains("wrong password") == true -> "Incorrect password. Please try again."
            exception?.message?.contains("invalid email") == true -> "Invalid email format."
            else -> "Login failed. Please try again."
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}