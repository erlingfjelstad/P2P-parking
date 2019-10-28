package eu.vincinity2020.p2p_parking.ui.profile.edituser


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.ui.profile.edituser.adapter.EditUserPagerAdapter
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.body.BodyFragment
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.info.InfoFragment
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.role.RoleFragment
import kotlinx.android.synthetic.main.layout_edit_user.*

class EditUserFragment : Fragment(), EditUserInterface {


    companion object {
        operator fun invoke(listener: EditFinishListener): EditUserFragment {
            val fragment = EditUserFragment()
            fragment.setFinishListener(listener)
            return fragment
        }
    }

    private lateinit var finishLister: EditFinishListener

    private fun setFinishListener(listener: EditFinishListener) {
        finishLister = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_edit_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        vpEditUser.adapter = EditUserPagerAdapter(childFragmentManager, arrayListOf(RoleFragment(this),
                BodyFragment(this), InfoFragment(this)))
        vpEditUser.offscreenPageLimit = 2
        navigationTabStrip.tabIndex = 0
        navigationTabStrip.setOnTouchListener { _, _ ->
            true
        }

        initTiles()

        requireActivity().addOnBackPressedCallback(requireActivity(), OnBackPressedCallback {
            return@OnBackPressedCallback when (vpEditUser?.currentItem) {
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
        finishLister.onEditComplete()
    }

}

interface EditUserInterface {
    fun onRoleNext()
    fun onBodyNext()
    fun onInfoComplete()
}

interface EditFinishListener {
    fun onEditComplete()
}