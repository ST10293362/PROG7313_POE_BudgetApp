package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.databinding.ActivityRegisterBinding
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModelFactory

/**
 * @author ST10293362
 * @author ST10257002
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityRegisterBinding.inflate(layoutInflater)
            setContentView(binding.root)

            initializeViewModels()
            setupClickListeners()
            observeViewModel()
        } catch (e: Exception) {
            showToast("Error initializing registration: ${e.message}")
            finish()
        }
    }

    private fun initializeViewModels() {
        val db = AppDatabase.getDatabase(applicationContext)
        val userRepository = UserRepository(db.userDao())
        val authService = AuthService(applicationContext)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]
    }

    private fun observeViewModel() {
        userViewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }

        userViewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.registerButton.isEnabled = !loading
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            registerButton.setOnClickListener { registerUser() }
        }
    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showToast("Please fill in all fields")
            return
        }

        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return
        }

        lifecycleScope.launch {
            try {
                userViewModel.registerUser(email, password)
                navigateToLogin()
            } catch (e: Exception) {
                showToast("Error registering user: ${e.message}")
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}