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

    fun getCategoriesByUserId(userId: UUID): LiveData<List<Category>> = liveData(Dispatchers.IO) {
        try {
            val categories = repository.getCategoriesByUserId(userId)
            emit(categories ?: emptyList())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}