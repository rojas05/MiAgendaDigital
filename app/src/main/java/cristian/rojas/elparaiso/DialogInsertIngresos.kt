import android.annotation.SuppressLint
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
import cristian.rojas.elparaiso.R
import cristian.rojas.elparaiso.databinding.AlertInsertarIngresosBinding
import cristian.rojas.elparaiso.entidadIngresos
import cristian.rojas.elparaiso.fecha

class DialogInsertIngresos(
    var insert: (entidadIngresos) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertInsertarIngresosBinding

    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertInsertarIngresosBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.EtFechaIngreso.setOnClickListener {
            showDatepickerDialogo()
        }

        binding.BtInsertarIngreso.setOnClickListener {
            insertar()
        }

        binding.EtDescripcionIngreso.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                    insertar()
                    return true
                }
                return false
            }
        })

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

    fun insertar() {
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
            val ingreso= entidadIngresos(null,binding.EtProductoIngreso.text.toString(),
                binding.EtFechaIngreso.text.toString(),
                binding.EtPrecioIngreso.text.toString().toInt(),
                binding.EtDescripcionIngreso.text.toString(),1)
            insert(ingreso)
            dismiss()
        }
    }

}