package common.base.view

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/8/16<br>
 * Time: 15:43<br>
 * <P>DESC:
 * MVX架构中的 View层的代理接口
 * </p>
 * ******************(^_^)***********************
 */
interface IViewDelegate {

    /**
     * View代理提供
     */
    @LayoutRes
    fun providedLayoutResId() = 0


    fun providedLayoutView(): View? = null

    /**
     * 初始化 View层的各控件
     */
    fun initViews()

    fun <V : View> getView(@IdRes viewId: Int): V?


}