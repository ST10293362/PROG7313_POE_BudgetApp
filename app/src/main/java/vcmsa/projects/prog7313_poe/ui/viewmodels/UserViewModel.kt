package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import java.util.UUID

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    
    fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double) {
        viewModelScope.launch {
            val user = repository.getUserById(userId)
            user?.let {
                it.minGoal = minGoal
                it.maxGoal = maxGoal
                repository.updateUser(it)
            }
        }
    }
} 