package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class that defines a Room one-to-many relationship between a [User] and their [Expense] records.
 *
 * This class allows Room to return a list of all expenses related to a specific user.
 * The embedded user is the parent entity, and Room joins it with child [Expense] entries
 * using the specified relation.
 *
 * @reference https://developer.android.com/training/data-storage/room/relationships#one-to-many
 *
 */
data class UserWithExpenses(

    /**
     * The [User] entity being embedded. Room includes its fields in the parent result object.
     */
    @Embedded
    val user: User,

    /**
     * The list of [Expense] entities associated with the user.
     *
     * @Relation:
     * - parentColumn: "id" — the primary key from the User table.
     * - entityColumn: "user_id" — the foreign key from the Expense table referencing the user.
     */
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"  // Changed from id_author to match Expense entity
    )
    val expenses: List<Expense> = emptyList()
)
