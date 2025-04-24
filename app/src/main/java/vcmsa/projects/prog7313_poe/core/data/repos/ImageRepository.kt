package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.ImageDao
import vcmsa.projects.prog7313_poe.core.models.Image
import java.util.UUID

/**
 * @author ST10257002
 */
class ImageRepository(
    private val dao: ImageDao
) {

    /**
     * @author ST10257002
     */
    suspend fun createImage(instance: Image) {
        dao.insert(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun updateImage(instance: Image) {
        dao.updateAuditable(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteImage(instance: Image) {
        dao.delete(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteImageById(id: UUID) {
        dao.delete(id)
    }


    /**
     * @author ST10257002
     */
    suspend fun getAllImages(): List<Image> {
        return dao.fetchAll()
    }
}