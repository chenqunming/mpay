package com.codeqm.mpay

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
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

    fun toWxPay(activity: Activity, payParameters: String, listener: PayListener): Boolean  {
        var param: JSONObject? = null
        param = try {
            JSONObject(payParameters)
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d(TAG, "微信支付 参数JSON解析错误：" + e.message)
            listener.onPayError(PARAMETERS_ERROR, "参数异常")
            return false
        }
        return wxPay(
            activity,
            param?.optString("appid"),
            param?.optString("partnerid"),
            param?.optString("prepay_id"),
            param?.optString("noncestr"),
            param?.optString("timestamp"),
            param?.optString("sign"),
            listener
        )
    }

    /**
     * 调起支付
     *
     * @param appId     应用ID
     * @param partnerId 商户ID
     * @param prepayId  预付订单
     * @param nonceStr  随机字符串
     * @param timeStamp 时间戳
     * @param sign      签名
     */
    fun wxPay(
        activity: Activity, appId: String?, partnerId: String?, prepayId: String?,
        nonceStr: String?, timeStamp: String?, sign: String?, listener: PayListener
    ): Boolean {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(partnerId)
            || TextUtils.isEmpty(prepayId) || TextUtils.isEmpty(
                nonceStr
            )
            || TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(
                sign
            )
        ) {
            Log.d(TAG, "微信支付 缺少参数或参数为空")
            listener.onPayError(PARAMETERS_ERROR, "参数异常")
            return false
        }

        if (wXApi == null) {
            init(activity, appId)
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

        val request = PayReq()
        request.appId = appId
        request.partnerId = partnerId
        request.prepayId = prepayId
        request.packageValue = "Sign=WXPay"
        request.nonceStr = nonceStr
        request.timeStamp = timeStamp
        request.sign = sign
        return wxPay(request, listener)

    }

    fun wxPay(request: PayReq, listener: PayListener): Boolean {
        listenerRef = WeakReference(listener)
        val startTime = System.currentTimeMillis()
        Log.d(TAG, "微信支付 startTime=$startTime")
        val result = wXApi!!.sendReq(request)
        Log.d(TAG, "微信支付 endTime=" + System.currentTimeMillis())
        Log.d(TAG, "微信支付 totalTime=" + (System.currentTimeMillis() - startTime))
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