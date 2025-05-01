package vcmsa.projects.prog7313_poe.core.models

import android.graphics.Color

/**
 * Enum class representing the strength level of a user credential (e.g., password).
 *
 * Each strength level has an associated display string and color for UI feedback.
 * Used primarily in credential validation and feedback mechanisms.
 *
 * Enum values include:
 * - [WEAK]: Fewer than 6 characters.
 * - [MEDIUM]: Between 6 and 9 characters.
 * - [STRONG]: 10 or more characters.
 *
 * @author ST10257002
 * @author ST13026084
 *
 * @reference https://developer.android.com/reference/kotlin/android/graphics/Color
 * @reference https://kotlinlang.org/docs/enum-classes.html
 */
enum class CredentialStrength(
    val displayText: String,
    val color: Int
) {

    //<editor-fold desc="Enum Values">

    /**
     * Weak credential strength (less than 6 characters), indicated in red.
     */
    WEAK("Weak", Color.RED),

    /**
     * Medium credential strength (6 to 9 characters), indicated in yellow.
     */
    MEDIUM("Medium", Color.YELLOW),

    /**
     * Strong credential strength (10 or more characters), indicated in green.
     */
    STRONG("Strong", Color.GREEN);

    //</editor-fold>
    //<editor-fold desc="Companion Object">

    companion object {

        /** Minimum character count required for a credential to be considered MEDIUM. */
        const val MEDIUM_STRENGTH_MIN_CHAR_LENGTH = 6

        /** Minimum character count required for a credential to be considered STRONG. */
        const val STRONG_STRENGTH_MIN_CHAR_LENGTH = 10

        /**
         * Evaluates the given credential string and returns its corresponding strength level.
         *
         * @param credential The string to evaluate, typically a password.
         * @return [CredentialStrength] indicating whether the string is WEAK, MEDIUM, or STRONG.
         */
        fun getStrength(credential: String): CredentialStrength {
            return when {
                credential.length < MEDIUM_STRENGTH_MIN_CHAR_LENGTH -> WEAK
                credential.length < STRONG_STRENGTH_MIN_CHAR_LENGTH -> MEDIUM
                else -> STRONG
            }
        }
    }

    //</editor-fold>
}
