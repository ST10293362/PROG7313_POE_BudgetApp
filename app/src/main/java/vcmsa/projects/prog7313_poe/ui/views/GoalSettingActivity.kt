package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.R
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.databinding.ActivityGoalSettingBinding
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModel
import vcmsa.projects.prog7313_poe.ui.viewmodels.UserViewModelFactory
import vcmsa.projects.prog7313_poe.core.services.AuthService

class GoalSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGoalSettingBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up services and ViewModel
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(db.userDao())
        authService = AuthService(
            applicationContext,
            db.sessionDao(),
            db.userDao()
        )
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        // Load existing values
        loadExistingValues()

        binding.saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveGoals()
            }
        }
    }

    private fun loadExistingValues() {
        lifecycleScope.launch {
            try {
                val user = userViewModel.getCurrentUser()
                user?.let {
                    binding.minGoalEditText.setText(it.minGoal?.toString() ?: "0.0")
                    binding.maxGoalEditText.setText(it.maxGoal?.toString() ?: "0.0")
                    binding.budgetEditText.setText(it.monthlyBudget?.toString() ?: "0.0")
                }
            } catch (e: Exception) {
                Toast.makeText(this@GoalSettingActivity, "Error loading user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveGoals() {
        try {
            val minGoal = binding.minGoalEditText.text.toString().toDoubleOrNull()
            val maxGoal = binding.maxGoalEditText.text.toString().toDoubleOrNull()
            val monthlyBudget = binding.budgetEditText.text.toString().toDoubleOrNull()

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

            // Update goals, budget, and goals_set status
            userViewModel.updateUserGoalsAndBudget(
                minGoal = minGoal,
                maxGoal = maxGoal,
                monthlyBudget = monthlyBudget,
                goalsSet = true
            )

            // Navigate to DashboardActivity
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving goals: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}