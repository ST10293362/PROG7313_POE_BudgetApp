package vcmsa.projects.prog7313_poe.core.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * Utility class for handling security-related operations.
 */
object SecurityUtils {
    private const val SALT_LENGTH = 16
    private const val HASH_ITERATIONS = 10000

    /**
     * Generates a random salt for password hashing.
     */
    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    /**
     * Hashes a password with a salt using SHA-256.
     */
    fun hashPassword(password: String, salt: String = generateSalt()): Pair<String, String> {
        val md = MessageDigest.getInstance("SHA-256")
        var hashedPassword = password.toByteArray()
        
        // Apply salt
        val saltedPassword = hashedPassword + salt.toByteArray()
        
        // Hash multiple times
        repeat(HASH_ITERATIONS) {
            hashedPassword = md.digest(saltedPassword)
        }
        
        return Pair(
            Base64.getEncoder().encodeToString(hashedPassword),
            salt
        )
    }

    /**
     * Verifies a password against a stored hash and salt.
     */
    fun verifyPassword(password: String, storedHash: String, storedSalt: String): Boolean {
        val (newHash, _) = hashPassword(password, storedSalt)
        return newHash == storedHash
    }
} 