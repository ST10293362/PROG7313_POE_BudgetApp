package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.databinding.ActivityLoginBinding
import vcmsa.projects.prog7313_poe.core.services.AuthService
import java.util.UUID

/**
 * Handles user login and redirection to DashboardActivity on success.
 *
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBindings()
        setupLayoutUi()
        auth = AuthService(applicationContext)
        setupOnClickListeners()
    }

    private fun tryLogin() {
        val username = binding.userNameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (isValidCredentials(username, password)) {
            binding.loadingIndicator.visibility = ProgressBar.VISIBLE
            completeLogin(username, password)
        }
    }

    private fun completeLogin(username: String, password: String) {
        binding.loadingIndicator.visibility = ProgressBar.VISIBLE

        lifecycleScope.launch {
            val result = auth.signIn(username, password)
            binding.loadingIndicator.visibility = ProgressBar.GONE

            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra("USER_ID", user.id)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Login error: User is null")
                }
            } else {
                showToast(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        return if (username.isNotBlank() && password.isNotBlank()) {
            true
        } else {
            showToast("Username and password are required.")
            false
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.registerTextView.id -> startActivity(Intent(this, RegisterActivity::class.java))
            binding.forgotPasswordTextView.id -> startActivity(Intent(this, PasswordResetActivity::class.java))
            binding.bypassLogin.id -> {
                val guestId = UUID.randomUUID()
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("USER_ID", guestId)
                intent.putExtra("IS_GUEST", true)
                startActivity(intent)
            }
            binding.loginButton.id -> tryLogin()
        }
    }

    private fun setupOnClickListeners() {
        binding.loginButton.setOnClickListener(this)
        binding.forgotPasswordTextView.setOnClickListener(this)
        binding.registerTextView.setOnClickListener(this)
        binding.bypassLogin.setOnClickListener(this)
    }

    private fun setupBindings() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
    }

    private fun setupLayoutUi() {
        enableEdgeToEdge()
        setContentView(binding.root)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
