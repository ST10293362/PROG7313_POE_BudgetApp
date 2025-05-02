package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.prog7313_poe.core.extensions.onTextChanged
import vcmsa.projects.prog7313_poe.databinding.ActivityRegisterBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.models.CredentialStrength
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.core.utils.ValidationUtils
import vcmsa.projects.prog7313_poe.R

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: AuthService

    // <editor-fold desc="Lifecycle">

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindings()
        setupLayoutUi()

        auth = AuthService(applicationContext)

        setupOnClickListeners()
        setupOnTextChangedListeners()
    }

    // </editor-fold>
    // <editor-fold desc="Functions">

    /**
     * Initiate the registration process.
     *
     * @author ST10293362
     * @author ST10257002
     */
    private fun tryRegister() {
        val firstName = binding.firstNameEditText.text.toString().trim()
        val lastName = binding.lastNameEditText.text.toString().trim()
        val userName = binding.userNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        // Validate inputs using ValidationUtils
        val validationErrors = ValidationUtils.validateInputs(
            "name" to firstName,
            "name" to lastName,
            "email" to email,
            "password" to password
        )

        if (validationErrors.isNotEmpty()) {
            validationErrors.forEach { (field, error) ->
                when (field) {
                    "name" -> {
                        if (firstName.isBlank()) {
                            binding.firstNameLayout.error = getString(R.string.error_required)
                        }
                        if (lastName.isBlank()) {
                            binding.lastNameLayout.error = getString(R.string.error_required)
                        }
                    }
                    "email" -> binding.emailLayout.error = error
                    "password" -> binding.passwordLayout.error = error
                }
            }
            return
        }

        if (userName.isBlank()) {
            binding.usernameLayout.error = getString(R.string.error_required)
            return
        }

        if (!isMatchingPasswords(password, confirmPassword)) {
            binding.confirmPasswordLayout.error = getString(R.string.error_passwords_dont_match)
            return
        }

        // Clear any previous errors
        clearErrors()

        // Show loading indicator and proceed with registration
        binding.loadingIndicator.visibility = ProgressBar.VISIBLE
        completeRegister(firstName, lastName, userName, email, password)
    }

    /**
     * Query firebase to register the user with the given credentials.
     *
     * @author ST10293362
     * @author ST10257002
     */
    private fun completeRegister(
        firstName: String,
        lastName: String,
        userName: String,
        email: String,
        password: String
    ) {
        lifecycleScope.launch {
            try {
                val result = auth.signUp(firstName, lastName, userName, password, email)

                if (result.isSuccess) {
                    showSuccess(getString(R.string.success_registration))
                    navigateToCompleteProfile(firstName, lastName)
                } else {
                    showError(result.exceptionOrNull()?.message ?: getString(R.string.error_generic))
                }
            } catch (e: Exception) {
                showError(e.message ?: getString(R.string.error_database))
            } finally {
                binding.loadingIndicator.visibility = ProgressBar.GONE
            }
        }
    }

    private fun clearErrors() {
        binding.firstNameLayout.error = null
        binding.lastNameLayout.error = null
        binding.usernameLayout.error = null
        binding.emailLayout.error = null
        binding.passwordLayout.error = null
        binding.confirmPasswordLayout.error = null
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToCompleteProfile(firstName: String, lastName: String) {
        val intent = Intent(this, CompleteProfileActivity::class.java).apply {
            putExtra("FIRST_NAME", firstName)
            putExtra("LAST_NAME", lastName)
        }
        startActivity(intent)
        finish()
    }

    private fun isMatchingPasswords(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    /**
     * Updates the UI to reflect the strength of the password.
     *
     * @see [CredentialStrength]
     * @author ST10293362
     * @author ST10257002
     */
    private fun updateCredentialStrengthUi(password: String) {
        binding.passwordStrengthView.updatePasswordStrength(password)
    }

    // </editor-fold>
    // <editor-fold desc="Event Handler">

    /**
     * Catch and handle on-click events from the view.
     *
     * @author ST10293362
     * @author ST10257002
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            binding.registerButton.id -> tryRegister()
        }
    }

    /**
     * @author ST10257002
     */
    private fun setupOnClickListeners() {
        binding.registerButton.setOnClickListener(this)
    }

    /**
     * Catch and handle on-text-changed events from the view.
     *
     * @author ST10257002
     */
    private fun setupOnTextChangedListeners() {
        binding.passwordEditText.onTextChanged { text ->
            updateCredentialStrengthUi(text.toString())
        }
    }

    // </editor-fold>
    // <editor-fold desc="Configuration">

    /**
     * @author ST10257002
     */
    private fun setupBindings() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
    }

    /**
     * @author ST10257002
     */
    private fun setupLayoutUi() {
        enableEdgeToEdge()
        setContentView(binding.root)
    }

    // </editor-fold>

}