package vcmsa.projects.prog7313_poe.core.data.repos

import vcmsa.projects.prog7313_poe.data.dao.SessionDao
import vcmsa.projects.prog7313_poe.data.dao.UserDao
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.UserSession
import java.util.UUID
import vcmsa.projects.prog7313_poe.core.models.Session

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
    private val sessionDao: SessionDao,
    private val userDao: UserDao
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
     * @param hashedPassword The hashed version of the user's password
     * @return [Result.success] with the authenticated user, or [Result.failure] on invalid login
     */
    suspend fun signIn(email: String, hashedPassword: String): Result<User> {
        return try {
            val user = userDao.fetchOneByEmail(email)
                ?: return Result.failure(Exception("User not found"))

            if (user.password != hashedPassword) {
                return Result.failure(Exception("Invalid password"))
            }

            // Clear any existing sessions
            sessionDao.clearAll()
            
            // Create new session
            createSession(user.id)
            
            // Verify session was created
            val session = sessionDao.getCurrent()
            if (session == null || session.userId != user.id) {
                return Result.failure(Exception("Failed to create session"))
            }

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
    suspend fun createSession(userId: UUID) {
        val session = UserSession(userId = userId)
        sessionDao.insertSession(session)
    }

    suspend fun getCurrentSession(): UserSession? {
        return sessionDao.getSessionById(1)
    }

    suspend fun clearSession() {
        sessionDao.deleteSessionById(1)
    }

    suspend fun updateSession(userId: UUID) {
        val session = UserSession(userId = userId)
        sessionDao.updateSession(session)
    }

    suspend fun deleteSessionsByUserId(userId: UUID) {
        sessionDao.deleteSessionsByUserId(userId.toString())
    }

    suspend fun getById(id: UUID): Session? = sessionDao.getById(id)

    suspend fun getByUserId(userId: UUID): List<Session> = sessionDao.getByUserId(userId)

    suspend fun getByToken(token: String): UserSession? = sessionDao.getByToken(token)

    suspend fun insert(session: Session) = sessionDao.insert(session)

    suspend fun update(session: Session) = sessionDao.update(session)

    suspend fun delete(session: Session) = sessionDao.delete(session)

    suspend fun deleteById(id: UUID) = sessionDao.deleteById(id)

    suspend fun deleteByUserId(userId: UUID) = sessionDao.deleteByUserId(userId)

    suspend fun deleteAll() = sessionDao.deleteAll()

    suspend fun getActiveSession(userId: UUID): Session? = sessionDao.getActiveSession(userId)

    suspend fun endSession(sessionId: UUID) = sessionDao.endSession(sessionId)
}
