package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

class UserRepository(
    private val dao: UserDao
) {
    suspend fun createUser(instance: User) {
        dao.insert(instance)
    }

    suspend fun updateUser(instance: User) {
        instance.touch()
        dao.update(instance)
    }

    suspend fun deleteUser(instance: User) {
        dao.delete(instance)
    }

    suspend fun deleteUserById(id: UUID) {
        dao.delete(id)
    }

    suspend fun getAllUsers(): List<User> {
        return dao.fetchAll()
    }

    suspend fun getUserById(id: UUID): User? {
        return dao.fetchOne(id)
    }

    suspend fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double, goalsSet: Boolean) {
        val user = getUserById(userId) ?: return
        user.apply {
            this.minGoal = minGoal
            this.maxGoal = maxGoal
            this.goalsSet = goalsSet
        }
        updateUser(user)
    }

    suspend fun updateUserGoalsAndBudget(
        userId: UUID,
        minGoal: Double,
        maxGoal: Double,
        monthlyBudget: Double,
        goalsSet: Boolean
    ) {
        val user = getUserById(userId) ?: return
        user.apply {
            this.minGoal = minGoal
            this.maxGoal = maxGoal
            this.monthlyBudget = monthlyBudget
            this.currentBudget = monthlyBudget
            this.goalsSet = goalsSet
            this.budgetLastReset = System.currentTimeMillis()
        }
        updateUser(user)
    }

    suspend fun updateProfileCompletion(userId: UUID, isCompleted: Boolean) {
        val user = getUserById(userId) ?: return
        user.profileCompleted = isCompleted
        updateUser(user)
    }
}