package vcmsa.projects.prog7313_poe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import vcmsa.projects.prog7313_poe.core.models.Category
import vcmsa.projects.prog7313_poe.core.models.Expense
import vcmsa.projects.prog7313_poe.core.models.Session
import vcmsa.projects.prog7313_poe.core.models.User
import vcmsa.projects.prog7313_poe.data.converters.StringListConverter
import vcmsa.projects.prog7313_poe.data.dao.CategoryDao
import vcmsa.projects.prog7313_poe.data.dao.ExpenseDao
import vcmsa.projects.prog7313_poe.data.dao.SessionDao
import vcmsa.projects.prog7313_poe.data.dao.UserDao

@Database(
    entities = [User::class, Category::class, Expense::class, Session::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 