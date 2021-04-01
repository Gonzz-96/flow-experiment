package dev.gonz.flow.experiment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.gonz.flow.experiment.databinding.ActivityMainBinding
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

        myFlow = flow {
            var counter = 1
            while (true) {
                emit(counter++)
                if (counter > 10_000) {
                    break
                }
                delay(10L)
            }
        }

        binding.fab.clicksFlow()
                .onEach { launchFlow() }
                .launchIn(lifecycleScope)
    }

    private fun launchFlow() {
        if (flowJob != null && flowJob!!.isActive) {
            flowJob?.cancel()
        }
        flowJob = myFlow.flowOn(Dispatchers.Default)
                .onEach { number ->
                    binding.tvCounter.text = number.toString()
                }
                .onCompletion {
                    Toast.makeText(this@MainActivity, "Flow was finished!", Toast.LENGTH_SHORT).show()
                }
                .launchIn(lifecycleScope)
    }
}
