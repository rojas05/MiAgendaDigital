package cristian.rojas.elparaiso

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class tabClassAdapter(activity: FragmentActivity, private val tabConut: Int) : FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = tabConut

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0-> FragmentSupplies()
            1-> FragmentTrabajos()
            2-> FragmentOthers()
            3-> FragmentIncome()
            else -> FragmentSupplies()
        }
    }

}