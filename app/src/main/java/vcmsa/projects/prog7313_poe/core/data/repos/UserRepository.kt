package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

/**
 * @author ST10257002
 */
class UserRepository(
    private val dao: UserDao
) {

    /**
     * @author ST10257002
     */
    suspend fun createUser(instance: User) {
        dao.insert(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun updateUser(instance: User) {
        dao.updateAuditable(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteUser(instance: User) {
        dao.delete(instance)
    }


    /**
     * @author ST10257002
     */
    suspend fun deleteUserById(id: UUID) {
        dao.delete(id)
    }


    /**
     * @author ST10257002
     */
    suspend fun getAllUsers(): List<User> {
        return dao.fetchAll()
    }
}