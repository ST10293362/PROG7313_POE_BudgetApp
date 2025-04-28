package vcmsa.projects.prog7313_poe.core.services

import android.content.Context
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.data.repos.SessionRepository

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
    ) = session.signUp(firstName, finalName, username, password, email)


    /**
     * @author ST10257002
     */
    suspend fun signIn(
        email: String, password: String
    ) = session.signIn(email, password)


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