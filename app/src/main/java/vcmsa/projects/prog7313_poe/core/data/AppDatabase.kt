package vcmsa.projects.prog7313_poe.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import vcmsa.projects.prog7313_poe.core.data.access.AccountDao
import vcmsa.projects.prog7313_poe.core.data.access.CategoryDao
import vcmsa.projects.prog7313_poe.core.data.access.ExpenseDao
import vcmsa.projects.prog7313_poe.core.data.access.SessionDao
import vcmsa.projects.prog7313_poe.core.data.access.UserDao
import vcmsa.projects.prog7313_poe.core.data.converters.DateConverter
import vcmsa.projects.prog7313_poe.core.data.converters.UuidConverter
import vcmsa.projects.prog7313_poe.core.models.Account
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.core.models.UserSession

/**
 * Context class for the room database.
 *
 * @see [androidx.room.RoomDatabase]
 * @see [androidx.room.Database]
 * @author ST10257002
 */@Database(
    version = 2, // Increment version number
    entities = [
        Account::class,
        Expense::class,
        Category::class,
        User::class,
        UserSession::class,
    ],
    exportSchema = true
)
@TypeConverters(
    DateConverter::class,
    UuidConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "expense_database.db"

        // Define the migration from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user ADD COLUMN goals_set INTEGER NOT NULL DEFAULT 0")
            }
        }

        // Add migration to the array
        private val MIGRATIONS = arrayOf(MIGRATION_1_2)

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

        private val DatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun sessionDao(): SessionDao
    abstract fun userDao(): UserDao
}