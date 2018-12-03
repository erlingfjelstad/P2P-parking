package eu.vincinity2020.p2p_parking.utils.toolbar

import android.content.Context
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.layout_toolbar.view.*


class ToolbarManager constructor(
        private var builder: FragmentToolbar,
        private var container: View) {

    /***
     * Choose FragmentToolbar.NO_TOOLBAR as ID, if no toolbar required.
     * */

    fun prepareToolbar(context: Context, drawerLayout: DrawerLayout?) {
        if (builder.resId != FragmentToolbar.NO_TOOLBAR) {
            val fragmentToolbar = container.findViewById(builder.resId) as Toolbar

            if (builder.title != -1) {
                fragmentToolbar.tvTitle.setText(builder.title)
            }

            if (builder.menuId != -1) {
                fragmentToolbar.inflateMenu(builder.menuId)
            }

            if (builder.isHamburger) {
                fragmentToolbar.ivHamburger.visibility = View.VISIBLE
                fragmentToolbar.ivHamburger.setOnClickListener({
                    drawerLayout?.openDrawer(GravityCompat.START)
                })
            } else {
                fragmentToolbar.ivHamburger.visibility = View.GONE
            }



            if (builder.isBackButton) {
                assert(builder.isHamburger, { "Both Hamburger and Back cannot be set true" })
                fragmentToolbar.ivBackArrow.visibility = View.VISIBLE
                fragmentToolbar.ivBackArrow.setOnClickListener({
                    (context as AppCompatActivity).supportFragmentManager.popBackStackImmediate()
                })
            } else {
                fragmentToolbar.ivBackArrow.visibility = View.GONE
            }

            if (!builder.menuItems.isEmpty() && !builder.menuClicks.isEmpty()) {
                val menu = fragmentToolbar.menu
                for ((index, menuItemId) in builder.menuItems.withIndex()) {
                    (menu.findItem(menuItemId) as MenuItem).setOnMenuItemClickListener(builder.menuClicks[index])
                }
            }
        }
    }
}