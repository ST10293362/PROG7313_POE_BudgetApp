package vcmsa.projects.prog7313_poe.core.models

import android.graphics.Color

/**
 * @author ST10257002
 */
enum class CredentialStrength(
    val displayText: String, val color: Int
) {

    // <editor-fold desc="Enum Values">


    WEAK("Weak", Color.RED), MEDIUM("Medium", Color.YELLOW), STRONG("Strong", Color.GREEN);


    // </editor-fold>
    // <editor-fold desc="Class Extensions">


    companion object {
        
        const val MEDIUM_STRENGTH_MIN_CHAR_LENGTH = 6
        const val STRONG_STRENGTH_MIN_CHAR_LENGTH = 10

        /**
         * Determines the strength of the given credential based on hardcoded
         * limits, and the returns a display-friendly string and applicable text
         * color for user-facing display.
         * 
         * @return The [CredentialStrength] of the given credential.
         * @author ST10257002
         */
        fun getStrength(
            credential: String
        ): CredentialStrength {
            return when {
                credential.length < MEDIUM_STRENGTH_MIN_CHAR_LENGTH -> WEAK
                credential.length < STRONG_STRENGTH_MIN_CHAR_LENGTH -> MEDIUM
                else -> STRONG
            }
        }
    }


// </editor-fold>

}