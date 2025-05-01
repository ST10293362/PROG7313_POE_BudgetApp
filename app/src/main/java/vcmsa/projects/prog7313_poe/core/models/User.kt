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

@Entity(
    tableName = "user",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["username"], unique = true),
        Index(value = ["email_address"], unique = true),
    ]
)
data class User(
    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "password_salt")
    var passwordSalt: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "surname")
    var surname: String,

    @ColumnInfo(name = "date_of_birth")
    var dateOfBirth: Date?,

    @ColumnInfo(name = "cell_number")
    var cellNumber: String?,

    @ColumnInfo(name = "email_address")
    var emailAddress: String,

    @ColumnInfo(name = "min_goal")
    var minGoal: Double?,

    @ColumnInfo(name = "max_goal")
    var maxGoal: Double?,

    @ColumnInfo(name = "image_uri")
    var imageUri: String? = null,

    @ColumnInfo(name = "goals_set")
    var goalsSet: Boolean = false,

    @ColumnInfo(name = "profile_completed")
    var profileCompleted: Boolean = false,

    @ColumnInfo(name = "monthly_budget")
    var monthlyBudget: Double = 0.0,

    @ColumnInfo(name = "current_budget")
    var currentBudget: Double = 0.0,

    @ColumnInfo(name = "budget_last_reset")
    var budgetLastReset: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "created_at")
    override val createdAt: Instant = Instant.now(),

    @ColumnInfo(name = "updated_at")
    override var updatedAt: Instant = Instant.now()
) : KeyedEntity, AuditableEntity {
    companion object {
        const val TABLE_NAME = "user"
    }
}