package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import vcmsa.projects.prog7313_poe.core.models.supers.AuditableEntity
import vcmsa.projects.prog7313_poe.core.models.supers.KeyedEntity

/**
 * Interface definition for a room DAO with CRUD operations.
 * This is not a valid [androidx.room.Dao] and as such, should not be registered
 * directly in the database. The purpose of this interface is to reduce
 * boilerplate within valid dao interfaces.
 *
 * @author ST10257002
 */
interface BaseDao<T> where T : KeyedEntity, T : AuditableEntity {

    /**
     * Insert a new entity into the database.
     *
     * @param instance The entity to insert into the database.
     *
     * @throws [android.database.sqlite.SQLiteConstraintException] on conflicts.
     * @author ST10257002
     */
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insert(instance: T)


    /**
     * Insert multiple new entities into the database.
     *
     * @param instanceCollection The entity collection to insert into the database.
     *
     * @throws [android.database.sqlite.SQLiteConstraintException] on conflicts.
     * @author ST10257002
     */
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insertAll(instanceCollection: List<T>)


    /**
     * Update an existing entity in the database.
     *
     * @param instance The entity to update in the database.
     *
     * @author ST10257002
     */
    @Update
    suspend fun update(instance: T)


    /**
     * Updates a collection of existing entities in the database.
     *
     * @param instanceCollection The collection to update in the database.
     *
     * @author ST10257002
     */
    @Update
    suspend fun updateAll(instanceCollection: List<T>)


    /**
     * Deletes a specific entity from the database.
     *
     * @param instance The entity to delete from the database.
     *
     * @author ST10257002
     */
    @Delete
    suspend fun delete(instance: T)


    /**
     * Deletes a collection of specific entities from the database.
     *
     * @param instanceCollection The collection to delete from the database.
     *
     * @author ST10257002
     */
    @Delete
    suspend fun deleteAll(instanceCollection: List<T>)

}