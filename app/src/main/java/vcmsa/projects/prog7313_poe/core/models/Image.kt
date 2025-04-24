package vcmsa.projects.prog7313_poe.core.models

import android.graphics.Bitmap
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
    tableName = "image",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id_author"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["id_author"])
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


    /**
     * The image data stored as a [Bitmap].
     *
     * In the room SQLite database, this information is stored as a [ByteArray]
     * and then reassembled into a complex [Bitmap] when retrieved. This ensures
     * that images are not corrupted as would happen in a directory/URI-based
     * approach to image storage if directories were inadvertently altered.
     *
     * @see [vcmsa.projects.prog7313_poe.core.data.converters.ImageConverter]
     * @author ST10257002
     */
    @ColumnInfo(
        name = "byte_array"
    )
    val bitmap: Bitmap,


    //</editor-fold>
    //<editor-fold desc="SQLite relationships">


    /**
     * SQLite Foreign Key relationship to [User].
     *
     * @author ST10257002
     */
    @ColumnInfo(name = "id_author")
    var idAuthor: UUID,


    //</editor-fold>

) : KeyedEntity, AuditableEntity {
    companion object {
        const val TABLE_NAME = "image"
    }
}