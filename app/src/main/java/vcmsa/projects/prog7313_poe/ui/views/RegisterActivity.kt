package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.prog7313_poe.core.extensions.onTextChanged
import vcmsa.projects.prog7313_poe.databinding.ActivityRegisterBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.services.AuthService


/**
 * @author ST10293362
 * @author ST10257002
 */
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

        // VALIDATION

        var valid = true

        if (
            !isValidInput(
                firstName, lastName, userName, email, password, confirmPassword
            )
        ) {
            Toast.makeText(
                this, "Fields cannot be empty or whitespace", Toast.LENGTH_SHORT
            ).show()

            valid = false
        }
        
        if (!isValidEmail(email)) {
            Toast.makeText(
                this, "Invalid email format", Toast.LENGTH_SHORT
            ).show()

            valid = false
        }

        if (
            !isMatchingPasswords(password = password, confirmPassword = confirmPassword)
        ) {
            Toast.makeText(
                this, "The password must match the confirmation field", Toast.LENGTH_SHORT
            ).show()

            valid = false
        }

        // NAVIGATION

        if (valid) {
            binding.loadingIndicator.visibility = ProgressBar.VISIBLE
            completeRegister(
                email = email, password = password, firstName = firstName, lastName = lastName
            )
        }
    }


    /**
     * Query firebase to register the user with the given credentials.
     *
     * @author ST10293362
     * @author ST10257002
     */
    private fun completeRegister(
        email: String, password: String, firstName: String, lastName: String
    ) {
        binding.loadingIndicator.visibility = ProgressBar.VISIBLE

        lifecycleScope.launch {
            val userName = binding.userNameEditText.text.toString().trim()

            val result = auth.signUp(firstName, lastName, userName, password, email)

            binding.loadingIndicator.visibility = ProgressBar.GONE

            if (result.isSuccess) {
                Toast.makeText(
                    this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@RegisterActivity, CompleteProfileActivity::class.java)
                intent.putExtra("FIRST_NAME", firstName)
                intent.putExtra("LAST_NAME", lastName)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@RegisterActivity, result.exceptionOrNull()?.message ?: "Unknown error", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    /**
     * Validate whether the given credentials are correctly formatted.
     * 
     * @author ST10257002
     */
    private fun isValidInput(
        vararg fields: String
    ): Boolean {
        for (field in fields) {
            if (field.isBlank()) {
                return false
            }
        }

        return true
    }


    /**
     * Validate whether the email is correctly formatted.
     * 
     * @author ST10293362
     * @author ST10257002
     */
    private fun isValidEmail(
        email: String
    ): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    /**
     * Validate whether the password matches the confirmation field.
     * 
     * @author ST10293362
     * @author ST10257002
     */
    private fun isMatchingPasswords(
        password: String, confirmPassword: String
    ): Boolean {
        return (password == confirmPassword)
    }


    /**
     * Calculate the strength of the password.
     * 
     * @author ST10293362
     * @author ST10257002
     */
    private fun getPasswordStrength(
        password: String
    ): String {
        return when {
            password.length < 6 -> {
                binding.passwordStrengthTextView.setTextColor(Color.RED)
                "Weak"
            }

            password.length < 10 -> {
                binding.passwordStrengthTextView.setTextColor(Color.YELLOW)
                "Medium"
            }

            else -> {
                binding.passwordStrengthTextView.setTextColor(Color.GREEN)
                "Strong"
            }
        }
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
            binding.registerButton.id -> {
                tryRegister()
            }
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
        with(binding) {
            passwordEditText.onTextChanged { input ->
                binding.passwordStrengthTextView.text = getPasswordStrength(input)
            }
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