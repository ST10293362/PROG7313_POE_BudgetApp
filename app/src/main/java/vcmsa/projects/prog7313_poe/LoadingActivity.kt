package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.FontCallback.getHandler
import com.google.firebase.auth.FirebaseAuth

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
            if (auth.currentUser  != null) {

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("USER_EMAIL", auth.currentUser ?.email)
                startActivity(intent)
            } else {

                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()

    }
}