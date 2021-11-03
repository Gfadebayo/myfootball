package com.exzell.myfootball

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppExecutors {

    val diskExecutor: ExecutorService

    val ioExecutor: ExecutorService

    val mainExecutor: Executor

    init {
        diskExecutor = Executors.newFixedThreadPool(3)

        ioExecutor = Executors.newFixedThreadPool(3)

        mainExecutor = MainExecutor()
    }

    inner class MainExecutor: Executor{

        private val handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }
}