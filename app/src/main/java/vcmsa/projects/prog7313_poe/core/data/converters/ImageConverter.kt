package vcmsa.projects.prog7313_poe.core.data.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

/**
 * Type convertor for directly disassembled images.
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
class ImageConverter {

    /**
     * Convert from [Bitmap] to [ByteArray]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toByteArray(data: Bitmap?): ByteArray? {
        return if (data == null) {
            Log.i(
                ImageConverter::class.toString(),
                "[E] Could not convert ${Bitmap::class.toString()} to ${ByteArray::class.toString()}"
            )
            null
        } else {
            val stream = ByteArrayOutputStream()
            data.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }
    }

    /**
     * Convert from [ByteArray] to [Bitmap]
     *
     * @see [androidx.room.TypeConverter]
     * @author ST10257002
     */
    @TypeConverter
    fun toBitmap(data: ByteArray?): Bitmap? {
        return if (data == null) {
            Log.i(
                ImageConverter::class.toString(),
                "[E] Could not convert ${ByteArray::class.toString()} to ${Bitmap::class.toString()}"
            )
            null
        } else {
            BitmapFactory.decodeByteArray(data, 0, data.size)
        }
    }
}