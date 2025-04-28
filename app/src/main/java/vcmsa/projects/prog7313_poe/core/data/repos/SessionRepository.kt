package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.SessionDao
import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.UserSession

/**
 * @author ST10257002
 */
class SessionRepository(
    private val userDao: UserDao, private val sessionDao: SessionDao
) {

    //<editor-fold desc="Functions">


    /**
     * Try to register a user with the given credentials.
     *
     * @see [Result]
     * @return Success result containing the user if successful.
     * @author ST10257002
     */
    suspend fun signUp(
        firstName: String, finalName: String, username: String, password: String, email: String
    ): Result<User> {
        return try {
            val takenUsernameUser = userDao.fetchOneByUsername(username)
            if (takenUsernameUser != null) {
                return Result.failure(Exception("Username is already taken"))
            }

            val user = User(
                name = firstName,
                surname = finalName,
                username = username,
                password = password,
                emailAddress = email,
                maxGoal = null,
                minGoal = null,
                dateOfBirth = null,
                cellNumber = null
            )

            userDao.insert(user)
            sessionDao.create(UserSession(userId = user.id))

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Try to authenticate a user with the given credentials.
     *
     * @see [Result]
     * @return Success result containing the user if successful.
     * @author ST10257002
     */
    suspend fun signIn(
        email: String, password: String
    ): Result<User> {
        return try {
            val user = userDao.fetchOneByCredentials(email, password)

            if (user == null) {
                return Result.failure(
                    Exception("User not found with the given credentials")
                )
            }

            sessionDao.create(UserSession(userId = user.id))
            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Clear any currently active users from the session manager.
     *
     * This method is safe to call even if the session manager does not
     * currently contain any authenticated users.
     *
     * @see [Result]
     * @return [Boolean] result confirming a successful operation.
     * @author ST10257002
     */
    suspend fun logout(): Result<Boolean> {
        return try {
            sessionDao.clearAll()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    //</editor-fold>
    //<editor-fold desc="Accessory">


    /**
     * Query the session manager for the currently authenticated user.
     *
     * @see [Result]
     * @return The active user or null if none are available.
     * @author ST10257002
     */
    suspend fun getCurrentUser(): Result<User?> {
        return try {
            val session = sessionDao.getCurrent()
            if (session != null) {
                val user = userDao.fetchOne(session.userId)
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    //</editor-fold>

}