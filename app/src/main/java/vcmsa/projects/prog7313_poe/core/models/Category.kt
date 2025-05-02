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
 *
 * @reference https://developer.android.com/training/data-storage/room/defining-data
 * @reference https://developer.android.com/reference/androidx/room/Entity
 * @reference https://developer.android.com/reference/java/time/Instant
 * @reference https://kotlinlang.org/docs/data-classes.html
 */
@Entity(
    tableName = "category",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["name"], unique = true),
        Index(value = ["user_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Category(

    /**
     * Unique identifier for the category.
     */
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    /**
     * Timestamp of category creation.
     */
    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    /**
     * Timestamp of last update.
     */
    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now(),

    /**
     * The name of the category.
     */
    @ColumnInfo(name = "name")
    val name: String = "",

    /**
     * The UUID of the user who owns this category.
     * Acts as a foreign key to the [User] table.
     */
    @ColumnInfo(name = "user_id")
    val userId: UUID = UUID.randomUUID(),

    /**
     * Indicates if the category is the default one for the user.
     */
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,

    /**
     * Running total of all expenses assigned to this category.
     */
    @ColumnInfo(name = "total_amount")
    var totalAmount: Double = 0.0

) : AuditableEntity, KeyedEntity {

    companion object {
        /** Constant to access the table name for Room-related queries. */
        const val TABLE_NAME = "category"
    }

    /**
     * Secondary no-args constructor required by Room when using default values.
     */
    constructor() : this(
        name = "",
        userId = UUID.randomUUID()
    )
}