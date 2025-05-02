package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import vcmsa.projects.prog7313_poe.data.converters.StringListConverter
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
@Entity(tableName = "expenses")
@TypeConverters(StringListConverter::class)
data class Expense(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "userId")
    val userId: UUID,
    @ColumnInfo(name = "categoryId")
    val categoryId: UUID,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date")
    val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "photos")
    val photos: List<String> = emptyList()
) {

    companion object {
        /** Constant for Room table name. */
        const val TABLE_NAME = "expenses"
    }
}
