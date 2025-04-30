package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.supers.AuditableEntity
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
        dao.updateUserGoals(userId, minGoal, maxGoal, goalsSet)
    }

    suspend fun updateUserGoalsAndBudget(
        userId: UUID,
        minGoal: Double,
        maxGoal: Double,
        monthlyBudget: Double,
        goalsSet: Boolean
    ) {
        dao.updateUserGoalsAndBudget(
            userId = userId,
            minGoal = minGoal,
            maxGoal = maxGoal,
            monthlyBudget = monthlyBudget,
            goalsSet = goalsSet
        )
    }

    suspend fun updateProfileCompletion(userId: UUID, isCompleted: Boolean) {
        dao.updateProfileCompletion(userId, isCompleted)
    }
}