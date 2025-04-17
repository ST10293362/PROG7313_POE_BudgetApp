package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import vcmsa.projects.prog7313_poe.core.models.supers.IAuditable
import vcmsa.projects.prog7313_poe.core.models.supers.IKeyed
import vcmsa.projects.prog7313_poe.core.models.supers.IStashable
import java.time.Instant
import java.util.UUID

/**
 * @author ST10257002
 */
@Entity(
    tableName = "trophy",
    indices = [
        Index(
            value = ["id"],
            unique = true
        )
    ]
)
data class Trophy(

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

    
    @ColumnInfo(
        name = "deleted_at"
    )
    override var stashedAt: Instant?,


    //</editor-fold>
    //<editor-fold desc="Entity attributes">


    /**
     * The alias of the trophy.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "name"
    )
    var name: String,


    /**
     * Description of the trophy.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "description"
    )
    var description: String?,


    //</editor-fold>

) : IKeyed, IAuditable, IStashable {
    companion object {
        const val TABLE_NAME = "trophy"
    }
}