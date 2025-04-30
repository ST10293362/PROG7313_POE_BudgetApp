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
    tableName = "expense",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["user_id"]),
        Index(value = ["category_id"]),
        Index(value = ["account_id"])

    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
        )
    ]
)
data class Expense(
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now(),

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "start_date")
    val startDate: Instant,

    @ColumnInfo(name = "end_date")
    val endDate: Instant? = null,

    @ColumnInfo(name = "user_id")
    val userId: UUID,

    @ColumnInfo(name = "account_id")
    val accountId: UUID,

    @ColumnInfo(name = "category_id")
    val categoryId: UUID
) : AuditableEntity, KeyedEntity {
    companion object {
        const val TABLE_NAME = "expense"
    }


}