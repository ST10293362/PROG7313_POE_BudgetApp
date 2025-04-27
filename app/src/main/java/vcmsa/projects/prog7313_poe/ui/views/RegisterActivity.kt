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
import com.google.firebase.auth.FirebaseAuth
import vcmsa.projects.prog7313_poe.core.extensions.onTextChanged
import vcmsa.projects.prog7313_poe.databinding.ActivityRegisterBinding

/**
 * @author ST10293362
 * @author ST10257002
 */
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    // <editor-fold desc="Lifecycle">


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindings()
        setupLayoutUi()

        setupOnClickListeners()
        setupOnTextChangedListeners()

        auth = FirebaseAuth.getInstance()
    }

    private fun checkPasswordStrength(password: String) {
        val strength = when {
            password.length < 6 -> {
                binding.passwordStrengthTextView.setTextColor(Color.RED) // Use Color.RED from android.graphics.Color
                "Weak"
            }

            password.length < 10 -> {
                binding.passwordStrengthTextView.setTextColor(Color.YELLOW) // Use Color.YELLOW from android.graphics.Color
                "Medium"
            }

            else -> {
                binding.passwordStrengthTextView.setTextColor(Color.GREEN) // Use Color.GREEN from android.graphics.Color
                "Strong"
            }
        }

        binding.passwordStrengthTextView.text = "Password Strength: $strength"
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
                val firstName = binding.firstNameEditText.text.toString().trim()
                val lastName = binding.lastNameEditText.text.toString().trim()
                val userName = binding.userNameEditText.text.toString().trim()
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()
                val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

                if (firstName.isNotEmpty() && lastName.isNotEmpty() && userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if (!isValidEmail(email)) {
                        Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if (password != confirmPassword) {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return
                    }

                    binding.loadingIndicator.visibility = ProgressBar.VISIBLE

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            binding.loadingIndicator.visibility = ProgressBar.GONE
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this, CompleteProfileActivity::class.java)
                                intent.putExtra("FIRST_NAME", firstName)
                                intent.putExtra("LAST_NAME", lastName)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Registration failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.addOnFailureListener { e ->
                            binding.loadingIndicator.visibility = ProgressBar.GONE
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
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
     * @author ST10257002
     */
    private fun setupOnTextChangedListeners() {
        with (binding) {
            passwordEditText.onTextChanged { input ->
                checkPasswordStrength(input)
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