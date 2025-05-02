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
 * Represents a category used to classify and group financial transactions or expenses.
 *
 * Each category belongs to a user and may be marked as default.
 * It also keeps a running total of all associated expenses (denormalized).
 *
 * Implements [KeyedEntity] for UUID identification and [AuditableEntity] for timestamps.
 * Used in Room persistence with foreign key to the [User] entity.
 *
 * Includes a no-argument constructor for Room's instantiation requirements.
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
    tableName = "categories",
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
data class Category(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    
    @ColumnInfo(name = "userId")
    val userId: UUID,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "color")
    val color: String,
    
    @ColumnInfo(name = "totalAmount")
    val totalAmount: Double = 0.0,
    
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now()
) : KeyedEntity, AuditableEntity {

    companion object {
        /** Constant to access the table name for Room-related queries. */
        const val TABLE_NAME = "categories"
    }
}
