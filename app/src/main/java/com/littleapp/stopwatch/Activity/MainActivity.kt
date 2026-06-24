package com.littleapp.stopwatch.Activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.littleapp.stopwatch.R
import com.littleapp.stopwatch.Unit.THEME
import com.littleapp.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val context: Context = this
    private var isResume = false
    private val handler = Handler(Looper.getMainLooper())
    private var tMilliSec: Long = 0
    private var tStart: Long = 0
    private var tBuff: Long = 0
    private var tUpdate = 0L
    private var sec = 0
    private var min = 0
    private var milliSec = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.text = getString(R.string.stop_watch)

        binding.btStart.setOnClickListener {
            if (!isResume) {
                tStart = SystemClock.uptimeMillis()
                handler.postDelayed(runnable, 0)
                binding.chronometer.start()
                isResume = true
                binding.btStop.visibility = View.GONE
                binding.btStart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pause))
            } else {
                tBuff += tMilliSec
                handler.removeCallbacks(runnable)
                binding.chronometer.stop()
                isResume = false
                binding.btStop.visibility = View.VISIBLE
                binding.btStart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play))
            }
        }

        binding.btStop.setOnClickListener {
            if (!isResume) {
                binding.btStart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play))
                tMilliSec = 0L
                tStart = 0L
                tBuff = 0L
                tUpdate = 0L
                sec = 0
                min = 0
                milliSec = 0
                binding.lastTimeDate.text = binding.chronometer.text.toString()
                binding.chronometer.text = getString(R.string._00_00_00)
            }
        }
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            tMilliSec = SystemClock.uptimeMillis() - tStart
            tUpdate = tBuff + tMilliSec
            sec = (tUpdate / 1000).toInt()
            min = sec / 60
            sec %= 60
            milliSec = (tUpdate % 100).toInt()
            binding.chronometer.text = String.format(getString(R.string._02d_02d_02d), min, sec, milliSec)
            handler.postDelayed(this, 60)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}