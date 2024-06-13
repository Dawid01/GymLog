package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.models.ExerciseSet

class SetAdapter(
    private val sets: MutableList<ExerciseSet>,
    private val bodyOnly: Boolean,
    private val context: Context,
    private val listener: ItemListener? = null
) : RecyclerView.Adapter<SetAdapter.SetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_set_item, parent, false)
        return SetViewHolder(view)
    }

    override fun getItemCount(): Int = sets.size

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val exerciseSet = sets[position]
        with(holder) {
            tvSet.text = "${position + 1}"
            tvPrevious.text = "-"

            val repsText = if (exerciseSet.rep > 0) exerciseSet.rep.toString() else ""
            tvReps.setText(repsText)
            tvReps.setHint("0")

            val weightText = if (exerciseSet.volume > 0) {
                if (exerciseSet.volume % 1 == 0f) exerciseSet.volume.toInt().toString() else exerciseSet.volume.toString()
            } else {
                ""
            }
            tvWeight.setText(weightText)
            tvWeight.setHint("0")

            checkBox.isChecked = exerciseSet.checked

            checkBox.setOnCheckedChangeListener { _, value ->
                exerciseSet.checked = value
                listener?.onValueChange()
            }

            tvReps.addTextChangedListener { value ->
                exerciseSet.rep = value?.toString()?.toIntOrNull() ?: 0
                listener?.onValueChange()
            }

            if (bodyOnly) {
                tvWeight.visibility = View.GONE
            } else {
                tvWeight.addTextChangedListener { value ->
                    exerciseSet.volume = value?.toString()?.toFloatOrNull() ?: 0f
                    listener?.onValueChange()
                }
            }

            itemView.setOnLongClickListener {
                listener?.onItemLongClick(adapterPosition, this@SetAdapter)
                true
            }
        }
    }

    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSet: TextView = itemView.findViewById(R.id.set_value)
        val tvPrevious: TextView = itemView.findViewById(R.id.previous_value)
        val tvReps: EditText = itemView.findViewById(R.id.reps_value)
        val tvWeight: EditText = itemView.findViewById(R.id.weight_value)
        val checkBox: CheckBox = itemView.findViewById(R.id.is_checked_value)
    }

    interface ItemListener {
        fun onValueChange()
        fun onItemLongClick(position: Int, adapter: SetAdapter)
    }
}
