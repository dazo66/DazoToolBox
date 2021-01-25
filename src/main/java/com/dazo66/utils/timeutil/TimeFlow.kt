package com.dazo66.utils.timeutil


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.log4j.Logger

/**
 * @author Dazo66
 */
class TimeFlow(internal var pool: TimeTaskPool) : Runnable {
    internal var thread = Thread(this)

    fun flush() {
        //中断线程的while循环 结束线程
        thread.interrupt()
        //创建新的线程 由于只有一个线程 这里就不使用线程池了
        thread = Thread(this)
        thread.start()
    }

    override fun run() {
        logger.info("Time Thread Start")
        while (!Thread.currentThread().isInterrupted) {
            //如果没有任务 就使线程长时间休眠
            if (pool.getNextTasks().size == 0) {
                try {
                    Thread.sleep(1111114514)
                } catch (e: InterruptedException) {
                    println("线程刷新")
                    break//捕获到异常之后，执行break跳出循环。
                }

            }
            val timeInterval = pool.nextTasks[0].getTiggerTime().time - System.currentTimeMillis()
            try {
                if (timeInterval > 0) {
                    Thread.sleep(timeInterval)
                }
            } catch (e: InterruptedException) {
                logger.info("Time Thread Flush")
                break//捕获到异常之后，执行break跳出循环。
            }
            if (pool.nextTasks[0].getTiggerTime().time < System.currentTimeMillis()) {
                continue
            }
            for (task in pool.nextTasks) {
                try {
                    GlobalScope.launch {
                        try {
                            //依次运行任务
                            task.run()
                        } catch (e: Exception) {
                            logger.warn("定时任务执行出错：${ExceptionUtils.printRootCauseStackTrace(e)}")
                        }
                    }
                    //当执行次数为0时从等待任务中删除
                    if (task.times == 0) {
                        pool.remove(task)
                    }
                } catch (e: Exception) {
                    logger.warn(String.format("运行任务：[%s]时出现异常[%s]", task.javaClass.name,
                            e.message))
                }

            }
        }
    }

    companion object {
        private val logger = Logger.getLogger(TimeFlow::class.java)
    }

}
