package cristian.rojas.elparaiso

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import cristian.rojas.elparaiso.databinding.AlertEliminarBinding

class DialogDeleteIngresos(
    var item: entidadIngresos,
    var delete: (Int) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertEliminarBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertEliminarBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.tvDescripcionDelete.text = getString(R.string.delete) + "\n" + "\n" + item.descripcion

        binding.btcancelar.setOnClickListener { dismiss() }

        binding.btEliminar.setOnClickListener {
            delete(item.id_ingreso!!.toInt())
            dismiss()
        }

        binding.LavDelete.setAnimation(R.raw.delete)
        binding.LavDelete.playAnimation()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}