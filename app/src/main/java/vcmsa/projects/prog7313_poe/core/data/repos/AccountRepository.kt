package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.AccountDao
import vcmsa.projects.prog7313_poe.core.models.Account
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
    suspend fun createAccount(instance: Account) {
        dao.insert(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun updateAccount(instance: Account) {
        dao.updateAuditable(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteAccount(instance: Account) {
        dao.delete(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteAccountById(id: UUID) {
        dao.delete(id)
    }


    /**
     * @author ST10257002
     */
    suspend fun getAllAccounts(): List<Account> {
        return dao.fetchAll()
    }
}