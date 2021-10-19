# mpay

集成微信支付和支付宝支付

  MPay.pay(this, ALI_PAY, "", object : PayListener {
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
