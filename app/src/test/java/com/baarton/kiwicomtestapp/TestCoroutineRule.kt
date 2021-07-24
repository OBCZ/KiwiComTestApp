package com.baarton.kiwicomtestapp

import com.baarton.kiwicomtestapp.app.IDatabaseModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.java.KoinJavaComponent

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {

    private val dbModule: IDatabaseModule by KoinJavaComponent.inject(IDatabaseModule::class.java)
    private val testCoroutineDispatcher = dbModule.coroutineDispatcher!!
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)

            base.evaluate()

            Dispatchers.resetMain()
            testCoroutineScope.cleanupTestCoroutines()
        }
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testCoroutineScope.runBlockingTest { block() }

}