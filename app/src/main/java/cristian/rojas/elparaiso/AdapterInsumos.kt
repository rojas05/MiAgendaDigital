package cristian.rojas.elparaiso

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterInsumos (var items:List<entidadEgresos>, private val onClickListener: (entidadEgresos) -> Unit, private val onLogClickListener: (entidadEgresos) -> Unit) : RecyclerView.Adapter<viewHolderEgresos>()

{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderEgresos {
        return viewHolderEgresos(LayoutInflater.from(parent.context).inflate(R.layout.item_egresos,parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderEgresos, position: Int) {
        val item = items[position]
        holder.renderEgresos(item,onClickListener,onLogClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setFilterList(mList: List<entidadEgresos>) {
        this.items = mList
        notifyDataSetChanged()
    }

}
