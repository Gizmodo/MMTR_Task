package com.example.fragmentvm.utils


import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//val <T> action1: FlowCollector<T>
/*

@InternalCoroutinesApi
fun <T> StateFlow<T>.collectWhenStarted(
    lifecycleOwner: LifecycleOwner,
    action: suspend (value: T) -> Unit,

) {
    lifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
        collect(action)
    }
}

*/
fun <T> Fragment.collectWhenStarted(flow: Flow<T>, block: (T) -> Unit) {
    flow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        .onEach { block(it) }
        .launchIn(viewLifecycleOwner.lifecycleScope)
}
fun <T> LifecycleOwner.collectWhenStarted(flow: Flow<T>, firstTimeDelay: Long = 0L, action: suspend (value: T) -> Unit) {
    lifecycleScope.launch {
        delay(firstTimeDelay)
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(action)
        }
    }
}
/*
//Same Result -> Boilerplate Code
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        homeFragmentViewModel.personInfo.collect {
            Log.d(TAG, "Message: $it")
        }
    }
}

//Same Result -> Cleaner Code
homeFragmentViewModel.personInfo.collectWhenStarted(this) {
    Log.d(TAG, "Flow Extension Function: $it")
}
*/
