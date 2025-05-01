package vcmsa.projects.prog7313_poe.core.utils

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Utility class providing input validation methods for common fields such as email,
 * password, name, phone number, and amount.
 *
 * It includes reusable methods that can be used throughout the application for form
 * validation and error handling.
 *
 * @reference https://developer.android.com/reference/android/util/Patterns
 * @reference https://owasp.org/www-community/OWASP_Validation_Regex_Repository
 * @reference https://developer.android.com/guide/topics/ui/notifiers/toasts#java
 *
 * @author ST13026084
 */
object ValidationUtils {

    // Regex pattern for validating email addresses.
    private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)\$"

    // Minimum required password length.
    private const val MIN_PASSWORD_LENGTH = 8

    /**
     * Validates whether a given email address matches the expected email pattern.
     *
     * @param email The email address to validate.
     * @return `true` if valid, `false` otherwise.
     */
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Pattern.matches(EMAIL_PATTERN, email)
    }

    /**
     * Checks if the given password meets the minimum length requirement.
     *
     * @param password The password string to validate.
     * @return `true` if password is valid, `false` otherwise.
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    /**
     * Checks whether a name is valid — non-empty and at least 2 characters.
     *
     * @param name The name string to validate.
     * @return `true` if name is valid, `false` otherwise.
     */
    fun isValidName(name: String): Boolean {
        return !TextUtils.isEmpty(name) && name.length >= 2
    }

    /**
     * Validates a phone number to ensure it's not empty and contains at least 10 digits.
     *
     * @param phone The phone number string to validate.
     * @return `true` if phone number is valid, `false` otherwise.
     */
    fun isValidPhoneNumber(phone: String): Boolean {
        return !TextUtils.isEmpty(phone) && phone.length >= 10
    }

    /**
     * Validates whether the input amount is a positive number.
     *
     * @param amount The amount string to validate.
     * @return `true` if the amount is a valid positive number, `false` otherwise.
     */
    fun isValidAmount(amount: String): Boolean {
        return try {
            amount.toDouble() > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    /**
     * Validates multiple fields at once based on field name and value pairs.
     * Returns a map of field names to corresponding error messages (if any).
     *
     * Supported fields: email, password, name, phone, amount
     *
     * @param inputs A vararg of field name and input value pairs.
     * @return A [Map] of field names to error messages (empty if all inputs are valid).
     */
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
