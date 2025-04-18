package vcmsa.projects.prog7313_poe.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import vcmsa.projects.prog7313_poe.core.data.converters.DateConverter
import vcmsa.projects.prog7313_poe.core.data.converters.ImageConverter
import vcmsa.projects.prog7313_poe.core.data.converters.UuidConverter
import vcmsa.projects.prog7313_poe.core.models.Account
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.core.models.Image
import vcmsa.projects.prog7313_poe.core.models.Trophy
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.UserTrophy

/**
 * Context class for the room database.
 *
 * @see [androidx.room.RoomDatabase]
 * @see [androidx.room.Database]
 * @author ST10257002
 */
@Database(
    entities = [
        Trophy::class,
        UserTrophy::class,
        Account::class,
        Expense::class,
        Category::class,
        Image::class,
        User::class,
    ],
    version = 1
)
@TypeConverters(
    DateConverter::class,
    UuidConverter::class,
    ImageConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    // Initialise the database as a singleton instance
    // Companion objects are static by nature
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Fetch the singleton database instance.
         *
         * @author ST10257002
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                // Build the database anew or from existing contexts
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "expense_database"
                ).build()
                INSTANCE = instance

                // Function return for the instance
                instance
            }
        }
    }

    // TODO: Implement DAOs
}