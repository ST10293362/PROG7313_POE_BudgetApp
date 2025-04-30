package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import vcmsa.projects.prog7313_poe.core.models.supers.AuditableEntity
import vcmsa.projects.prog7313_poe.core.models.supers.KeyedEntity
import java.time.Instant
import java.util.Date
import java.util.UUID

/**
 * @author ST10257002
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

    //<editor-fold desc="Inherited members">


    @PrimaryKey
    override val id: UUID = UUID.randomUUID(),


    @ColumnInfo(
        name = "created_at"
    )
    override val createdAt: Instant = Instant.now(),


    @ColumnInfo(
        name = "updated_at"
    )
    override var updatedAt: Instant = Instant.now(),


    //</editor-fold>
    //<editor-fold desc="Entity attributes">


    /**
     * @author ST10257002
     */
    @ColumnInfo(
        name = "username"
    )
    var username: String,


    /**
     * @author ST10257002
     */
    @ColumnInfo(
        name = "password"
    )
    var password: String,


    @ColumnInfo(
        name = "password_salt"
    )
    var passwordSalt: String,


    /**
     * @author ST10257002
     */
    @ColumnInfo(
        name = "name"
    )
    var name: String,


    /**
     * @author ST10257002
     */
    @ColumnInfo(
        name = "surname"
    )
    var surname: String,


    /**
     * @author ST10257002
     */
    @ColumnInfo(
        name = "date_of_birth"
    )
    var dateOfBirth: Date?,


    /**
     * @author ST10257002
     */
    @ColumnInfo(
        name = "cell_number"
    )
    var cellNumber: String?,


    /**
     * @author ST10257002
     */
    @ColumnInfo(
        name = "email_address"
    )
    var emailAddress: String,


    /**
     * The minimum monthly spending goal for every expense created by this
     * [User].
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "min_goal"
    )
    var minGoal: Double?,


    /**
     * The maximum monthly spending goal for every expense created by this
     * [User].
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "max_goal"
    )
    var maxGoal: Double?,

    
    @ColumnInfo(name = "image_uri")
    var imageUri: String? = null,


    /**
     * Indicates whether the user has set their spending goals
     */
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

    //</editor-fold>

) : KeyedEntity, AuditableEntity {
    companion object {
        const val TABLE_NAME = "user"
    }
}