package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import vcmsa.projects.prog7313_poe.core.models.supers.AuditableEntity
import vcmsa.projects.prog7313_poe.core.models.supers.KeyedEntity
import java.time.Instant
import java.util.Date
import java.util.UUID

/**
 * Entity representing an application user in the Room database.
 *
 * Includes personal info, authentication credentials, savings goals,
 * budgeting data, and progress indicators (e.g., profile completion).
 *
 * Inherits audit properties from [AuditableEntity] and uniquely identifies
 * each row with [KeyedEntity].
 *
 * @reference https://developer.android.com/training/data-storage/room/defining-data
 * @reference https://developer.android.com/reference/androidx/room/Entity
 *
 * @see AuditableEntity
 * @see KeyedEntity
 *
 * @author ST10257002
 * @author ST10326084
 */
@Entity(
    tableName = "user",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["username"], unique = true),
        Index(value = ["email_address"], unique = true)
    ]
)
data class User(
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    /** Unique username for login. */
    @ColumnInfo(name = "username")
    var username: String,

    /** Encrypted password (hash). */
    @ColumnInfo(name = "password")
    var password: String,

    /** Salt used for password hashing. */
    @ColumnInfo(name = "password_salt")
    var passwordSalt: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "surname")
    var surname: String,

    /** Optional date of birth. */
    @ColumnInfo(name = "date_of_birth")
    var dateOfBirth: Date?,

    /** Optional mobile number. */
    @ColumnInfo(name = "cell_number")
    var cellNumber: String?,

    /** Unique email address. */
    @ColumnInfo(name = "email_address")
    var emailAddress: String,

    /** Optional minimum monthly savings goal. */
    @ColumnInfo(name = "min_goal")
    var minGoal: Double?,

    /** Optional maximum monthly savings goal. */
    @ColumnInfo(name = "max_goal")
    var maxGoal: Double?,

    /** Optional URI for the user’s profile image. */
    @ColumnInfo(name = "image_uri")
    var imageUri: String? = null,

    /** Flag indicating if savings goals are configured. */
    @ColumnInfo(name = "goals_set")
    var goalsSet: Boolean = false,

    /** Flag for tracking profile completion. */
    @ColumnInfo(name = "profile_completed")
    var profileCompleted: Boolean = false,

    /** User-defined monthly budget. */
    @ColumnInfo(name = "monthly_budget")
    var monthlyBudget: Double = 0.0,

    /** Budget available for current period. */
    @ColumnInfo(name = "current_budget")
    var currentBudget: Double = 0.0,

    /** Timestamp (ms) for last budget reset. */
    @ColumnInfo(name = "budget_last_reset")
    var budgetLastReset: Long = System.currentTimeMillis(),

    /** Auto-generated creation timestamp. */
    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    /** Auto-updated last modification timestamp. */
    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now()
) : KeyedEntity, AuditableEntity {
    companion object {
        const val TABLE_NAME = "user"
    }
}
