package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

/**
 * Repository layer for managing [User] entities through [UserDao].
 * Provides a clean separation between the Room database and the UI/business logic.
 *
 * All insert/update operations call `touch()` to update the last-modified timestamp.
 *
 * @author ST10257002
 * @author ST10326084
 * @since 2024
 */
class UserRepository(
    private val dao: UserDao
) {

    /**
     * Inserts a new user into the database.
     *
     * @param instance The [User] object to create.
     */
    suspend fun createUser(instance: User) {
        dao.insert(instance)
    }

    /**
     * Updates an existing user, refreshing its audit metadata.
     *
     * @param instance The [User] to be updated.
     */
    suspend fun updateUser(instance: User) {
        instance.touch()
        dao.update(instance)
    }

    /**
     * Deletes a user from the database.
     *
     * @param instance The [User] to delete.
     */
    suspend fun deleteUser(instance: User) {
        dao.delete(instance)
    }

    /**
     * Deletes a user by their unique UUID.
     *
     * @param id The UUID of the user to delete.
     */
    suspend fun deleteUserById(id: UUID) {
        dao.delete(id)
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A [List] of [User] objects.
     */
    suspend fun getAllUsers(): List<User> {
        return dao.fetchAll()
    }

    /**
     * Retrieves a single user by their unique ID.
     *
     * @param id The UUID of the user to retrieve.
     * @return The corresponding [User], or null if not found.
     */
    suspend fun getUserById(id: UUID): User? {
        return dao.fetchOne(id)
    }
}
