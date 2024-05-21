package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.models.Routine
import com.szczepaniak.dawid.gymlog.models.Workout

class RoutinesAdapter(private val routines: List<Routine>, private val context: Context) : RecyclerView.Adapter<RoutinesAdapter.RoutineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.routine_item, parent, false)
        return RoutineViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routines[position]
        holder.tvTitle.text = routine.name
        initializeItem(holder, routine)

        holder.settings.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle(routine.name)
            builder.setMessage("Do you want to delete this routine?")

            builder.setPositiveButton("Yes") { dialog, which ->
                //TODO delete routine
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return routines.size
    }

    class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.name_text)
        val tvExercises: TextView = itemView.findViewById(R.id.exercises_list_text)
        val musclesLayout: LinearLayout = itemView.findViewById(R.id.muscles_icons_layout)
        val settings: ImageView = itemView.findViewById(R.id.info_image)

    }

    private fun initializeItem(holder: RoutineViewHolder, routine: Routine){


        if(routine.exercises.isNotEmpty()) {
            var exercisesStr = ""
            for (exercise in routine.exercises) {
                exercisesStr += "${exercise.name}, "
            }
            val maxLength = 100
            var truncatedText = if (exercisesStr.length > maxLength) {
                exercisesStr.substring(0, maxLength - 3) + "..."
            } else {
                exercisesStr.substring(0, exercisesStr.length - 1)
            }

            if (truncatedText.endsWith(",") || truncatedText.endsWith(" ") || truncatedText.endsWith(
                    "-"
                )
            ) {
                truncatedText = truncatedText.substring(0, truncatedText.length - 1)
            }

            holder.tvExercises.text = truncatedText
            val layoutParams = ViewGroup.LayoutParams(80, 80)

            for (muscle in routine.muscles) {
                val imageView = ImageView(context)
                imageView.setImageResource(getIconImage(muscle))
                imageView.layoutParams = layoutParams
                holder.musclesLayout.addView(imageView)
            }
        }else{
            holder.tvExercises.text = "Empty list of exercises!"
        }
    }

    @SuppressLint("DiscouragedApi")
    fun getIconImage(muscle: String): Int {
        return context.resources.getIdentifier(muscle, "drawable", context.packageName)
    }
}