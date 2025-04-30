package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.CategoryDao
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

/**
 * Repository for managing [Category] entities via [CategoryDao].
 * Provides an abstraction layer between the UI layer and Room database.
 *
 * This class handles inserts, updates, and deletes — and also ensures
 * that auditing metadata like timestamps are refreshed via `touch()`.
 *
 * @author ST10257002
 * @author ST10326084
 */
class CategoryRepository(
    private val dao: CategoryDao
) {

    /**
     * Inserts a new category into the database.
     *
     * @param instance The [Category] to be inserted.
     */
    suspend fun createCategory(instance: Category) {
        dao.insert(instance)
    }

    /**
     * Updates an existing category.
     *
     * Automatically calls `touch()` to update the `updatedAt` timestamp
     * before saving.
     *
     * @param instance The [Category] to be updated.
     */
    suspend fun updateCategory(instance: Category) {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Deletes a category entity from the database.
     *
     * @param instance The [Category] to delete.
     */
    suspend fun deleteCategory(instance: Category) {
        dao.delete(instance)
    }

    /**
     * Deletes a category using its unique identifier.
     *
     * @param id UUID of the target [Category].
     */
    suspend fun deleteCategoryById(id: UUID) {
        dao.delete(id)
    }

    /**
     * Retrieves all category entries from the database.
     *
     * @return A list of all [Category]s.
     */
    suspend fun getAllCategories(): List<Category> {
        return dao.fetchAll()
    }
}
