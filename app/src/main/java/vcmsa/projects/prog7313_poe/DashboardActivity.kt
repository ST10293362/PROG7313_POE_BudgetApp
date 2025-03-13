package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var welcomeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        logoutButton = findViewById(R.id.logoutButton)
        welcomeTextView = findViewById(R.id.welcomeTextView)


        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "User "
        welcomeTextView.text = "Welcome, $userEmail!"

        logoutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}