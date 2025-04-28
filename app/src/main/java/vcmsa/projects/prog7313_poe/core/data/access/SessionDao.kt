package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vcmsa.projects.prog7313_poe.core.models.UserSession

/**
 * @author ST10257002
 */
@Dao
interface SessionDao {

    /**
     * @author ST10257002
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(instance: UserSession)


    /**
     * @author ST10257002
     */
    @Query(
        """
        DELETE FROM user_session
        """
    )
    suspend fun clearAll()


    /**
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM user_session
        WHERE id = 1
        """
    )
    suspend fun getCurrent(): UserSession?
    
}