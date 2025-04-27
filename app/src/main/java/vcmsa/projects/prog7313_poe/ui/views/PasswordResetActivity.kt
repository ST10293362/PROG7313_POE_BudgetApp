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
                val email = binding.emailEditText.text.toString().trim()
                if (email.isNotBlank()) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset email sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                }
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