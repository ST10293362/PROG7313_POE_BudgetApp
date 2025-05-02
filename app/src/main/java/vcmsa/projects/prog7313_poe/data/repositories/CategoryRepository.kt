package vcmsa.projects.prog7313_poe.data.repositories

import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.data.dao.CategoryDao

class CategoryRepository(private val categoryDao: CategoryDao) {
    fun getCategoriesByUserId(userId: String): List<Category> {
        return categoryDao.getCategoriesByUserId(userId)
    }

    fun getCategoryById(categoryId: String): Category? {
        return categoryDao.getCategoryById(categoryId)
    }
} 