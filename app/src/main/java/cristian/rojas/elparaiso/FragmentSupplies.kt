package cristian.rojas.elparaiso

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import cristian.rojas.elparaiso.databinding.FragmentInsumosBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class FragmentSupplies : Fragment() {
    private lateinit var adapter: AdapterInsumos
    private lateinit var insumosL:List<entidadEgresos>
    private var _binding: FragmentInsumosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsumosBinding.inflate(inflater,container,false)

        CoroutineScope(Dispatchers.IO).launch {
            dates()
        }

        adapter = AdapterInsumos(
            mutableListOf(),
            onClickListener =
            {onItemSelected(it)},
            onLogClickListener =
            {onLogItem(it)}
            )
        binding.recycler.adapter = adapter

        binding.sv.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, _, _ ->
                binding.recycler.isNestedScrollingEnabled = true
            })

        binding.btAgregarInsumo.setOnClickListener {
                DialogInsertEgresos(
                    insert = {
                        insertar(it)
                    }
                ).show(parentFragmentManager,"dialog")
            }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filterList(p0)
                return true
            }

        })
        return binding.root
    }

    private fun filterList(p0: String?) {
        if (p0 != null) {
            val filterList = ArrayList<entidadEgresos>()
            if (::insumosL.isInitialized){
                for (i in insumosL) {
                    if (i.costo.toString().lowercase(Locale.ROOT).contains(p0)) {
                        filterList.add(i)
                    } else if (i.descripcion.lowercase(Locale.ROOT).contains(p0)) {
                        filterList.add(i)
                    } else if (i.fecha.toString().lowercase(Locale.ROOT).contains(p0)) {
                        filterList.add(i)
                    }
                    if (filterList.isNotEmpty()) {
                        adapter.setFilterList(filterList)
                    }
                }
            }
        }
    }

    private fun insertar(item: entidadEgresos) {
        val egreso= entidadEgresos(null,item.fecha,item.costo,item.descripcion,"Insumo",item.finca)
        CoroutineScope(Dispatchers.IO).launch{
            DB.getInstancia(requireContext()).DaoEgresos().insertEgresos(egreso)
            dates()
        }
    }


    private suspend fun dates() {
        CoroutineScope(Dispatchers.IO).launch{
            insumosL = DB.getInstancia(requireContext()).DaoEgresos().getInsumos()
            launch(Dispatchers.Main) {
                adapter = AdapterInsumos(
                    insumosL,
                    onClickListener =
                    {onItemSelected(it)},
                    onLogClickListener =
                    {onLogItem(it)}
                )
                binding.recycler.adapter = adapter
                if (context != null){
                    binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    private fun onItemSelected(item:entidadEgresos){
        DialogUpdateEgresos(item,
            update = {
                update(it)
            }
        ).show(parentFragmentManager,"dialog")
    }

    private fun onLogItem(item: entidadEgresos) {

        vibratePhone()
        DialogDelete(
            item,
            delete =
            {deleteEgreso(it)}
        ).show(parentFragmentManager,"dialog")
    }

    private fun deleteEgreso(id: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            DB.getInstancia(requireContext()).DaoEgresos()
                .deleteEgreso(id)
            dates()
        }
    }

    private fun update(item: entidadEgresos) {
        CoroutineScope(Dispatchers.IO).launch{
            if (item.fecha != null && item.costo != null) {
                DB.getInstancia(requireContext()).DaoEgresos()
                    .updateEgreso(item.descripcion,item.fecha,item.costo,item.id_egreso!!)
            }
           dates()
        }
    }

    private fun Fragment.vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}