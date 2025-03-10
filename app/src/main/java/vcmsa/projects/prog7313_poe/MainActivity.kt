package vcmsa.projects.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val animation = AnimationUtils.loadAnimation(this, R.anim.typewriter_animation)


        loadingText.visibility = View.VISIBLE
        loadingText.startAnimation(animation)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
            finish()
        }

        registerButton.setOnClickListener {
            val register = Intent(this, RegisterActivity::class.java)
            startActivity(register)
            finish()
        }
    }
}