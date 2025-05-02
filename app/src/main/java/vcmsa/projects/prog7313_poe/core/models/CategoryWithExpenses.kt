package vcmsa.projects.prog7313_poe.core.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a Room relationship between a [Category] and its related [Expense] entries.
 *
 * This DTO is used by Room to perform joined queries where each category includes its associated expenses.
 * The [@Relation] annotation handles the one-to-many mapping between `Category.id` and `Expense.category_id`.
 *
 * The default constructor is included for Room compatibility when using query results.
 *
 * @author ST10257002
 * @author ST13026084
 *
 * @reference https://developer.android.com/training/data-storage/room/relationships
 * @reference https://developer.android.com/reference/androidx/room/Relation
 * @reference https://kotlinlang.org/docs/data-classes.html
 */
data class CategoryWithExpenses(

    /**
     * The parent [Category] entity.
     *
     * Annotated with `@Embedded` so Room can deconstruct this field and include all columns of the `Category`.
     */
    @Embedded
    val category: Category,

    /**
     * List of [Expense] items associated with this [Category].
     *
     * Room automatically maps this using the foreign key relationship between `category.id` and `expense.category_id`.
     */
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id" // Must match the foreign key in the Expense entity
    )
    val expenses: List<Expense>

) {
    /**
     * Secondary no-argument constructor required by Room when using this DTO in queries.
     */
    constructor() : this(Category(), emptyList())
}