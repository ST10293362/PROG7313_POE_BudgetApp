package vcmsa.projects.prog7313_poe.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.data.repos.CategoryRepository
import vcmsa.projects.prog7313_poe.core.data.repos.ExpenseRepository
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.core.services.AuthService
import java.util.UUID

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val authService: AuthService
) : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> = _expenses

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _currentUser = MutableLiveData<UUID?>()
    val currentUser: LiveData<UUID?> = _currentUser

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            _currentUser.value = user?.id
            user?.id?.let { loadUserData(it) }
        }
    }

    private fun loadUserData(userId: UUID) {
        viewModelScope.launch {
            try {
                val userExpenses = expenseRepository.getByUserId(userId)
                val userCategories = categoryRepository.getByUserId(userId)
                _expenses.value = userExpenses
                _categories.value = userCategories
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.insert(expense)
                categoryRepository.updateTotalAmount(expense.categoryId, expense.amount)
                loadUserData(expense.userId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                val oldExpense = expenseRepository.getById(expense.id)
                expenseRepository.update(expense)
                if (oldExpense != null) {
                    // Update category totals
                    if (oldExpense.categoryId != expense.categoryId) {
                        categoryRepository.updateTotalAmount(oldExpense.categoryId, -oldExpense.amount)
                        categoryRepository.updateTotalAmount(expense.categoryId, expense.amount)
                    } else {
                        val amountDiff = expense.amount - oldExpense.amount
                        categoryRepository.updateTotalAmount(expense.categoryId, amountDiff)
                    }
                }
                loadUserData(expense.userId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteExpense(expenseId: UUID) {
        viewModelScope.launch {
            try {
                val expense = expenseRepository.getById(expenseId)
                if (expense != null) {
                    expenseRepository.deleteById(expenseId)
                    categoryRepository.updateTotalAmount(expense.categoryId, -expense.amount)
                    loadUserData(expense.userId)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryRepository.insert(category)
                loadUserData(category.userId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryRepository.update(category)
                loadUserData(category.userId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteCategory(categoryId: UUID) {
        viewModelScope.launch {
            try {
                val category = categoryRepository.getById(categoryId)
                if (category != null) {
                    categoryRepository.deleteById(categoryId)
                    loadUserData(category.userId)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
} 