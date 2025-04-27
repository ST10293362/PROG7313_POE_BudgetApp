package vcmsa.projects.prog7313_poe.ui.views

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.databinding.ActivityPasswordResetBinding

class PasswordResetActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPasswordResetBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupBindings()
        
        setContentView(binding.root)
        
        auth = FirebaseAuth.getInstance()

        binding.resetButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset email sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    
    // <editor-fold desc="Configuration">


    /**
     * @author ST10257002
     */
    private fun setupBindings() {
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
    }
    

    // </editor-fold>
    
}