package vcmsa.projects.prog7313_poe.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe.core.data.AppDatabase
import vcmsa.projects.prog7313_poe.core.services.AuthService

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var authService: AuthService
    protected lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeServices()
    }

    private fun initializeServices() {
        authService = AuthService(this)
        database = AppDatabase.getDatabase(this)
    }

    protected fun showLoading(loadingView: View) {
        loadingView.visibility = View.VISIBLE
    }

    protected fun hideLoading(loadingView: View) {
        loadingView.visibility = View.GONE
    }

    protected fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    protected fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

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