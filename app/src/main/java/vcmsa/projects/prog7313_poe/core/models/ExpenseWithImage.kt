package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Database relationship DTO class that links [Expense] to [Image].
 *
 * @author ST10257002
 */
data class ExpenseWithImage(

    @Embedded val expense: Expense,

    @Relation(
        parentColumn = "id_document",
        entityColumn = "id"
    )
    val image: Image?

)