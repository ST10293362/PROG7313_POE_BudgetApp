package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import vcmsa.projects.prog7313_poe.core.models.supers.IAuditable
import java.time.Instant
import java.util.Date
import java.util.UUID

/**
 * SQLite composite key entity.
 * 
 * Tables that use composite keys do not have an ID of their own. Instead, two
 * columns are both the primary keys and the foreign keys. The purpose of this
 * entity is to link the two tables indirectly to avoid duplicates.
 * 
 * @author ST10257002
 */
@Entity(
    tableName = "user_trophy",
    primaryKeys = ["id_author", "id_trophy"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id_author"],
            onDelete = ForeignKey.CASCADE // TODO: Confirm correct behaviour
        ),
        ForeignKey(
            entity = Trophy::class,
            parentColumns = ["id"],
            childColumns = ["id_trophy"],
            onDelete = ForeignKey.CASCADE // TODO: Confirm correct behaviour
        )
    ],
    indices = [
        Index( // TODO: Confirm correct behaviour
            value = ["id_author"],
            unique = true
        ),
        Index( // TODO: Confirm correct behaviour
            value = ["id_trophy"],
            unique = true
        )
    ]
)
data class UserTrophy(

    //<editor-fold desc="Inherited members">


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
        name = "date_start"
    )
    var dateOfStart: Date?,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "date_final"
    )
    var dateOfFinal: Date?,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "progress_now"
    )
    var progressNow: Int,


    /**
     * TODO: Javadoc
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "progress_max"
    )
    var progressMax: Int,


    //</editor-fold>
    //<editor-fold desc="SQLite relationships">


    /**
     * SQLite composite key referencing [Trophy] ([idTrophy]) and [User]
     * ([idAuthor]).
     *
     * @author ST10257002
     */
    val idTrophy: UUID,


    /**
     * SQLite composite key referencing [Trophy] ([idTrophy]) and [User]
     * ([idAuthor]).
     *
     * @author ST10257002
     */
    val idAuthor: UUID,


    //</editor-fold>

) : IAuditable