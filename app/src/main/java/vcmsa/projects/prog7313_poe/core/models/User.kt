package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "monthlyBudget")
    val monthlyBudget: Double = 0.0,
    @ColumnInfo(name = "currentBudget")
    val currentBudget: Double = 0.0,
    @ColumnInfo(name = "minGoal")
    val minGoal: Double = 0.0,
    @ColumnInfo(name = "maxGoal")
    val maxGoal: Double = 0.0,
    @ColumnInfo(name = "goalsSet")
    val goalsSet: Boolean = false,
    @ColumnInfo(name = "profileCompleted")
    val profileCompleted: Boolean = false
)