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

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {
    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> = _expenses

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result: List<Expense> = repository.getAllExpenses()
                withContext(Dispatchers.Main) {
                    _expenses.value = result
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _expenses.value = emptyList()
                }
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createExpense(expense)
            loadExpenses() // Reload expenses after adding new one
        }
    }
}