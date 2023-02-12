package cristian.rojas.elparaiso

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import cristian.rojas.elparaiso.databinding.ActivityDatosFincaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Activity_datos_finca : AppCompatActivity() {

    lateinit var binding: ActivityDatosFincaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDatosFincaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.BtGuardar.setOnClickListener {
            if (TextUtils.isEmpty(binding.EtPropietario.text)){
                binding.EtPropietario.error = getString(R.string.obligatorio)
                binding.EtPropietario.requestFocus()
            }else if (TextUtils.isEmpty(binding.EtFinca.text)){
                binding.EtFinca.error = getString(R.string.obligatorio)
                binding.EtFinca.requestFocus()
            }else if (TextUtils.isEmpty(binding.EtClave.text)){
                binding.EtClave.error = getString(R.string.obligatorio)
                binding.EtClave.requestFocus()
            }else{
                val datos = entidadFinca(null,binding.EtFinca.text.toString(),binding.EtPropietario.text.toString())
                insertar(datos)
                if (binding.EtClave.text.toString().toInt() == 1084251327){
                    preferences(binding.EtClave.text.toString().toInt(),binding.EtFinca.text.toString())
                }else{
                    binding.EtClave.error = getString(R.string.errorClave)
                }
            }
        }

    }

    private fun insertar(it: entidadFinca) {
        CoroutineScope(Dispatchers.IO).launch{
            DB.getInstancia(this@Activity_datos_finca).DaoFinca().insertPerfil(it)
        }
    }

    private fun preferences (clave : Int, finca : String){
        val preferencias = getSharedPreferences( "registro", Context.MODE_PRIVATE)
        val editor = preferencias.edit()
        editor.putString("clave",clave.toString())
        editor.putString("finca",finca)
        editor.apply()
        startActivity(Intent(this,ActivityPrincipal::class.java))
    }
}