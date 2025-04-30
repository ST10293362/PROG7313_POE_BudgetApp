package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.supers.AuditableEntity
import java.util.UUID

/**
 * Repository class for managing [User] entities via [UserDao].
 * Handles all user-related CRUD operations and acts as the data layer abstraction
 * for the ViewModel or other parts of the app.
 *
 * Provides methods to create, update, delete, and fetch user data from the local database.
 *
 * @author ST10257002
 * @author ST13026084
 */
class UserRepository(
    private val dao: UserDao
) {

    /**
     * Inserts a new [User] entity into the database.
     *
     * @param instance The [User] object to insert.
     * Calls [UserDao.insert].
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun createUser(instance: User) {
        dao.insert(instance)
    }

    /**
     * Updates an existing [User] record in the database.
     * Automatically updates its audit timestamp by calling [User.touch].
     *
     * @param instance The [User] entity to update.
     * Calls [UserDao.update].
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun updateUser(instance: User) {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Deletes a [User] entity from the database.
     *
     * @param instance The [User] object to delete.
     * Calls [UserDao.delete].
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun deleteUser(instance: User) {
        dao.delete(instance)
    }

    /**
     * Deletes a [User] by their unique [UUID].
     *
     * @param id The ID of the user to delete.
     * Calls [UserDao.delete] by ID.
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun deleteUserById(id: UUID) {
        dao.delete(id)
    }

    /**
     * Retrieves a list of all users stored in the database.
     *
     * @return A list of [User] entities.
     * Calls [UserDao.fetchAll].
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun getAllUsers(): List<User> {
        return dao.fetchAll()
    }

    /**
     * Fetches a single [User] by their unique [UUID].
     *
     * @param id The ID of the user to retrieve.
     * @return The [User] object if found, or null otherwise.
     * Calls [UserDao.fetchOne].
     *
     * @author ST10257002
     * @author ST13026084
     */
    suspend fun getUserById(id: UUID): User? {
        return dao.fetchOne(id)
    }
}
