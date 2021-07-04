package dev.gonz.flow.experiment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.gonz.flow.experiment.databinding.ActivityMainBinding
import dev.gonz.flow.experiment.db.StopwatchDb
import dev.gonz.flow.experiment.stopwatch.Stopwatch
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var myFlow: Flow<Int>
    private var flowJob: Job? = null

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.fab.clicksFlow()
            .onEach { launchStopwatch() }
            .launchIn(lifecycleScope)
    }

    private fun launchStopwatch() {
        if (flowJob != null) {
            flowJob!!.cancel()
            flowJob = null
            return
        }

        val db = StopwatchDb.getDb(applicationContext)

        val stopwatch = Stopwatch(Dispatchers.Default, db.dao(), 1_000L)

        flowJob = stopwatch.stopwatchFlow()
            .onEach { binding.tvCounter.text = it.toClock() }
            .launchIn(lifecycleScope)
    }
}

fun Long.toClock() =
    "${this / 60}:${if (this % 60 < 10) "0${this % 60}" else this % 60}"
