package eu.vincinity2020.p2p_parking.ui.profile.edituser.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class EditUserPagerAdapter(fragmentManager: FragmentManager,
                           private val fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}