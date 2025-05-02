package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class representing a Room database relationship between a [User] and their [Account]s.
 *
 * This class is used by Room to model a one-to-many relationship: a single user can be associated
 * with multiple accounts. The @Embedded annotation includes the [User] object directly, and the
 * @Relation annotation specifies how the associated [Account] records are retrieved.
 *
 * @reference https://developer.android.com/training/data-storage/room/relationships#one-to-many
 *

 */
data class UserWithAccounts(

    /**
     * The parent entity in the relationship.
     * Room embeds the [User] fields directly into the result.
     */
    @Embedded val user: User,

    /**
     * A list of [Account] entities associated with the user.
     *
     * @Relation:
     * - parentColumn: Refers to the primary key column in the User table ("id").
     * - entityColumn: Refers to the foreign key column in the Account table ("id_author").
     */
    @Relation(
        parentColumn = "id",
        entityColumn = "id_author"
    )
    val accounts: List<Account>

)
