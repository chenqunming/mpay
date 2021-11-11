package com.codeqm.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codeqm.mpay.PayFactory
import com.codeqm.mpay.TYPE_WX_PAY
import com.codeqm.mpay.listener.PayListener

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




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


    }
}