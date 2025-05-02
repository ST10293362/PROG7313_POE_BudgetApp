package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.CategoryDao
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

class CategoryRepository(
    private val dao: CategoryDao
) {


    suspend fun createCategory(instance: Category) {
        dao.insert(instance)
    }



    suspend fun updateCategory(instance: Category) {
        instance.touch()
        dao.update(instance)
    }

    suspend fun getCategoriesByUserId(userId: UUID): List<Category> {
        return dao.getCategoriesByUserId(userId)
    }


    suspend fun deleteCategory(instance: Category) {
        dao.delete(instance)
    }



    suspend fun deleteCategoryById(id: UUID) {
        dao.delete(id)
    }



    suspend fun getAllCategories(): List<Category> {
        return dao.fetchAll()
    }

}