package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.data.repositories.CategoryRepository
import vcmsa.projects.prog7313_poe.data.repositories.UserRepository
import vcmsa.projects.prog7313_poe.utils.AppDatabase

class DashboardViewModel : ViewModel() {
    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val userRepository: UserRepository
    private val categoryRepository: CategoryRepository

    init {
        val db = AppDatabase.getDatabase()
        userRepository = UserRepository(db.userDao())
        categoryRepository = CategoryRepository(db.categoryDao())
    }

    fun loadUserData() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual user ID
                val userId = "test-user-id"
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    _userData.value = user
                } else {
                    _error.value = "User not found"
                }
            } catch (e: Exception) {
                _error.value = "Error loading user data: ${e.message}"
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual user ID
                val userId = "test-user-id"
                val categories = categoryRepository.getCategoriesByUserId(userId)
                _categories.value = categories
            } catch (e: Exception) {
                _error.value = "Error loading categories: ${e.message}"
            }
        }
    }

    fun updateUserData(userId: String, minGoal: Double, maxGoal: Double, monthlyBudget: Double) {
        viewModelScope.launch {
            try {
                userRepository.updateUserGoalsAndBudget(userId, minGoal, maxGoal, monthlyBudget)
                loadUserData() // Reload user data after update
            } catch (e: Exception) {
                _error.value = "Error updating user data: ${e.message}"
            }
        }
    }
} 