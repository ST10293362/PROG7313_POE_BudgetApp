package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.CategoryDao
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

/**
 * Repository class for handling operations related to [Category] entities.
 * Acts as an intermediary between the data access layer ([CategoryDao])
 * and the rest of the application.
 *
 * Provides methods for inserting, updating, deleting, and retrieving categories.
 *
 * @author ST10257002
 * @author ST13026084
 */
class CategoryRepository(
    private val dao: CategoryDao
) {

    /**
     * Inserts a new [Category] into the database.
     *
     * @param instance The [Category] entity to insert.
     * Uses the [CategoryDao.insert] method.
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun createCategory(instance: Category) {
        dao.insert(instance)
    }

    /**
     * Updates an existing [Category] in the database.
     * Also refreshes the audit timestamp using the [Category.touch] method.
     *
     * @param instance The [Category] to update.
     * Uses the [CategoryDao.update] method.
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun updateCategory(instance: Category) {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Deletes a specific [Category] from the database.
     *
     * @param instance The [Category] to delete.
     * Uses the [CategoryDao.delete] method.
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun deleteCategory(instance: Category) {
        dao.delete(instance)
    }

    /**
     * Deletes a [Category] from the database using its UUID.
     *
     * @param id UUID of the [Category] to delete.
     * Uses the [CategoryDao.delete] method with ID.
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun deleteCategoryById(id: UUID) {
        dao.delete(id)
    }

    /**
     * Retrieves a list of all [Category] records from the database.
     *
     * @return A list of [Category] objects.
     * Uses the [CategoryDao.fetchAll] method.
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun getAllCategories(): List<Category> {
        return dao.fetchAll()
    }
}
