package com.codeqm.mpay.impl

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codeqm.mpay.base.Alipay
import com.codeqm.mpay.base.PayStrategy
import com.codeqm.mpay.listener.PayListener

/**
 * 描述：支付宝支付策略
 * @author CQM
 * @time   2021/11/11
 */
class AliPayStrategy(
    private val activity: AppCompatActivity,
    private val payParameters: String,
    private val listener: PayListener
) : PayStrategy {
    private var TAG: String = this::class.java.simpleName

    override fun payAction() {
        Log.d(
            TAG,
            "支付宝支付 payParameters=$payParameters  listener=$listener"
        )
        Alipay.instance.toPay(activity, payParameters, listener)
    }
}