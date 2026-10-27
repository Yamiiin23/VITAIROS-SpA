package cl.vitairos.vitairosm.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VitairosDao {
    @Query("SELECT * FROM mood_entries ORDER BY createdAt DESC")
    fun observeMoods(): Flow<List<MoodEntry>>

    @Insert
    suspend fun insertMood(entry: MoodEntry)

    @Delete
    suspend fun deleteMood(entry: MoodEntry)

    @Query("SELECT COUNT(*) FROM mood_entries")
    suspend fun countMoods(): Int

    @Query("SELECT * FROM appointments ORDER BY createdAt DESC")
    fun observeAppointments(): Flow<List<Appointment>>

    @Insert
    suspend fun insertAppointment(appointment: Appointment)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("SELECT COUNT(*) FROM appointments")
    suspend fun countAppointments(): Int
}
