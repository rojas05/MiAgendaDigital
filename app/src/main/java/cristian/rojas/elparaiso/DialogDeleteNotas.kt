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
import cristian.rojas.elparaiso.databinding.AlertDeleteNotasBinding
import cristian.rojas.elparaiso.databinding.AlertUpdateDatosFincaBinding
import java.text.SimpleDateFormat
import java.util.*

class DialogDeleteNotas(
    var delete: (Int) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertDeleteNotasBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertDeleteNotasBinding.inflate(LayoutInflater.from(context))

        val dateFormat = SimpleDateFormat("yyyy")
        val date = dateFormat.format(Date())

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.LavDeleteNotas.setAnimation(R.raw.delete)
        binding.LavDeleteNotas.playAnimation()

        binding.BtDeleteNotas.setOnClickListener {
            delete(date.toInt())
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}