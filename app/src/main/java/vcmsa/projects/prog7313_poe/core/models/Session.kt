package vcmsa.projects.prog7313_poe.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "userId")
    val userId: UUID,
    @ColumnInfo(name = "startTime")
    val startTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "endTime")
    val endTime: Long? = null
) 