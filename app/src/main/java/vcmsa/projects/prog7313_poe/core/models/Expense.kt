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
 * Represents an individual financial expense entry linked to a user, category, and account.
 *
 * The [Expense] entity includes timestamp metadata (via [AuditableEntity]), the amount spent,
 * a descriptive field, a date range (start and optional end), and foreign keys to related entities.
 *
 * It is used with Room for persistence and includes indexing for optimized queries.
 *
 * @author ST10257002
 * @author ST13026084
 *
 * @reference https://developer.android.com/training/data-storage/room/defining-data
 * @reference https://developer.android.com/reference/androidx/room/Entity
 * @reference https://developer.android.com/reference/kotlin/java/time/Instant
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
            childColumns = ["account_id"]
        )
    ]
)
data class Expense(

    /**
     * Primary key unique identifier for the expense.
     */
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    /**
     * Timestamp of when the expense was created.
     */
    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    /**
     * Timestamp of the last time this record was updated.
     */
    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now(),

    /**
     * The amount spent in this expense entry.
     */
    @ColumnInfo(name = "amount")
    val amount: Double,

    /**
     * Optional textual description of the expense.
     */
    @ColumnInfo(name = "description")
    val description: String,

    /**
     * The start date of the expense period.
     */
    @ColumnInfo(name = "start_date")
    val startDate: Instant,

    /**
     * The optional end date of the expense period.
     */
    @ColumnInfo(name = "end_date")
    val endDate: Instant? = null,

    /**
     * Foreign key reference to the user who recorded this expense.
     */
    @ColumnInfo(name = "user_id")
    val userId: UUID,

    /**
     * Foreign key reference to the account used for the expense.
     */
    @ColumnInfo(name = "account_id")
    val accountId: UUID,

    /**
     * Foreign key reference to the category associated with this expense.
     */
    @ColumnInfo(name = "category_id")
    val categoryId: UUID

) : AuditableEntity, KeyedEntity {

    companion object {
        /** Constant for Room table name. */
        const val TABLE_NAME = "expense"
    }
}
