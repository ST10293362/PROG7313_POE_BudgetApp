package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.CategoryDao
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID
import java.time.Instant

/**
 * Repository for managing category data in the database.
 * Provides basic CRUD operations for categories.
 *
 * @author ST10257002
 * @author ST10326084
 */
class CategoryRepository(private val categoryDao: CategoryDao) {
    /**
     * Gets a category by its ID.
     *
     * @param id The ID of the category to fetch
     * @return The category if found, null otherwise
     */
    suspend fun getById(id: UUID): Category? = categoryDao.getById(id)

    /**
     * Gets all categories from the database.
     *
     * @return List of all categories
     */
    suspend fun getAllCategories(): List<Category> = categoryDao.getAllCategories()

    /**
     * Inserts a new category into the database.
     *
     * @param category The category to insert
     * @return Result indicating success or failure
     */
    suspend fun insert(category: Category): Result<Category> = try {
        categoryDao.insert(category)
        Result.success(category)
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Updates an existing category in the database.
     *
     * @param category The category to update
     * @return Result indicating success or failure
     */
    suspend fun update(category: Category): Result<Category> = try {
        categoryDao.update(category)
        Result.success(category)
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Deletes a category from the database.
     *
     * @param category The category to delete
     * @return Result indicating success or failure
     */
    suspend fun delete(category: Category): Result<Unit> = try {
        categoryDao.delete(category)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Checks whether a category with the given ID exists in the database.
     *
     * @param id The unique identifier of the category to check.
     * @return true if the category exists, false otherwise.
     */
    suspend fun exists(id: UUID): Boolean = categoryDao.exists(id)
}