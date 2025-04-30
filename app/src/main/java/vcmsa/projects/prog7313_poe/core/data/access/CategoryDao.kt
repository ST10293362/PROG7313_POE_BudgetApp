package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.CategoryWithExpenses
import java.util.UUID

/**
 * DAO for managing [Category] entities and their associated operations.
 * This interface extends [BaseDao] to inherit common CRUD methods and adds
 * custom Room queries specific to the `category` table.
 *
 * @see androidx.room.Dao
 * @see vcmsa.projects.prog7313_poe.core.models.Category
 *
 * @author ST10257002
 * @author ST10326084
 *
 * @reference https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
interface CategoryDao : BaseDao<Category> {

    //<editor-fold desc="CRUD Operations">

    /**
     * Deletes a category from the database using its unique ID.
     *
     * @param targetId UUID of the category to delete.
     * @author ST10257002
     * @author ST10326084
     */
    @Query(
        """
        DELETE FROM category 
        WHERE id = :targetId
        """
    )
    suspend fun delete(targetId: UUID)

    /**
     * Retrieves all categories stored in the database.
     *
     * @return List of [Category] objects.
     * @author ST10257002
     * @author ST10326084
     */
    @Query("SELECT * FROM category")
    suspend fun fetchAll(): List<Category>

    /**
     * Retrieves a specific category by ID.
     *
     * @param targetId UUID of the category to fetch.
     * @return The [Category] with the given ID, or null if not found.
     * @author ST10257002
     * @author ST10326084
     */
    @Query("SELECT * FROM category WHERE id = :targetId")
    suspend fun fetchOne(targetId: UUID): Category?

    //</editor-fold>

    //<editor-fold desc="Extensions">

    /**
     * Checks whether a category exists by its ID.
     *
     * @param targetId UUID of the category to check.
     * @return True if the category exists, false otherwise.
     * @author ST10257002
     * @author ST10326084
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
     * Retrieves a [CategoryWithExpenses] composite object, containing
     * a category and all related expenses. Executed in a transaction
     * to ensure data consistency for the one-to-many relationship.
     *
     * @param targetId UUID of the category to fetch with related expenses.
     * @return [CategoryWithExpenses] object containing full relationship data.
     *
     * @reference https://developer.android.com/reference/androidx/room/Transaction
     * @author ST10257002
     * @author ST10326084
     */
    @Transaction
    @Query("SELECT * FROM category WHERE id = :targetId")
    suspend fun fetchCategoryWithExpenses(targetId: UUID): CategoryWithExpenses

    //</editor-fold>
}
