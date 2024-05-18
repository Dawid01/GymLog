package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.models.Exercise

class ExercisesAdapter(private val exercises: Array<Exercise>, private val context: Context) : RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ExerciseViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.icon.setImageDrawable(context.getResources().getDrawable(getIconImage(exercise.muscle!!)))
        holder.tvName.text = exercise.name
        holder.tvMuscle.text = exercise.muscle.toString().replace("_", " ").capitalize()
        holder.tvDifficulty.text = exercise.difficulty.toString().uppercase()
        holder.tvDifficulty.setTextColor(getDifficultyColor(exercise.difficulty!!))
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val icon: ImageView = itemView.findViewById(R.id.icon)
       val tvName: TextView = itemView.findViewById(R.id.name_text)
       val tvMuscle: TextView = itemView.findViewById(R.id.muscle_text)
       val tvDifficulty: TextView = itemView.findViewById(R.id.difficulty_text)
    }

    @SuppressLint("DiscouragedApi")
    fun getIconImage(muscle: String) : Int{
        return context.resources.getIdentifier(muscle, "drawable", context.packageName)
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