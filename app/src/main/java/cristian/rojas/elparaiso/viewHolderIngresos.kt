package cristian.rojas.elparaiso

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import cristian.rojas.elparaiso.databinding.ItemIngresosBinding

class viewHolderIngresos( var view: View):RecyclerView.ViewHolder(view) {

    val binding = ItemIngresosBinding.bind(view)


    @SuppressLint("ResourceAsColor")
    fun render(item: entidadIngresos, onClickListener: (entidadIngresos) -> Unit, onLogClickListener: (entidadIngresos) -> Unit){
        Price().priceSplit(item.precio!!){
            binding.tvCostoIngresos.text = it
        }

        binding.tvDescripcionIngreso.text = item.descripcion
        binding.tvFechaIngresos.text = item.fecha
        binding.tvProductoIngresos.text = item.producto
        binding.cvIngresos.animation = AnimationUtils.loadAnimation(view.context,R.anim.recycler_transition)
        binding.cvIngresos.setOnClickListener { onClickListener (item) }
        binding.cvIngresos.setOnLongClickListener {
            onLogClickListener(item)
            return@setOnLongClickListener true
        }
    }
}