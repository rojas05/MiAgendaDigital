package cristian.rojas.elparaiso

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterIngresos(private var items:List<entidadIngresos>, private val onClickListener: (entidadIngresos) -> Unit,private val onLogClickListener: (entidadIngresos) -> Unit) : RecyclerView.Adapter<viewHolderIngresos>()

{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderIngresos {
        return viewHolderIngresos(LayoutInflater.from(parent.context).inflate(R.layout.item_ingresos,parent,false,))
    }

    override fun onBindViewHolder(holder: viewHolderIngresos, position: Int) {
        val item = items[position]
        holder.render(item,onClickListener,onLogClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setFilterList(mList: List<entidadIngresos>) {
        this.items = mList
        notifyDataSetChanged()
    }

}