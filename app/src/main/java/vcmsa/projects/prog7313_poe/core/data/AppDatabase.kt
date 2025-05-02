package vcmsa.projects.prog7313_poe.core.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import vcmsa.projects.prog7313_poe.core.data.access.*
import vcmsa.projects.prog7313_poe.core.data.converters.DateConverter
import vcmsa.projects.prog7313_poe.core.data.converters.UuidConverter
import vcmsa.projects.prog7313_poe.core.data.converters.InstantConverter
import vcmsa.projects.prog7313_poe.core.data.converters.StringListConverter
import vcmsa.projects.prog7313_poe.core.models.*

/**
 * Main Room Database class for the app.
 *
 * This serves as the central access point to DAOs and enables persistence
 * using Room. Includes support for UUIDs and date/time types through
 * [TypeConverter]s.
 *
 * Also defines schema migrations when upgrading versions.
 *
 * @see RoomDatabase
 * @see Database
 * @see TypeConverters
 *
 *
 * @reference https://developer.android.com/training/data-storage/room
 * @reference https://developer.android.com/reference/androidx/room/RoomDatabase
 */
@Database(
    version = 2, // Increment version when schema changes
    entities = [
        Account::class,
        Expense::class,
        Category::class,
        User::class,
        UserSession::class,
        Session::class,
    ],
    exportSchema = true
)
@TypeConverters(
    DateConverter::class,
    UuidConverter::class,
    InstantConverter::class,
    StringListConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "expense_database.db"

        /**
         * Defines a schema migration from version 1 to 2.
         *
         * Adds a new column to the `user` table to track if goals were set.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user ADD COLUMN goals_set INTEGER NOT NULL DEFAULT 0")
            }
        }

        /**
         * Array of supported migrations. Passed into Room's builder.
         */
        private val MIGRATIONS = arrayOf(MIGRATION_1_2)

        /**
         * Returns a singleton instance of the [AppDatabase].
         *
         * @param context The application context.
         * @return Singleton [AppDatabase] instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(*MIGRATIONS)
                    .addCallback(DatabaseCallback)
                    .build()

                INSTANCE = instance
                instance
            }
        }

        /**
         * Optional callback triggered when the DB is created/opened.
         */
        private val DatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Optional: Seed default data here
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Optional: Perform health checks or logging
            }
        }
    }

    // DAO Accessors

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun sessionDao(): SessionDao
    abstract fun userDao(): UserDao
}