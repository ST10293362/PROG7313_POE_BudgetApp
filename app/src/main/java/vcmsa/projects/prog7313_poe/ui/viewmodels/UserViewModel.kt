// UserViewModel.kt
package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun getUserById(userId: UUID): User? = withContext(Dispatchers.IO) {
        repository.getUserById(userId)
    }

    suspend fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double, goalsSet: Boolean = true) {
        if (minGoal <= 0 || maxGoal <= 0) return
        withContext(Dispatchers.IO) {
            try {
                repository.updateUserGoals(userId, minGoal, maxGoal, goalsSet)
            } catch (e: Exception) {
                // Handle database errors
            }
        }
    }

    suspend fun updateUserGoalsAndBudget(
        userId: UUID,
        minGoal: Double,
        maxGoal: Double,
        monthlyBudget: Double,
        goalsSet: Boolean
    ) {
        withContext(Dispatchers.IO) {
            try {
                repository.updateUserGoalsAndBudget(
                    userId = userId,
                    minGoal = minGoal,
                    maxGoal = maxGoal,
                    monthlyBudget = monthlyBudget,
                    goalsSet = goalsSet
                )
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    suspend fun updateProfileCompletion(userId: UUID, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            repository.updateProfileCompletion(userId, isCompleted)
        }
    }
}