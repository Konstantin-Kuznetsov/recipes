package com.example.feed.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Flow<T>.test(scope: CoroutineScope): TestObserver<T> =
    TestObserver(scope, this)

class TestObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T>
) {
    val valuesList: List<T>
        get() = valuesMutableList

    private val valuesMutableList = mutableListOf<T>()
    private val job: Job = scope.launch {
        flow.collect { valuesMutableList.add(it) }
    }

    fun finish() {
        job.cancel()
    }
}