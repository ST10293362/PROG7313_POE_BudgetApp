package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        loadingText = findViewById(R.id.loadingText)

        // Load the animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.typewriter_animation)

        // Start the animation
        loadingText.visibility = View.VISIBLE
        loadingText.startAnimation(animation)

        // Simulate loading time
        Handler().postDelayed({
            // After loading, navigate to the main screen
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 3000) // 3 seconds delay
    }
}