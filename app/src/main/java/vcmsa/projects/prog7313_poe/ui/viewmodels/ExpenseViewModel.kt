package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.models.Expense
import java.util.UUID

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> = _expenses

    private val _currentExpense = MutableLiveData<Expense?>()
    val currentExpense: LiveData<Expense?> = _currentExpense

    private val _photos = MutableLiveData<List<String>>()
    val photos: LiveData<List<String>> = _photos

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                val result = withContext(Dispatchers.IO) {
                    repository.getAllExpenses()
                }
                _expenses.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun loadExpenseById(expenseId: UUID) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                val result = withContext(Dispatchers.IO) {
                    repository.getExpenseById(expenseId)
                }
                _currentExpense.postValue(result)
                result?.let { expense ->
                    _photos.postValue(expense.photos)
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                withContext(Dispatchers.IO) {
                    repository.createExpense(expense)
                }
                loadExpenses() // Refresh the list
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                withContext(Dispatchers.IO) {
                    repository.updateExpense(expense)
                }
                loadExpenses() // Refresh the list
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                withContext(Dispatchers.IO) {
                    repository.deleteExpense(expense)
                }
                loadExpenses() // Refresh the list
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun addPhoto(expenseId: UUID, photoUri: String) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                withContext(Dispatchers.IO) {
                    repository.addPhoto(expenseId, photoUri)
                }
                loadExpenseById(expenseId) // Refresh the expense with new photos
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun deletePhoto(expenseId: UUID, photoUri: String) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                withContext(Dispatchers.IO) {
                    repository.deletePhoto(expenseId, photoUri)
                }
                loadExpenseById(expenseId) // Refresh the expense with updated photos
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun getExpensesByUserId(userId: UUID) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                val result = withContext(Dispatchers.IO) {
                    repository.getExpensesByUserId(userId)
                }
                _expenses.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun getExpensesByCategory(userId: UUID, categoryId: UUID) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                val result = withContext(Dispatchers.IO) {
                    repository.getExpensesByCategory(userId, categoryId)
                }
                _expenses.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _loading.postValue(false)
            }
        }
    }
}