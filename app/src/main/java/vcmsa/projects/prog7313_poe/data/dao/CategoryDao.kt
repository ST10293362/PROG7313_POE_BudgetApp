package vcmsa.projects.prog7313_poe.data.dao

import androidx.room.*
import vcmsa.projects.prog7313_poe.core.models.Category
import java.util.UUID

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: UUID): Category?

    @Query("SELECT * FROM categories WHERE userId = :userId")
    suspend fun getByUserId(userId: UUID): List<Category>

    @Query("SELECT * FROM categories WHERE name = :name")
    suspend fun getByName(name: String): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("DELETE FROM categories WHERE userId = :userId")
    suspend fun deleteByUserId(userId: UUID)

    @Query("UPDATE categories SET totalAmount = totalAmount + :amount WHERE id = :categoryId")
    suspend fun updateTotalAmount(categoryId: UUID, amount: Double)
} 