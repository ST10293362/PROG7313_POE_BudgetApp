package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import vcmsa.projects.prog7313_poe.core.models.supers.IAuditable
import vcmsa.projects.prog7313_poe.core.models.supers.IKeyed
import java.time.Instant
import java.util.Date
import java.util.UUID

/**
 * @author ST10257002
 */
@Entity(
    tableName = "user",
    foreignKeys = [
        ForeignKey(
            entity = Image::class,
            parentColumns = ["id"],
            childColumns = ["id_avatar"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["id"],
            unique = true
        )
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


    //</editor-fold>
    //<editor-fold desc="SQLite relationships">


    /**
     * SQLite Foreign Key relationship to [Image].
     * 
     * Connects the profile image of the user to its profile.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "id_avatar"
    )
    var idAvatar: UUID?


    //</editor-fold>

) : IKeyed, IAuditable {
    companion object {
        const val TABLE_NAME = "user"
    }
}