package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.activities.WorkoutPreviewActivity
import com.szczepaniak.dawid.gymlog.models.Workout
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutAdapter(private val context: Context) : PagingDataAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return WorkoutViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)
        workout?.let { holder.bind(it) }
    }

    class WorkoutViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.title_text)
        private val tvDate: TextView = itemView.findViewById(R.id.date_text)
        private val tvTime: TextView = itemView.findViewById(R.id.time_text)
        private val tvVolume: TextView = itemView.findViewById(R.id.volume_text)
        private val tvSets: TextView = itemView.findViewById(R.id.sets_text)

        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
        private val line: View = itemView.findViewById(R.id.line)
        private val infoLayout: View = itemView.findViewById(R.id.info_layout)

        @SuppressLint("SetTextI18n")
        fun bind(workout: Workout) {
            tvTitle.text = workout.title

            val dateFormat = SimpleDateFormat("EEEE dd.MM.yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(workout.startTime).capitalize(Locale.ROOT)
            tvDate.text = formattedDate

            tvTime.text = Singleton.formatTime(workout.getDuration())
            ratingBar.rating = workout.rating.toFloat()
            tvSets.text = "${workout.sets}"
            val volumeText = if (workout.volume > 0) {
                if (workout.volume % 1 == 0f) workout.volume.toInt().toString() else workout.volume.toString()
            } else {
                "0"
            }
            tvVolume.text = "$volumeText kg"

            itemView.setOnClickListener {
                val p1 = androidx.core.util.Pair<View, String>(tvTitle, tvTitle.transitionName)
                val p2 = androidx.core.util.Pair<View, String>(infoLayout, infoLayout.transitionName)
                val p3 = androidx.core.util.Pair<View, String>(line, line.transitionName)
                val p4 = androidx.core.util.Pair<View, String>(ratingBar, ratingBar.transitionName)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    p1, p2, p3, p4
                )
                val intent = Intent(context, WorkoutPreviewActivity::class.java)
               // intent.putExtra("selected_workout", workout)
                Singleton.setSelectedWorkout(workout)
                context.startActivity(intent, options.toBundle())
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
