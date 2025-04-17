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
    tableName = "expense",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id_author"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["id_account"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Image::class,
            parentColumns = ["id"],
            childColumns = ["id_document"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["id_category"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(
            value = ["id"],
            unique = true
        )
    ]
)
data class Expense(

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
     * The receipt number or descriptive alias of the transaction.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "detail"
    )
    var detail: String,


    /**
     * The vendor that the transaction was made to to.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "vendor"
    )
    var vendor: String,


    /**
     * The total monetary value of the transaction.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "amount"
    )
    var amount: Double,


    /**
     * The [Date] that the transaction occurred.
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "date_of_expense"
    )
    var dateOfExpense: Date,


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


    /**
     * SQLite Foreign Key relationship to [Account].
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "id_account"
    )
    var idAccount: UUID,


    /**
     * SQLite Foreign Key relationship to [Image].
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "id_document"
    )
    var idDocument: UUID?,


    /**
     * SQLite Foreign Key relationship to [Category].
     *
     * @author ST10257002
     */
    @ColumnInfo(
        name = "id_category"
    )
    var idCategory: UUID?,

    
    //</editor-fold>

) : IKeyed, IAuditable {
    companion object {
        const val TABLE_NAME = "expense"
    }
}