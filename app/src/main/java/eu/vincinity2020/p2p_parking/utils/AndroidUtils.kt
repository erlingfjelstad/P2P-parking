package eu.vincinity2020.p2p_parking.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class AndroidUtils {

    companion object {
        fun attachFragment(fragmentManager: FragmentManager,
                           fragment: Fragment,
                           viewId: Int,
                           tag: String,
                           addToStack: Boolean) {
           var transaction= fragmentManager.beginTransaction().replace(viewId, fragment, tag);
            if (addToStack)
                transaction.addToBackStack(tag)
            transaction.commit()
        }
    }


}