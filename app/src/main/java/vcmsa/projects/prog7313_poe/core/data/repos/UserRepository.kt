package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

/**
 * Repository class for managing [User] entities.
 *
 * Acts as a clean abstraction layer between the ViewModels and Room DAO,
 * encapsulating data logic such as pre-checks and mutation before DB writes.
 *
 * @reference https://developer.android.com/topic/libraries/architecture/data-layer
 * @reference https://developer.android.com/reference/androidx/room/Dao
 *
 * @see UserDao
 *
 * @author ST10257002
 * @author ST10326084
 */
class UserRepository(
    private val dao: UserDao
) {

    /**
     * Insert a new [User] record into the database.
     *
     * @param instance The user entity to persist.
     *
     * @see UserDao.insert
     */
    suspend fun createUser(instance: User) {
        dao.insert(instance)
    }

    /**
     * Update an existing [User] record in the database.
     *
     * @param instance The updated user data.
     *
     * Automatically updates audit fields using `touch()`.
     *
     * @see UserDao.update
     */
    suspend fun updateUser(instance: User) {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Delete a user from the database.
     *
     * @param instance The [User] entity to remove.
     *
     * @see UserDao.delete
     */
    suspend fun deleteUser(instance: User) {
        dao.delete(instance)
    }

    /**
     * Delete a user by ID.
     *
     * @param id UUID of the user to delete.
     *
     * @see UserDao.delete
     */
    suspend fun deleteUserById(id: UUID) {
        dao.delete(id)
    }

    /**
     * Fetch all users from the database.
     *
     * @return A list of [User] objects.
     *
     * @see UserDao.fetchAll
     */
    suspend fun getAllUsers(): List<User> {
        return dao.fetchAll()
    }

    /**
     * Fetch a user by their UUID.
     *
     * @param id The UUID of the user.
     * @return The corresponding [User], or null if not found.
     *
     * @see UserDao.fetchOne
     */
    suspend fun getUserById(id: UUID): User? {
        return dao.fetchOne(id)
    }

    /**
     * Update the goals for a specific user.
     *
     * @param userId The UUID of the user.
     * @param minGoal The new minimum savings goal.
     * @param maxGoal The new maximum savings goal.
     * @param goalsSet Whether the goals have been initialized.
     *
     * @see User.minGoal
     * @see UserDao.update
     */
    suspend fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double, goalsSet: Boolean) {
        val user = getUserById(userId) ?: return
        user.apply {
            this.minGoal = minGoal
            this.maxGoal = maxGoal
            this.goalsSet = goalsSet
        }
        updateUser(user)
    }

    /**
     * Update both goal and budget information for a user.
     *
     * @param userId UUID of the user.
     * @param minGoal Minimum goal value.
     * @param maxGoal Maximum goal value.
     * @param monthlyBudget The user's monthly budget.
     * @param goalsSet Whether the goals have been set.
     *
     * @see User.monthlyBudget
     * @see User.budgetLastReset
     */
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

    /**
     * Updates the profile completion flag for the specified user.
     *
     * @param userId UUID of the user.
     * @param isCompleted Whether the profile is marked as complete.
     *
     * @see User.profileCompleted
     */
    suspend fun updateProfileCompletion(userId: UUID, isCompleted: Boolean) {
        val user = getUserById(userId) ?: return
        user.profileCompleted = isCompleted
        updateUser(user)
    }
}
