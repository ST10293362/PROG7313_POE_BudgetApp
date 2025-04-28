package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import vcmsa.projects.prog7313_poe.R
import java.text.NumberFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var currentSavingsText: TextView
    private lateinit var savingsGoalText: TextView
    private lateinit var savingsProgress: LinearProgressIndicator
    private lateinit var progressText: TextView
    private lateinit var currentMonthText: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    // Sample data - replace with actual data from your database
    private var currentSavings = 5000.0
    private var savingsGoal = 10000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        initializeViews()

        // Set up bottom navigation
        setupBottomNavigation()

        // Update the UI with current data
        updateSavingsUI()
        updateCurrentMonth()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Set initial progress
        savingsProgress.progress = 0
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already on home, do nothing
                    true
                }
                R.id.navigation_add_expense -> {
                    startActivity(Intent(this, AddExpenseActivity::class.java))
                    true
                }
                R.id.navigation_transactions -> {
                    startActivity(Intent(this, ViewExpensesActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, CompleteProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateSavingsUI() {
        // Format currency
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
        currentSavingsText.text = currencyFormat.format(currentSavings)
        savingsGoalText.text = currencyFormat.format(savingsGoal)

        // Calculate progress percentage
        val progress = (currentSavings / savingsGoal * 100).toInt()
        savingsProgress.progress = progress

        // Update progress text
        progressText.text = "$progress% of goal reached"
    }

    private fun updateCurrentMonth() {
        val calendar = Calendar.getInstance()
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        currentMonthText.text = "$month $year"
    }

    // This method can be called when savings are updated
    fun updateSavings(newSavings: Double) {
        currentSavings = newSavings
        updateSavingsUI()
    }

    // This method can be called when savings goal is updated
    fun updateSavingsGoal(newGoal: Double) {
        savingsGoal = newGoal
        updateSavingsUI()
    }
}