package vcmsa.projects.prog7313_poe.core.data.converters

import androidx.room.TypeConverter
import java.util.UUID

/**
 * Type convertor for UUID.
 *
 * @see [androidx.room.TypeConverter]
 * @see [androidx.room.RoomDatabase]
 * @author ST10257002
 */
class UuidConverter {

    /**
     * Convert from [String] to [UUID]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toUuid(data: String): UUID {
        return UUID.fromString(data)
    }


    /**
     * Convert from [UUID] to [String]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toString(data: UUID): String {
        return data.toString()
    }
}