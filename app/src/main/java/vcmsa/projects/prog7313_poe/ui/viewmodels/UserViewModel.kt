// UserViewModel.kt
package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.services.AuthService
import vcmsa.projects.prog7313_poe.core.utils.SecurityUtils
import java.util.UUID

class UserViewModel(
    private val repository: UserRepository,
    private val authService: AuthService
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    suspend fun loadCurrentUser() {
        withContext(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                val result = authService.getCurrentUser()
                result.onSuccess { user ->
                    _user.postValue(user)
                }.onFailure { e ->
                    _error.postValue(e.message)
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    suspend fun registerUser(email: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                val (hashedPassword, salt) = SecurityUtils.hashPassword(password)
                val username = email.split("@")[0] // Use email prefix as username
                val result = authService.signUp(
                    firstName = username,
                    finalName = "",
                    username = username,
                    password = hashedPassword,
                    email = email
                )
                result.onSuccess { user ->
                    _user.postValue(user)
                }.onFailure { e ->
                    _error.postValue(e.message)
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    suspend fun updateCurrentUser(user: User?) {
        user?.let {
            withContext(Dispatchers.IO) {
                try {
                    _loading.postValue(true)
                    repository.updateUser(it)
                    _user.postValue(it)
                } catch (e: Exception) {
                    _error.postValue(e.message)
                } finally {
                    _loading.postValue(false)
                }
            }
        }
    }

    suspend fun getUserById(userId: UUID): User? = withContext(Dispatchers.IO) {
        try {
            _loading.postValue(true)
            val result = repository.getUserById(userId)
            _user.postValue(result)
            result
        } catch (e: Exception) {
            handleError(e)
            null
        } finally {
            _loading.postValue(false)
        }
    }

    suspend fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double, goalsSet: Boolean = true) {
        if (minGoal <= 0 || maxGoal <= 0) {
            _error.postValue("Goals must be positive values")
            return
        }
        withContext(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                repository.updateUserGoals(userId, minGoal, maxGoal, goalsSet)
                getUserById(userId) // Refresh user data
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _loading.postValue(false)
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
                _loading.postValue(true)
                repository.updateUserGoalsAndBudget(
                    userId = userId,
                    minGoal = minGoal,
                    maxGoal = maxGoal,
                    monthlyBudget = monthlyBudget,
                    goalsSet = goalsSet
                )
                getUserById(userId) // Refresh user data
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    suspend fun updateProfileCompletion(userId: UUID, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                repository.updateProfileCompletion(userId, isCompleted)
                getUserById(userId) // Refresh user data
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    private fun handleError(e: Exception) {
        _error.postValue(e.message ?: "An unknown error occurred")
    }
}