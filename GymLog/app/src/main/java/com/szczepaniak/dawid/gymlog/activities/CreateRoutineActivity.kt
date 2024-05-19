package com.szczepaniak.dawid.gymlog.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.szczepaniak.dawid.gymlog.R

class CreateRoutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_routine)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val addButton: Button = findViewById(R.id.add_exercise_button)
        addButton.setOnClickListener{
            val intent= Intent(this, ExcercisesActivity::class.java)
            startActivity(intent)
        }


        val cancel: TextView = findViewById(R.id.cancel_text)
        cancel.setOnClickListener{
            finish()
        }

    }
}