package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithExpenses(
    @Embedded
    val category: Category,

    @Relation(
        parentColumn = "id",
        entityColumn = "category_id" // Changed from id_category to match Expense column name
    )
    val expenses: List<Expense>
) {
    // Add no-args constructor for Room
    constructor() : this(Category(), emptyList())
}