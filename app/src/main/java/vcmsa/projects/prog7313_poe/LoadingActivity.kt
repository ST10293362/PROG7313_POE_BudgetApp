package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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


        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            startActivity(Intent(this@LoadingActivity, LoginActivity::class.java))
            finish()
        }
    }
}