package eu.vincinity2020.p2p_parking.ui.auth.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.ui.auth.registeruser.RegisterUserActivity
import eu.vincinity2020.p2p_parking.ui.dashboard.DashboardActivity
import eu.vincinity2020.p2p_parking.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), LoginView {

    @Inject
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        App.get(this)
                .appComponent()
                .loginComponentBuilder()
                .loginModule(LoginModule())
                .build()
                .inject(this)
        presenter.attach(this)

        initViews()
    }

    private fun initViews() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        btnLogin_login.setOnClickListener {
            if (isInputValid()) {
                attemptLogin()
            }
        }

        frlRegisterNewUser.setOnClickListener {
            launchRegisterUserActivity()
        }
        edtUsername_login.setText(App.get(this).getUser()?.email ?: "")
    }

    private fun isInputValid(): Boolean {
        if (edtUsername_login.value.isBlank() || !edtUsername_login.value.isEmail()) {
            tilUsername_login.error = "Please enter a valid username"
            return false
        } else {
            tilUsername_login.error = null
        }

        if (edtPassword_login.value.length <= 4) {
            tilPassword_login.error = "Password cannot be less than 5 characters"
            return false
        } else {
            tilPassword_login.error = null
        }

        return true
    }

    private fun attemptLogin() {
        presenter.attemptLogin(edtUsername_login.value, edtPassword_login.value)
    }

    private fun launchRegisterUserActivity() {
        val logoPair = Pair<View, String>(imvP2pLogo, getString(R.string.logo_transition))
        val buttonPair = Pair<View, String>(btnLogin_login, getString(R.string.login_button_transition))
        val options = ActivityOptions.makeSceneTransitionAnimation(this, logoPair, buttonPair)
        val intent = Intent(this, RegisterUserActivity::class.java)
        startActivity(intent, options.toBundle())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun onLoadFinish() {
        hideProgress()
    }

    override fun onTimeout() {
        P2PDialog.errorDialog(this, getString(R.string.unable_to_connect_to_server)).show()
    }

    override fun onNetworkError() {
        P2PDialog.errorDialog(this, getString(R.string.unable_to_connect_to_server)).show()
    }

    override fun onLoginError(e: Throwable) {
        P2PDialog.errorDialog(this, e.getErrorMessage()).show()
    }

    override fun onFcmTokenSaved() {
        hideProgress()
        startActivity<DashboardActivity>()
        finish()
    }

    override fun showProgress() {
        pbLogin.show()
    }

    override fun hideProgress() {
        pbLogin.gone()
    }

    override fun onUnknownError(errorMessage: String) {

    }

}
