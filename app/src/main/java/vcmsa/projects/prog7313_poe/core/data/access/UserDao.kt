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
 * Interface definition for direct database operations.
 *
 * @see [vcmsa.projects.prog7313_poe.core.models.User]
 * @see [androidx.room.Dao]
 * @author ST10257002
 */
@Dao
interface UserDao : BaseDao<User> {

    //<editor-fold desc="CRUD Operations">


    /**
     * Deletes a specific entity from the database using its unique ID.
     *
     * @param targetId The unique identifier ([java.util.UUID]) of the entity to delete.
     *
     * @author ST10257002
     */
    @Query(
        """
        DELETE FROM user 
        WHERE id = :targetId
        """
    )
    suspend fun delete(targetId: UUID)


    /**
     * Fetches the contents of the database table.
     *
     * @return [List] collection containing every entity in the database.
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM user
        """
    )
    suspend fun fetchAll(): List<User>


    /**
     * Fetches a specific entity by its primary key identity.
     *
     * @param targetId The unique identifier ([UUID]) of the entity to query.
     *
     * @return The specific entity that was queried.
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM user
        WHERE id = :targetId
        """
    )
    suspend fun fetchOne(targetId: UUID): User?


    /**
     * @return The specific entity that was queried.
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM user
        WHERE   username = :username 
            AND password = :password
        """
    )
    suspend fun fetchOneByCredentials(username: String, password: String): User?


    /**
     * @return The specific entity that was queried.
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM user
        WHERE username = :username
        """
    )
    suspend fun fetchOneByUsername(username: String): User?


    /**
     * Fetches a user by their email address.
     */
    @Query(
        """
        SELECT * FROM user
        WHERE email_address = :email
        """
    )
    suspend fun fetchOneByEmail(email: String): User?


    //</editor-fold>
    //<editor-fold desc="Extensions">


    /**
     * Checks whether an entity with the given ID exists in the database.
     *
     * @param targetId The unique identifier ([UUID]) of the entity to query.
     *
     * @author ST10257002
     */
    @Query(
        """
        SELECT EXISTS(
            SELECT * FROM user WHERE id = :targetId
        )
        """
    )
    suspend fun exists(targetId: UUID): Boolean

    // In UserDao.kt
    @Query("UPDATE user SET min_goal = :minGoal, max_goal = :maxGoal, goals_set = :goalsSet WHERE id = :userId")
    suspend fun updateUserGoals(userId: UUID, minGoal: Double, maxGoal: Double, goalsSet: Boolean)

    /**
     * Fetches an [UserWithAccounts] object.
     *
     * @param targetId The unique identifier ([UUID]) of the user.
     *
     * @author ST10257002
     */
    @Transaction
    @Query(
        """
            SELECT * FROM user
            WHERE id = :targetId
        """
    )
    suspend fun fetchUserWithAccounts(targetId: UUID): UserWithAccounts


    /**
     * Fetches an [UserWithCategories] object.
     *
     * @param targetId The unique identifier ([UUID]) of the user.
     *
     * @author ST10257002
     */
    @Transaction
    @Query(
        """
            SELECT * FROM user
            WHERE id = :targetId
        """
    )
    suspend fun fetchUserWithCategories(targetId: UUID): UserWithCategories


    /**
     * Fetches an [UserWithExpenses] object.
     *
     * @param targetId The unique identifier ([UUID]) of the user.
     *
     * @author ST10257002
     */
    @Transaction
    @Query(
        """
            SELECT * FROM user
            WHERE id = :targetId
        """
    )
    suspend fun fetchUserWithExpenses(targetId: UUID): UserWithExpenses


    @Query("UPDATE user SET goals_set = :goalsSet WHERE id = :userId")
    suspend fun updateGoalsSet(userId: UUID, goalsSet: Boolean)
    //</editor-fold>

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

    @Query("""
        UPDATE user 
        SET current_budget = current_budget - :amount 
        WHERE id = :userId
    """)
    suspend fun updateCurrentBudget(userId: UUID, amount: Double)

    @Query("""
        UPDATE user 
        SET current_budget = monthly_budget,
            budget_last_reset = :currentDate 
        WHERE id = :userId
    """)
    suspend fun resetMonthlyBudget(userId: UUID, currentDate: Long = System.currentTimeMillis())

    @Query("UPDATE user SET profile_completed = :isCompleted WHERE id = :userId")
    suspend fun updateProfileCompletion(userId: UUID, isCompleted: Boolean)
}