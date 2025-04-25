package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Database relationship DTO class that links [Account] to [Expense].
 *
 * @author ST10257002
 */
data class AccountWithExpenses(

    @Embedded val account: Account,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_account"
    )
    val expenses: List<Expense>

)