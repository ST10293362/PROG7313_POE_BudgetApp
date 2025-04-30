package vcmsa.projects.prog7313_poe.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View
import vcmsa.projects.prog7313_poe.R

class PasswordStrengthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val strengthText: TextView
    private val strengthIndicator: View

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_password_strength, this, true)
        
        strengthText = findViewById(R.id.strengthText)
        strengthIndicator = findViewById(R.id.strengthIndicator)
    }

    fun updatePasswordStrength(password: String) {
        val strength = calculatePasswordStrength(password)
        updateStrengthIndicator(strength)
    }

    private fun calculatePasswordStrength(password: String): Int {
        var strength = 0

        // Length check (0-2 points)
        when {
            password.length >= 12 -> strength += 2
            password.length >= 8 -> strength += 1
        }

        // Character type checks (0-4 points)
        if (password.any { it.isUpperCase() }) strength += 1
        if (password.any { it.isLowerCase() }) strength += 1
        if (password.any { it.isDigit() }) strength += 1
        if (password.any { !it.isLetterOrDigit() }) strength += 1

        // Additional complexity checks (0-2 points)
        if (password.length >= 8 && password.toSet().size >= 6) strength += 1
        if (!password.contains(Regex("(.)\\1{2,}"))) strength += 1 // No repeated characters

        return strength.coerceIn(0, 6)
    }

    private fun updateStrengthIndicator(strength: Int) {
        val (strengthText, strengthColor) = when (strength) {
            0 -> "Very Weak" to R.color.password_strength_very_weak
            1 -> "Weak" to R.color.password_strength_weak
            2 -> "Fair" to R.color.password_strength_fair
            3 -> "Good" to R.color.password_strength_good
            4 -> "Strong" to R.color.password_strength_strong
            5, 6 -> "Very Strong" to R.color.password_strength_very_strong
            else -> "Unknown" to R.color.password_strength_unknown
        }

        this.strengthText.text = "Password Strength: $strengthText"
        this.strengthIndicator.setBackgroundColor(context.getColor(strengthColor))
    }
} 