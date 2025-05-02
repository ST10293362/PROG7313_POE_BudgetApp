package vcmsa.projects.prog7313_poe.core.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * Utility class for handling security-related operations such as password hashing
 * and verification. Implements hashing with SHA-256 and salt for secure storage.
 *
 * @reference https://owasp.org/www-community/controls/Password_Storage_Cheat_Sheet
 * @reference https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html
 * @reference https://developer.android.com/topic/security/best-practices#store-credentials
 *
 */
object SecurityUtils {

    // Length of salt in bytes. A longer salt increases randomness.
    private const val SALT_LENGTH = 16

    // Number of hashing iterations to enhance resistance against brute force attacks.
    private const val HASH_ITERATIONS = 10000

    /**
     * Generates a cryptographically secure random salt and encodes it as a Base64 string.
     *
     * @return A Base64-encoded string representing the salt.
     */
    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    /**
     * Hashes the given password with a salt using SHA-256 and multiple iterations.
     *
     * @param password The plain-text password to hash.
     * @param salt The salt to use. If not provided, a new one is generated.
     * @return A [Pair] where:
     *   - First value is the Base64-encoded hash.
     *   - Second value is the salt used in hashing.
     */
    fun hashPassword(password: String, salt: String = generateSalt()): Pair<String, String> {
        val md = MessageDigest.getInstance("SHA-256")
        var hashedPassword = password.toByteArray()

        // Combine password bytes with salt
        val saltedPassword = hashedPassword + salt.toByteArray()

        // Perform multiple hashing rounds for added security
        repeat(HASH_ITERATIONS) {
            hashedPassword = md.digest(saltedPassword)
        }

        return Pair(
            Base64.getEncoder().encodeToString(hashedPassword),
            salt
        )
    }

    /**
     * Verifies a plain-text password by hashing it with the provided salt and comparing
     * it to the stored hash.
     *
     * @param password The plain-text password to verify.
     * @param storedHash The previously stored Base64-encoded hash.
     * @param storedSalt The salt that was originally used to hash the password.
     * @return `true` if the password matches the hash, `false` otherwise.
     */
    fun verifyPassword(password: String, storedHash: String, storedSalt: String): Boolean {
        val (newHash, _) = hashPassword(password, storedSalt)
        return newHash == storedHash
    }
}
