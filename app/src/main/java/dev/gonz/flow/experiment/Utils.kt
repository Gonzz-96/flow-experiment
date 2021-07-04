package dev.gonz.flow.experiment

import android.view.View
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.Flow

@ExperimentalCoroutinesApi
fun View.clicksFlow() = callbackFlow<Unit> {
    this@clicksFlow.setOnClickListener { offer(Unit) }
    awaitClose { this@clicksFlow.setOnClickListener(null) }
}
