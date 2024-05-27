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

class SetAdapter(private val sets: MutableList<ExerciseSet>, private val bodyOnly: Boolean, private val context: Context, private val listener: ItemListener? = null): RecyclerView.Adapter<SetAdapter.SetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_set_item, parent, false)
        return SetViewHolder(view)
    }

    override fun getItemCount(): Int {
       return sets.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {

        val exerciseSet = sets[position]
        holder.tvSet.text = "${position + 1}"
        holder.tvPrevious.text = "-"
        holder.tvReps.setText(if(exerciseSet.rep > 0) exerciseSet.rep.toString() else "")
        holder.tvReps.setHint("0")
        holder.tvWeight.setText(if(exerciseSet.volume > 0) exerciseSet.volume.toString() else "")
        holder.tvWeight.setHint("0")
        holder.checkBox.isChecked = exerciseSet.checked
        if((position + 1) % 2 == 0){
            holder.background.setBackgroundColor(com.google.android.material.R.attr.colorSurface)
        }

        holder.checkBox.setOnCheckedChangeListener{ _, value ->
            listener?.onValueChange()
            sets[position].checked = value
        }

        holder.tvReps.addTextChangedListener {value ->
            listener?.onValueChange()
            exerciseSet.rep = value.toString().toInt()
        }

        if(bodyOnly){
            holder.tvWeight.visibility = View.GONE

        }else{

            holder.tvWeight.addTextChangedListener { value ->
                listener?.onValueChange()
                exerciseSet.volume = value.toString().toFloat()
            }
        }
        holder.itemView.setOnLongClickListener {
            listener?.onValueChange()
            listener?.onItemLongClick(position, this)
            true
        }


    }

    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvSet: TextView = itemView.findViewById(R.id.set_value)
        val tvPrevious: TextView = itemView.findViewById(R.id.previous_value)
        val tvReps: EditText = itemView.findViewById(R.id.reps_value)
        val tvWeight: EditText = itemView.findViewById(R.id.weight_value)
        val background: View = itemView.findViewById(R.id.item_background)
        val checkBox: CheckBox = itemView.findViewById(R.id.is_checked_value)

    }

    interface ItemListener {
        fun onValueChange()
        fun onItemLongClick(position: Int, adapter: SetAdapter)

    }

}