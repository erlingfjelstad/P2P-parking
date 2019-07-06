package eu.vincinity2020.p2p_parking.ui.edituser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gigamole.navigationtabstrip.NavigationTabStrip
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.ui.edituser.adapter.EditUserPagerAdapter
import eu.vincinity2020.p2p_parking.ui.edituser.fragments.BodyFragment
import eu.vincinity2020.p2p_parking.ui.edituser.fragments.InfoFragment
import eu.vincinity2020.p2p_parking.ui.edituser.fragments.RoleFragment
import kotlinx.android.synthetic.main.activity_edit_user.*
import org.jetbrains.anko.toast

class EditUserActivity : AppCompatActivity(), EditUserInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        initViews()
    }

    private fun initViews() {
        vpEditUser.adapter = EditUserPagerAdapter(supportFragmentManager, arrayListOf(RoleFragment(this),
                BodyFragment(this), InfoFragment()))
        vpEditUser.offscreenPageLimit = 2
        navigationTabStrip.tabIndex = 0
        navigationTabStrip.onTabStripSelectedIndexListener = object : NavigationTabStrip.OnTabStripSelectedIndexListener {
            override fun onEndTabSelected(title: String?, index: Int) {
                navigationTabStrip.tabIndex = vpEditUser.currentItem
            }

            override fun onStartTabSelected(title: String?, index: Int) {
                navigationTabStrip.tabIndex = vpEditUser.currentItem

            }
        }

        initTiles()
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

    override fun onBackPressed() {
        when (vpEditUser.currentItem) {
            2 -> {
                vpEditUser.setCurrentItem(1, true)
                navigationTabStrip.tabIndex = 1
            }
            1 -> {
                vpEditUser.setCurrentItem(0, true)
                navigationTabStrip.tabIndex = 0
            }
            0 -> {
                super.onBackPressed()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}


interface EditUserInterface {
    fun onRoleNext()

    fun onBodyNext()

    fun onInfoComplete()
}