package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Represents a single-entry table storing the currently authorized/logged-in user.
 * Used for session persistence in Room.
 *
 */
@Entity(
    tableName = "user_session",
    indices = [Index(value = ["id"], unique = true)]
)
data class UserSession(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "user_id") val userId: UUID
)
