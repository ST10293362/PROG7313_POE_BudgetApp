package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Database relationship DTO class that links [User] to [Category].
 *
 * @author ST10257002
 */
data class UserWithCategories(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"  // Changed from id_author to match Category entity
    )
    val categories: List<Category> = emptyList()
)