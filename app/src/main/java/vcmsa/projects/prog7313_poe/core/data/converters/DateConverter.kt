package vcmsa.projects.prog7313_poe.core.data.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.util.Date
import java.util.UUID

/**
 * Type converter for DateTime types.
 * 
 * Since room is an abstraction over SQLite, it cannot handle the complex data 
 * types of the Kotlin compiler. Type convertor classes allow complex data types
 * to be stored as primitive types, and then converted back to the original type
 * when retrieved from the database at runtime.
 *
 * @see [androidx.room.TypeConverter]
 * @see [androidx.room.RoomDatabase]
 * @author ST10257002
 */
class DateConverter {

    /**
     * Convert from [Long] to [Instant]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toInstant(data: Long): Instant {
        return Instant.ofEpochMilli(data)
    }


    /**
     * Convert from [Long] to [Date]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toDate(data: Long): Date {
        return Date(data)
    }
    
    
    /**
     * Convert from [Instant] to [Long]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toLong(data: Instant): Long {
        return data.toEpochMilli()
    }

    
    /**
     * Convert from [Date] to [Long]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toLong(data: Date): Long {
        return data.time
    }
}