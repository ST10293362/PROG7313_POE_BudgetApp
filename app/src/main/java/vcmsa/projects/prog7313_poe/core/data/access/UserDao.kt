package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.UserWithAccounts
import vcmsa.projects.prog7313_poe.core.models.UserWithCategories
import vcmsa.projects.prog7313_poe.core.models.UserWithExpenses
import java.util.UUID

/**
 * Data Access Object (DAO) for interacting with [User] entities in the database.
 *
 * This interface includes CRUD operations, goal/budget updates, login queries,
 * and `@Transaction`-based relational queries for linked tables like expenses,
 * categories, and accounts.
 *
 * @see androidx.room.Dao
 * @see vcmsa.projects.prog7313_poe.core.models.User
 * @see https://developer.android.com/training/data-storage/room/accessing-data
 *
 * @author ST10257002
 * @author ST10326084
 * @since 2024
 */
@Dao
interface UserDao : BaseDao<User> {

    //<editor-fold desc="CRUD Operations">

    /**
     * Deletes a user by their UUID.
     *
     * @param targetId The UUID of the user to delete.
     */
    @Query("DELETE FROM user WHERE id = :targetId")
    suspend fun delete(targetId: UUID)

    /**
     * Returns all users in the database.
     *
     * @return A list of all [User] entities.
     */
    @Query("SELECT * FROM user")
    suspend fun fetchAll(): List<User>

    /**
     * Retrieves a user by their ID.
     *
     * @param targetId UUID of the user to retrieve.
     * @return The [User] if found, else null.
     */
    @Query("SELECT * FROM user WHERE id = :targetId")
    suspend fun fetchOne(targetId: UUID): User?

    /**
     * Retrieves a user by both username and password (for login).
     *
     * @param username The user's username.
     * @param password The raw or hashed password.
     * @return The matching [User], or null.
     */
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    suspend fun fetchOneByCredentials(username: String, password: String): User?

    /**
     * Retrieves a user by username.
     *
     * @param username The username to look for.
     * @return The [User] if found, else null.
     */
    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun fetchOneByUsername(username: String): User?

    /**
     * Retrieves a user by email address.
     *
     * @param email The email to search.
     * @return The [User] if found, else null.
     */
    @Query("SELECT * FROM user WHERE email_address = :email")
    suspend fun fetchOneByEmail(email: String): User?

    //</editor-fold>

    //<editor-fold desc="Existence & Updates">

    /**
     * Checks whether a user with the given ID exists.
     *
     * @param targetId UUID of the user.
     * @return True if user exists, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :targetId)")
    suspend fun exists(targetId: UUID): Boolean

    /**
     * Updates a user's goals and goal status.
     */
    @Query("UPDATE user SET min_goal = :minGoal, max_goal = :maxGoal, goals_set = :goalsSet WHERE id = :userId")
    suspend fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double, goalsSet: Boolean)

    /**
     * Updates whether the user has completed their goal setup.
     */
    @Query("UPDATE user SET goals_set = :goalsSet WHERE id = :userId")
    suspend fun updateGoalsSet(userId: UUID, goalsSet: Boolean)

    /**
     * Updates a user's goals along with their monthly budget.
     * Resets current budget to monthly budget.
     */
    @Query("""
        UPDATE user 
        SET min_goal = :minGoal,
            max_goal = :maxGoal,
            monthly_budget = :monthlyBudget,
            current_budget = :monthlyBudget,
            budget_last_reset = :currentDate,
            goals_set = :goalsSet 
        WHERE id = :userId
    """)
    suspend fun updateUserGoalsAndBudget(
        userId: UUID,
        minGoal: Double,
        maxGoal: Double,
        monthlyBudget: Double,
        goalsSet: Boolean,
        currentDate: Long = System.currentTimeMillis()
    )

    /**
     * Subtracts from the user's current budget.
     *
     * @param userId The user's UUID.
     * @param amount The amount to subtract.
     */
    @Query("UPDATE user SET current_budget = current_budget - :amount WHERE id = :userId")
    suspend fun updateCurrentBudget(userId: UUID, amount: Double)

    /**
     * Resets the user's budget for a new month.
     *
     * @param userId The user's UUID.
     * @param currentDate Optional: timestamp of reset.
     */
    @Query("""
        UPDATE user 
        SET current_budget = monthly_budget,
            budget_last_reset = :currentDate 
        WHERE id = :userId
    """)
    suspend fun resetMonthlyBudget(userId: UUID, currentDate: Long = System.currentTimeMillis())

    /**
     * Marks whether the user's profile setup is complete.
     *
     * @param isCompleted True if profile is fully filled out.
     */
    @Query("UPDATE user SET profile_completed = :isCompleted WHERE id = :userId")
    suspend fun updateProfileCompletion(userId: UUID, isCompleted: Boolean)

    //</editor-fold>

    //<editor-fold desc="Relational Queries (@Transaction)">

    /**
     * Fetches a user along with their linked [Account]s.
     * Requires a valid Room @Relation in [UserWithAccounts].
     */
    @Transaction
    @Query("SELECT * FROM user WHERE id = :targetId")
    suspend fun fetchUserWithAccounts(targetId: UUID): UserWithAccounts

    /**
     * Fetches a user along with their [Category] data.
     */
    @Transaction
    @Query("SELECT * FROM user WHERE id = :targetId")
    suspend fun fetchUserWithCategories(targetId: UUID): UserWithCategories

    /**
     * Fetches a user along with their [Expense]s.
     */
    @Transaction
    @Query("SELECT * FROM user WHERE id = :targetId")
    suspend fun fetchUserWithExpenses(targetId: UUID): UserWithExpenses

    //</editor-fold>
}
