package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                val result = repository.getAllCategories()
                withContext(Dispatchers.Main) {
                    _categories.value = result
                }
            } catch (e: Exception) {
                handleError(e)
                withContext(Dispatchers.Main) {
                    _categories.value = emptyList()
                }
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun insert(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                repository.insert(category)
                loadCategories() // Reload categories after adding
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun delete(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                repository.delete(category)
                loadCategories() // Reload categories after deletion
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun update(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                repository.update(category)
                loadCategories() // Reload categories after update
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _loading.postValue(false)
            }
        }
    }


    private fun handleError(e: Exception) {
        _error.postValue(e.message ?: "An unknown error occurred")
    }
}