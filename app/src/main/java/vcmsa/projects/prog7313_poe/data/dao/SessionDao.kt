package vcmsa.projects.prog7313_poe.data.dao

import androidx.room.*
import vcmsa.projects.prog7313_poe.core.models.Session
import java.util.UUID

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getById(id: UUID): Session?

    @Query("SELECT * FROM sessions WHERE userId = :userId")
    suspend fun getByUserId(userId: UUID): List<Session>

    @Query("SELECT * FROM sessions WHERE userId = :userId AND endTime IS NULL")
    suspend fun getActiveSession(userId: UUID): Session?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: Session)

    @Update
    suspend fun update(session: Session)

    @Delete
    suspend fun delete(session: Session)

    @Query("DELETE FROM sessions WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("DELETE FROM sessions WHERE userId = :userId")
    suspend fun deleteByUserId(userId: UUID)

    @Query("DELETE FROM sessions")
    suspend fun deleteAll()

    @Query("UPDATE sessions SET endTime = CURRENT_TIMESTAMP WHERE id = :sessionId")
    suspend fun endSession(sessionId: UUID)
} 