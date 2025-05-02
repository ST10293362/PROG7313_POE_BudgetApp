package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import vcmsa.projects.prog7313_poe.core.models.Account
import vcmsa.projects.prog7313_poe.core.models.AccountWithExpenses
import java.util.UUID

/**
 * DAO (Data Access Object) for managing [Account] entities using Room.
 * Provides CRUD operations and additional queries involving account relationships.
 *
 * @see BaseDao - Generic base interface for common insert/update/delete
 * @see androidx.room.Dao - Room's DAO interface marker
 *
 *
 * @reference https://developer.android.com/training/data-storage/room
 */

@Dao
interface AccountDao : BaseDao<Account> {

    //<editor-fold desc="CRUD Operations">

    /**
     * Deletes an account from the database by its UUID.
     *
     * @param targetId The UUID of the account to be deleted.
     *
     * @reference https://developer.android.com/reference/androidx/room/Query
     */
    @Query(
        """
        DELETE FROM account 
        WHERE id = :targetId
        """
    )
    suspend fun delete(targetId: UUID)

    /**
     * Retrieves all accounts from the database.
     *
     * @return A list of all stored [Account] entities.
     */
    @Query("SELECT * FROM account")
    suspend fun fetchAll(): List<Account>

    /**
     * Retrieves a specific account by its ID.
     *
     * @param targetId UUID of the account to retrieve.
     * @return The account with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM account WHERE id = :targetId")
    suspend fun fetchOne(targetId: UUID): Account?

    //</editor-fold>

    //<editor-fold desc="Extensions">

    /**
     * Checks whether an account exists for the given ID.
     *
     * @param targetId UUID of the account to check.
     * @return True if the account exists, false otherwise.
     */
    @Query(
        """
        SELECT EXISTS(
            SELECT * FROM account WHERE id = :targetId
        )
        """
    )
    suspend fun exists(targetId: UUID): Boolean

    /**
     * Retrieves an [AccountWithExpenses] object — a composite object representing
     * an account and all of its associated expenses.
     *
     * This uses Room's `@Transaction` annotation to ensure data consistency when
     * fetching relations.
     *
     * @param targetId UUID of the account.
     * @return The composite [AccountWithExpenses] object.
     *
     * @reference https://developer.android.com/reference/androidx/room/Transaction
     */
    @Transaction
    @Query("SELECT * FROM account WHERE id = :targetId")
    suspend fun fetchAccountWithExpenses(targetId: UUID): AccountWithExpenses

    //</editor-fold>
}