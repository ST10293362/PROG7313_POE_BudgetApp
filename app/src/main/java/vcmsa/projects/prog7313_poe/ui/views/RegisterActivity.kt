package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color // Import the correct Color class
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.databinding.ActivityLoginBinding
import vcmsa.projects.prog7313_poe.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth


    // <editor-fold desc="Lifecycle">
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupBindings()
        
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkPasswordStrength(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.registerButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val userName = binding.userNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (!isValidEmail(email)) {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password != confirmPassword) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                binding.loadingIndicator.visibility = ProgressBar.VISIBLE

                auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    binding.loadingIndicator.visibility = ProgressBar.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CompleteProfileActivity::class.java)
                        intent.putExtra("FIRST_NAME", firstName)
                        intent.putExtra("LAST_NAME", lastName)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                    .addOnFailureListener { e ->
                        binding.loadingIndicator.visibility = ProgressBar.GONE
                        Toast.makeText (this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
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
    // <editor-fold desc="Configuration">


    /**
     * @author ST10257002
     */
    private fun setupBindings() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
    }
    

    // </editor-fold>
}