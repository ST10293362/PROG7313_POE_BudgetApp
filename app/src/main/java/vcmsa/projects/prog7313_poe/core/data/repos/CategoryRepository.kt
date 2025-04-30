package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.CategoryDao
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

/**
 * @author ST10257002
 */
class CategoryRepository(
    private val dao: CategoryDao
) {

    /**
     * @author ST10257002
     */
    suspend fun createCategory(instance: Category) {
        dao.insert(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun updateCategory(instance: Category) {
        instance.touch()
        dao.update(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteCategory(instance: Category) {
        dao.delete(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteCategoryById(id: UUID) {
        dao.delete(id)
    }


    /**
     * @author ST10257002
     */
    suspend fun getAllCategories(): List<Category> {
        return dao.fetchAll()
    }

        suspend fun getCategoriesByUserId(userId: UUID): List<Category> {
            return dao.getCategoriesByUserId(userId) ?: emptyList()

        }
    }
