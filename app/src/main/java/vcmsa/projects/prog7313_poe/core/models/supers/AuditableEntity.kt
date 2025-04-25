package vcmsa.projects.prog7313_poe.core.models.supers

import java.time.Instant

/**
 * Interface definition for an entity with auditing properties.
 *
 * @author ST10257002
 */
interface AuditableEntity {

    //<editor-fold desc="Interface members">


    /**
     * The [Instant] at which the entity was created.
     *
     * @author ST10257002
     */
    val createdAt: Instant

    /**
     * The [Instant] at which the entity was last updated.
     *
     * @author ST10257002
     */
    var updatedAt: Instant


    //</editor-fold>
    //<editor-fold desc="Functions">


    /**
     * Informs the entity that it has been updated at this [Instant].
     * This function should be called whenever the entity is updated to ensure
     * the [updatedAt] auditing property is updated.
     *
     * @author ST10257002
     */
    fun touch() {
        updatedAt = Instant.now()
    }


    //</editor-fold>
}