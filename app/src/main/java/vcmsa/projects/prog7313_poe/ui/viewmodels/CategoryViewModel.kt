package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    val categories: LiveData<List<Category>> = liveData(Dispatchers.IO) {
        try {
            val result = repository.getAllCategories()
            emit(result)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun addCategory(category: Category) = liveData(Dispatchers.IO) {
        try {
            repository.createCategory(category)
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    fun deleteCategory(category: Category) = liveData(Dispatchers.IO) {
        try {
            repository.deleteCategory(category)
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    fun updateCategory(category: Category) = liveData(Dispatchers.IO) {
        try {
            repository.updateCategory(category)
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    suspend fun getCategoriesByUserId(userId: UUID): List<Category> {
        return repository.getCategoriesByUserId(userId)
    }
}