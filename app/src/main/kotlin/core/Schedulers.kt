package core

import android.os.Process
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

interface RxSchedulers {

    val db: Scheduler

    val main: Scheduler

    val computation: Scheduler

    val io: Scheduler

}

class RxSchedulersImpl
@Inject
constructor() : RxSchedulers {

    private companion object {
        val NET_THREAD_KEEP_ALIVE = 10L
    }

    override val db: Scheduler

    override val main: Scheduler = AndroidSchedulers.mainThread()

    override val computation: Scheduler = Schedulers.computation()

    override val io: Scheduler = Schedulers.io()

    init {
        val dbExecutor = ThreadPoolExecutor(
                1, 1, NET_THREAD_KEEP_ALIVE, TimeUnit.SECONDS,
                LinkedBlockingQueue<Runnable>(),
                PriorityThreadFactory("net-thread-pool", Process.THREAD_PRIORITY_BACKGROUND))
        dbExecutor.allowCoreThreadTimeOut(true)
        db = Schedulers.from(dbExecutor)
    }
}

class PriorityThreadFactory(private val name: String, private val threadPriority: Int) : ThreadFactory {
    private val number = AtomicInteger()

    override fun newThread(r: Runnable): Thread {
        return object : Thread(r, "$name-${number.andIncrement}") {
            override fun run() {
                Process.setThreadPriority(threadPriority)
                super.run()
            }
        }
    }

}