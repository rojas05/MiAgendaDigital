package cristian.rojas.elparaiso

import DialogInsertIngresos
import android.annotation.SuppressLint
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


class FragmentIncome : Fragment() {
    private var _binding: FragmentInsumosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterIngresos
    private lateinit var ingresosL:List<entidadIngresos>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentInsumosBinding.inflate(inflater,container,false)

        CoroutineScope(Dispatchers.IO).launch{
            dates()
        }

        adapter = AdapterIngresos(
            mutableListOf(),
            onClickListener =
            {onItemSelected(it)},
            onLogClickListener =
            {onLogItem(it)}
        )
        binding.recycler.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filterList(p0)
                return true
            }

        })

        binding.sv.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, _, _ ->
                binding.recycler.isNestedScrollingEnabled = true
            })

        binding.btAgregarInsumo.setOnClickListener {
            DialogInsertIngresos(
                insert = {
                    insertar(it)
                }
            ).show(parentFragmentManager,"dialog")
        }

        return binding.root
    }


    private fun filterList(p0: String?) {
        if(p0 != null){
            val filterList = ArrayList<entidadIngresos>()
            if (::ingresosL.isInitialized){
                for (i in ingresosL){
                    if(i.producto.toString().lowercase(Locale.ROOT).contains(p0)){
                        filterList.add(i)
                    }else if ( i.descripcion.lowercase(Locale.ROOT).contains(p0)){
                        filterList.add(i)
                    }else if (i.fecha.toString().lowercase(Locale.ROOT).contains(p0)){
                        filterList.add(i)
                    }else if(i.precio.toString().lowercase(Locale.ROOT).contains(p0)){
                        filterList.add(i)
                    }
                    if (filterList.isNotEmpty()){
                        adapter.setFilterList(filterList)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun insertar(item: entidadIngresos){
        CoroutineScope(Dispatchers.IO).launch{
            DB.getInstancia(requireContext()).DaoIngresos().insertIngresos(item)
            dates()
        }
    }


    private suspend fun dates(){
        CoroutineScope(Dispatchers.IO).launch{
            ingresosL = DB.getInstancia(requireContext()).DaoIngresos().getIngresos()
            launch(Dispatchers.Main) {
                adapter = AdapterIngresos(
                    ingresosL,
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

    @SuppressLint("MissingInflatedId")
    fun onItemSelected(item:entidadIngresos){
        DialogUpdateIngresos(
           item,
           update = {
               update(it)
           }
        ).show(parentFragmentManager,"dialog")

    }

    private fun onLogItem(item: entidadIngresos) {

        vibratePhone()
        DialogDeleteIngresos(
            item,
            delete =
            {delete(it)}
        ).show(parentFragmentManager,"dialog")
    }

    private fun delete(id: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            DB.getInstancia(requireContext()).DaoIngresos()
                .deleteIngreso(id)
            dates()
        }
    }


    private fun update(item: entidadIngresos) {
        CoroutineScope(Dispatchers.IO).launch{
            if (item.precio != null && item.fecha != null && item.producto != null) {
                DB.getInstancia(requireContext()).DaoIngresos().updateIngresos(
                    item.descripcion,item.fecha,item.precio,item.producto,item.id_ingreso!!
                )
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