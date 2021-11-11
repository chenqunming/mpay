package com.codeqm.mpay.listener


//未安装微信
const val NO_INSTALLED = "-2000"
//微信版本过低
const val VERSION_LOW = "-2001"
//支付参数异常
const val PARAMETERS_ERROR = "-2002"
//支付失败
const val PAY_ERROR = "-2003"


interface PayListener {


    //唤起支付回调
    fun onPayStart(result: String)

    //支付成功
    fun onPaySuccess()

    //支付失败
    fun onPayError(code: String, message: String?)

    //支付取消
    fun onPayCancel()

    //支付完成
    fun onPayComplete()

}