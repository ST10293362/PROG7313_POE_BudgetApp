package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Database relationship DTO class that links [Category] to [Expense].
 *
 * @author ST10257002
 */
data class CategoryWithExpenses(
    
    @Embedded val category: Category,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id_category"
    )
    val expenses: List<Expense>
    
)