package vcmsa.projects.prog7313_poe.core.services

import android.content.Context
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.SessionRepository
import vcmsa.projects.prog7313_poe.core.data.repos.UserRepository
import vcmsa.projects.prog7313_poe.core.utils.SecurityUtils
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

/**
 * Service class responsible for handling user authentication logic including sign-up,
 * sign-in, logout, and session management.
 *
 * It serves as a bridge between the user interface and database, leveraging Room DAO
 * instances and a repository for user sessions.
 *
 * @reference https://developer.android.com/topic/security/best-practices#store-credentials
 * @reference https://developer.android.com/training/data-storage/room
 * @reference https://owasp.org/www-community/controls/Password_Storage_Cheat_Sheet
 *
 * @author ST10257002
 * @author ST13026084
 */
class AuthService(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {

    private val data = AppDatabase.getDatabase(context)
    private val userDao = data.userDao()
    private val sessionDao = data.sessionDao()
    private val session = SessionRepository(userDao, sessionDao)

    /**
     * Registers a new user with the given credentials.
     *
     * @param firstName The user's first name.
     * @param finalName The user's last name.
     * @param username The desired username.
     * @param password The raw password (to be hashed and salted).
     * @param email The user's email address.
     * @return A [Result] containing the created [User] object or a failure.
     */
    suspend fun signUp(
        firstName: String, finalName: String, username: String, password: String, email: String
    ): Result<User> {
        // Check if email already exists
        val existingUser = userDao.fetchOneByEmail(email)
        if (existingUser != null) {
            return Result.failure(Exception("Email address is already registered"))
        }

        // Check if username already exists
        val existingUsername = userDao.fetchOneByUsername(username)
        if (existingUsername != null) {
            return Result.failure(Exception("Username is already taken"))
        }

        val (hashedPassword, salt) = SecurityUtils.hashPassword(password)
        return session.signUp(firstName, finalName, username, hashedPassword, email, salt)
    }

    /**
     * Authenticates a user using the provided email/username and password.
     *
     * @param identifier The user's email address or username.
     * @param password The raw password to verify.
     * @return A [Result] containing the [User] if successful, or a failure otherwise.
     */
    suspend fun signIn(
        identifier: String, password: String
    ): Result<User> {
        // Try to find user by email first
        var user = userDao.fetchOneByEmail(identifier)
        
        // If not found by email, try username
        if (user == null) {
            user = userDao.fetchOneByUsername(identifier)
        }
        
        if (user == null) {
            return Result.failure(Exception("User not found"))
        }

        if (!SecurityUtils.verifyPassword(password, user.password, user.passwordSalt)) {
            return Result.failure(Exception("Invalid password"))
        }

        // Create a new session for the user
        val sessionResult = session.signIn(user.emailAddress, user.password)
        
        if (sessionResult.isSuccess) {
            return Result.success(user)
        }
        
        return Result.failure(Exception("Failed to create session"))
    }

    /**
     * Logs out the current user session.
     */
    suspend fun logout() = session.logout()

    /**
     * Retrieves the currently authenticated user from the session.
     *
     * @return A [Result] containing the current [User] or an error if not logged in.
     */
    suspend fun getCurrentUser() = session.getCurrentUser()

    /**
     * Checks whether a user is currently logged in.
     *
     * @return `true` if a user session exists and is valid, `false` otherwise.
     */
    suspend fun isLoggedIn() = getCurrentUser().isSuccess
}
