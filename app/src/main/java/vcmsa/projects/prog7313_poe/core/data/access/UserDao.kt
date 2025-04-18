package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Dao
import androidx.room.Query
import vcmsa.projects.prog7313_poe.core.models.User
import java.util.UUID

/**
 * Interface definition for direct database operations.
 *
 * @see [vcmsa.projects.prog7313_poe.core.models.User]
 * @see [androidx.room.Dao]
 * @author ST10257002
 */
@Dao
interface UserDao : BaseDao<User> {

    //<editor-fold desc="CRUD Operations">


    /**
     * Deletes a specific entity from the database using its unique ID.
     *
     * @param targetId The unique identifier ([java.util.UUID]) of the entity to delete.
     *
     * @author ST10257002
     */
    @Query(
        """
        DELETE FROM user 
        WHERE id = :targetId
        """
    )
    suspend fun delete(targetId: UUID)


    /**
     * Fetches the contents of the database table.
     *
     * @return [List] collection containing every entity in the database.
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM user
        """
    )
    suspend fun fetchAll(): List<User>


    /**
     * Fetches a specific entity by its primary key identity.
     *
     * @param targetId The unique identifier ([UUID]) of the entity to query.
     *
     * @return The specific entity that was queried.
     * @author ST10257002
     */
    @Query(
        """
        SELECT * FROM user
        WHERE id = :targetId
        """
    )
    suspend fun fetchOne(targetId: UUID): User?


    //</editor-fold>

}