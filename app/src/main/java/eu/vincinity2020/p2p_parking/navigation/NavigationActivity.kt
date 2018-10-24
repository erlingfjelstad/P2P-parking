package eu.vincinity2020.p2p_parking.navigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.common.BaseActivity
import eu.vincinity2020.p2p_parking.favorites.FavoritesFragment
import eu.vincinity2020.p2p_parking.map.MapFragment
import eu.vincinity2020.p2p_parking.profile.ProfileFragment
import eu.vincinity2020.p2p_parking.trip.TripFragment
import eu.vincinity2020.p2p_parking.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    //    default behaviour
    private var selectedFragmentTag: String = MAP_FRAGMENT

    companion object {
        const val ARG_SELECTED_FRAGMENT = "state:SelectedFragment"


        // individual fragment tags
        const val MAP_FRAGMENT = "MapFragment"
        const val TRIP_FRAGMENT = "TripFragment"
        const val FAVORITE_FRAGMENT = "FavoriteFragment"
        const val PROFILE_FRAGMENT = "ProfileFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_navigation)

        // handling fragment recovery

        val fragment: Fragment

        if (savedInstanceState == null) {
            fragment = MapFragment()
            AndroidUtils.attachFragment(supportFragmentManager, fragment,
                    R.id.fragment_container, MAP_FRAGMENT)
        } else {
            fragment = when (selectedFragmentTag) {
                MAP_FRAGMENT -> {
                    MapFragment()
                }
                TRIP_FRAGMENT -> {
                    TripFragment()
                }
                FAVORITE_FRAGMENT -> {
                    FavoritesFragment()
                }
                PROFILE_FRAGMENT -> {
                    ProfileFragment()
                }
                else -> {
                    MapFragment()
                }
            }

            val retainedFragment = supportFragmentManager.findFragmentByTag(selectedFragmentTag)

            if (retainedFragment != null) {
                AndroidUtils.attachFragment(supportFragmentManager, retainedFragment,
                        R.id.fragment_container, selectedFragmentTag)
            } else {
                AndroidUtils.attachFragment(supportFragmentManager, fragment,
                        R.id.fragment_container, selectedFragmentTag)
            }

        }

        bottom_navigation_view_map.setOnNavigationItemSelectedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(ARG_SELECTED_FRAGMENT, selectedFragmentTag)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            selectedFragmentTag = it.getString(ARG_SELECTED_FRAGMENT, MAP_FRAGMENT)
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val fragment: Fragment? = when (menuItem.itemId) {
            R.id.bottom_navigation_map -> {
                selectedFragmentTag = MAP_FRAGMENT
                MapFragment()
            }
            R.id.bottom_navigation_trips -> {
                selectedFragmentTag = TRIP_FRAGMENT
                TripFragment()
            }
            R.id.bottom_navigation_favorites -> {
                selectedFragmentTag = FAVORITE_FRAGMENT
                FavoritesFragment()
            }
            R.id.bottom_navigation_profile -> {
                selectedFragmentTag = PROFILE_FRAGMENT
                ProfileFragment()
            }
            else -> {
                null
            }
        }

        fragment?.let {
            AndroidUtils.attachFragment(supportFragmentManager, it,
                    R.id.fragment_container, selectedFragmentTag)
        }
        return true
    }
}