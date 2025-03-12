package vcmsa.projects.prog7313_poe

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PasswordResetActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        emailEditText = findViewById(R.id.emailEditText)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        val resetButton = findViewById<Button>(R.id.resetButton)

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                loadingIndicator.visibility = ProgressBar.VISIBLE
                loadingIndicator.postDelayed({
                    loadingIndicator.visibility = ProgressBar.GONE
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                }, 2000)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}