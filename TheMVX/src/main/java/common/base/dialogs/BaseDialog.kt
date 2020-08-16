package common.base.dialogs

import android.app.Dialog
import android.content.Context
import androidx.annotation.StyleRes

/**
 ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/8/16<br>
 * Time: 18:54<br>
 * <P>DESC:
 * Dialog的基类
 * </p>
 * ******************(^_^)***********************
 */
abstract class BaseDialog<I : BaseDialog<I>>(context: Context, @StyleRes dialogThemeRes: Int) :

    Dialog(context, dialogThemeRes) {

    constructor(context: Context) : this(context, 0)


    /**
     * 返回实例自身
     */
    protected fun self(): I = this as I

}