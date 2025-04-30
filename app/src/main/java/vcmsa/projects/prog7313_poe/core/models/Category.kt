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
 * @author ST10257002
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
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now(),

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "user_id")
    val userId: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,

    @ColumnInfo(name = "total_amount")
    var totalAmount: Double = 0.0
) : AuditableEntity, KeyedEntity {
    companion object {
        const val TABLE_NAME = "category"
    }

    // Add no-args constructor for Room
    constructor() : this(
        name = "",
        userId = UUID.randomUUID()
    )
}
