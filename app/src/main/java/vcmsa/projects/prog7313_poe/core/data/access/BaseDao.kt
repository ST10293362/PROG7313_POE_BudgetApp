package vcmsa.projects.prog7313_poe.core.data.access

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import vcmsa.projects.prog7313_poe.core.models.supers.AuditableEntity
import vcmsa.projects.prog7313_poe.core.models.supers.KeyedEntity

/**
 * Generic Room DAO interface for basic CRUD operations on any entity that implements
 * both [KeyedEntity] and [AuditableEntity].
 *
 * Note: This is **not** a valid Room DAO by itself and should not be used directly
 * in `@Database` annotations. Its purpose is to reduce boilerplate in DAOs by providing
 * reusable method signatures.
 *
 * @author ST10257002
 * @author ST10326084
 *
 * @reference https://developer.android.com/reference/androidx/room/Dao
 */
interface BaseDao<T> where T : KeyedEntity, T : AuditableEntity {

    /**
     * Inserts a single entity into the database.
     *
     * @param instance The entity to be inserted.
     * @throws android.database.sqlite.SQLiteConstraintException on conflict.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(instance: T)

    /**
     * Inserts a list of entities into the database.
     *
     * @param instanceCollection List of entities to be inserted.
     * @throws android.database.sqlite.SQLiteConstraintException on conflict.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(instanceCollection: List<T>)

    /**
     * Updates an existing entity.
     *
     * @param instance The entity to be updated.
     */
    @Update
    suspend fun update(instance: T)

    /**
     * Updates a collection of entities.
     *
     * @param instanceCollection The entities to be updated.
     */
    @Update
    suspend fun updateAll(instanceCollection: List<T>)

    /**
     * Deletes a specific entity from the database.
     *
     * @param instance The entity to be deleted.
     */
    @Delete
    suspend fun delete(instance: T)

    /**
     * Deletes a collection of entities from the database.
     *
     * @param instanceCollection The entities to be deleted.
     */
    @Delete
    suspend fun deleteAll(instanceCollection: List<T>)

    // The following are wrappers for convenience when using transactions manually.

    /**
     * Inserts an entity inside a transaction.
     *
     * @param instance The entity to insert.
     */
    suspend fun insertWithTransaction(instance: T) {
        insert(instance)
    }

    /**
     * Updates an entity inside a transaction.
     *
     * @param instance The entity to update.
     */
    suspend fun updateWithTransaction(instance: T) {
        update(instance)
    }

    /**
     * Deletes an entity inside a transaction.
     *
     * @param instance The entity to delete.
     */
    suspend fun deleteWithTransaction(instance: T) {
        delete(instance)
    }
}