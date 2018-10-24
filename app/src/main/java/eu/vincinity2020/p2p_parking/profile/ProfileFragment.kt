package eu.vincinity2020.p2p_parking.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.car.MyCarsActivity
import eu.vincinity2020.p2p_parking.common.BaseFragment
import eu.vincinity2020.p2p_parking.registeruser.RegisterUserActivity

class ProfileFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
    }

    @OnClick(R.id.profile_row_profile)
    fun onProfileClicked() {
        val intent = RegisterUserActivity.loggedInLaunchIntent(requireContext(), true)
        startActivity(intent)
    }

    @OnClick(R.id.profile_row_my_cars)
    fun onMyCarsClicked() {
        startActivity(Intent(requireContext(), MyCarsActivity::class.java))
    }
}