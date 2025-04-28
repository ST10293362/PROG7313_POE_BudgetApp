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

/**
 * @author ST10293362
 * @author ST10257002
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    // <editor-fold desc="Lifecycle">


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindings()
        setupLayoutUi()

        setupOnClickListeners()

        auth = FirebaseAuth.getInstance()
    }


    // </editor-fold>
    // <editor-fold desc="Functions">


    /**
     * Initiate the login process.
     *
     * @author ST10293362
     * @author ST10257002
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
     */
    private fun completeLogin(
        email: String, password: String
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.loadingIndicator.visibility = ProgressBar.GONE

                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "Login successful!", Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e ->
                binding.loadingIndicator.visibility = ProgressBar.GONE

                Toast.makeText(
                    this, e.message, Toast.LENGTH_LONG
                ).show()
            }
    }


    /**
     * Validates whether the given credentials are correctly formatted.
     *
     * @author ST10293362
     * @author ST10257002
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
     */
    private fun setupBindings() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
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