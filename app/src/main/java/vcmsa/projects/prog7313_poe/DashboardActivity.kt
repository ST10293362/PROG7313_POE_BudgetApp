package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var welcomeTextView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        welcomeTextView = findViewById(R.id.welcomeTextView)
        logoutButton = findViewById(R.id.logoutButton) // Ensure this ID exists in your layout

        // Get the current user and display their email
        val user = auth.currentUser
        welcomeTextView.text = "Welcome, ${user?.email}"

        // Set up logout button click listener
        logoutButton.setOnClickListener {
            auth.signOut() // Sign out the user
            startActivity(Intent(this, LoginActivity::class.java)) // Redirect to LoginActivity
            finish() // Finish the current activity
        }
    }
}