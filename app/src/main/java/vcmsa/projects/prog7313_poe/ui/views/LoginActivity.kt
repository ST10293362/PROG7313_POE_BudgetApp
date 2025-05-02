package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.prog7313_poe.databinding.ActivityLoginBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.services.AuthService


/**
 * @author ST10293362
 * @author ST10257002
 * @author ST10326084
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: AuthService
    

    // <editor-fold desc="Lifecycle">


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindings()
        setupLayoutUi()

        auth = AuthService(applicationContext)

        setupOnClickListeners()
    }


    // </editor-fold>
    // <editor-fold desc="Functions">


    /**
     * Initiate the login process.
     *
     * @author ST10293362
     * @author ST10257002
     * @author ST10326084
     */
    private fun tryLogin() {
        val identifier = binding.userNameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (isValidCredentials(identifier, password)) {
            binding.loadingIndicator.visibility = ProgressBar.VISIBLE
            completeLogin(identifier, password)
        }
    }


    /**
     * Query firebase to log the user in with the given credentials.
     *
     * @author ST10293362
     * @author ST10257002
     * @author ST10326084
     */
    private fun completeLogin(
        identifier: String, password: String
    ) {
        binding.loadingIndicator.visibility = ProgressBar.VISIBLE

        lifecycleScope.launch {
            try {
                val result = auth.signIn(identifier, password)

                if (result.isSuccess) {
                    val user = result.getOrNull()
                    if (user != null) {
                        if (!user.profileCompleted) {
                            // Navigate to complete profile if not completed
                            val intent = Intent(this@LoginActivity, CompleteProfileActivity::class.java).apply {
                                putExtra("USER_ID", user.id)
                                putExtra("FIRST_NAME", user.name)
                                putExtra("LAST_NAME", user.surname)
                            }
                            startActivity(intent)
                        } else {
                            // Navigate to dashboard if profile is completed
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        Toast.makeText(
                            this@LoginActivity, "Login successful!", Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity, result.exceptionOrNull()?.message ?: "Unknown error", Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.loadingIndicator.visibility = ProgressBar.GONE
            }
        }
    }



    /**
     * Validates whether the given credentials are correctly formatted.
     *
     * @author ST10293362
     * @author ST10257002
     * @author ST10326084
     */
    private fun isValidCredentials(
        email: String, password: String
    ): Boolean {
        if (email.isNotBlank() && password.isNotBlank()) {
            return true
        }

        Toast.makeText(
            this, "Email and password are required.", Toast.LENGTH_SHORT
        ).show()
        return false
    }


    // </editor-fold>
    // <editor-fold desc="Event Handler">


    /**
     * Catch and handle on-click events from the view.
     *
     * @author ST10293362
     * @author ST10257002
     * @author ST10326084
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            binding.registerTextView.id -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

            binding.forgotPasswordTextView.id -> {
                startActivity(Intent(this, PasswordResetActivity::class.java))
            }

            binding.bypassLogin.id -> {
                startActivity(Intent(this, DashboardActivity::class.java))
            }

            binding.loginButton.id -> {
                tryLogin()
            }
        }
    }


    /**
     * @author ST10257002
     * @author ST10326084
     */
    private fun setupOnClickListeners() {
        binding.loginButton.setOnClickListener(this)
        binding.forgotPasswordTextView.setOnClickListener(this)
        binding.registerTextView.setOnClickListener(this)
        binding.bypassLogin.setOnClickListener(this)
    }


    // </editor-fold>
    // <editor-fold desc="Configuration">


    /**
     * @author ST10257002
     * @author ST10326084
     */
    private fun setupBindings() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
    }


    /**
     * @author ST10257002
     * @author ST10326084
     */
    private fun setupLayoutUi() {
        enableEdgeToEdge()
        setContentView(binding.root)
    }

    // </editor-fold>

}