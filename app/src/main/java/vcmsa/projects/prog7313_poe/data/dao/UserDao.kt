package vcmsa.projects.prog7313_poe.data.dao

import androidx.room.*
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: UUID): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :userId)")
    suspend fun exists(userId: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun existsByEmail(email: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    suspend fun existsByUsername(username: String): Boolean

    @Query("UPDATE users SET minGoal = :minGoal, maxGoal = :maxGoal, monthlyBudget = :monthlyBudget, goalsSet = :goalsSet WHERE id = :userId")
    suspend fun updateUserGoalsAndBudget(userId: String, minGoal: Double, maxGoal: Double, monthlyBudget: Double, goalsSet: Boolean)

    @Query("UPDATE users SET goalsSet = :goalsSet WHERE id = :userId")
    suspend fun updateGoalsSet(userId: String, goalsSet: Boolean)

    @Query("UPDATE users SET currentBudget = :currentBudget WHERE id = :userId")
    suspend fun updateCurrentBudget(userId: String, currentBudget: Double)

    @Query("UPDATE users SET monthlyBudget = :monthlyBudget WHERE id = :userId")
    suspend fun resetMonthlyBudget(userId: String, monthlyBudget: Double)

    @Query("UPDATE users SET profileCompleted = :completed WHERE id = :userId")
    suspend fun updateProfileCompletion(userId: String, completed: Boolean)

    @Query("UPDATE users SET monthlyBudget = :monthlyBudget WHERE id = :userId")
    suspend fun updateMonthlyBudget(userId: UUID, monthlyBudget: Double)

    @Query("UPDATE users SET currentBudget = :currentBudget WHERE id = :userId")
    suspend fun updateCurrentBudget(userId: UUID, currentBudget: Double)

    @Query("UPDATE users SET minGoal = :minGoal, maxGoal = :maxGoal WHERE id = :userId")
    suspend fun updateGoals(userId: UUID, minGoal: Double, maxGoal: Double)

    @Query("UPDATE users SET profileCompleted = :completed WHERE id = :userId")
    suspend fun updateProfileCompleted(userId: UUID, completed: Boolean)

    @Query("UPDATE users SET monthlyBudget = :monthlyBudget, currentBudget = :monthlyBudget WHERE id = :userId")
    suspend fun resetMonthlyBudget(userId: UUID, monthlyBudget: Double)
} 