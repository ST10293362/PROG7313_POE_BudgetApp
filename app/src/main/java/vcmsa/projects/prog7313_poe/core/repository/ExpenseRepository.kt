package vcmsa.projects.prog7313_poe.core.repository

/**
 * @author ST10326084
 */
import vcmsa.projects.prog7313_poe.core.data.access.ExpenseDao
import vcmsa.projects.prog7313_poe.core.models.Expense

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun getAllExpenses(): List<Expense> {
        return expenseDao.fetchAll()
    }
}
