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
 * Represents an application user with personal information, credentials, and budget settings.
 * This entity supports Room persistence and includes auditing metadata for creation and update times.
 *
 * Users can define personal goals, profile data, and budgets for expense tracking.
 *
 * @author ST10257002
 * @author ST13026084
 *
 * @reference https://developer.android.com/training/data-storage/room/defining-data
 * @reference https://developer.android.com/reference/kotlin/java/time/Instant
 * @reference https://developer.android.com/reference/java/util/UUID
 */
@Entity(
    tableName = "user",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["username"], unique = true),
        Index(value = ["email_address"], unique = true),
    ]
)
data class User(

    // <editor-fold desc="Inherited members">

    /**
     * Unique identifier for the user.
     */
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    /**
     * Timestamp when the user was created.
     */
    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    /**
     * Timestamp when the user was last updated.
     */
    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now(),

    // </editor-fold>
    // <editor-fold desc="Entity attributes">

    /**
     * The user's unique login name.
     */
    @ColumnInfo(name = "username")
    var username: String,

    /**
     * The user's hashed password.
     */
    @ColumnInfo(name = "password")
    var password: String,

    /**
     * Salt used for hashing the user's password.
     */
    @ColumnInfo(name = "password_salt")
    var passwordSalt: String,

    /**
     * The user's given name.
     */
    @ColumnInfo(name = "name")
    var name: String,

    /**
     * The user's surname or family name.
     */
    @ColumnInfo(name = "surname")
    var surname: String,

    /**
     * User's date of birth.
     */
    @ColumnInfo(name = "date_of_birth")
    var dateOfBirth: Date?,

    /**
     * Optional user cellphone number.
     */
    @ColumnInfo(name = "cell_number")
    var cellNumber: String?,

    /**
     * User's unique email address.
     */
    @ColumnInfo(name = "email_address")
    var emailAddress: String,

    /**
     * User's minimum monthly spending goal.
     */
    @ColumnInfo(name = "min_goal")
    var minGoal: Double?,

    /**
     * User's maximum monthly spending goal.
     */
    @ColumnInfo(name = "max_goal")
    var maxGoal: Double?,

    /**
     * Optional URI path to the user's profile image.
     */
    @ColumnInfo(name = "image_uri")
    var imageUri: String? = null,

    /**
     * Flag indicating whether spending goals are set.
     */
    @ColumnInfo(name = "goals_set")
    var goalsSet: Boolean = false,

    /**
     * Flag indicating whether profile setup is complete.
     */
    @ColumnInfo(name = "profile_completed")
    var profileCompleted: Boolean = false,

    /**
     * Monthly budget allocated by the user.
     */
    @ColumnInfo(name = "monthly_budget")
    var monthlyBudget: Double = 0.0,

    /**
     * User's remaining or currently available budget.
     */
    @ColumnInfo(name = "current_budget")
    var currentBudget: Double = 0.0,

    /**
     * Timestamp of the last budget reset in milliseconds since epoch.
     */
    @ColumnInfo(name = "budget_last_reset")
    var budgetLastReset: Long = System.currentTimeMillis()

    // </editor-fold>

) : KeyedEntity, AuditableEntity {

    companion object {
        /** Constant table name for Room use. */
        const val TABLE_NAME = "user"
    }
}
