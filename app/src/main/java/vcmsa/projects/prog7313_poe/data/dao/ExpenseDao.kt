package vcmsa.projects.prog7313_poe.data.dao

import androidx.room.*
import vcmsa.projects.prog7313_poe.core.models.Expense
import java.util.UUID

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: UUID): Expense?

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getByUserId(userId: UUID): List<Expense>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId")
    suspend fun getByCategoryId(categoryId: UUID): List<Expense>

    @Query("SELECT * FROM expenses")
    suspend fun getAll(): List<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("DELETE FROM expenses WHERE userId = :userId")
    suspend fun deleteByUserId(userId: UUID)

    @Query("DELETE FROM expenses WHERE categoryId = :categoryId")
    suspend fun deleteByCategoryId(categoryId: UUID)
} 