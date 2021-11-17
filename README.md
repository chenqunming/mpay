# mpay

集成微信支付和支付宝支付

工厂模式+策略模式


依赖集成

implementation "io.github.chenqunming:mpay:1.0.1"

```

PayFactory(this).payAction(TYPE_WX_PAY, "",object : PayListener{
    override fun onPayStart(result: String) {
        Log.d(TAG, "onPayStart showLoading")
    }

    override fun onPaySuccess() {
        Log.d(TAG, "onPaySuccess")
    }

    override fun onPayError(code: String, message: String?) {
        Log.d(TAG, "onPayError")
    }

    override fun onPayCancel() {
        Log.d(TAG, "onPayCancel")
    }

    override fun onPayComplete() {
        Log.d(TAG, "onPayComplete hideLoading")
    }

})

```

