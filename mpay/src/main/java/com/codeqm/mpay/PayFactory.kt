package com.codeqm.mpay

import androidx.appcompat.app.AppCompatActivity
import com.codeqm.mpay.base.PayStrategy
import com.codeqm.mpay.impl.AliPayStrategy
import com.codeqm.mpay.impl.WXPayStrategy
import com.codeqm.mpay.listener.PayListener

/**
 * 描述：支付策略工厂
 * @author CQM
 * @time   2021/11/11
 */

const val TYPE_WX_PAY = 1 //1 微信支付
const val TYPE_ALI_PAY = 2 // 2支付宝支付

class PayFactory(private val activity: AppCompatActivity) {

    private var payStrategy: PayStrategy? = null

    fun payAction(type: Int, payParameters: String, listener: PayListener) {
        payStrategy = when (type) {
            TYPE_WX_PAY -> {
                WXPayStrategy(activity, payParameters, listener)
            }
            TYPE_ALI_PAY -> {
                AliPayStrategy(activity, payParameters, listener)
            }
            else -> {
                WXPayStrategy(activity, payParameters, listener)
            }
        }
        payStrategy?.payAction()
    }

}