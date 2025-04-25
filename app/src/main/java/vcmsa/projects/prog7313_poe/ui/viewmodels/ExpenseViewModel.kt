package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.models.Expense

/**
 * @author ST10326084
 */
class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    /**
     * @author ST10326084
     */
    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createExpense(expense)
        }
    }
}
