package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import vcmsa.projects.prog7313_poe.core.models.AccountWithExpenses
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.CategoryWithExpenses
import java.util.UUID

/**
 * Interface definition for direct database operations.
 *
 * @see [vcmsa.projects.prog7313_poe.core.models.Category]
 * @see [androidx.room.Dao]
 * @author ST10257002
 */
@Dao
interface CategoryDao : BaseDao<Category> {

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
        DELETE FROM category 
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
        SELECT * FROM category
        """
    )
    suspend fun fetchAll(): List<Category>


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
        SELECT * FROM category
        WHERE id = :targetId
        """
    )
    suspend fun fetchOne(targetId: UUID): Category?


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
            SELECT * FROM category WHERE id = :targetId
        )
        """
    )
    suspend fun exists(targetId: UUID): Boolean



    /**
     * Fetches an [CategoryWithExpenses] object.
     *
     * @param targetId The unique identifier ([UUID]) of the category.
     *
     * @author ST10257002
     */
    @Transaction
    @Query(
        """
            SELECT * FROM category
            WHERE id = :targetId
        """
    )
    suspend fun fetchCategoryWithExpenses(targetId: UUID): CategoryWithExpenses


    //</editor-fold>

    @Query("SELECT * FROM category WHERE user_id = :userId")
    suspend fun getCategoriesByUserId(userId: UUID): List<Category>

    @Query("SELECT * FROM category WHERE user_id = :userId AND is_default = 1")
    suspend fun getDefaultCategories(userId: UUID): List<Category>

    @Query("SELECT * FROM category WHERE user_id = :userId AND is_default = 0")
    suspend fun getUserCategories(userId: UUID): List<Category>

    @Query("UPDATE category SET total_amount = total_amount + :amount WHERE id = :categoryId")
    suspend fun updateCategoryTotal(categoryId: UUID, amount: Double)

    @Transaction
    suspend fun createDefaultCategories(userId: UUID) {
        val defaultCategories = listOf(
            Category(name = "Savings", userId = userId, isDefault = true),
            Category(name = "Utilities", userId = userId, isDefault = true),
            Category(name = "Investments", userId = userId, isDefault = true)
        )
        defaultCategories.forEach { insert(it) }
    }
}