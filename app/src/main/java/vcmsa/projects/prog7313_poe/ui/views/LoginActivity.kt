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
            
            binding.loginButton.id -> {
                val mail = binding.emailEditText.text.toString().trim()
                val pass = binding.passwordEditText.text.toString().trim()

                if (mail.isNotEmpty() && pass.isNotEmpty()) {
                    binding.loadingIndicator.visibility = ProgressBar.VISIBLE

                    auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener { task ->
                        binding.loadingIndicator.visibility = ProgressBar.GONE
                        if (task.isSuccessful) {
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener { e ->
                        binding.loadingIndicator.visibility = ProgressBar.GONE
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
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