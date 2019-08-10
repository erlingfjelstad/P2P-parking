package eu.vincinity2020.p2p_parking.ui.profile.edituser


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.gigamole.navigationtabstrip.NavigationTabStrip
import com.labo.kaji.fragmentanimations.MoveAnimation
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_FRAGMENT_ANIMATION_DURATION
import eu.vincinity2020.p2p_parking.ui.profile.edituser.adapter.EditUserPagerAdapter
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.BodyFragment
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.InfoFragment
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.RoleFragment
import eu.vincinity2020.p2p_parking.utils.disable
import kotlinx.android.synthetic.main.layout_edit_user.*
import org.jetbrains.anko.support.v4.toast

class EditUserFragment: Fragment(), EditUserInterface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_edit_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        vpEditUser.adapter = EditUserPagerAdapter(childFragmentManager, arrayListOf(RoleFragment(this),
                BodyFragment(this), InfoFragment()))
        vpEditUser.offscreenPageLimit = 2
        navigationTabStrip.tabIndex = 0
        navigationTabStrip.setOnTouchListener { _, _ ->
            true
        }

        initTiles()

        requireActivity().addOnBackPressedCallback(requireActivity(), OnBackPressedCallback {
            when (vpEditUser.currentItem) {
                2 -> {
                    vpEditUser.setCurrentItem(1, true)
                    navigationTabStrip.tabIndex = 1
                    true
                }
                1 -> {
                    vpEditUser.setCurrentItem(0, true)
                    navigationTabStrip.tabIndex = 0
                    true
                }
                0 -> {
                    false
                }
                else -> {
                    false
                }
            }
        })
    }

    private fun initTiles() {

    }

    override fun onRoleNext() {
        vpEditUser.setCurrentItem(1, true)
        navigationTabStrip.tabIndex = 1
    }

    override fun onBodyNext() {
        vpEditUser.setCurrentItem(2, true)
        navigationTabStrip.tabIndex = 2
    }

    override fun onInfoComplete() {
        toast("Info will be saved now")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            MoveAnimation.create(MoveAnimation.LEFT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        } else {
            MoveAnimation.create(MoveAnimation.LEFT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        }
    }

}

interface EditUserInterface {
    fun onRoleNext()
    fun onBodyNext()
    fun onInfoComplete()
}