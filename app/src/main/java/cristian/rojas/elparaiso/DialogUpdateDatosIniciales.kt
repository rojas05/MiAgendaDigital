package cristian.rojas.elparaiso

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import cristian.rojas.elparaiso.databinding.AlertUpdateDatosFincaBinding

class DialogUpdateDatosIniciales(
    var datosfinca: List<entidadFinca>,
    var update: (entidadFinca) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertUpdateDatosFincaBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertUpdateDatosFincaBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        for (item in datosfinca) {
            binding.tvNombreFinca.text = getString(R.string.app_name)
            binding.EtPropietarioUpdate.setText(item.propietario)
            binding.EtFincaUpdate.setText(item.nombre)
        }

        binding.EtFincaUpdate.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                    actualizar()
                    return true
                }
                return false
            }
        })

        binding.BtUpdate.setOnClickListener {
            actualizar()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun actualizar (){
        if (TextUtils.isEmpty(binding.EtPropietarioUpdate.text)){
            binding.EtPropietarioUpdate.error = getString(R.string.obligatorio)
            binding.EtPropietarioUpdate.requestFocus()
        }else if (TextUtils.isEmpty(binding.EtFincaUpdate.text)){
            binding.EtFincaUpdate.error = getString(R.string.obligatorio)
            binding.EtFincaUpdate.requestFocus()
        }else{
            val datos = entidadFinca(1,binding.EtFincaUpdate.text.toString(),binding.EtPropietarioUpdate.text.toString())
            update(datos)
            dismiss()
        }
    }
}