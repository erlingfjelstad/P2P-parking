package eu.vincinity2020.p2p_parking.ui.auth.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.crashlytics.android.Crashlytics
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.ui.auth.registeruser.RegisterUserActivity
import kotlinx.android.synthetic.main.activity_login.*
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

        ButterKnife.bind(this)

        App.get(this)
                .appComponent()
                .loginComponentBuilder()
                .loginModule(LoginModule())
                .build()
                .inject(this)

        // Set up the login form.
        etPassword.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener {
            attemptLogin()
        }

        if (intent != null && intent.hasExtra("email"))
            etEmail.setText(intent.getStringExtra("email"))




    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {

        // Reset errors.

        etEmail.error = null
        etPassword.error = null

        // Store values at the time of the login attempt.
        val emailStr = etEmail.text.toString()
        val passwordStr = etPassword.text.toString()

        var cancel = false
        var focusView: View? = null


        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            etEmail.error = getString(R.string.error_field_required)
            focusView = etEmail
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            etEmail.error = getString(R.string.error_invalid_email)
            focusView = etEmail
            cancel = true
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr) ) {
            etPassword.error = getString(R.string.error_field_required)
            focusView = etPassword
            cancel = true
        } else if (!isPasswordValid(passwordStr))
        {
            etPassword.error = getString(R.string.error_invalid_password)
            focusView = etPassword
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            App.get(this).hideKeyboard(etPassword)
            presenter.attemptLogin(emailStr, passwordStr)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {

        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }

    @OnClick(R.id.button_register_user)
    fun onRegisterUserButtonClicked() {
        startActivity(Intent(this, RegisterUserActivity::class.java))
    }

    override fun onLoadFinish() {
        showProgress(false)
    }

    override fun onTimeout() {
        showToast(getString(R.string.unable_to_connect_to_server));
    }

    override fun onNetworkError() {
        showToast(getString(R.string.unable_to_connect_to_server));

    }

    override fun onFcmTokenSaved() {
        showProgress(false)
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }

    override fun onSuccessfulLogin(user: User) {
        App.get(this).setIsLoggedIn(true)
        App.get(this).setUserEmail(user.id)
        user.password = etPassword.text.toString()
        App.get(this).setUser(user)

        presenter.saveFcmToken(getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE).getString(AppConstants.FCM_TOKEN, "")!!,
                user.email, user.password)
    }

    override fun onUnknownError(errorMessage: String) {

    }

}
