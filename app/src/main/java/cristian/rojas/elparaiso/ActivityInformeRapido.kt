package cristian.rojas.elparaiso

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import cristian.rojas.elparaiso.databinding.ActivityInformeRapidoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ActivityInformeRapido : AppCompatActivity() {
    lateinit var binding: ActivityInformeRapidoBinding

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInformeRapidoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.informeRapido)

        val dateFormat = SimpleDateFormat("yyyy")
        val date = dateFormat.format(Date())

        val year = "$date%"

        insumos(year)
        trabajos(year)
        otros(year)
        totalEgresos(year)
        totalIngresos(year)
        ingresoLibre(year)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun insumos(year: String){
        CoroutineScope(Dispatchers.IO).launch{
            val total = DB.getInstancia(this@ActivityInformeRapido).DaoEgresos().getTotalInsumo(year)
            launch {
                if (total[0] != null){
                    Price().priceSplit(total[0].toString().toInt()){
                        binding.tvInsumosTotal.text = it
                    }
                }else{
                    binding.tvInsumosTotal.setText(getString(R.string.datosInsuficientes))
                    binding.tvInsumosTotal.setTextColor(getColor(R.color.purple_200))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun trabajos(year: String){
        CoroutineScope(Dispatchers.IO).launch{
            val total = DB.getInstancia(this@ActivityInformeRapido).DaoEgresos().getTotalTrabajo(year)
            launch{
                 if (total[0] != null){
                     Price().priceSplit(total[0].toString().toInt()){
                        binding.tvTrabajoTotal.text = it
                    }
                 }else{
                    binding.tvTrabajoTotal.setText(getString(R.string.datosInsuficientes))
                    binding.tvTrabajoTotal.setTextColor(getColor(R.color.purple_200))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun otros(year: String){
        CoroutineScope(Dispatchers.IO).launch{
            val total= DB.getInstancia(this@ActivityInformeRapido).DaoEgresos().getTotalOtro(year)
            launch {
                if (total[0]!= null){
                    Price().priceSplit(total[0].toString().toInt()){
                        binding.tvOtrostotal.text = it
                    }
                }else{
                    binding.tvOtrostotal.setText(getString(R.string.datosInsuficientes))
                    binding.tvOtrostotal.setTextColor(getColor(R.color.purple_200))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    fun totalEgresos(year: String){
        CoroutineScope(Dispatchers.IO).launch{
            val total= DB.getInstancia(this@ActivityInformeRapido).DaoEgresos().getTotalEgresos(year)
            launch {
                if (total[0] != null){
                    Price().priceSplit(total[0].toString().toInt()){
                        binding.tvtotalEgresos.text = getString(R.string.Total)+it
                    }
                }else{
                    binding.tvtotalEgresos.text = getString(R.string.datosInsuficientes)
                    binding.tvtotalEgresos.setTextColor(getColor(R.color.purple_200))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun totalIngresos(year: String){
        CoroutineScope(Dispatchers.IO).launch{
            val total = DB.getInstancia(this@ActivityInformeRapido).DaoIngresos().getTotalIngresos(year)
            launch {
                if (total[0] != null){
                    Price().priceSplit(total[0].toString().toInt()){
                        binding.tvtotalIngresos.text = getString(R.string.Total)+it
                    }
                }else{
                    binding.tvtotalIngresos.text = getString(R.string.datosInsuficientes)
                    binding.tvtotalIngresos.setTextColor(getColor(R.color.purple_200))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
  fun ingresoLibre(year: String){
        CoroutineScope(Dispatchers.IO).launch {
            val egresos = DB.getInstancia(this@ActivityInformeRapido).DaoEgresos().getTotalEgresos(year)
            val ingresos = DB.getInstancia(this@ActivityInformeRapido).DaoIngresos().getTotalIngresos(year)
            launch {
                if (egresos[0] == null){
                    binding.tvtotal.text = getString(R.string.datosInsuficientes)
                    binding.tvtotal.setTextColor(getColor(R.color.purple_200))
                }else if (ingresos[0] == null){
                    binding.tvtotal.text = getString(R.string.datosInsuficientes)
                    binding.tvtotal.setTextColor(getColor(R.color.purple_200))
                }else{
                    val total = ingresos[0]-egresos[0]
                    Price().priceSplit(total.toString().toInt()){
                        binding.tvtotal.text = getString(R.string.Total)+it
                    }
                    if (total <= 0){
                        binding.tvtotal.setTextColor(getColor(R.color.red))
                    }
                }
            }
        }
    }
}