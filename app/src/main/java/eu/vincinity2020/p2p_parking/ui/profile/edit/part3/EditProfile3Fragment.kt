package eu.vincinity2020.p2p_parking.ui.profile.edit.part3

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick

import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment

import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity.Companion.EDIT_PROFILE_1
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity.Companion.EDIT_PROFILE_2
import eu.vincinity2020.p2p_parking.ui.profile.edit.part1.EditProfile1Fragment
import eu.vincinity2020.p2p_parking.ui.profile.edit.part2.EditProfile2Fragment
import eu.vincinity2020.p2p_parking.utils.AndroidUtils
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar

import org.osmdroid.config.Configuration
import javax.inject.Inject

class EditProfile3Fragment : BaseFragment(), EditProfile3MvpView{


    override fun updateUserProfileInfo(user: User) {
//        etFirstName.setText(user.firstName)
//        etLastName.setText( user.lastName)
//        etEmail.setText(user.email)
////        tvCountry.setText( user.country.country)
    }

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.edit_profile)
            .build()


    @Inject
    lateinit var presenter: EditProfile3Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))


        (activity?.application as App)
                .appComponent()
                .editProfile3ComponentBuilder()
                .viewProfileModule(EditProfile3Module())
                .build()
                .inject(this)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profile_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

//        init views here
        presenter.attach(this)

//        if (arguments != null && arguments!!.containsKey(AppConstants.Arguments.ARGS_USER_EMAIL.name)) {
//            presenter.getUserProfile(arguments!!.getLong(AppConstants.Arguments.ARGS_USER_EMAIL.name))
//        }
//        else
//            presenter.getUserProfile(App.get(activity?.applicationContext!!).getUserEmail()!!)
    }


    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .editProfile3ComponentBuilder()
                .viewProfileModule(EditProfile3Module())
                .build()
                .inject(this)
    }


    @OnClick(R.id.tvProfile1)
    fun onProfile1Clicked() {
        (activity as NavigationActivity ).selectedFragmentTag = EDIT_PROFILE_1
        AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, EditProfile1Fragment(),
                R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag, false)
    }

    @OnClick(R.id.tvProfile2)
    fun onProfile2Clicked() {
        (activity as NavigationActivity).selectedFragmentTag = EDIT_PROFILE_2
        AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, EditProfile2Fragment(),
                R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag, false)

    }

//    @OnClick(R.id.tvProfile3)
//    fun onProfile3Clicked() {
//
//        (activity as NavigationActivity).selectedFragmentTag = EditProfileActivity.EDIT_PROFILE_3
//        AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, EditProfile3Fragment(),
//                R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag)
//
//    }


    override fun onUnknownError(errorMessage: String) {

    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }


    override fun onLoadFinish() {
    }
}