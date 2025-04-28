package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entity to track the currently authorised user.
 *
 * @author ST10257002
 */
@Entity(
    tableName = "user_session",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class UserSession(

    @PrimaryKey
    val id: Int = 1,


    @ColumnInfo(
        name = "user_id"
    )
    val userId: UUID

)