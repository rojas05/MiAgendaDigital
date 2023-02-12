package cristian.rojas.elparaiso

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import cristian.rojas.elparaiso.databinding.AlertInsertarEgresosBinding


class DialogInsertEgresos(
    var insert: (entidadEgresos) -> Unit
):DialogFragment() {

    private lateinit var binding: AlertInsertarEgresosBinding

    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertInsertarEgresosBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.EtFecha.setOnClickListener {
            showDatepickerDialogo()
        }

        binding.EtDescripcion.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                    insertar()
                    return true
                }
                return false
            }
        })

        binding.BtInsertar.setOnClickListener {
            insertar()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun showDatepickerDialogo() {
        val datePicker = fecha{dia, mes, an -> onDateSelecter(dia, mes, an) }
        datePicker.show(requireActivity().supportFragmentManager  ,"datePiker")
    }

    @SuppressLint("SetTextI18n")
    fun onDateSelecter(dia:Int, mes:Int, an:Int){
        val mm = mes + 1
        binding.EtFecha.setText("$dia/$mm/$an")
    }

    fun insertar(){
        if (TextUtils.isEmpty(binding.EtPrecio.text)){
            binding.EtPrecio.error = getString(R.string.obligatorio)
            binding.EtPrecio.requestFocus()
        }else if (TextUtils.isEmpty(binding.EtFecha.text)){
            binding.EtFecha.error = getString(R.string.obligatorio)
            binding.EtFecha.requestFocus()
        }else if (TextUtils.isEmpty(binding.EtDescripcion.text)){
            binding.EtDescripcion.error = getString(R.string.obligatorio)
            binding.EtDescripcion.requestFocus()
        }else{
            val egreso= entidadEgresos(null,binding.EtFecha.text.toString(),binding.EtPrecio.text.toString().toInt(),binding.EtDescripcion.text.toString(),"Insumo",1)
            insert(egreso)
            dismiss()
        }
    }

}


