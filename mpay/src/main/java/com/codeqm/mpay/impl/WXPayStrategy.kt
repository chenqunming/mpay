package com.codeqm.mpay.impl

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codeqm.mpay.base.PayStrategy
import com.codeqm.mpay.base.WXPay
import com.codeqm.mpay.listener.PARAMETERS_ERROR
import com.codeqm.mpay.listener.PayListener
import com.tencent.mm.opensdk.modelpay.PayReq
import org.json.JSONException
import org.json.JSONObject

/**
 * 描述：微信支付策略
 * @author CQM
 * @time   2021/11/11
 */
class WXPayStrategy(
    private val activity: AppCompatActivity,
    private val payReq: PayReq,
    private val listener: PayListener
) : PayStrategy {
    private var TAG: String = this::class.java.simpleName

    constructor(
        activity: AppCompatActivity,
        payParameters: String,
        listener: PayListener
    ) : this(activity, PayReq(), listener) {
        try {
            val params = JSONObject(payParameters)
            payReq.appId = params.getString("appid")
            payReq.partnerId = params.getString("partnerid")
            payReq.prepayId = params.getString("prepay_id")
            payReq.packageValue = "Sign=WXPay"
            payReq.nonceStr = params.getString("noncestr")
            payReq.timeStamp = params.getString("timestamp")
            payReq.sign = params.getString("sign")
        } catch (e: JSONException) {
            Log.d(TAG, "微信支付 参数JSON解析错误：" + e.message)
            listener.onPayError(PARAMETERS_ERROR, "参数异常")
            e.printStackTrace()
        }
    }

    override fun payAction() {
        Log.d(TAG, "微信支付 payParameters=$payReq listener=$listener")
        WXPay.INSTANCE.toPay(activity, payReq, listener)
    }
}