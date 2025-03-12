package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class LoadingActivity : AppCompatActivity() {
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        loadingText = findViewById(R.id.loadingText)
        val animation = AnimationUtils.loadAnimation(this, R.anim.typewriter_animation)
        loadingText.startAnimation(animation)
        loadingText.visibility = View.VISIBLE

        val auth = FirebaseAuth.getInstance()


        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            if (auth.currentUser  != null) {
                val intent = Intent(this@LoadingActivity, DashboardActivity::class.java)
                intent.putExtra("USER_EMAIL", auth.currentUser ?.email)
                startActivity(intent)
            } else {
                startActivity(Intent(this@LoadingActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}