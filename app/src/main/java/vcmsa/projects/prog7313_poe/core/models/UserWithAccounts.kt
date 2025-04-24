package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Database relationship DTO class that links [User] to [Account].
 *
 * @author ST10257002
 */
data class UserWithAccounts(
    
    @Embedded val user: User,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id_author"
    )
    val accounts: List<Account>
    
)