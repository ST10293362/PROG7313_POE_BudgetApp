package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vcmsa.projects.prog7313_poe.core.models.UserSession

/**
 * DAO for managing session state via a singleton [UserSession] table.
 *
 * This assumes a **single-user session model**, where session is stored
 * with a fixed primary key (id = 1). Inserting replaces any existing session.
 *
 * @since 2025
 *
 * @see androidx.room.Dao
 * @see https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
interface SessionDao {

    /**
     * Inserts a new [UserSession] into the session table.
     *
     * This will replace the existing session if one already exists,
     * due to the use of [OnConflictStrategy.REPLACE].
     *
     * @param instance The [UserSession] to persist.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(instance: UserSession)

    /**
     * Clears all user session data from the session table.
     *
     * This is called during logout or reset flows.
     */
    @Query("DELETE FROM user_session")
    suspend fun clearAll()

    /**
     * Fetches the currently active [UserSession].
     *
     * Since only one session is expected, this assumes the ID is fixed at 1.
     *
     * @return The currently stored [UserSession], or null if none is set.
     */
    @Query("SELECT * FROM user_session WHERE id = 1")
    suspend fun getCurrent(): UserSession?
}
