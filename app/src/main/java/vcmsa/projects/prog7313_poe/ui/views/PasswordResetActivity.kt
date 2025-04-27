package vcmsa.projects.prog7313_poe.ui.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vcmsa.projects.prog7313_poe.databinding.ActivityPasswordResetBinding

class PasswordResetActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPasswordResetBinding
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
     * Initiate the reset process.
     *
     * @author ST10293362
     * @author ST10257002
     */
    private fun tryReset() {
        val email = binding.emailEditText.text.toString().trim()

        if (isValidCredentials(email)) {
            completeReset(
                email = email
            )
        }
    }


    /**
     * Query firebase to send a password reset email to the given address.
     *
     * @author ST10293362
     * @author ST10257002
     */
    private fun completeReset(
        email: String
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this, "Done. Check your inbox for an email from us!", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this, task.exception?.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    /**
     * Validates whether the given credentials are correctly formatted.
     *
     * @author ST10293362
     * @author ST10257002
     */
    private fun isValidCredentials(
        email: String
    ): Boolean {
        if (email.isNotBlank()) {
            return true
        }

        Toast.makeText(
            this, "Email cannot be empty.", Toast.LENGTH_SHORT
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
            binding.resetButton.id -> {
                tryReset()
            }
        }
    }


    /**
     * @author ST10257002
     */
    private fun setupOnClickListeners() {
        binding.resetButton.setOnClickListener(this)
    }


    // </editor-fold>
    // <editor-fold desc="Configuration">


    /**
     * @author ST10257002
     */
    private fun setupBindings() {
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
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