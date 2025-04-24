package vcmsa.projects.prog7313_poe.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.core.repository.ExpenseRepository

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExpense(expense)
        }
    }
}
