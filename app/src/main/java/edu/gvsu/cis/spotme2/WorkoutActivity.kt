package edu.gvsu.cis.spotme2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelLazy
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime

class WorkoutActivity : AppCompatActivity() {
    var backBool: Boolean = false
    var legBool: Boolean = false
    var armBool: Boolean = false
    var shoulderBool: Boolean = false
    var chestBool: Boolean = false
    var cardioBool: Boolean = false
    var backString: String = "Back"
    var legString: String = "Legs"
    var armString: String = "Arms"
    var shoulderString: String = "Shoulders"
    var chestString: String = "Chest"
    var cardioString: String = "String"
    var noteString: String = ""
    var nameString: String = ""
    var lengthString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JodaTimeAndroid.init(this)
        setContentView(R.layout.activity_workout)

        val next = findViewById<Button>(R.id.nextButton)
        next.setOnClickListener {v ->
            val intent = Intent(this@WorkoutActivity, AdvertisingActivity::class.java)
            startActivity(intent)
        }

        fun onCheckboxClicked(view: View) {
            if (view is CheckBox) {
                val checked: Boolean = view.isChecked

                when (view.id) {
                    R.id.backBox -> {
                        if (checked) {
                            backBool = true
                        }
                    }
                    R.id.legBox -> {
                        if (checked) {
                            legBool = true
                        }
                    }
                    R.id.armsBox -> {
                        if (checked) {
                            armBool = true
                        }
                    }
                    R.id.shoulderBox -> {
                        if (checked) {
                            shoulderBool = true
                        }
                    }
                    R.id.chestBox -> {
                        if (checked) {
                            chestBool = true
                        }
                    }
                    R.id.cardioBox -> {
                        if (checked) {
                            cardioBool = true
                        }
                    }
                    R.id.nameText -> {
                        if(R.id.nameText.toString() != ""){
                            nameString = R.id.nameText.toString()
                        }
                    }
                    R.id.lengthText -> {
                        if(R.id.lengthText.toString() != ""){
                            lengthString = R.id.lengthText.toString()
                        }
                    }
                    R.id.notesText -> {
                        if(R.id.notesText.toString() != ""){
                            noteString = R.id.notesText.toString()
                        }
                    }
                }
            }
        }

        class WorkoutObject {
            val ITEMS: MutableList<WorkoutObject> = ArrayList()

            fun addItem(item: WorkoutObject) {
                ITEMS.add(item)
            }

            inner class WorkoutObject(var backBool: Boolean, var legBool: Boolean,
             var armBool: Boolean, var shoulderBool: Boolean, var chestBool: Boolean, var cardioBool: Boolean,
             var nameString: String, var noteString: String)
            {
                override fun toString(): String {
                    var workoutString = "$nameString's Workout Plan: "
                    if (backBool) {
                        workoutString += "$backString, "
                    }
                    if (armBool) {
                        workoutString += "$armString, "
                    }
                    if (legBool) {
                        workoutString += "$legString, "
                    }
                    if (shoulderBool) {
                        workoutString += "$shoulderString, "
                    }
                    if (chestBool) {
                        workoutString += "$chestString, "
                    }
                    if (cardioBool) {
                        workoutString += "$cardioString, "
                    }
                    if (lengthString != ""){
                        workoutString += "for $lengthString Minutes, \n"
                    }
                    if (noteString != ""){
                        workoutString += "Extra Notes About Today's Workout: $noteString \n"
                    }
                    return workoutString + " " + DateTime.now()
                }
            }
        }
    }
}