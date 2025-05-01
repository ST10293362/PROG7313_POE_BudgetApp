package vcmsa.projects.prog7313_poe.core.data.converters

import androidx.room.TypeConverter
import java.util.UUID

/**
 * Type converter for [UUID] values used with Room.
 *
 * SQLite does not support UUID natively, so Room uses this class to convert UUIDs
 * to [String] when storing in the database, and back to [UUID] when retrieving.
 *
 * Registered globally via `@TypeConverters(...)` on the Room database class.
 *
 * @see androidx.room.TypeConverter
 * @see androidx.room.RoomDatabase
 *
 * @author ST10257002
 * @author ST10326084
 *
 * @reference https://developer.android.com/reference/androidx/room/TypeConverter
 */
class UuidConverter {

    /**
     * Converts a [String] into a [UUID].
     *
     * Used by Room when reading from the database.
     *
     * @param data A valid UUID string representation.
     * @return A [UUID] object.
     */
    @TypeConverter
    fun toUuid(data: String): UUID {
        return UUID.fromString(data)
    }

    /**
     * Converts a [UUID] into a [String].
     *
     * Used by Room when saving to the database.
     *
     * @param data The UUID object.
     * @return The string representation of the UUID.
     */
    @TypeConverter
    fun toString(data: UUID): String {
        return data.toString()
    }
}
