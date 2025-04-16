package vcmsa.projects.prog7313_poe.core.models.supers

import java.time.Instant

/**
 * Interface definition for a soft-deletable entity.
 *
 * @author ST10257002
 */
interface IStashable {

    //<editor-fold desc="Interface members">


    /**
     * The [Instant] at which the entity was soft-deleted.
     *
     * @see [vcmsa.projects.prog7313_poe.core.data.converters.DateConverter]
     * @author ST10257002
     */
    var stashedAt: Instant?


    //</editor-fold>
    //<editor-fold desc="Functions">


    /**
     * @return The [Boolean] state of the entity.
     * @author ST10257002
     */
    fun isStashed(): Boolean = (stashedAt != null)


    /**
     * Soft-deletes the entity by setting the [stashedAt] property to the
     * current [Instant] if it has not already been set. Entities with this flag
     * should be treated as destroyed.
     *
     * @author ST10257002
     */
    fun softDestroy() {
        if (isStashed() == false) {
            stashedAt = Instant.now()
        }
    }


    /**
     * Restores the entity by clearing the [stashedAt] property.
     *
     * @author ST10257002
     */
    fun softRestore() {
        if (isStashed()) {
            stashedAt = null
        }
    }


    //</editor-fold>
}