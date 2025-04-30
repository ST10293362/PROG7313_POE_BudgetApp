package vcmsa.projects.prog7313_poe.core.data.repos

import androidx.lifecycle.LiveData
import vcmsa.projects.prog7313_poe.core.data.access.ExpenseDao
import vcmsa.projects.prog7313_poe.core.models.Expense
import java.util.UUID

/**
 * @author ST10326084
 * @author ST10257002
 */
class ExpenseRepository(
    private val dao: ExpenseDao
) {

    /**
     * @author ST10326084
     * @author ST10257002
     */
    suspend fun createExpense(instance: Expense) {
        dao.insert(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun updateExpense(instance: Expense) {
        instance.touch()
        dao.update(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteExpense(instance: Expense) {
        dao.delete(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteExpenseById(id: UUID) {
        dao.delete(id)
    }


    /**
     * @author ST10326084
     * @author ST10257002
     */
    suspend fun getAllExpenses(): List<Expense> {
        return dao.getAllExpenses()
    }
}