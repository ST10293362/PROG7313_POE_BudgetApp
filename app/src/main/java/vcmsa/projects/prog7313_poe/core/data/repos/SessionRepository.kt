package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.core.data.access.SessionDao
import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.UserSession
import java.util.UUID

/**
 * Handles authentication and session management using RoomDB.
 *
 * This repository is responsible for signing up new users, signing in existing users,
 * managing active sessions, and accessing the currently logged-in user.
 *
 * All operations return Kotlin's [Result] wrapper to propagate exceptions safely.
 *
 * Passwords should be pre-hashed with salt **before** being passed in.
 *
 * @author ST10257002
 * @author ST10326084
 */

class SessionRepository(
    private val userDao: UserDao,
    private val sessionDao: SessionDao
) {

    //<editor-fold desc="Authentication">

    /**
     * Registers a new user and creates a session upon success.
     *
     * @param firstName First name of the user
     * @param finalName Last name of the user
     * @param username Desired username (must be unique)
     * @param hashedPassword Hashed password string
     * @param email Email address
     * @param passwordSalt Salt used for hashing (should be stored for verification)
     *
     * @return [Result.success] containing the new [User] on success, or [Result.failure] on error
     */
    suspend fun signUp(
        firstName: String,
        finalName: String,
        username: String,
        hashedPassword: String,
        email: String,
        passwordSalt: String
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
                password = hashedPassword,
                passwordSalt = passwordSalt,
                emailAddress = email,
                maxGoal = null,
                minGoal = null,
                dateOfBirth = null,
                cellNumber = null
            )

            userDao.insert(user)
            createSession(user.id)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Attempts to log in a user using email and hashed password.
     *
     * @param email Email address
     * @param hashedPassword The hashed version of the user’s password
     * @return [Result.success] with the authenticated user, or [Result.failure] on invalid login
     */
    suspend fun signIn(email: String, hashedPassword: String): Result<User> {
        return try {
            val user = userDao.fetchOneByEmail(email)
                ?: return Result.failure(Exception("User not found"))

            if (user.password != hashedPassword) {
                return Result.failure(Exception("Invalid password"))
            }

            createSession(user.id)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logs out the currently authenticated user by clearing all active sessions.
     *
     * Safe to call even if no session is currently active.
     *
     * @return [Result.success] with `true` on success
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

    //<editor-fold desc="Session Access">

    /**
     * Retrieves the currently authenticated user, if a session exists.
     *
     * @return [Result.success] with the active [User], or null if no session is active
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

    /**
     * Internal helper to create a new user session.
     *
     * @param userId UUID of the user to assign to the session.
     */
    private suspend fun createSession(userId: UUID) {
        sessionDao.create(UserSession(userId = userId))
    }
}
