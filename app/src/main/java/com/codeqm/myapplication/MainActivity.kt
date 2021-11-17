package com.codeqm.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.codeqm.mpay.PayFactory
import com.codeqm.mpay.TYPE_ALI_PAY
import com.codeqm.mpay.TYPE_WX_PAY
import com.codeqm.mpay.listener.PayListener

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tvAlipay = findViewById<TextView>(R.id.tvAlipay)
        val tvWxpay = findViewById<TextView>(R.id.tvAlipay)
        tvAlipay.setOnClickListener {
            PayFactory(this).payAction(TYPE_ALI_PAY, "",object : PayListener{
                override fun onPayStart(result: String) {
                    Log.d(TAG, "支付宝onPayStart showLoading")
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

        }
        tvWxpay.setOnClickListener {
            PayFactory(this).payAction(TYPE_WX_PAY, "",object : PayListener{
                override fun onPayStart(result: String) {
                    Log.d(TAG, "微信支付 onPayStart showLoading")
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

        }




    }
}