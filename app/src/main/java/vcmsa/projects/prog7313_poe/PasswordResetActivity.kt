package vcmsa.projects.prog7313_poe

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class PasswordResetActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        auth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.emailEditText)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        val resetButton = findViewById<Button>(R.id.resetButton)

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString()
            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        loadingIndicator.visibility = ProgressBar.VISIBLE
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                loadingIndicator.visibility = ProgressBar.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}