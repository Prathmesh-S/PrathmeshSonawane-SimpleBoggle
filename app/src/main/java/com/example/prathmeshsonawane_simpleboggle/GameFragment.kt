package com.example.prathmeshsonawane_simpleboggle

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.Toast

class GameFragment : Fragment() {

    private var buttonNames = arrayOf(
        "00",
        "01",
        "02",
        "03",
        "10",
        "11",
        "12",
        "13",
        "20",
        "21",
        "22",
        "23",
        "30",
        "31",
        "32",
        "33"
    )
    private val allButtons = mutableListOf<Button>()
    private lateinit var editText: EditText
    private lateinit var clearButton: Button
    private lateinit var submitButton: Button
    private  var currentWord = ""
    private var listOfWords= mutableListOf<String>()
    private var lastIndex = ""
    private var score = 0
    private lateinit var dictionary :Set<String>

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.game_container, container, false)

        //Init All Variables
        editText = view.findViewById(R.id.editText)
        clearButton = view.findViewById(R.id.clearButton)
        submitButton = view.findViewById(R.id.submitButton)
        dictionary = loadDictionary()

        for (buttonName in buttonNames) {
            val nameOfButton = "button$buttonName"
            val resourceId = resources.getIdentifier(nameOfButton, "id", context?.packageName)

            // Check if resource ID is valid
            if (resourceId != 0) {
                // Get the button using its resource ID
                val button = view.findViewById<Button>(resourceId)
                allButtons.add(button)
            }

        }

        //Init all buttons
        initButtons(allButtons)
        setOnClickListenersForAllButtons(allButtons)

        //Clear Button
        clearButton.setOnClickListener {
            clearAllButtons(allButtons)
        }

        //Get Score by clicking submit Button
        submitButton.setOnClickListener {
            getScore()
        }

        //Return view so that fragment can be inflated
        return view
    }

    //Create Listeners for Each Button
    private fun setOnClickListenersForAllButtons(allButtons: List<Button>) {
        for (button in allButtons) {
            button.setOnClickListener {
                if (lastIndex == "") {
                    currentWord = button.text.toString()
                    editText.setText(currentWord)
                    lastIndex = resources.getResourceEntryName(button.id).toString().takeLast(2)
                    button.isEnabled = false
                } else {
                    Log.d("ButtonClicked", button.id.toString())
                   val newIndex =  resources.getResourceEntryName(button.id).toString().takeLast(2)
                    val currentIndex = lastIndex

                    val row = currentIndex.take(1).toInt()
                    val col = currentIndex.takeLast(1).toInt()
                    val possibleNextStepIndexes =
                        arrayOf(((row+1).toString() + col.toString()), ((row-1).toString() + col.toString()),
                        ((row).toString() + (col+1).toString()),  ((row).toString() + (col-1).toString()),
                            ((row+1).toString() + (col+1).toString()), ((row-1).toString() + (col-1).toString()),
                            ((row+1).toString() + (col-1).toString()),   ((row-1).toString() + (col+1).toString())
                    )

                    //If Consecutive Letters: Allow
                    if(newIndex in possibleNextStepIndexes) {
                        currentWord += button.text.toString()
                        editText.setText(currentWord)
                        lastIndex = newIndex
                        button.isEnabled = false
                    } else {
                        //Letter is not consecutive
                        Toast.makeText(context, "Button is not consecutive!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initButtons(allButtons: List<Button>) {
        var listOfVowels = setOf<String>("A", "E", "I", "O", "U")
        var listOfConsonants = setOf<String>("A","B", "C", "D","E", "F", "G", "H","I", "J", "K", "L", "M", "N", "O","P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        val vowel1 = listOfVowels.random()
        listOfVowels = listOfVowels - vowel1
        val vowel2 = listOfVowels.random()
        listOfConsonants = listOfConsonants - vowel1 - vowel2
        var listOfLetter = setOf<String>(vowel1, vowel2)

        //Get all the button values
        for (i in 2 until allButtons.size) {
            val letter = listOfConsonants.random()
            listOfLetter = listOfLetter + letter
            listOfConsonants = listOfConsonants - letter
        }

        //Used For Testing
        var newlistOfLetter = arrayListOf<String>("T","I", "R", "E","E", "F", "G", "S","I", "J", "K", "L", "M", "N", "O","P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

        //Assign buttons their values
        for (button in allButtons) {
            //val letter = listOfLetter.random()
            val letter = newlistOfLetter.removeAt(0)
            button.text = letter
            listOfLetter = listOfLetter - letter
        }
    }

    private fun clearAllButtons(allButtons: List<Button>) {
        editText.setText("")
        currentWord = ""
        lastIndex = ""

        for (button in allButtons) {
            button.isEnabled = true
        }
    }

    private fun getScore() {
        val word = currentWord
        var tempScore = 0
        val listOfVowels = setOf<String>("A", "E", "I", "O", "U")
        var listOfConsonants = setOf<String>("A","B", "C", "D","E", "F", "G", "H","I", "J", "K", "L", "M", "N", "O","P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

        //Min 2 vowels
        var countVowels = 0
        for (char in word) {
            if (listOfVowels.contains(char.toString())) {
                countVowels++
            }
        }

        if (countVowels <2) {
            Toast.makeText(context, "This word has less than two vowels! 10 Points have been deducted!", Toast.LENGTH_SHORT).show()
            score -= 10
            if (score < 0) {
                score = 0
            }
            return
        }

        //Check to see if word has already been claimed.
        if (word in listOfWords) {
            Toast.makeText(context, "You already chose this word before!", Toast.LENGTH_SHORT).show()
            return
        }

        //Check if word is at least 4 char long
        if (word.length <4) {
            Toast.makeText(context, "This word is not long enough! 10 Points have been deducted!", Toast.LENGTH_SHORT).show()
            score -= 10
            if (score < 0) {
                score = 0
            }
            return
        }
        //Check if word is real
        if (word !in dictionary) {
            Toast.makeText(context, "This is not a word! 10 Points have been deducted!", Toast.LENGTH_SHORT).show()
            score -= 10
            if (score < 0) {
                score = 0
            }
            return
        }

        //Calculate Points
        tempScore += (5 * countVowels)
        tempScore += (word.length - countVowels)
        val specialChar = setOf('S', 'Z', 'P', 'X', 'Q')

        for (char in word) {
            if (specialChar.contains(char)) {
                tempScore *= 2
            }
        }

        //At this point, the word is valid and a score has been calculated.
        listOfWords.add(word)
        Toast.makeText(context, "Great Job! You got $tempScore points!", Toast.LENGTH_SHORT).show()
        score +=tempScore
        clearAllButtons(allButtons)
    }

    private fun loadDictionary() : Set<String> {
        val words = mutableSetOf<String>()
        context?.assets?.open("words.txt")?.bufferedReader().use { br ->
            br?.forEachLine { line ->
                words.add(line.trim().uppercase())
            }
        }
        return words
    }
}