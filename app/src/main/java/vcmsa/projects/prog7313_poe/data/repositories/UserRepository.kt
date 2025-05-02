package vcmsa.projects.prog7313_poe.data.repositories

import androidx.lifecycle.LiveData
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.data.dao.UserDao

class UserRepository(private val userDao: UserDao) {
    fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    fun updateUserGoalsAndBudget(userId: String, minGoal: Double, maxGoal: Double, monthlyBudget: Double) {
        userDao.updateUserGoalsAndBudget(userId, minGoal, maxGoal, monthlyBudget)
    }
} 