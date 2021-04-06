package dev.gonz.flow.experiment.stopwatch

import android.util.Log
import dev.gonz.flow.experiment.db.StopwatchState
import dev.gonz.flow.experiment.db.StopwatchStateDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class Stopwatch(
    private val dispatcher: CoroutineDispatcher,
    private val storageApi: StopwatchStateDao,
    private val stopwatchInterval: Long = 100L
) {
    private var stopwatchState: Long? = null
    private var counter = 0L

    fun stopwatchFlow(): Flow<Long> = ticker()
        .combine(storageApi.stopwatchStateFlow()) { tick, state ->
            stopwatchState = tick + (state?.time ?: 0L)
            stopwatchState ?: 0L
        }
        .distinctUntilChanged()
        .onCompletion {
            persist(stopwatchState ?: 0L)
        }
        .flowOn(dispatcher)

    private fun ticker() = flow {
        counter = 0L
        while (true) {
            Log.v("Ticker", "Tick $counter")
            emit(counter++)
            delay(stopwatchInterval)
        }
    }

    private fun persist(value: Long) = GlobalScope.launch {
        storageApi.insertNewState(StopwatchState(value))
        Log.v("Ticker", "Last: ${storageApi.getLast()}")
    }
}
