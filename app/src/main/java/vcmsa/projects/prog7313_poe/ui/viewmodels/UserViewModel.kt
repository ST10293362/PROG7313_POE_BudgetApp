package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun getUserById(userId: UUID): LiveData<User> = liveData(Dispatchers.IO) {
        val user = repository.getUserById(userId)
        emit(user ?: User(
            id = UUID.randomUUID(),
            username = "",
            password = "",
            passwordSalt = "",
            name = "",
            surname = "",
            dateOfBirth = null,
            cellNumber = "",
            emailAddress = "",
            minGoal = 0.0,
            maxGoal = 0.0,
            monthlyBudget = 0.0,
            currentBudget = 0.0,
            budgetLastReset = System.currentTimeMillis(),
            goalsSet = false
        ))
    }

    fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double, goalsSet: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (minGoal <= 0 || maxGoal <= 0) {
                    return@launch
                }
                repository.updateUserGoals(userId, minGoal, maxGoal, goalsSet)
            } catch (e: Exception) {
                // Handle database errors
            }
        }
    }

    fun updateUserGoalsAndBudget(
        userId: UUID,
        minGoal: Double,
        maxGoal: Double,
        monthlyBudget: Double,
        goalsSet: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun updateProfileCompletion(userId: UUID, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfileCompletion(userId, isCompleted)
        }
    }
}