package com.szczepaniak.dawid.gymlog.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R

class MusclesAdapter(private val muscles: List<String>, private val context: Context) : RecyclerView.Adapter<MusclesAdapter.MuscleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.muscle_item, parent, false)
        return MuscleViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: MuscleViewHolder, position: Int) {
        val muscle = muscles[position]
        if(muscle != "") {
            holder.tvName.text = muscle.replace("_", " ").capitalize()
            holder.icon.setImageDrawable(context.getResources().getDrawable(getIconImage(muscle)))
        }else{
            holder.tvName.text = "All Muscles"
            holder.icon.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return muscles.size
    }

    class MuscleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.muscle_name)
        val icon: ImageView = itemView.findViewById(R.id.muscle_icon)
    }


    @SuppressLint("DiscouragedApi")
    fun getIconImage(muscle: String) : Int{
        return context.resources.getIdentifier(muscle, "drawable", context.packageName)
    }

}
