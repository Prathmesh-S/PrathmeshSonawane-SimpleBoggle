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

    }

    override fun onNewGameRequested() {
        val gameFragment = supportFragmentManager.findFragmentById(R.id.gameContainer) as? GameFragment
        gameFragment?.restart()
    }

    override fun onScoreUpdate(score: Int) {
        val scoreFragment = supportFragmentManager.findFragmentById(R.id.scoreContainer) as? ScoreFragment
        scoreFragment?.newScore(score)
    }
}