package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var profileButton: Button
    private lateinit var categoriesButton: Button
    private lateinit var viewExpensesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        profileButton = findViewById(R.id.profileButton)
        viewExpensesButton = findViewById(R.id.statsButton)

        profileButton.setOnClickListener {
            startActivity(Intent(this, CompleteProfileActivity::class.java))
        }

        viewExpensesButton.setOnClickListener {
            startActivity(Intent(this, ViewExpensesActivity::class.java))
        }
    }
}