package eu.vincinity2020.p2p_parking.ui.profile.view

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import butterknife.OnClick

import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.BaseFragment

import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.ui.profile.edit.part1.EditProfile1Fragment
import eu.vincinity2020.p2p_parking.utils.AndroidUtils
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar

import kotlinx.android.synthetic.main.fragment_view_profile.*
import org.osmdroid.config.Configuration
import javax.inject.Inject

class ViewProfileFragment : BaseFragment(), ViewProfileMvpView {


    override fun updateUserProfileInfo(user: User) {
        etFirstName.setText(user.firstName)
        etLastName.setText(user.lastName)
        etEmail.setText(user.email)
        etCountry.setText(user.country.country)
        etMobile.setText(user.phone)


        when {
            user.role.trim().toLowerCase() == "medical" -> tvHealth.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.true_black))
            user.role.trim().toLowerCase() == "owner" -> tvOwner.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.true_black))
            else -> tvOwner.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.true_black))
        }

        when {
            user.gender.trim().toLowerCase() == "male" -> tvMale.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.true_black))
            user.gender.trim().toLowerCase() == "female" -> tvFemale.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.true_black))
            else -> tvOther.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.true_black))
        }
    }

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.profile)
            .build()


    @Inject
    lateinit var presenter: ViewProfilePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))


        (activity?.application as App)
                .appComponent()
                .viewProfileComponentBuilder()
                .viewProfileModule(ViewProfileModule())
                .build()
                .inject(this)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

//        init views here
        presenter.attach(this)

        if (arguments != null && arguments!!.containsKey(AppConstants.Arguments.ARGS_USER_EMAIL.name)) {
            presenter.getUserProfile(arguments!!.getLong(AppConstants.Arguments.ARGS_USER_EMAIL.name))
        } else
            presenter.getUserProfile(App.get(activity?.applicationContext!!).getUserEmail()!!)
    }


    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .viewProfileComponentBuilder()
                .viewProfileModule(ViewProfileModule())
                .build()
                .inject(this)
    }

    @OnClick(R.id.btnEditProfile)
    fun onEditProfileClicked() {
        (activity as NavigationActivity).selectedFragmentTag = NavigationActivity.EDIT_PROFILE_1
        AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, EditProfile1Fragment(),
                R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag, true)    }


    override fun onUnknownError(errorMessage: String) {

    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }


    override fun onLoadFinish() {
    }
}