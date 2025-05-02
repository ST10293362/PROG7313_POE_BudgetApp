package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.CategoryWithExpenses
import java.util.UUID

/**
 * DAO interface for performing database operations on [Category] entities.
 *
 * Includes CRUD operations, existence checks, and Room @Transaction queries
 * for relational mappings with [Expense] via [CategoryWithExpenses].
 *
 * @see androidx.room.Dao
 * @see vcmsa.projects.prog7313_poe.core.models.Category
 *
 * @reference https://developer.android.com/training/data-storage/room/accessing-data
 *
 * @author ST10257002
 * @author ST10326084
 */
@Dao
interface CategoryDao : BaseDao<Category> {

    //<editor-fold desc="CRUD Operations">

    /**
     * Deletes a category from the database using its UUID.
     *
     * @param targetId The unique identifier of the category to delete.
     */
    @Query("DELETE FROM category WHERE id = :targetId")
    suspend fun delete(targetId: UUID)

    /**
     * Fetches all categories from the database.
     *
     * @return A list of all [Category] records.
     */
    @Query("SELECT * FROM category")
    suspend fun fetchAll(): List<Category>

    /**
     * Fetches a single [Category] by its ID.
     *
     * @param targetId The UUID of the category to retrieve.
     * @return The [Category] if found, otherwise null.
     */
    @Query("SELECT * FROM category WHERE id = :targetId")
    suspend fun fetchOne(targetId: UUID): Category?

    //</editor-fold>

    //<editor-fold desc="Extensions">

    /**
     * Checks whether a category exists by its ID.
     *
     * @param targetId UUID of the category.
     * @return True if it exists, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT * FROM category WHERE id = :targetId)")
    suspend fun exists(targetId: UUID): Boolean

    /**
     * Retrieves all categories that belong to a specific user.
     *
     * @param userId UUID of the user.
     * @return List of [Category] linked to that user.
     */
    @Query("SELECT * FROM category WHERE user_id = :userId")
    suspend fun getCategoriesByUserId(userId: UUID): List<Category>

    /**
     * Fetches a [CategoryWithExpenses] object, which contains the category and its associated expenses.
     *
     * This uses Room's `@Transaction` to load related entities in a single call.
     *
     * @param targetId The UUID of the category to fetch.
     * @return A [CategoryWithExpenses] instance.
     */
    @Transaction
    @Query("SELECT * FROM category WHERE id = :targetId")
    suspend fun fetchCategoryWithExpenses(targetId: UUID): CategoryWithExpenses

    //</editor-fold>
}
