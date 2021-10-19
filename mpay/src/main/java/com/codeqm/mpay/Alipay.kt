package com.codeqm.mpay

import android.app.Activity
import android.util.Log
import com.alipay.sdk.app.PayTask
import java.lang.ref.WeakReference

class Alipay private constructor() {

    private val TAG = this.javaClass.simpleName

    companion object {
        val instance: Alipay by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Alipay()
        }
    }

    private var listenerRef: WeakReference<PayListener>? = null
    private var activityRef: WeakReference<Activity>? = null


    fun toAliPay(activity: Activity, orderInfo: String, listener: PayListener) {
        listenerRef = WeakReference<PayListener>(listener)
        activityRef = WeakReference<Activity>(activity)
        Thread(PayRunnable(orderInfo)).start()
        listener.onPayStart("success")
    }

    class PayRunnable(private val orderInfo: String) : Runnable {
        override fun run() {
            instance.activityRef?.let { ref ->
                ref.get()?.let {
                    val alipay = PayTask(it)
                    val result = alipay.payV2(orderInfo, true)
                    it.runOnUiThread {
                        instance.payBackResult(result)
                    }
                }

            }
        }

    }

    fun payBackResult(payResult: Map<String, String>) {
        Log.d(TAG, "支付宝支付 结果：$payResult")
        val resultStatus = payResult["resultStatus"]
        Log.d(TAG, "支付宝支付 resultStatus=$resultStatus")

//        9000	订单支付成功
//        8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//        4000	订单支付失败
//        5000	重复请求
//        6001	用户中途取消
//        6002	网络连接出错
//        6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//        其它	其它支付错误

        listenerRef?.let { ref ->
            ref.get()?.let {
                when (resultStatus) {
                    null, "" -> {
                        //结果解析错误
                        Log.d(TAG, "支付宝支付 onPayError:结果解析错误")
                        it.onPayError(PAY_ERROR, "结果解析错误")
                    }
                    "9000" -> {
                        //支付成功
                        Log.d(TAG, "支付宝支付 onPaySuccess:支付成功")
                        it.onPaySuccess()
                    }
                    "6001" -> {
                        //支付成功
                        Log.d(TAG, "支付宝支付 onPayCancel:用户中途取消")
                        it.onPayCancel()
                    }
                    else -> {
                        //支付错误
                        Log.d(TAG, "支付宝支付 onPayError:$payResult")
                        it.onPayError(resultStatus, payResult.toString())
                    }
                }
                it.onPayComplete()
            }

        }


    }


}