package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.AccountDao
import vcmsa.projects.prog7313_poe.core.models.Account
import vcmsa.projects.prog7313_poe.core.models.AccountWithExpenses
import java.util.UUID

/**
 * @author ST10257002
 */
class AccountRepository(
    private val dao: AccountDao
) {

    /**
     * @author ST10257002
     */
    suspend fun createAccount(instance: Account): Result<Unit> = runCatching {
        dao.insert(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun updateAccount(instance: Account): Result<Unit> = runCatching {
        instance.touch()
        dao.update(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteAccount(instance: Account): Result<Unit> = runCatching {
        dao.delete(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteAccountById(id: UUID): Result<Unit> = runCatching {
        dao.delete(id)
    }


    /**
     * @author ST10257002
     */
    suspend fun getAllAccounts(): Result<List<Account>> = runCatching {
        dao.fetchAll()
    }

    suspend fun getAccountById(id: UUID): Result<Account?> = runCatching {
        dao.fetchOne(id)
    }

    suspend fun getAccountWithExpenses(id: UUID): Result<AccountWithExpenses> = runCatching {
        dao.fetchAccountWithExpenses(id)
    }
}