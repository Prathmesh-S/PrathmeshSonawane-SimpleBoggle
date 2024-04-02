package com.example.prathmeshsonawane_simpleboggle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlin.math.abs

class ScoreFragment : Fragment() {

    private lateinit var score: TextView
    private lateinit var restartButton: Button
    private var listener: OnNewGameRequestedListener? = null

    //Create an interface the main Activity can utilize.
    interface OnNewGameRequestedListener {
        fun onNewGameRequested()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewGameRequestedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnNewGameRequestedListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.score_container, container, false)

        //Init
        score = view.findViewById(R.id.textViewScore)
        restartButton = view.findViewById(R.id.restartGameButton)

        restartButton.setOnClickListener{
            score.text = "Score : 0"
            listener?.onNewGameRequested()
        }

        return view
    }

    fun newScore(scoreVal : Int) {
        score.text = "Score : " + scoreVal.toString()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}