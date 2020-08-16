package common.base.utils

import android.util.Log

/**
 ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/8/16<br>
 * Time: 19:15<br>
 * <P>DESC:
 * 通用Log输出类
 * </p>
 * ******************(^_^)***********************
 */
object CommonLog {
    var ISDEBUG: Boolean = false

    fun logEnable(toEnable: Boolean) {
        ISDEBUG = toEnable
    }

    fun w(tag: String?, content: String?) {
        if (ISDEBUG) {
            Log.w(tag, content)
        }
    }

    fun w(tag: String?, vararg objs: Any?) {
        if (ISDEBUG) {
            Log.w(tag, getInfo(*objs))
        }
    }

    fun i(tag: String?, content: String?) {
        if (ISDEBUG) {
            Log.i(tag, content)
        }
    }

    fun i(tag: String?, vararg objs: Any?) {
        if (ISDEBUG) {
            Log.i(tag, getInfo(*objs))
        }
    }

    fun d(tag: String?, content: String?) {
        if (ISDEBUG) {
            Log.d(tag, content)
        }
    }

    fun d(tag: String?, vararg objs: Any?) {
        if (ISDEBUG) {
            Log.d(tag, getInfo(*objs))
        }
    }

    fun e(tag: String?, content: String?) {
        if (ISDEBUG) {
            Log.e(tag, content)
        }
    }

    fun e(tag: String?, content: String?, e: Throwable?) {
        if (ISDEBUG) {
            Log.e(tag, content, e)
        }
    }

    fun e(tag: String?, vararg objs: Any?) {
        if (ISDEBUG) {
            Log.e(tag, getInfo(*objs))
        }
    }

    fun e(
        logTag: String?,
        logContent: String?,
        appendStackInfo: Boolean
    ) {
        if (!appendStackInfo) {
            e(logTag, logContent)
        } else {
            val sb = StringBuilder()
            appendStack(sb)
            sb.append(logContent)
            e(logTag, sb.toString())
        }
    }

    fun i(
        logTag: String?,
        logContent: String?,
        appendStackInfo: Boolean
    ) {
        if (!appendStackInfo) {
            i(logTag, logContent)
        } else {
            val sb = StringBuilder()
            appendStack(sb)
            sb.append(logContent)
            i(logTag, sb.toString())
        }
    }

    fun getInfo(vararg objs: Any?): String {
        if (objs == null) {
            return ""
        }
        val sb = StringBuilder()
        for (`object` in objs) {
            sb.append(`object`)
        }
        return sb.toString()
    }

    fun sysOut(msg: Any?) {
        if (ISDEBUG) {
            println(msg)
        }
    }

    fun sysErr(msg: Any?) {
        if (ISDEBUG) {
            System.err.println(msg)
        }
    }

    private const val START_STACK_INDEX = 4
    private const val PRINT_STACK_COUNT = 2

    private fun appendStack(sb: StringBuilder) {
        val stacks =
            Thread.currentThread().stackTrace
        if (stacks != null && stacks.size > START_STACK_INDEX) {
            val lastIndex =
                Math.min(stacks.size - 1, START_STACK_INDEX + PRINT_STACK_COUNT)
            for (i in lastIndex downTo START_STACK_INDEX) {
                if (stacks[i] == null) {
                    continue
                }
                var fileName = stacks[i]!!.fileName
                if (fileName != null) {
                    val dotIndx = fileName.indexOf('.')
                    if (dotIndx > 0) {
                        fileName = fileName.substring(0, dotIndx)
                    }
                }
                sb.append(fileName)
                sb.append('(')
                sb.append(stacks[i]!!.lineNumber)
                sb.append(")")
                sb.append("->")
            }
            val stackTraceElement = stacks[START_STACK_INDEX]
            if (stackTraceElement != null) {
                sb.append(stackTraceElement.methodName)
            }
        }
        sb.append('\n')
    }

    /**
     * 由于Android系统对日志长度有限制的，最大长度为4K（注意是字符串的长度），超过这个范围的自动截断，会出现打印不全的情况
     * @param logLevelFlagChar Log的输出级别字符标志；取值: 'd'; 'i'; 'e'; 'w'; 'v'
     * @param logTag Log的tag
     * @param logContent Log的内容
     */
    fun fullLog(
        logLevelFlagChar: Char,
        logTag: String?,
        logContent: String
    ) {
        var logContent = logContent
        if (ISDEBUG && !CheckUtil.isEmpty(logContent)) {
            val segmentSize = 3 * 1024
            val len = logContent.length
            if (len <= segmentSize) {
                log(logLevelFlagChar, logTag, logContent)
            } else {
                while (logContent.length > segmentSize) {
                    val suitableLogContent = logContent.substring(0, segmentSize)
                    log(logLevelFlagChar, logTag, suitableLogContent)
                    logContent = logContent.substring(segmentSize)
                    //                    StringBuilder sb = new StringBuilder(logContent);
//                    sb.delete(0, segmentSize);
//                    logContent = sb.toString();
//                    sb.append(logContent, segmentSize, logContent.length());
//                    logContent = logContent.replace(logContent, "");//这个效率低
                }
                log(logLevelFlagChar, logTag, logContent)
            }
        }
    }

    fun iFullLog(logTag: String?, logContent: String) {
        fullLog('i', logTag, logContent)
    }

    fun iFullLog(logTag: String?, vararg logObj: Any?) {
        iFullLog(logTag, getInfo(*logObj))
    }

    fun log(
        logLevelFlagChar: Char,
        logTag: String?,
        logContent: String?
    ) {
        if (ISDEBUG) {
            when (logLevelFlagChar) {
                'i' -> i(logTag, logContent)
                'e' -> e(logTag, logContent)
                'd' -> d(logTag, logContent)
                'w' -> w(logTag, logContent)
                'v' -> Log.v(logTag, logContent)
            }
        }
    }
}