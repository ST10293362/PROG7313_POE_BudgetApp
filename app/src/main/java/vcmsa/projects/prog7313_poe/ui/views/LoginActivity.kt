package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vcmsa.projects.prog7313_poe.databinding.ActivityLoginBinding

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


/**
 * @author ST10293362
 * @author ST10257002
 * @author ST10326084
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    // firebase
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    //Room
    private lateinit var authService: AuthService



    // <editor-fold desc="Lifecycle">


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindings()
        setupLayoutUi()

        // room
        authService = AuthService(applicationContext)

        setupOnClickListeners()

        // firebase is not currently needed
        // auth = FirebaseAuth.getInstance()
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
        val mail = binding.emailEditText.text.toString().trim()
        val pass = binding.passwordEditText.text.toString().trim()

        if (isValidCredentials(mail, pass)) {
            binding.loadingIndicator.visibility = ProgressBar.VISIBLE

            completeLogin(
                email = mail, password = pass
            )
        }
    }


    /**
     * Query firebase to log the user in with the given credentials.
     *
     * @author ST10293362
     * @author ST10257002
     * @author ST10326084
     */

    private fun completeLogin(email: String, password: String) {
        binding.loadingIndicator.visibility = ProgressBar.VISIBLE

        lifecycleScope.launch {
            val result = authService.signIn(email, password)

            binding.loadingIndicator.visibility = ProgressBar.GONE

            if (result.isSuccess) {
                Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_LONG).show()

                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginActivity, result.exceptionOrNull()?.message ?: "Unknown error", Toast.LENGTH_LONG).show()
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
            this, "Username and password are required.", Toast.LENGTH_SHORT
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