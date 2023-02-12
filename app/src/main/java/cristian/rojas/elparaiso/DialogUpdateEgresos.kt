package cristian.rojas.elparaiso

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import cristian.rojas.elparaiso.databinding.AlertInsertarEgresosBinding

class DialogUpdateEgresos(
    var item: entidadEgresos,
    var update: (entidadEgresos) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertInsertarEgresosBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertInsertarEgresosBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)


           binding.EtFecha.setText(item.fecha)
           binding.EtPrecio.setText(item.costo.toString())
           binding.EtDescripcion.setText(item.descripcion)


        binding.EtFecha.setOnClickListener {
            showDatepickerDialogo()
        }

        binding.BtInsertar.setOnClickListener {
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
                val egreso= entidadEgresos(item.id_egreso,binding.EtFecha.text.toString(),binding.EtPrecio.text.toString().toInt(),binding.EtDescripcion.text.toString(),"Insumo",1)
                update(egreso)
                dismiss()
            }
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

}