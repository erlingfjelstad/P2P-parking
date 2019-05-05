package eu.vincinity2020.p2p_parking.app.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import eu.vincinity2020.p2p_parking.utils.toolbar.ToolbarManager
import kotlinx.android.synthetic.main.activity_navigation.*

abstract class BaseFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
        AndroidSupportInjection.inject(this)*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        val  mActivity =  activity as NavigationActivity

        ToolbarManager(builder(), view).prepareToolbar(mActivity,mActivity.drawerLayout)
    }
    protected abstract fun builder(): FragmentToolbar

}