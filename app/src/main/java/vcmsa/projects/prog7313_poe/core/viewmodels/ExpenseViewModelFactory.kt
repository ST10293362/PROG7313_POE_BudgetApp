package vcmsa.projects.prog7313_poe.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository

/**
 * @author ST10326084
 */
class ExpenseViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {

    /**
     * @author ST10326084
     */
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java) == false) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        @Suppress("UNCHECKED_CAST") 
        return ExpenseViewModel(repository) as T
    }
}
