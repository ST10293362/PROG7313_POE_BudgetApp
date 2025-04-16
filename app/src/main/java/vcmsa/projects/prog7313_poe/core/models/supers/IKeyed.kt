package vcmsa.projects.prog7313_poe.core.models.supers

import java.util.UUID

/**
 * Interface definition for an identifiable entity.
 *
 * @author ST10257002
 */
interface IKeyed {

    //<editor-fold desc="Interface members">


    /**
     * @see [vcmsa.projects.prog7313_poe.core.data.converters.UuidConverter]
     * @author ST10257002
     */
    val id: UUID


    //</editor-fold>
}