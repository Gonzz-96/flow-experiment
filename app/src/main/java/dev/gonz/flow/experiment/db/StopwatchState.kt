package dev.gonz.flow.experiment.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stopwatch_state")
data class StopwatchState(
    val time: Long,
    @PrimaryKey
    val id: Int = 1
)
