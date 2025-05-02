package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
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
    tableName = "user_sessions",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId")
    ]
)
data class UserSession(
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val token: String,
    val expiresAt: Instant,
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = Instant.now()
) : KeyedEntity, AuditableEntity
