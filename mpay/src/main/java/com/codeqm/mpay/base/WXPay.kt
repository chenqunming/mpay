package com.codeqm.mpay.base

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.codeqm.mpay.listener.*
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * @author 98du
 */
class WXPay private constructor() {
    private val TAG = this.javaClass.simpleName


    companion object {
        val INSTANCE: WXPay by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WXPay()
        }
    }

    /**
     * 获取微信接口
     *
     * @return
     */
    var wXApi: IWXAPI? = null
    private var listenerRef: WeakReference<PayListener?>? = null

    /**
     * 初始化微信支付接口
     *
     * @param appId
     */
    private fun init(context: Context, appId: String?): Boolean {
        wXApi = WXAPIFactory.createWXAPI(context.applicationContext, null)
        val register = wXApi?.registerApp(appId) ?: false
        Log.d(TAG, "微信支付 registerApp=$register")
        return register
    }

    fun toPay(activity: Activity, request: PayReq, listener: PayListener): Boolean {

        if (wXApi == null) {
            init(activity, request.appId)
        }
        if (!wXApi!!.isWXAppInstalled) {
            Log.d(TAG, "微信支付 未安装微信")
            listener.onPayError(NO_INSTALLED, "未安装微信")
            return false
        }
        if (wXApi!!.wxAppSupportAPI < Build.PAY_SUPPORTED_SDK_INT) {
            Log.d(TAG, "微信支付 微信版本过低")
            listener.onPayError(VERSION_LOW, "微信版本过低")
            return false
        }


        listenerRef = WeakReference(listener)
        val startTime = System.currentTimeMillis()
        Log.d(TAG, "微信支付 startTime=$startTime")
        val result = wXApi!!.sendReq(request)
        Log.d(TAG, "微信支付 sendReq=$result")
        if (result) {
            listener.onPayStart("success")
        } else {
            listener.onPayStart("fail")
        }
        return result
    }

    /**
     * 响应支付回调
     *
     * @param code        错误码
     * @param message    错误描述
     */
    fun onResp(code: Int, message: String) {
        Log.d(TAG, "微信支付 onResp error_code=$code message=$message")
        if (listenerRef == null || listenerRef!!.get() == null) {
            return
        }
        when (code) {
            0 -> {
                //支付成功
                listenerRef!!.get()!!.onPaySuccess()
            }
            1 -> {
                //支付异常
                listenerRef!!.get()!!.onPayError(PAY_ERROR, message)
            }
            2 -> {
                //支付取消
                listenerRef!!.get()!!.onPayCancel()
            }
        }
        listenerRef!!.get()!!.onPayComplete()
        listenerRef!!.clear()
    }


}