package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.activities.ExerciseInfoActivity
import com.szczepaniak.dawid.gymlog.models.Exercise

class ExercisesAdapter(private val exercises: List<Exercise>, private val context: Context, private val canSelect: Boolean, private val listener: OnSelectOrUnselectItem? = null, private val itemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    private val selectedExercises = HashSet<Exercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ExerciseViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.icon.setImageDrawable(context.resources.getDrawable(getIconImage(exercise.muscle!!)))
        holder.tvName.text = exercise.name
        holder.tvMuscle.text = exercise.muscle.toString().replace("_", " ").capitalize()
        holder.tvDifficulty.text = exercise.difficulty.toString().uppercase()
        holder.tvDifficulty.setTextColor(getDifficultyColor(exercise.difficulty!!))

        holder.info.setOnClickListener {
            val intent = Intent(context, ExerciseInfoActivity::class.java)
            intent.putExtra("name", exercise.name)
            intent.putExtra("muscle", exercise.muscle)
            intent.putExtra("type", exercise.type)
            intent.putExtra("equipment", exercise.equipment)
            intent.putExtra("difficulty", exercise.difficulty)
            intent.putExtra("instructions", exercise.instructions)
            val p1 = androidx.core.util.Pair<View, String>(holder.icon, holder.icon.transitionName)
            val p2 = androidx.core.util.Pair<View, String>(holder.tvName, holder.tvName.transitionName)
            val p3 = androidx.core.util.Pair<View, String>(holder.card, holder.card.transitionName)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                p1, p2, p3
            )

            context.startActivity(intent, options.toBundle())
        }

        var isSelected = checkIsExerciseSelected(exercise)
        holder.selected.visibility = if (isSelected) View.VISIBLE else View.GONE



        holder.itemView.setOnClickListener {
            if(canSelect) {
                isSelected = checkIsExerciseSelected(exercise)
                if (isSelected) {
                    removeSelectedExercise(exercise)
                    holder.selected.visibility = View.GONE
                    listener?.onSelectedChange(selectedExercises)
                } else {
                    selectedExercises.add(exercise)
                    holder.selected.visibility = View.VISIBLE
                    listener?.onSelectedChange(selectedExercises)
                }
            }
            itemClickListener?.onItemClick(position)
        }


    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val tvName: TextView = itemView.findViewById(R.id.name_text)
        val tvMuscle: TextView = itemView.findViewById(R.id.exercises_list_text)
        val tvDifficulty: TextView = itemView.findViewById(R.id.difficulty_text)
        val info: ImageView = itemView.findViewById(R.id.info_image)
        val card: CardView = itemView.findViewById(R.id.card)
        val selected: ImageView = itemView.findViewById(R.id.selected_image)
    }

    @SuppressLint("DiscouragedApi")
    fun getIconImage(muscle: String): Int {
        return context.resources.getIdentifier(muscle, "drawable", context.packageName)
    }

    fun getDifficultyColor(difficulty: String): Int {
        return when (difficulty) {
            "beginner" -> Color.GREEN
            "intermediate" -> Color.parseColor("#FFA500")
            "expert" -> Color.RED
            else -> Color.WHITE
        }
    }

    fun checkIsExerciseSelected(exercise: Exercise) : Boolean{
        for(selected in selectedExercises){
            if(exercise.equals(selected)){
                return true
            }
        }
        return false
    }

    fun removeSelectedExercise(exercise: Exercise){
        for(selected in selectedExercises){
            if(exercise.equals(selected)){
                selectedExercises.remove(selected)
            }
        }
    }


    interface OnSelectOrUnselectItem {
        fun onSelectedChange(selected:  HashSet<Exercise>)
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}
