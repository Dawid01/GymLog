package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.models.Workout
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutAdapter : PagingDataAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)
        workout?.let { holder.bind(it) }
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.title_text)
        private val tvDate: TextView = itemView.findViewById(R.id.date_text)
        private val tvTime: TextView = itemView.findViewById(R.id.time_text)
        private val tvVolume: TextView = itemView.findViewById(R.id.volume_text)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)


        @SuppressLint("SetTextI18n")
        fun bind(workout: Workout) {
            tvTitle.text = workout.title

            val dateFormat = SimpleDateFormat("EEEE dd.MM.yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(workout.startTime)
            tvDate.text = formattedDate

            tvTime.text = "Time: " + formatTime(workout.getDuration())
            ratingBar.rating = workout.rating.toFloat()
            val volumeText = if (workout.volume > 0) {
                if (workout.volume % 1 == 0f) workout.volume.toInt().toString() else workout.volume.toString()
            } else {
                "0"
            }
            tvVolume.text = "Volume: " + volumeText + "kg"
        }

        private fun formatTime(seconds: Long): String {
            val s = seconds % 60
            val m = (seconds / 60) % 60
            val h = (seconds / 3600)

            return when {
                h > 0 -> String.format("%d h %d min %d s", h, m, s).replace(" 0 min", "").replace(" 0 s", "")
                m > 0 -> String.format("%d min %d s", m, s).replace(" 0 s", "")
                else -> String.format("%d s", s)
            }
        }
    }

    class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }

}
