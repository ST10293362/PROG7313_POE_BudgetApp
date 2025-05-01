package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import vcmsa.projects.prog7313_poe.core.models.supers.AuditableEntity
import vcmsa.projects.prog7313_poe.core.models.supers.KeyedEntity
import java.time.Instant
import java.util.UUID

/**
 * Represents a user-created account in the financial system.
 *
 * Each account is uniquely identified by a UUID and includes metadata for auditing purposes.
 * It supports Room persistence with foreign key constraints and indexed columns for efficiency.
 *
 * Implements [KeyedEntity] to provide a UUID identifier and [AuditableEntity] for tracking creation and update times.
 *
 * @author ST10257002
 * @author ST13026084
 *
 * @reference https://developer.android.com/training/data-storage/room/defining-data
 * @reference https://developer.android.com/reference/androidx/room/Entity
 * @reference https://developer.android.com/reference/java/time/Instant
 * @reference https://kotlinlang.org/docs/data-classes.html
 */
@Entity(
    tableName = "account",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id_author"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["name", "id_author"], unique = true),
        Index(value = ["id_author"])
    ]
)
data class Account(

    //<editor-fold desc="Inherited members">

    /**
     * Primary key for the account, uniquely identifying each record using a UUID.
     *
     * Inherited from [KeyedEntity].
     */
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    /**
     * Timestamp when the account was created.
     *
     * Inherited from [AuditableEntity].
     */
    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    /**
     * Timestamp when the account was last updated.
     *
     * Inherited from [AuditableEntity].
     */
    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now(),

    //</editor-fold>
    //<editor-fold desc="Entity attributes">

    /**
     * The display name or alias of the account.
     */
    @ColumnInfo(name = "name")
    var name: String,

    //</editor-fold>
    //<editor-fold desc="SQLite relationships">

    /**
     * Foreign key referencing the user (author) who owns this account.
     *
     * Maps to the `id` column in the `User` table.
     */
    @ColumnInfo(name = "id_author")
    var idAuthor: UUID

    //</editor-fold>

) : KeyedEntity, AuditableEntity {
    companion object {
        /** Constant to reference the table name associated with this entity. */
        const val TABLE_NAME = "account"
    }
}
