package cristian.rojas.elparaiso

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import cristian.rojas.elparaiso.databinding.ItemEgresosBinding


class viewHolderEgresos( var view: View):RecyclerView.ViewHolder(view) {

    val binding = ItemEgresosBinding.bind(view)


    fun renderEgresos(item  : entidadEgresos,onClickListener: (entidadEgresos) -> Unit, onLogClickListener: (entidadEgresos) -> Unit){
        Price().priceSplit(item.costo!!){
            binding.tvCosto.text = it
        }
        binding.tvDescripcion.text = item.descripcion
        binding.tvFecha.text = item.fecha
        binding.cv.animation = AnimationUtils.loadAnimation(view.context,R.anim.recycler_transition)
        binding.cv.setOnClickListener { onClickListener (item) }
        binding.cv.setOnLongClickListener {
            onLogClickListener(item)
            return@setOnLongClickListener true
        }

    }
}