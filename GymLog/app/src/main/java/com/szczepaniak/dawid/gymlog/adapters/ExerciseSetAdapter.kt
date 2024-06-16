package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.activities.ExerciseInfoActivity
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.ExerciseSet
import java.util.Locale

class ExerciseSetAdapter(
    private val exercises: MutableList<Exercise>,
    private val context: Context,
    private val editable: Boolean = true,
    private val valueChangeListener: ValueChangeListener? = null
) : RecyclerView.Adapter<ExerciseSetAdapter.ExerciseSetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exerciseset_item, parent, false)
        return ExerciseSetViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseSetViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

    inner class ExerciseSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val tvName: TextView = itemView.findViewById(R.id.name_text)
        private val tvMuscle: TextView = itemView.findViewById(R.id.exercises_list_text)
        private val tvDifficulty: TextView = itemView.findViewById(R.id.difficulty_text)
        private val info: ImageView = itemView.findViewById(R.id.info_image)
        private val setRecyclerView: RecyclerView = itemView.findViewById(R.id.set_recycler_view)
        private val addSetButton: Button = itemView.findViewById(R.id.add_set_button)
        private val wieightColumn: View = itemView.findViewById(R.id.weight_column)
        private val previousColumn: View = itemView.findViewById(R.id.previous_column)
        private val checkedColumn: View = itemView.findViewById(R.id.checked_column)

        init {
            info.setOnClickListener {
                val exercise = exercises[adapterPosition]
                val intent = Intent(context, ExerciseInfoActivity::class.java).apply {
                    putExtra("name", exercise.name)
                    putExtra("muscle", exercise.muscle)
                    putExtra("type", exercise.type)
                    putExtra("equipment", exercise.equipment)
                    putExtra("difficulty", exercise.difficulty)
                    putExtra("instructions", exercise.instructions)
                }
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity)
                context.startActivity(intent, options.toBundle())
            }
            if(!editable){
                previousColumn.visibility = View.GONE
                checkedColumn.visibility = View.GONE
                addSetButton.visibility = View.GONE
                info.visibility = View.GONE
            }
        }

        fun bind(exercise: Exercise) {
            icon.setImageResource(getIconImage(exercise.muscle!!))
            tvName.text = exercise.name
            tvMuscle.text = exercise.muscle.toString().replace("_", " ").capitalize(Locale.ROOT)
            tvDifficulty.text = exercise.difficulty.toString().uppercase()
            tvDifficulty.setTextColor(getDifficultyColor(exercise.difficulty!!))

            val sets: MutableList<ExerciseSet> = exercise.sets?.toMutableList() ?: mutableListOf(ExerciseSet(0, 0, false, 0, 0f))
            exercise.sets = sets
            valueChangeListener?.onValueChange()
            val bodyOnly: Boolean = exercise.equipment?.equals("body_only") == true
            val setAdapter = SetAdapter(sets, bodyOnly, editable, context, object : SetAdapter.ItemListener {
                override fun onValueChange() {
                    valueChangeListener?.onValueChange()
                }

                override fun onItemLongClick(position: Int, adapter: SetAdapter) {
                    if(editable) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Delete?")
                            .setPositiveButton("Yes") { dialog, id ->
                                if (sets.size > 1 && position in 0 until sets.size) {
                                    sets.removeAt(position)
                                    adapter.notifyItemRemoved(position)
                                    adapter.notifyItemRangeChanged(position, sets.size)
                                    exercise.sets = sets
                                    valueChangeListener?.onValueChange()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "You can't delete the last set",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, id ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
            })

            setRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = setAdapter
            }

            addSetButton.setOnClickListener {
                if (sets.size < 5) {
                    val previousSet = sets[sets.size - 1]
                    sets.add(ExerciseSet(sets.size, 0, false, previousSet.rep, previousSet.volume))
                    exercise.sets = sets.toList()
                    setAdapter.notifyItemInserted(sets.size - 1)
                    valueChangeListener?.onValueChange()
                } else {
                    Toast.makeText(context, "Maximum 5 sets allowed", Toast.LENGTH_SHORT).show()
                }
            }

            wieightColumn.visibility = if (bodyOnly) View.GONE else View.VISIBLE
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getIconImage(muscle: String): Int {
        return context.resources.getIdentifier(muscle, "drawable", context.packageName)
    }

    private fun getDifficultyColor(difficulty: String): Int {
        return when (difficulty) {
            "beginner" -> Color.GREEN
            "intermediate" -> Color.parseColor("#FFA500")
            "expert" -> Color.RED
            else -> Color.WHITE
        }
    }



    interface ValueChangeListener {
        fun onValueChange()
    }
}