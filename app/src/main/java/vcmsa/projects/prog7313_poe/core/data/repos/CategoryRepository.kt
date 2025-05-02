package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.CategoryDao
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

/**
 * Repository class for managing [Category] entities.
 *
 * Provides an abstraction layer for UI components and delegates data operations
 * to [CategoryDao]. This allows for testability and separation of concerns.
 *
 * @reference https://developer.android.com/topic/libraries/architecture/data-layer
 * @reference https://developer.android.com/reference/androidx/room/Dao
 *
 * @see CategoryDao
 *
 * @author ST10257002
 * @author ST10326084
 */
class CategoryRepository(
    private val dao: CategoryDao
) {

    /**
     * Inserts a new [Category] into the database.
     */
    suspend fun createCategory(instance: Category) {
        dao.insert(instance)
    }

    /**
     * Updates an existing [Category] with new data.
     */
    suspend fun updateCategory(instance: Category) {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Fetches all categories associated with a specific user.
     *
     * @param userId The ID of the user to filter by.
     */
    suspend fun getCategoriesByUserId(userId: UUID): List<Category> {
        return dao.getCategoriesByUserId(userId)
    }

    /**
     * Deletes the specified [Category] instance.
     */
    suspend fun deleteCategory(instance: Category) {
        dao.delete(instance)
    }

    /**
     * Deletes a [Category] by its UUID.
     */
    suspend fun deleteCategoryById(id: UUID) {
        dao.delete(id)
    }

    /**
     * Returns a full list of all [Category] records.
     */
    suspend fun getAllCategories(): List<Category> {
        return dao.fetchAll()
    }
}
