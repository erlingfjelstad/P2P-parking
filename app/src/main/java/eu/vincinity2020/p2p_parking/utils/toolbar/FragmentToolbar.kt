package eu.vincinity2020.p2p_parking.utils.toolbar

import android.support.annotation.BoolRes
import android.support.annotation.IdRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.view.MenuItem

class FragmentToolbar
private constructor(@IdRes val resId: Int,
                    @StringRes val title: Int,
                    @MenuRes val menuId: Int,
                    @BoolRes val isHamburger: Boolean,
                    @BoolRes val isBackButton: Boolean,
                    @BoolRes val isNotification: Boolean,
                    val menuItems: MutableList<Int>,
                    val menuClicks: MutableList<MenuItem.OnMenuItemClickListener?>,
                    val backButtonListener: IBackButtonCallback?) {

    companion object {
        @JvmField
        val NO_TOOLBAR = -1
        val NO_NOTIFICATION = -1
    }


    class Builder {
        private var resId: Int = -1
        private var menuId: Int = -1
        private var title: Int = -1
        private var isHamburger: Boolean = false
        private var isBackButton: Boolean = false
        private var isNotification: Boolean = false
        private var isSort: Boolean = false
        private var menuItems: MutableList<Int> = mutableListOf()
        private var menuClicks: MutableList<MenuItem.OnMenuItemClickListener?> = mutableListOf()
        private var backButtonListener: IBackButtonCallback? = null

        /***
         * Pass FragmentToolbar.NO_TOOLBAR as ID if no toolbar required.
         * */

        fun withId(@IdRes resId: Int) = apply { this.resId = resId }

        fun withTitle(title: Int) = apply { this.title = title }

        fun withHamburger() = apply { this.isHamburger = true }

        fun withBackButtonCallback(backButtonListener: IBackButtonCallback) = apply {
            this.backButtonListener = backButtonListener
        }

        fun withBackButton() = apply { this.isBackButton = true }

        fun withNotification() = apply {
            this.isNotification = true
        }

        fun withMenu(@MenuRes menuId: Int) = apply { this.menuId = menuId }

        fun withMenuItems(menuItems: MutableList<Int>, menuClicks: MutableList<MenuItem.OnMenuItemClickListener?>) = apply {
            this.menuItems.addAll(menuItems)
            this.menuClicks.addAll(menuClicks)
        }

        fun build() = FragmentToolbar(resId, title, menuId, isHamburger,
                isBackButton, isNotification, menuItems, menuClicks, backButtonListener)
    }
}