package vcmsa.projects.prog7313_poe.core.services

import android.content.Context
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.SessionRepository
import vcmsa.projects.prog7313_poe.core.utils.SecurityUtils
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.UserSession

/**
 * Service class responsible for handling user authentication logic including sign-up,
 * sign-in, logout, and session management.
 *
 * @reference https://developer.android.com/topic/security/best-practices#store-credentials
 * @reference https://developer.android.com/training/data-storage/room
 * @reference https://owasp.org/www-community/controls/Password_Storage_Cheat_Sheet
 *
 * @author ST10257002
 * @author ST13026084
 */
class AuthService(context: Context) {
    private val data = AppDatabase.getDatabase(context)
    private val userDao = data.userDao()
    private val sessionDao = data.sessionDao()
    private val session = SessionRepository(userDao, sessionDao)

    suspend fun signUp(
        firstName: String, finalName: String, username: String, password: String, email: String
    ): Result<User> {
        val existing = userDao.fetchOneByUsername(username)
        if (existing != null) return Result.failure(Exception("Username already taken"))

        val (hashedPassword, salt) = SecurityUtils.hashPassword(password)

        val user = User(
            name = firstName,
            surname = finalName,
            username = username,
            password = hashedPassword,
            passwordSalt = salt,
            emailAddress = email,
            dateOfBirth = null,
            cellNumber = null,
            minGoal = null,
            maxGoal = null
        )

        return try {
            userDao.insert(user)
            sessionDao.clearAll()
            sessionDao.create(UserSession(userId = user.id))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(username: String, password: String): Result<User> {
        return try {
            val user = userDao.fetchOneByUsername(username)
                ?: return Result.failure(Exception("User not found"))

            if (!SecurityUtils.verifyPassword(password, user.password, user.passwordSalt)) {
                return Result.failure(Exception("Invalid password"))
            }

            sessionDao.clearAll()
            sessionDao.create(UserSession(userId = user.id))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() = session.logout()
    suspend fun getCurrentUser() = session.getCurrentUser()
    suspend fun isLoggedIn() = getCurrentUser().isSuccess
}
