package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.data.entities.RegisterRequest
import eu.vincinity2020.p2p_parking.ui.about.AboutActivity
import eu.vincinity2020.p2p_parking.ui.auth.registeruser.adapter.NavigationAdapter
import eu.vincinity2020.p2p_parking.ui.getstarted.GetStartedActivity
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.ui.privacy.PrivacyActivity
import eu.vincinity2020.p2p_parking.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_register_user.*
import kotlinx.android.synthetic.main.layout_register.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("WrongConstant")
class RegisterUserActivity: BaseActivity(), RegisterView {

    @Inject
    lateinit var presenter: RegistrationPresenter

    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        App.get(this)
                .appComponent()
                .registerUserComponentBuilder()
                .registerUserModule(RegisterUserModule())
                .build()
                .inject(this)

        presenter.attach(this)

        initViews()
    }

    private fun initViews() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        setupPasswordWatcher()
        btnRegister_register.setOnClickListener {
            if (isInputValid()) {
                registerUser()
            }
        }

        imvNavHamburger.setOnClickListener {
            drawerLayout.openDrawer(Gravity.START, true)
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        val adapter = NavigationAdapter(AppConstants.navigationItems) {
            navigateTo(it)
            drawerLayout.closeDrawer(Gravity.START, true)
        }
        revNavigation.layoutManager = LinearLayoutManager(this)
        revNavigation.adapter = adapter
    }

    private fun navigateTo(position: Int) {
        when (position) {
            0 -> {
                drawerLayout.closeDrawer(Gravity.START, true)
            }
            1 -> {
                startActivity<PrivacyActivity>()
            }
            2 -> {
                startActivity<GetStartedActivity>()
            }
            3 -> {
                startActivity<AboutActivity>()
            }

        }
    }

    private fun isInputValid(): Boolean {
        if (edtFullName_register.value.isBlank()) {
            tilFullName_register.error = "Please enter your full name"
            return false
        } else {
            tilFullName_register.error = null
        }

        if (edtPassword_register.value.length < 5) {
            tilPassword_register.error = "Password length should be at least 5 characters"
            return false
        } else {
            tilPassword_register.error = null
        }

        if (edtPassword_register.value != edtRepeatPassword_register.value) {
            tilPassword_register.error = "Passwords do not match"
            return false
        } else {
            tilPassword_register.error = null
        }

        if (edtEmail_register.value.isBlank() || !edtEmail_register.value.isEmail()) {
            tilEmail_register.error = "Please enter a valid email address"
            return false
        } else {
            tilEmail_register.error = null
        }

        if (edtMobile_register.value.isBlank() || !edtMobile_register.value.isValidMobile()) {
            tilMobile_register.error = "Please enter a valid mobile number"
            return false
        } else {
            tilMobile_register.error = null
        }

        return true
    }

    private fun registerUser() {
        val registerRequest = RegisterRequest(
                edtFullName_register.value,
                edtFullName_register.value,
                edtMobile_register.value,
                edtEmail_register.value,
                edtPassword_register.value,
                P2PPreferences(this).getString(AppConstants.FCM_TOKEN) ?: ""
        )
        presenter.registerUser(registerRequest)
    }

    private fun setupPasswordWatcher() {
        disposables.add(edtRepeatPassword_register.textChanges()
                .map { it.toString() }
                .debounce(750, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repeatedPassword ->
                    if (repeatedPassword != edtPassword_register.value) {
                        tilRepeatPassword_register.error = "Passwords do not match"
                    } else {
                        tilRepeatPassword_register.error = null
                    }
                }
        )

        disposables.add(edtPassword_register.textChanges()
                .map { it.toString() }
                .debounce(750, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { password ->
                    if (password != edtRepeatPassword_register.value) {
                        tilRepeatPassword_register.error = "Passwords do not match"
                    } else {
                        tilRepeatPassword_register.error = null
                    }
                }
        )
    }

    override fun onRegisterSuccessful() {
        startActivity<NavigationActivity>()
        finishAffinity()
    }

    override fun onRegisterError(e: Throwable) {
        P2PDialog.errorDialog(this, e.getErrorMessage()).show()
    }

    override fun hideProgress() {
        pbRegister.gone()
    }

    override fun showProgress() {
        pbRegister.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}