package vcmsa.projects.prog7313_poe.data.dao

import androidx.room.*
import vcmsa.projects.prog7313_poe.core.models.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM account WHERE id = :accountId")
    fun getAccountById(accountId: String): Account?

    @Query("SELECT * FROM account WHERE id_author = :userId")
    fun getAccountsByUserId(userId: String): List<Account>

    @Query("SELECT * FROM account")
    fun getAllAccounts(): List<Account>

    @Query("SELECT EXISTS(SELECT 1 FROM account WHERE id = :accountId)")
    fun exists(accountId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(account: Account)

    @Update
    fun updateAccount(account: Account)

    @Delete
    fun deleteAccount(account: Account)

    @Query("DELETE FROM account WHERE id = :accountId")
    fun deleteAccountById(accountId: String)
} 