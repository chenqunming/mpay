package com.codeqm.mpay

import android.app.Activity
import android.util.Log

const val WX_PAY =1
const val ALI_PAY =2
object MPay {

    var TAG = this::class.java.simpleName

    /**
     * 支付 type：1 微信支付 ， 2支付宝支付
     */
    fun pay(activity: Activity, payWay: Int, payParameters: String, listener: PayListener) {
        when (payWay) {
            WX_PAY -> {
                Log.d(TAG, "微信支付 payWay=$payWay  payParameters=$payParameters listener=$listener")
                WXPay.INSTANCE.toWxPay(activity, payParameters, listener)
            }
            ALI_PAY -> {
                Log.d(
                    TAG,
                    "支付宝支付 payWay=$payWay  payParameters=$payParameters  listener=$listener"
                )
                Alipay.instance.toAliPay(activity, payParameters, listener)
            }
            else -> {
                Log.d(TAG, "微信支付 payWay=$payWay  payParameters=$payParameters  listener=$listener")
                WXPay.INSTANCE.toWxPay(activity, payParameters, listener)
            }
        }
    }

}