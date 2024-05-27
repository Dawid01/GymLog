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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.activities.ExerciseInfoActivity
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.ExerciseSet

class ExerciseSetAdapter(private val exercises: MutableList<Exercise>, private val context: Context) : RecyclerView.Adapter<ExerciseSetAdapter.ExerciseSetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exerciseset_item, parent, false)
        return ExerciseSetViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ExerciseSetViewHolder, position: Int) {
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
        var sets: MutableList<ExerciseSet> = mutableListOf()
        sets.add(ExerciseSet(0, 0))
        val setAdapter = SetAdapter(sets, context)
        holder.setRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.setRecyclerView.adapter = setAdapter
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ExerciseSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val tvName: TextView = itemView.findViewById(R.id.name_text)
        val tvMuscle: TextView = itemView.findViewById(R.id.exercises_list_text)
        val tvDifficulty: TextView = itemView.findViewById(R.id.difficulty_text)
        val info: ImageView = itemView.findViewById(R.id.info_image)
        val card: CardView = itemView.findViewById(R.id.card)
        val selected: ImageView = itemView.findViewById(R.id.selected_image)
        val setRecyclerView: RecyclerView = itemView.findViewById(R.id.set_recycler_view)
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

}