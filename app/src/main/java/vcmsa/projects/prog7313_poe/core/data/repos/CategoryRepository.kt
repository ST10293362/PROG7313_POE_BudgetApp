package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.data.dao.CategoryDao
import java.util.UUID

/**
 * @author ST10257002
 */
class CategoryRepository(private val categoryDao: CategoryDao) {

    /**
     * @author ST10257002
     */
    suspend fun getById(id: UUID): Category? = categoryDao.getById(id)

    /**
     * @author ST10257002
     */
    suspend fun getByUserId(userId: UUID): List<Category> = categoryDao.getByUserId(userId)

    /**
     * @author ST10257002
     */
    suspend fun getByName(name: String): Category? = categoryDao.getByName(name)

    /**
     * @author ST10257002
     */
    suspend fun insert(category: Category) = categoryDao.insert(category)

    /**
     * @author ST10257002
     */
    suspend fun update(category: Category) = categoryDao.update(category)

    /**
     * @author ST10257002
     */
    suspend fun delete(category: Category) = categoryDao.delete(category)

    /**
     * @author ST10257002
     */
    suspend fun deleteById(id: UUID) = categoryDao.deleteById(id)

    /**
     * @author ST10257002
     */
    suspend fun deleteByUserId(userId: UUID) = categoryDao.deleteByUserId(userId)

    /**
     * @author ST10257002
     */
    suspend fun updateTotalAmount(categoryId: UUID, amount: Double) = categoryDao.updateTotalAmount(categoryId, amount)
}