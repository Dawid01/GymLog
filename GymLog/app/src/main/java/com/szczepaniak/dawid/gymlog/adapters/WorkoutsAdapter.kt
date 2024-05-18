package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.models.Workout

class WorkoutAdapter(private val workouts: List<Workout>) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return WorkoutViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.tvDate.text = workout.date.toString()
        holder.tvTime.text = "Time: ${workout.time} mins"
        holder.tvVolume.text = "Volume: ${workout.volume} kg"
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.date_text)
        val tvTime: TextView = itemView.findViewById(R.id.time_text)
        val tvVolume: TextView = itemView.findViewById(R.id.volume_text)
    }
}
