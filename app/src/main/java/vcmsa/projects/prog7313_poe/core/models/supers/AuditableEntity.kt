package vcmsa.projects.prog7313_poe.core.models.supers

import java.time.Instant

/**
 * Interface definition for an entity with auditing properties.
 * Entities implementing this interface will track creation and last updated timestamps.
 *
 * Commonly used in applications where tracking data modification history is important.
 *
 * @author ST10257002
 * @author ST13026084
 * @reference Android Developer Documentation – https://developer.android.com
 */
interface AuditableEntity {

    //<editor-fold desc="Interface members">

    /**
     * The [Instant] at which the entity was created.
     * This should be set once during entity initialization and not modified afterwards.
     *
     * @author ST10257002
     * @author ST13026084
     */
    val createdAt: Instant

    /**
     * The [Instant] at which the entity was last updated.
     * This value is updated every time the entity is modified.
     *
     * @author ST10257002
     * @author ST13026084
     */
    var updatedAt: Instant

    //</editor-fold>
    //<editor-fold desc="Functions">

    /**
     * Informs the entity that it has been updated at the current [Instant].
     * Should be called before saving the entity to persist the update timestamp.
     *
     * @author ST10257002
     * @author ST13026084
     */
    fun touch() {
        updatedAt = Instant.now()
    }

    //</editor-fold>
}
