package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class that defines a Room one-to-many relationship between a [User] and their [Category] entries.
 *
 * This class allows Room to automatically fetch all categories associated with a specific user.
 * The @Embedded annotation includes the user data, and the @Relation annotation describes
 * how the [Category] table references the [User] table.
 *
 * @reference https://developer.android.com/training/data-storage/room/relationships#one-to-many
 *
 * @author ST10257002
 * @author ST13026084
 */
data class UserWithCategories(

    /**
     * The parent [User] entity. Room will embed the user fields directly in the resulting object.
     */
    @Embedded
    val user: User,

    /**
     * A list of [Category] entities that are associated with the user.
     *
     * @Relation:
     * - parentColumn: "id" from the User table (serves as the primary key).
     * - entityColumn: "user_id" from the Category table (serves as the foreign key linking back to the user).
     */
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"  // Changed from id_author to match Category entity
    )
    val categories: List<Category> = emptyList()
)
