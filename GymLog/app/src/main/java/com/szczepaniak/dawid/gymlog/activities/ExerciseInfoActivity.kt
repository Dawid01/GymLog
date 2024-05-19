package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.szczepaniak.dawid.gymlog.R
import java.util.Locale


class ExerciseInfoActivity : AppCompatActivity() {



    @SuppressLint("UseCompatLoadingForDrawables", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val icon: ImageView = findViewById(R.id.icon)
        val tvName: TextView = findViewById(R.id.name_text)
        val tvMuscle: TextView = findViewById(R.id.muscle_text)
        val tvType: TextView = findViewById(R.id.type_text)
        val tvEquipment: TextView = findViewById(R.id.equipment_text)
        val tvDifficulty: TextView = findViewById(R.id.difficulty_text)
        val tvInstruction: TextView = findViewById(R.id.instructions_text)
        val tutorialButton: Button = findViewById(R.id.tutorial_button)
        val backButton: Button = findViewById(R.id.back_button)


        icon.setImageDrawable(getResources().getDrawable(getIconImage(intent.getStringExtra("muscle")!!)))
        tvName.text = intent.getStringExtra("name")
        tvMuscle.text = intent.getStringExtra("muscle").toString().replace("_", " ").capitalize(
            Locale.ROOT)
        tvType.text = intent.getStringExtra("type").toString().replace("_", " ").capitalize(
            Locale.ROOT)
        tvEquipment.text = intent.getStringExtra("equipment").toString().replace("_", " ").capitalize(
            Locale.ROOT)
        tvDifficulty.text = intent.getStringExtra("difficulty").toString().uppercase()
        tvDifficulty.setTextColor(getDifficultyColor(intent.getStringExtra("difficulty")!!))
        tvInstruction.text = intent.getStringExtra("instructions").toString().capitalize(Locale.ROOT)

        tutorialButton.setOnClickListener {
            val youtubeUrl = "https://www.youtube.com/results?search_query=${intent.getStringExtra("name")}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            intent.setPackage("com.google.android.youtube")
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "YouTube app is not installed", Toast.LENGTH_SHORT).show()
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                startActivity(webIntent)
            }
        }

        backButton.setOnClickListener {
            finishAfterTransition()
        }


    }

    @SuppressLint("DiscouragedApi")
    fun getIconImage(muscle: String) : Int{
        return resources.getIdentifier(muscle, "drawable", packageName)
    }

    fun getDifficultyColor(dificulty: String) : Int {
        when(dificulty){
            "beginner" -> return Color.GREEN
            "intermediate" -> return Color.YELLOW
            "expert" -> return Color.RED
        }
        return Color.WHITE
    }
}