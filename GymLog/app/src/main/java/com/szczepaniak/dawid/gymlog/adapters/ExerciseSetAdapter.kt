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
import com.szczepaniak.dawid.gymlog.activities.ExerciseInfoActivity
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.ExerciseSet

class ExerciseSetAdapter(private val exercises: MutableList<Exercise>, private val context: Context, private val valueChangeListener: ValueChangeListener? = null) : RecyclerView.Adapter<ExerciseSetAdapter.ExerciseSetViewHolder>() {


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
        sets.add(ExerciseSet(0, false, 0, 0f))
        val bodyOnly: Boolean = exercise.equipment?.equals("body_only") == true
        val setAdapter = SetAdapter(sets, bodyOnly, context, object : SetAdapter.ItemListener{
            override fun onValueChange() {
                valueChangeListener?.onValueChange()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onItemLongClick(position: Int, adapter: SetAdapter) {

                val builder = AlertDialog.Builder(context)
                builder.setMessage("Delete?")
                    .setPositiveButton("Yes") { dialog, id ->
                        if(sets.size > 1) {
                            sets.removeAt(position)
                            adapter.notifyDataSetChanged()
                        }else{
                            Toast.makeText(context, "You can't delete last set", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }

                val alert = builder.create()
                alert.show()

            }

        })
        holder.setRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.setRecyclerView.adapter = setAdapter
        holder.addSetButton.setOnClickListener {
            if(sets.size < 5) {
                sets.add(ExerciseSet(sets.size, false,0, 0f))
                setAdapter.notifyItemInserted(sets.size)
            }else{
                Toast.makeText(context, "Max 5 sets", Toast.LENGTH_SHORT).show()
            }
        }

        holder.wieightColumn.visibility = if(bodyOnly) View.GONE else View.VISIBLE
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
        val addSetButton: Button = itemView.findViewById(R.id.add_set_button)
        val wieightColumn: View = itemView.findViewById(R.id.weight_column)
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

    interface ValueChangeListener {
        fun onValueChange()
    }
}