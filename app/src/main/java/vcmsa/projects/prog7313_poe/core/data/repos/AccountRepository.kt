package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.AccountDao
import vcmsa.projects.prog7313_poe.core.models.Account
import java.util.UUID

/**
 * Repository for handling operations on [Account] data models using [AccountDao].
 * Provides basic CRUD operations for accounts.
 *
 * @author ST10257002
 * @author ST10326084
 * @since 2024
 */
class AccountRepository(
    private val dao: AccountDao
) {
    /**
     * Creates a new account entry in the database.
     *
     * @param instance The [Account] object to be inserted.
     * @return A [Result] indicating success or error.
     */
    suspend fun createAccount(instance: Account): Result<Unit> = runCatching {
        dao.insert(instance)
    }

    /**
     * Updates an existing account.
     *
     * @param instance The account to update.
     * Automatically refreshes the `updatedAt` field via `touch()`.
     */
    suspend fun updateAccount(instance: Account): Result<Unit> = runCatching {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Deletes an account entity from the database.
     *
     * @param instance The [Account] to delete.
     */
    suspend fun deleteAccount(instance: Account): Result<Unit> = runCatching {
        dao.delete(instance)
    }

    /**
     * Deletes an account by its unique identifier.
     *
     * @param id UUID of the target account.
     */
    suspend fun deleteAccountById(id: UUID): Result<Unit> = runCatching {
        dao.delete(id)
    }

    /**
     * Retrieves all account records from the database.
     *
     * @return A list of [Account]s wrapped in a [Result].
     */
    suspend fun getAllAccounts(): Result<List<Account>> = runCatching {
        dao.fetchAll()
    }

    /**
     * Retrieves a specific account by its UUID.
     *
     * @param id The unique identifier of the account.
     * @return The corresponding [Account] or null if not found.
     */
    suspend fun getAccountById(id: UUID): Result<Account?> = runCatching {
        dao.fetchOne(id)
    }

    /**
     * Checks whether an account with the given ID exists in the database.
     *
     * @param id The unique identifier of the account to check.
     * @return true if the account exists, false otherwise.
     */
    suspend fun exists(id: UUID): Boolean = dao.exists(id)
}
