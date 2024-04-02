package com.example.prathmeshsonawane_simpleboggle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.pow

class MainActivity : AppCompatActivity(), ScoreFragment.OnNewGameRequestedListener, GameFragment.OnScoreUpdateListener {

    private lateinit var sensorManager: SensorManager
    private var shakeDetector: ShakeDetector? = null
    private var accelerometer: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameFragment = GameFragment()
        gameFragment.setOnScoreUpdateListener(this)

        //Populate the two fragments
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.gameContainer, gameFragment )
                replace(R.id.scoreContainer, ScoreFragment())
                commit()
            }
        }

        val shakeDetector = ShakeDetector {
            onScoreUpdate(0)
            onNewGameRequested()
        }
        // Get the system service for SensorManager
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

// Get the accelerometer sensor
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

// Register the ShakeDetector as a listener for accelerometer sensor events
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)


    }

    override fun onNewGameRequested() {
        val gameFragment = supportFragmentManager.findFragmentById(R.id.gameContainer) as? GameFragment
        gameFragment?.restart()
    }

    override fun onScoreUpdate(score: Int) {
        val scoreFragment = supportFragmentManager.findFragmentById(R.id.scoreContainer) as? ScoreFragment
        scoreFragment?.newScore(score)
    }

    //Extra Credit:

    private class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {
        private var lastTime: Long = 0
        private var lastX: Float = 0.0f
        private var lastY: Float = 0.0f
        private var lastZ: Float = 0.0f
        private val shakeThreshold = 400

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastTime) > 100) {
                val x = event?.values?.get(0) ?: 0f
                val y = event?.values?.get(1) ?: 0f
                val z = event?.values?.get(2) ?: 0f

                val speed = Math.sqrt(((x - lastX).toDouble().pow(2.0) + (y - lastY).toDouble().pow(2.0) + (z - lastZ).toDouble().pow(2.0))) / (currentTime - lastTime) * 10000
                if (speed > shakeThreshold) {
                    onShake()
                }

                lastTime = currentTime
                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

}