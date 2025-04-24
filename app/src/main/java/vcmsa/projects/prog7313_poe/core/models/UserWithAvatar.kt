package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Database relationship DTO class that links [User] to [Image].
 *
 * @author ST10257002
 */
data class UserWithAvatar(
    
    @Embedded val user: User,
    
    @Relation(
        parentColumn = "id_avatar",
        entityColumn = "id"
    )
    val avatar: Image?
    
)