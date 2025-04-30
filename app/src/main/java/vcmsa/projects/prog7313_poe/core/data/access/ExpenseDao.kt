package vcmsa.projects.prog7313_poe.core.data.access

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import vcmsa.projects.prog7313_poe.core.models.Expense
import java.time.Instant
import java.util.UUID

/**
 * Interface definition for direct database operations.
 *
 * @see [vcmsa.projects.prog7313_poe.core.models.Expense]
 * @see [androidx.room.Dao]
 * @author ST10257002
 */
@Dao
interface ExpenseDao : BaseDao<Expense> {

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
        DELETE FROM expense 
        WHERE id = :targetId
        """
    )
    suspend fun delete(targetId: UUID)


    /**
     * Fetches the contents of the database table.
     *
     * @return [LiveData] containing every entity in the database.
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM expense
        """
    )
    suspend fun getAllExpenses(): List<Expense>


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
        SELECT * FROM expense
        WHERE id = :targetId
        """
    )
    suspend fun fetchOne(targetId: UUID): Expense?


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
            SELECT * FROM expense WHERE id = :targetId
        )
        """
    )
    suspend fun exists(targetId: UUID): Boolean


    //</editor-fold>

    @Query("SELECT * FROM expense WHERE user_id = :userId")
    suspend fun getExpensesByUserId(userId: UUID): List<Expense>

    @Query("SELECT * FROM expense WHERE user_id = :userId AND category_id = :categoryId")
    suspend fun getExpensesByCategory(userId: UUID, categoryId: UUID): List<Expense>

    @Query("SELECT * FROM expense WHERE user_id = :userId AND start_date BETWEEN :startDate AND :endDate")
    suspend fun getExpensesByDateRange(userId: UUID, startDate: Instant, endDate: Instant): List<Expense>

    @Query("SELECT SUM(amount) FROM expense WHERE user_id = :userId AND category_id = :categoryId")
    suspend fun getTotalExpensesByCategory(userId: UUID, categoryId: UUID): Double?


    @Transaction
    suspend fun addExpense(expense: Expense) {
        insert(expense)
        // Update category total
        expense.categoryId.let { categoryId ->
            expense.amount.let { amount ->
                // Get categoryDao from somewhere (you might need to inject it)
                // categoryDao.updateCategoryTotal(categoryId, amount)
            }
        }
    }
}