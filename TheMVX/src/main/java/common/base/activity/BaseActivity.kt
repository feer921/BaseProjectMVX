package common.base.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import common.base.utils.CommonLog

/**
 ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/8/16<br>
 * Time: 18:49<br>
 * <P>DESC:
 * Activity基类
 * </p>
 * ******************(^_^)***********************
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val TAG = javaClass.simpleName
    protected var LIFE_CIRCLE_DEBUG = false
    protected var PRINT_TASK_ID = false

    protected lateinit var mContext: Context
    protected lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onCreate() " + if (PRINT_TASK_ID) " taskId: $taskId" else "")
        }
        appContext = applicationContext
        mContext = this
    }

    override fun onStart() {
        super.onStart()
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onStart()")
        }
    }

    override fun onResume() {
        super.onResume()
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onResume()")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onNewIntent() intent = $intent")
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onRestart()")
        }
    }

    override fun onPause() {
        super.onPause()
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onPause()")
        }
    }

    override fun onStop() {
        super.onStop()
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onStop()")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG,"---> onActivityResult() requestCode = $requestCode resultCode = $resultCode data = $data")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onSaveInstanceState() outState = $outState")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onRestoreInstanceState() savedInstanceState = $savedInstanceState")
        }
    }



    /**
     * 结束/finish()自已
     * @param needTransitionAnim 是否需要过场动画
     */
    protected fun finishSelf(needTransitionAnim: Boolean) {
        finish()
        if (needTransitionAnim) {
//            switchActivity(true)
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onConfigurationChanged() newConfig = $newConfig")
        }
    }

    override fun onBackPressed() {
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> onBackPressed()")
        }
        super.onBackPressed()
    }

    /**
     * Activity生命周期是否经历了finish()
     */
    protected var isResumeFinish = false

    /**
     * 注：该方法为主动调用方法，不在Activity的生命周期流程中
     * 则需要注意：如果Activity是自动结束(如，屏幕旋转等)的，因不会走finish()而导致在此方法内作的释放不执行
     */
    @CallSuper
    override fun finish() {
        isResumeFinish = true
        if (LIFE_CIRCLE_DEBUG) {
            i(TAG, "---> finish() isResumeDestroy = $isResumeDestroy")
        }
        //changed by fee  :这里屏蔽来自onDestroy()方法内的调用finish()时,不再调用super.finish()
        if (!isResumeDestroy) {
            super.finish()
        }
    }

    /**
     * Activity生命周期是否经历了onDestroy()
     */
    private var isResumeDestroy = false
    override fun onDestroy() {
        //added by fee :解决Activity在自动销毁过程中,不走finish()而导致一些子Activity在finish()作释放功能没有执行的问题，或者不在本框架内处理
        isResumeDestroy = true
        if (LIFE_CIRCLE_DEBUG) {
            CommonLog.i(TAG, "---> onDestroy()")
        }
        if (!isResumeFinish) {
            finish()
        }
        super.onDestroy()
    }

    /**
     * 基类提供普通Log输出之error级信息输出
     * 注意一点：因为第二个参数是可变参数，该方法允许只传一个参数eg.: e("")
     * @param logTag log的TAG，如果为null,会使用[.TAG]
     * @param logBody
     */
    protected fun e(logTag: String?, vararg logBody: Any?) {
        CommonLog.e(logTag ?: TAG, logBody)
    }

    /**
     * 基类提供普通Log输出之info级信息输出
     * 注意一点：因为第二个参数是可变参数，该方法允许只传一个参数eg.: i("")
     * @param logTag log的TAG，如果为null,会使用[.TAG]
     * @param logBody Log内的具体要打印的信息
     */
    protected fun i(logTag: String?, vararg logBody: Any?) {
        CommonLog.i(logTag ?: TAG, logBody)
    }

    protected fun w(logTag: String?, vararg logBody: Any?) {
        CommonLog.w(logTag ?: TAG, logBody)
    }

    /**
     * 如果Activity内有Fragment，则Fragment可以通过该方法回调出可能需要请求宿主 do something
     * 并且本宿主处理完成后 返回应该返回的数据
     */
    fun onFragmentOptReq(curFragment: Fragment, curOptReq: String, curReqData: Any?): Any? {
        //here do nothing
        return null
    }
    /**
     * 将dimen资源id,转换为系统中的px值
     * @param dimenResId 定义的dimen 资源 ID
     * @return px像素值
     */
    protected fun dimenResPxValue(@DimenRes dimenResId: Int): Int {
        return resources.getDimensionPixelSize(dimenResId)
    }

    /**
     * 跳转到目标Activity
     * @param startIntent 启动 Intent
     * @param 启动的请求码 如果需要目标Activity返回相关结果时，用来区分的请求码
     * @param needReturnResult 是否本次跳转到目标Activity需要返回相关结果
     */
    protected fun jumpToActivity(startIntent: Intent, requestCode: Int, needReturnResult: Boolean) {
        if (needReturnResult) {//注意一个小点：当想要跳转的目标Activity的启动模式为 singleTask/singleInstance
            //使用该API跳转时，系统仍然会使目标Activity以 默认/standard 模式启动
            startActivityForResult(startIntent, requestCode)
        }
        else{
            startActivity(startIntent)
        }
    }

    protected fun jumpToActivity(targetActivityClass: Class<*>) {
        val startIntent = Intent(mContext, targetActivityClass)
        jumpToActivity(startIntent, 0, false)
    }

    protected fun jumpToActivity(
        targetActiviyClass: Class<*>,
        requestCode: Int,
        needReturnResult: Boolean
    ) {
        val startIntent = Intent(mContext, targetActiviyClass)
        jumpToActivity(startIntent, requestCode, needReturnResult)
    }

    protected fun jumpToActivity(intentBlock: Intent.() -> Unit) {
        val intent = Intent()
        intent.intentBlock()
        startActivity(intent)
    }
}