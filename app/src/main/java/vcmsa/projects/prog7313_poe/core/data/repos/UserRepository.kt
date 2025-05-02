package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.data.dao.UserDao
import java.util.UUID

class UserRepository(private val userDao: UserDao) {
    suspend fun getById(id: UUID): User? = userDao.getById(id)

    suspend fun getByEmail(email: String): User? = userDao.getByEmail(email)

    suspend fun getByUsername(username: String): User? = userDao.getByUsername(username)

    suspend fun insert(user: User) = userDao.insert(user)

    suspend fun update(user: User) = userDao.update(user)

    suspend fun delete(user: User) = userDao.delete(user)

    suspend fun deleteById(id: UUID) = userDao.deleteById(id)

    suspend fun deleteAll() = userDao.deleteAll()

    suspend fun getAll() = userDao.getAllUsers()

    suspend fun exists(userId: UUID) = userDao.exists(userId.toString())

    suspend fun existsByEmail(email: String): Boolean = userDao.existsByEmail(email)

    suspend fun existsByUsername(username: String): Boolean = userDao.existsByUsername(username)

    suspend fun updateMonthlyBudget(userId: UUID, monthlyBudget: Double) = userDao.updateMonthlyBudget(userId, monthlyBudget)

    suspend fun updateCurrentBudget(userId: UUID, currentBudget: Double) = userDao.updateCurrentBudget(userId, currentBudget)

    suspend fun updateGoals(userId: UUID, minGoal: Double, maxGoal: Double) = userDao.updateGoals(userId, minGoal, maxGoal)

    suspend fun updateProfileCompleted(userId: UUID, completed: Boolean) = userDao.updateProfileCompleted(userId, completed)

    suspend fun resetMonthlyBudget(userId: UUID, monthlyBudget: Double) = userDao.resetMonthlyBudget(userId, monthlyBudget)
}