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
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "username"
    )
    var username: String,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "password"
    )
    var password: String,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "name"
    )
    var name: String,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "surname"
    )
    var surname: String,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "date_of_birth"
    )
    var dateOfBirth: Date?,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "cell_number"
    )
    var cellNumber: String?,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "email_address"
    )
    var emailAddress: String,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "min_goal"
    )
    var minGoal: Double?,


    /**
     * TODO: Javadoc
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
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "id_avatar"
    )
    var idAvatar: UUID?


    //</editor-fold>

) : IKeyed, IAuditable