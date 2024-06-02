package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.activities.WorkoutActivity
import com.szczepaniak.dawid.gymlog.models.Routine

class RoutinesAdapter(
    private val routines: MutableList<Routine>,
    private val context: Context,
    private val onItemSettingsClickListener: OnItemSettingsClickListener? = null
) : RecyclerView.Adapter<RoutinesAdapter.RoutineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.routine_item, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routines[position]
        holder.bind(routine)
    }

    override fun getItemCount(): Int = routines.size

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.name_text)
        private val tvExercises: TextView = itemView.findViewById(R.id.exercises_list_text)
        private val musclesLayout: LinearLayout = itemView.findViewById(R.id.muscles_icons_layout)
        private val settings: ImageView = itemView.findViewById(R.id.info_image)
        private val startRoutineButton: Button = itemView.findViewById(R.id.start_routine_button)

        init {
            settings.setOnClickListener {
                onItemSettingsClickListener?.onItemSettingsClick(adapterPosition)
            }
        }

        fun bind(routine: Routine) {
            tvTitle.text = routine.name

            if (routine.exercises.isNotEmpty()) {
                val exercisesStr = routine.exercises.joinToString { it.name.toString() }
                val maxLength = 100
                val truncatedText = if (exercisesStr.length > maxLength) {
                    "${exercisesStr.substring(0, maxLength - 3)}..."
                } else {
                    exercisesStr
                }
                tvExercises.text = truncatedText
                musclesLayout.removeAllViews()
                val layoutParams = ViewGroup.LayoutParams(100, 100)
                for (muscle in routine.muscles) {
                    val imageView = ImageView(context)
                    imageView.setImageResource(getIconImage(muscle))
                    imageView.layoutParams = layoutParams
                    musclesLayout.addView(imageView)
                }
            } else {
                tvExercises.text = "Empty list of exercises!"
            }

            startRoutineButton.setOnClickListener {
                Singleton.setSelectedRoutine(routine)
                val intent = Intent(context, WorkoutActivity::class.java).apply {
                    putExtra("selected", true)
                }
                context.startActivity(intent)
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getIconImage(muscle: String): Int {
        return context.resources.getIdentifier(muscle, "drawable", context.packageName)
    }

    interface OnItemSettingsClickListener {
        fun onItemSettingsClick(position: Int)
    }
}
