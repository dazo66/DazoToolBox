package com.dazo66.utils.TimeUtil

import org.apache.log4j.Logger
import java.util.*

/**
 * @author Dazo66
 */
class TimeTaskPool {

    //创建线程安全的列表
    internal var nextTasks: MutableList<TimedTask> = Collections.synchronizedList<TimedTask>(ArrayList())
    internal var tasks: MutableList<TimedTask> = Collections.synchronizedList<TimedTask>(ArrayList())

    //时间流对象 主要是包装了时间任务的线程
    internal var flow: TimeFlow

    init {
        flow = TimeFlow(this)
    }

    fun add(task: TimedTask) {
        if (task.runnable != null) {
            logger.info(String.format("添加时间任务 触发时间：{} 当前时间：{}", task.tiggerTime.toString(), Date().toString()))
            tasks.add(task)
            flow.flush()
        }
    }

    fun remove(task: TimedTask) {
        tasks.remove(task)
        flow.flush()
    }

    /**
     * 得到下一个要运行的任务列表
     * 同时运行的会放在一起
     * 只会比较时间先后 总是把先运行的拿出来
     *
     * @return
     */
    fun getNextTasks(): List<TimedTask> {
        nextTasks.clear()
        for (task in tasks) {
            if (nextTasks.size == 0) {
                nextTasks.add(task)
            } else {
                val flag = task.getTiggerTime().compareTo(nextTasks[0].getTiggerTime())
                if (flag == 0) {
                    nextTasks.add(task)
                } else if (flag < 0) {
                    nextTasks.clear()
                    nextTasks.add(task)
                }
            }
        }
        return nextTasks
    }

    fun timeoutEvent(millisTimeout: Int, runnable: Runnable) {
        add(TimedTask()
                .setTimes(1)
                .setRunnable(runnable)
                .setTiggerTime(Date(System.currentTimeMillis() + millisTimeout))
        )
    }

    companion object {
        private val logger = Logger.getLogger(TimeTaskPool::class.java)
    }
}
