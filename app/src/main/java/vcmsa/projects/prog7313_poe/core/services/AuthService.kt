package vcmsa.projects.prog7313_poe.core.services

import android.content.Context
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.SessionRepository
import vcmsa.projects.prog7313_poe.core.utils.SecurityUtils
import vcmsa.projects.prog7313_poe.core.models.User

/**
 * @author ST10257002
 */
class AuthService(
    context: Context
) {

    private val data = AppDatabase.getDatabase(context)
    private val userDao = data.userDao()
    private val sessionDao = data.sessionDao()
    private val session = SessionRepository(userDao, sessionDao)


    /**
     * @author ST10257002
     */
    suspend fun signUp(
        firstName: String, finalName: String, username: String, password: String, email: String
    ): Result<User> {
        val (hashedPassword, salt) = SecurityUtils.hashPassword(password)
        return session.signUp(firstName, finalName, username, hashedPassword, email, salt)
    }


    /**
     * @author ST10257002
     */
    suspend fun signIn(
        email: String, password: String
    ): Result<User> {
        val user = userDao.fetchOneByEmail(email) ?: return Result.failure(Exception("User not found"))
        
        if (!SecurityUtils.verifyPassword(password, user.password, user.passwordSalt)) {
            return Result.failure(Exception("Invalid password"))
        }
        
        return session.signIn(email, user.password)
    }


    /**
     * @author ST10257002
     */
    suspend fun logout() = session.logout()


    /**
     * @author ST10257002
     */
    suspend fun getCurrentUser() = session.getCurrentUser()


    /**
     * @author ST10257002
     */
    suspend fun isLoggedIn() = getCurrentUser().isSuccess
}