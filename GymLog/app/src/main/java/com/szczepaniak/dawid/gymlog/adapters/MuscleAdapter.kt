import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import java.util.Locale

class MusclesAdapter(private val muscles: List<String>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<MusclesAdapter.MuscleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.muscle_item, parent, false)
        return MuscleViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: MuscleViewHolder, position: Int) {
        val muscle = muscles[position]
        if(muscle != "") {
            holder.icon.visibility = View.VISIBLE
            holder.tvName.text = muscle.replace("_", " ").capitalize(Locale.ROOT)
            holder.icon.setImageDrawable(context.getDrawable(getIconImage(muscle)))
            holder.itemView.setOnClickListener {
                listener.onItemClick(muscle)
            }
        } else {
            holder.tvName.text = "All Muscles"
            holder.icon.visibility = View.GONE
            holder.itemView.setOnClickListener {
                listener.onItemClick(muscle)
            }
        }
    }

    override fun getItemCount(): Int {
        return muscles.size
    }

    inner class MuscleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.muscle_name)
        val icon: ImageView = itemView.findViewById(R.id.muscle_icon)
    }

    interface OnItemClickListener {
        fun onItemClick(muscle: String)
    }

    @SuppressLint("DiscouragedApi")
    private fun getIconImage(muscle: String): Int {
        return context.resources.getIdentifier(muscle, "drawable", context.packageName)
    }
}
