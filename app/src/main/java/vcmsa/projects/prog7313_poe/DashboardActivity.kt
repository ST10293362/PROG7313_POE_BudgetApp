package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    private lateinit var addExpenseButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        addExpenseButton = findViewById(R.id.addExpenseButton)


        addExpenseButton.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

    }
}