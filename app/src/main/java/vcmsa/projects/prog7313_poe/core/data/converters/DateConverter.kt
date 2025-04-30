package vcmsa.projects.prog7313_poe.core.data.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.util.Date
import java.util.UUID

/**
 * Type converter for [Date] and [Instant] objects to and from [Long],
 * enabling Room to persist time-related fields as primitives.
 *
 * SQLite (used by Room) does not support Java/Kotlin date/time types directly,
 * so Room requires these conversions to store timestamps in `INTEGER` format.
 *
 * Registered in the Room database via `@TypeConverters(...)`.
 *
 * @see androidx.room.TypeConverter
 * @see androidx.room.RoomDatabase
 *
 * @author ST10257002
 * @author ST10326084
 *
 * @References: https://developer.android.com/reference/androidx/room/TypeConverter
 */
class DateConverter {

    /**
     * Converts a [Long] timestamp into a [Instant] object.
     *
     * @param data A millisecond-based Unix timestamp.
     * @return A corresponding [Instant] object.
     */
    @TypeConverter
    fun toInstant(data: Long): Instant {
        return Instant.ofEpochMilli(data)
    }

    /**
     * Converts a [Long] timestamp into a [Date] object.
     *
     * @param data A millisecond-based Unix timestamp.
     * @return A corresponding [Date] object.
     */
    @TypeConverter
    fun toDate(data: Long): Date {
        return Date(data)
    }

    /**
     * Converts an [Instant] object to a [Long] timestamp.
     *
     * @param data The [Instant] to convert.
     * @return The number of milliseconds since the Unix epoch.
     */
    @TypeConverter
    fun toLong(data: Instant): Long {
        return data.toEpochMilli()
    }

    /**
     * Converts a [Date] object to a [Long] timestamp.
     *
     * @param data The [Date] to convert.
     * @return The number of milliseconds since the Unix epoch.
     */
    @TypeConverter
    fun toLong(data: Date): Long {
        return data.time
    }
}
