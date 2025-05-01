package vcmsa.projects.prog7313_poe.core.models.supers

import java.util.UUID

/**
 * Interface definition for an identifiable entity.
 * Any entity implementing this interface must provide a unique [UUID] as its identifier.
 *
 * Useful in database operations, where a unique key is required for each record.
 *
 * @author ST10257002
 * @author ST13026084
 * @reference Kotlin Language Documentation – Interfaces: https://kotlinlang.org/docs/interfaces.html
 */
interface KeyedEntity {

    //<editor-fold desc="Interface members">

    /**
     * Unique identifier of the entity.
     * Typically generated once and used as the primary key for database persistence.
     *
     * @see [vcmsa.projects.prog7313_poe.core.data.converters.UuidConverter]
     * @author ST10257002
     * @author ST13026084
     */
    val id: UUID

    //</editor-fold>
}
