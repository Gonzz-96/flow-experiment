package dev.gonz.flow.experiment.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StopwatchStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewState(time: StopwatchState)

    @Query("SELECT * FROM stopwatch_state where id = :id LIMIT 1")
    fun stopwatchStateFlow(id: Int = 1): Flow<StopwatchState?>

    @Query("SELECT * FROM stopwatch_state where id = :id LIMIT 1")
    suspend fun getLast(id: Int = 1): StopwatchState?
 }
