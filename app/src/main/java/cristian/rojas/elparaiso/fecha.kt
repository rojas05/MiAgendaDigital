package cristian.rojas.elparaiso

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class fecha(val lisener: (dia:Int, mes: Int, an:Int)-> Unit): DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, dia: Int, mes: Int, an: Int) {
        lisener(dia, mes, an)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c:Calendar = Calendar.getInstance()
        val d = c.get(Calendar.DAY_OF_MONTH)
        val m = c.get(Calendar.MONTH)
        val a = c.get(Calendar.YEAR)

        val picker = DatePickerDialog(activity as Context, this, a,m,d)
        picker.datePicker.maxDate = c.timeInMillis
        return picker
    }
}