package vcmsa.projects.prog7313_poe.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Context class for the room database.
 * 
 * @see [androidx.room.RoomDatabase]
 * @see [androidx.room.Database]
 * @author ST10257002
 */
@Database(
    version = 1
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