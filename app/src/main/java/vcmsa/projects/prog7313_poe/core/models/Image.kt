package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
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
    tableName = "image",
    indices = [
        Index(
            value = ["id"],
            unique = true
        )
    ]
)
data class Image(

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


    // TODO


    //</editor-fold>
    //<editor-fold desc="SQLite relationships">


    // TODO


    //</editor-fold>

) : KeyedEntity, AuditableEntity {
    companion object {
        const val TABLE_NAME = "image"
    }
}