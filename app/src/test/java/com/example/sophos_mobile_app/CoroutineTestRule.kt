package com.example.sophos_mobile_app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@ExperimentalCoroutinesApi
class CoroutineTestRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}