package com.codeqm.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codeqm.mpay.ALI_PAY
import com.codeqm.mpay.MPay
import com.codeqm.mpay.PayListener
import com.codeqm.mpay.WX_PAY

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



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

        });


    }
}