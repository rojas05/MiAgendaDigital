package cristian.rojas.elparaiso

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import cristian.rojas.elparaiso.databinding.AlertCierreBinding
import java.text.SimpleDateFormat
import java.util.*

class DialogCierre(val Pdfyear: (String) -> Unit): DialogFragment() {

    private lateinit var binding: AlertCierreBinding

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCierreBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        vibratePhone()

        val yearFormat = SimpleDateFormat("yyyy")
        val year = yearFormat.format(Date())

        val dayFormat = SimpleDateFormat("dd")
        val day = dayFormat.format(Date())

        val monthFormat = SimpleDateFormat("MM")
        val month = monthFormat.format(Date())

        if (month.toString().toInt() == 12){
            if (day.toString().toInt() >=20){
                binding.tvDescripcionCierre.text = getString(R.string.si)
            }else{
                binding.tvDescripcionCierre.text = getString(R.string.recomienda)
            }
        }else{
            binding.tvDescripcionCierre.text = getString(R.string.pronto)
        }

        binding.LavCierre.setAnimation(R.raw.data)
        binding.LavCierre.playAnimation()

        binding.btGenerar.setOnClickListener {
            Pdfyear(year)
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    private fun Fragment.vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(400)
        }
    }

}