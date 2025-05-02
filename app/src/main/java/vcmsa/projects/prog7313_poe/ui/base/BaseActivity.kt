package vcmsa.projects.prog7313_poe.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.services.AuthService

/**
 * BaseActivity provides shared functionality for all activities in the app.
 * It handles service initialization, user feedback (loading/error/success),
 * and coroutine-based operation handling.
 *
 * This abstract class promotes DRY principles by centralizing repetitive
 * logic such as showing/hiding loaders and error handling.
 *
 * @reference https://developer.android.com/reference/androidx/lifecycle/LifecycleCoroutineScope
 * @reference https://developer.android.com/guide/components/activities/activity-lifecycle
 * @reference https://developer.android.com/guide/topics/ui/notifiers/toasts
 */
abstract class BaseActivity : AppCompatActivity() {

    // Authentication service for managing user auth operations
    protected lateinit var authService: AuthService

    // Database instance for interacting with Room database
    protected lateinit var database: AppDatabase

    /**
     * Lifecycle callback invoked when the activity is created.
     * Initializes the authentication and database services.
     *
     * @param savedInstanceState The previously saved state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeServices()
    }

    /**
     * Initializes core services used across activities:
     * - AuthService for authentication
     * - AppDatabase for data access
     */
    private fun initializeServices() {
        authService = AuthService(this)
        database = AppDatabase.getDatabase(this)
    }

    /**
     * Shows the given loading view (typically a ProgressBar or Spinner).
     *
     * @param loadingView The View to display.
     */
    protected fun showLoading(loadingView: View) {
        loadingView.visibility = View.VISIBLE
    }

    /**
     * Hides the given loading view.
     *
     * @param loadingView The View to hide.
     */
    protected fun hideLoading(loadingView: View) {
        loadingView.visibility = View.GONE
    }

    /**
     * Displays an error message to the user using a Toast.
     *
     * @param message The error message to show.
     */
    protected fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Displays a success message to the user using a Toast.
     *
     * @param message The success message to show.
     */
    protected fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Handles coroutine-based database operations safely.
     * Displays loading indicators, executes the operation, and handles success or error callbacks.
     *
     * @param loadingView Optional loading indicator view to show/hide.
     * @param operation The suspend function representing the database operation.
     * @param onSuccess Lambda invoked upon successful execution.
     * @param onError Lambda invoked if an exception occurs.
     */
    protected fun handleDatabaseOperation(
        loadingView: View? = null,
        operation: suspend () -> Unit,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = { showError(it.message ?: "An error occurred") }
    ) {
        lifecycleScope.launch {
            try {
                loadingView?.let { showLoading(it) }
                operation()
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            } finally {
                loadingView?.let { hideLoading(it) }
            }
        }
    }

    /**
     * Handles coroutine-based authentication operations with similar logic to [handleDatabaseOperation].
     * Used for login, registration, logout, etc.
     *
     * @param loadingView Optional loading indicator view to show/hide.
     * @param operation The suspend function representing the auth operation.
     * @param onSuccess Lambda invoked upon successful execution.
     * @param onError Lambda invoked if an exception occurs.
     */
    protected fun handleAuthOperation(
        loadingView: View? = null,
        operation: suspend () -> Unit,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = { showError(it.message ?: "Authentication failed") }
    ) {
        lifecycleScope.launch {
            try {
                loadingView?.let { showLoading(it) }
                operation()
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            } finally {
                loadingView?.let { hideLoading(it) }
            }
        }
    }
}