package com.example.lab7_1_kt

import androidx.appcompat.app.AppCompatActivity

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import com.example.lab7_1_kt.R

class MainActivity : AppCompatActivity() {
    private var rabprogerss = 0
    private var turprogess = 0
    private var seekBar: SeekBar? = null
    private var seekBar2: SeekBar? = null
    private var btn_start: Button? = null

    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            1 -> seekBar!!.progress = rabprogerss
        }
        if (rabprogerss >= 100 && turprogess < 100) {
            Toast.makeText(this@MainActivity, "兔子勝利", Toast.LENGTH_SHORT).show()
            btn_start!!.isEnabled = true
        }
        false
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.seekBar)
        seekBar2 = findViewById(R.id.seekBar2)
        btn_start = findViewById(R.id.btn_start)

        btn_start!!.setOnClickListener {
            btn_start!!.isEnabled = false
            rabprogerss = 0
            turprogess = 0
            seekBar!!.progress = 0
            seekBar2!!.progress = 0

            runThread()

            runAsyncTask()
        }
    }

    private fun runThread() {
        Thread(Runnable {
            while (rabprogerss <= 100 && turprogess <= 100) {
                try {
                    Thread.sleep(100)
                    rabprogerss += (Math.random() * 3).toInt()
                    val msg = Message()
                    msg.what = 1
                    mHandler.sendMessage(msg)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }).start()
    }

    private fun runAsyncTask() {
        object : AsyncTask<Void, Int, Boolean>() {
            override fun doInBackground(vararg voids: Void): Boolean? {
                while (turprogess <= 100 && rabprogerss < 100) {
                    try {
                        Thread.sleep(100)
                        turprogess += (Math.random() * 3).toInt()
                        publishProgress(turprogess)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
                return true
            }

            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
                seekBar2!!.progress = values[0]!!
            }

            override fun onPostExecute(aBoolean: Boolean?) {
                super.onPostExecute(aBoolean)
                if (turprogess >= 100 && rabprogerss < 100) {
                    Toast.makeText(this@MainActivity, "烏龜勝利", Toast.LENGTH_SHORT).show()
                    btn_start!!.isEnabled = true
                }
            }
        }.execute()
    }

}

