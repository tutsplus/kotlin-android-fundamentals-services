package com.annapurna.envato.androidservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val myHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startBackgroundService(view: View) {
        val intent = Intent(this, MyBackgroundService::class.java)
        startService(intent)
    }

    fun stopBackgroundService(view: View) {
        val intent = Intent(this, MyBackgroundService::class.java)
        stopService(intent)
    }

    fun startIntentService(view: View) {

        val myResultReceiver = MyIntentResultReceiver(null)

        val intent = Intent(this, MyIntentService::class.java)
        intent.putExtra("intent_result_receiver", myResultReceiver)
        startService(intent)
    }

    fun startForegroundService(view: View) {
        val intent = Intent(this, MyForegroundService::class.java)
        intent.action = MyForegroundService.ACTION_START_FOREGROUND_SERVICE
        startService(intent)
    }

    fun stopForegroundService(view: View) {
        val intent = Intent(this, MyForegroundService::class.java)
        intent.action = MyForegroundService.ACTION_STOP_FOREGROUND_SERVICE
        startService(intent)
    }

    inner class MyIntentResultReceiver(handler: Handler?): ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {

            if (resultCode == 15 && resultData != null) {
                val result = resultData.getString("rr_result")

                myHandler.post {
                    tvIntentServiceResult.text = result
                }
            }

            super.onReceiveResult(resultCode, resultData)
        }
    }

    val myBroadcastReceiver = object: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            val result = intent?.getStringExtra("br_result")
            myHandler.post {
                tvIntentServiceResult.text = result
            }
        }

    }

    override fun onResume() {
        super.onResume()

        // Registering BroadcastReceiver
        val myIntentFilter = IntentFilter()
        myIntentFilter.addAction("action.service.broadcast.receive")
        registerReceiver(myBroadcastReceiver, myIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myBroadcastReceiver)
    }
}
