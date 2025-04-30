package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import vcmsa.projects.prog7313_poe.core.models.User

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModelFactory
import java.util.UUID

class GoalSettingActivity : AppCompatActivity() {
    private lateinit var minGoalEditText: EditText
    private lateinit var maxGoalEditText: EditText
    private lateinit var budgetEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var userViewModel: UserViewModel
    private var userId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_setting)

        userId = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("USER_ID", UUID::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("USER_ID") as? UUID
        }

        if (userId == null) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize views
        minGoalEditText = findViewById(R.id.minGoalEditText)
        maxGoalEditText = findViewById(R.id.maxGoalEditText)
        budgetEditText = findViewById(R.id.budgetEditText)
        saveButton = findViewById(R.id.saveButton)

        // Set up ViewModel
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(db.userDao())
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        // Load existing values
        loadExistingValues()

        saveButton.setOnClickListener {
            saveGoals()
        }
    }

    private fun loadExistingValues() {
        userId?.let { id ->
            userViewModel.getUserById(id).observe(this) { user ->
                minGoalEditText.setText(user.minGoal?.toString() ?: "0.0")
                maxGoalEditText.setText(user.maxGoal?.toString() ?: "0.0")
                budgetEditText.setText(user.monthlyBudget.toString())
            }
        }
    }


    private fun saveGoals() {
        try {
            val minGoal = minGoalEditText.text.toString().toDoubleOrNull()
            val maxGoal = maxGoalEditText.text.toString().toDoubleOrNull()
            val monthlyBudget = budgetEditText.text.toString().toDoubleOrNull()

            if (minGoal == null || maxGoal == null || monthlyBudget == null) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
                return
            }

            if (minGoal >= maxGoal) {
                Toast.makeText(this, "Minimum goal must be less than maximum goal", Toast.LENGTH_SHORT).show()
                return
            }

            if (monthlyBudget <= 0) {
                Toast.makeText(this, "Monthly budget must be greater than zero", Toast.LENGTH_SHORT).show()
                return
            }

            // Update goals, budget and goals_set status
            userViewModel.updateUserGoalsAndBudget(
                userId = userId!!,
                minGoal = minGoal,
                maxGoal = maxGoal,
                monthlyBudget = monthlyBudget,
                goalsSet = true
            )

            // Navigate to DashboardActivity
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving goals: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}