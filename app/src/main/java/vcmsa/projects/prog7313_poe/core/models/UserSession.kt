package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Represents a single-entry table storing the currently authorized/logged-in user.
 * This entity is useful for maintaining a persistent login session within Room database.
 *
 * The table should only contain a single row at any time with a fixed primary key of `1`.
 *
 * @author ST10257002
 * @author ST13026084
 *
 * @reference https://developer.android.com/training/data-storage/room/defining-data
 * @reference https://developer.android.com/reference/java/util/UUID
 */
@Entity(
    tableName = "user_session",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class UserSession(

    /**
     * Fixed primary key used to enforce a single-row session table.
     */
    @PrimaryKey
    val id: Int = 1,

    /**
     * UUID of the currently authenticated user.
     */
    @ColumnInfo(name = "user_id")
    val userId: UUID

)