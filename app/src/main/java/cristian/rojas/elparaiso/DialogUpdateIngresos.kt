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
import cristian.rojas.elparaiso.databinding.AlertInsertarIngresosBinding

class DialogUpdateIngresos(
    var item: entidadIngresos,
    var update: (entidadIngresos) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertInsertarIngresosBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertInsertarIngresosBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.EtProductoIngreso.setText(item.producto)
        binding.EtFechaIngreso.setText(item.fecha)
        binding.EtPrecioIngreso.setText(item.precio.toString())
        binding.EtDescripcionIngreso.setText(item.descripcion)
        binding.tvTitulo.text = item.producto


        binding.EtFechaIngreso.setOnClickListener {
            showDatepickerDialogo()
        }

        binding.BtInsertarIngreso.setOnClickListener {
            if (TextUtils.isEmpty(binding.EtPrecioIngreso.text)){
                binding.EtPrecioIngreso.error = getString(R.string.obligatorio)
                binding.EtPrecioIngreso.requestFocus()
            }else if (TextUtils.isEmpty(binding.EtFechaIngreso.text)){
                binding.EtFechaIngreso.error = getString(R.string.obligatorio)
                binding.EtFechaIngreso.requestFocus()
            }else if (TextUtils.isEmpty(binding.EtDescripcionIngreso.text)){
                binding.EtDescripcionIngreso.error = getString(R.string.obligatorio)
                binding.EtDescripcionIngreso.requestFocus()
            }else if (TextUtils.isEmpty(binding.EtProductoIngreso.text)){
                binding.EtProductoIngreso.error = getString(R.string.obligatorio)
                binding.EtProductoIngreso.requestFocus()
            }else{
                val ingreso= entidadIngresos(item.id_ingreso,binding.EtProductoIngreso.text.toString(),
                    binding.EtFechaIngreso.text.toString(),
                    binding.EtPrecioIngreso.text.toString().toInt()
                    ,binding.EtDescripcionIngreso.text.toString(),1)
                update(ingreso)
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
        binding.EtFechaIngreso.setText("$dia/$mm/$an")
    }

}