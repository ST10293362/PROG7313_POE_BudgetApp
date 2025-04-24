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
    tableName = "account",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id_author"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["name", "id_author"], unique = true),
        Index(value = ["id_author"])
    ]
)
data class Account(

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
     * The alias of the account.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "name"
    )
    var name: String,


    //</editor-fold>
    //<editor-fold desc="SQLite relationships">


    /**
     * SQLite Foreign Key relationship to [User].
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "id_author"
    )
    var idAuthor: UUID,


    //</editor-fold>

) : KeyedEntity, AuditableEntity {
    companion object {
        const val TABLE_NAME = "account"
    }
}