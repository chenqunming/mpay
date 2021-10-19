package com.codeqm.mpay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXPayEntryActivity : Activity(), IWXAPIEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (WXPay.INSTANCE.wXApi == null) {
            finish()
        }
        WXPay.INSTANCE.wXApi?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        WXPay.INSTANCE.wXApi?.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {}
    override fun onResp(baseResp: BaseResp) {
        //4、支付结果回调 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
        Log.e("weiXinPay", "baseResp:$baseResp")
        if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errStr != null) {
                Log.e("weiXinPay", "errStr=" + baseResp.errStr)
            }
            WXPay.INSTANCE.onResp(baseResp.errCode, baseResp.errStr)
            finish()
        }
    }
}