package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data Transfer Object (DTO) that represents the relationship between an [Account] and its associated [Expense] entries.
 *
 * This class is used by Room to automatically fetch the list of [Expense] items related to a particular [Account]
 * using a one-to-many relationship. It enables the use of joined queries without manual SQL.
 *
 * The [account] field is the parent entity, and [expenses] is the list of child entities connected via foreign key.
 *
 * @author ST10257002
 * @author ST13026084
 *
 * @reference https://developer.android.com/reference/androidx/room/Relation
 * @reference https://developer.android.com/training/data-storage/room/relationships
 * @reference https://developer.android.com/training/data-storage/room/accessing-data
 */
data class AccountWithExpenses(

    /**
     * The parent [Account] entity.
     *
     * Annotated with `@Embedded` to allow Room to include all account fields directly.
     */
    @Embedded
    val account: Account,

    /**
     * The list of [Expense] entities related to the [Account].
     *
     * Uses Room's `@Relation` annotation to define the link between the `id` column in `Account`
     * and the `account_id` column in the `Expense` table.
     */
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id"  // Ensure this matches the foreign key in the Expense entity
    )
    val expenses: List<Expense> = emptyList()
)
