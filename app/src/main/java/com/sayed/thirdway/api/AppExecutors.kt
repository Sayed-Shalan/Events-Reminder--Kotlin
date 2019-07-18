package com.sayed.thirdway.api

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class AppExecutors(private var diskIO: Executor,private var networkIO: Executor,private var mainThread: Executor) {

    @Inject
    constructor():this(
        Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
        MainThreadExecutor())

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

}