package vcmsa.projects.prog7313_poe.core.data.repos

import androidx.lifecycle.LiveData
import vcmsa.projects.prog7313_poe.core.data.access.ExpenseDao
import vcmsa.projects.prog7313_poe.core.models.Expense
import java.util.UUID

/**
 * Repository for managing [Expense] data via [ExpenseDao].
 * Provides CRUD operations and a LiveData stream of all expenses
 * for UI observation.
 *
 * Acts as the data abstraction layer for view models.
 *
 * @author ST10326084
 * @author ST10257002
 */

class ExpenseRepository(
    private val dao: ExpenseDao
) {

    /**
     * Inserts a new expense into the database.
     *
     * @param instance The [Expense] entity to insert.
     */
    suspend fun createExpense(instance: Expense) {
        dao.insert(instance)
    }

    /**
     * Updates an existing expense and refreshes its audit timestamp.
     *
     * @param instance The [Expense] entity to update.
     */
    suspend fun updateExpense(instance: Expense) {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Deletes an expense from the database.
     *
     * @param instance The [Expense] entity to delete.
     */
    suspend fun deleteExpense(instance: Expense) {
        dao.delete(instance)
    }

    /**
     * Deletes an expense based on its UUID.
     *
     * @param id UUID of the [Expense] to delete.
     */
    suspend fun deleteExpenseById(id: UUID) {
        dao.delete(id)
    }

    /**
     * Retrieves a live list of all [Expense] entities from the database.
     * Useful for automatic UI updates when data changes.
     *
     * @return [LiveData] stream of the full expense list.
     */
    fun getAllExpenses(): LiveData<List<Expense>> {
        return dao.fetchAll()
    }
}
