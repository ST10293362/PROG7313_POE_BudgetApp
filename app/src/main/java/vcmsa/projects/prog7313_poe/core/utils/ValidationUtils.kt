package vcmsa.projects.prog7313_poe.core.utils

import android.text.TextUtils
import java.util.regex.Pattern

object ValidationUtils {
    private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)\$"
    private const val MIN_PASSWORD_LENGTH = 8

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Pattern.matches(EMAIL_PATTERN, email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    fun isValidName(name: String): Boolean {
        return !TextUtils.isEmpty(name) && name.length >= 2
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        return !TextUtils.isEmpty(phone) && phone.length >= 10
    }

    fun isValidAmount(amount: String): Boolean {
        return try {
            amount.toDouble() > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun validateInputs(vararg inputs: Pair<String, String>): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        
        inputs.forEach { (field, value) ->
            when (field) {
                "email" -> if (!isValidEmail(value)) errors[field] = "Invalid email address"
                "password" -> if (!isValidPassword(value)) errors[field] = "Password must be at least 8 characters"
                "name" -> if (!isValidName(value)) errors[field] = "Name must be at least 2 characters"
                "phone" -> if (!isValidPhoneNumber(value)) errors[field] = "Invalid phone number"
                "amount" -> if (!isValidAmount(value)) errors[field] = "Amount must be greater than 0"
            }
        }
        
        return errors
    }
} 