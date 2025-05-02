package vcmsa.projects.prog7313_poe.ui.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.prog7313_poe.databinding.ActivityPasswordResetBinding
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class PasswordResetActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPasswordResetBinding
    private lateinit var auth: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindings()
        setupLayoutUi()

        // Initialize AuthService
        val db = AppDatabase.getDatabase(applicationContext)
        auth = AuthService(
            applicationContext,
            db.sessionDao(),
            db.userDao()
        )

        setupOnClickListeners()
    }

    private fun tryReset() {
        val email = binding.emailEditText.text.toString().trim()

        if (isValidCredentials(email)) {
            completeReset(email)
        }
    }

    private fun completeReset(email: String) {
        lifecycleScope.launch {
            try {
                val result = auth.resetPassword(email)
                if (result.isSuccess) {
                    Toast.makeText(
                        this@PasswordResetActivity, 
                        "Done. Check your inbox for an email from us!", 
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@PasswordResetActivity, 
                        result.exceptionOrNull()?.message ?: "Failed to send reset email", 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@PasswordResetActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidCredentials(email: String): Boolean {
        if (email.isNotBlank()) {
            return true
        }

        Toast.makeText(
            this, "Email cannot be empty.", Toast.LENGTH_SHORT
        ).show()

        return false
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.resetButton.id -> tryReset()
            binding.backToLoginTextView.id -> {
                finish()
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.resetButton.setOnClickListener(this)
        binding.backToLoginTextView.setOnClickListener(this)
    }

    private fun setupBindings() {
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupLayoutUi() {
        enableEdgeToEdge()
    }
}