package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.data.dto.Country
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.ui.auth.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register_user.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RegisterUserActivity : BaseActivity(), RegisterView, DatePickerDialog.OnDateSetListener {


    companion object {
        private const val ARG_LOGGED_IN = "arg:isLoggedIn"

        fun loggedInLaunchIntent(context: Context, isLoggedIn: Boolean): Intent {
            val intent = Intent(context, RegisterUserActivity::class.java)
            intent.putExtra(ARG_LOGGED_IN, isLoggedIn)

            return intent
        }
    }


    @Inject
    lateinit var presenter: RegistrationPresenter
    
    @BindView(R.id.dob_text_view_register_user)
    lateinit var dobTextView: TextInputEditText

    @BindView(R.id.dob_text_input_layout_register_user)
    lateinit var dobTextInputLayout: TextInputLayout

    @BindString(R.string.error_invalid_email)
    lateinit var invalidEmail: String

    @BindString(R.string.error_invalid_password)
    lateinit var invalidCredentials: String

    @BindString(R.string.error_invalid_input)
    lateinit var invalidInput: String

    @BindString(R.string.error_field_required)
    lateinit var fieldRequired: String

    @BindString(R.string.edit_profile)
    lateinit var editProfile: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        ButterKnife.bind(this)

        App.get(this)
                .appComponent()
                .registerUserComponentBuilder()
                .registerUserModule(RegisterUserModule())
                .build()
                .inject(this)

        intent.extras?.let {
            val isInEditMode = it.getBoolean(ARG_LOGGED_IN)

            if (isInEditMode) {
                button_confirm_registration.text = editProfile
                supportActionBar?.title = editProfile
            }
        }

        presenter.getCountries()

    }

    @OnClick(R.id.dob_text_input_layout_register_user)
    fun onDateOfBirthClicked() {
        openDatePickerDialog()
    }

    @OnClick(R.id.dob_text_view_register_user)
    fun onDobTextViewClicked() {
        openDatePickerDialog()
    }

    @OnClick(R.id.button_confirm_registration)
    fun onRegistrateUserClicked() {
        validateForm()
    }

    private fun validateForm() {
        if (!validateEmail() && !validateFirstName() && !validateLastName() && !validateMobile() && !validateCountry())

        {
            showProgress(true)
            val token = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE).getString(AppConstants.FCM_TOKEN, "")

            presenter.attemptRegistration(etFirstName.text.toString(), etLastName.text.toString(), etContactNumber.text.toString(),
                    etEmail.text.toString(), etPassword.text.toString(), spCountry.selectedItem as Country,
                    spGender.selectedItem.toString(), spUserRole.selectedItem.toString(), token!!)
        }

    }



    private fun validateEmail(): Boolean {
        var error = false
        etEmail.error = null
        etEmail.text?.let {
            if (!it.contains("@")) {
                etEmail.error = invalidEmail
                error = true
            }

            if (it.length <= 3) {
                etEmail.error = invalidEmail
                error = true
            }
        }

        if (etEmail.text == null) {
            etEmail.error = invalidEmail
            error = true
        }

        return error
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
    }


    private fun validateFirstName(): Boolean {
        var error = false
        etFirstName.error = null
        etFirstName.text?.let {
            if (it.length <= 3) {
                etFirstName.error = invalidInput
                error = true
            }
        }

        if (etFirstName.text == null) {
            etFirstName.error = fieldRequired
            error = true
        }

        return error
    }


    private fun validateLastName(): Boolean {
        etLastName.error = null
        var error = false
        etLastName.text?.let {
            if (it.length <= 3) {
                etLastName.error = invalidInput
                error = true
            }
        }

        if (etLastName.text == null) {
            etLastName.error = fieldRequired
            error = true
        }

        return error
    }


    private fun validateMobile(): Boolean {
        etContactNumber.error = null
        var error = false
        etContactNumber.text?.let {
            if (it.length <= 8) {
                etContactNumber.error = invalidInput
                error = true
            }
        }

        if (etContactNumber.text == null) {
            etContactNumber.error = fieldRequired
            error = true
        }

        return error
    }


    private fun validateCountry(): Boolean {
        var error = false
        if (spCountry.selectedItem == null) {
            error = true
        }

        return error
    }




    private fun openDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(this, this, 1970, 1, 1)

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        // 18 years to be a valid driver
        calendar.set(Calendar.YEAR, currentYear - 18)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }


    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val chosenDate = String.format(Locale.US, "%d-%d-%d", year, month, day)
        dobTextView.setText(chosenDate, TextView.BufferType.EDITABLE)

    }


    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {

        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        registrationForm.visibility = if (show) View.GONE else View.VISIBLE
        registrationForm.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        registrationForm.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        progress.visibility = if (show) View.VISIBLE else View.GONE
        progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


    fun initCitySpinner(countries: ArrayList<Country>)
    {
        countries.add(Country(0,"Please select country", null, null ,null))

        val dataAdapter2 = object : ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, countries) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<View>(android.R.id.text1) as TextView
                textView.text = getItem(position)?.country
                return view
            }

//            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
//
//                var v: View? = null
//
//                if (position == 0) {
//                    val tv = TextView(context)
//                    tv.height = 0
//                    tv.visibility = View.GONE
//                    v = tv
//                } else {
//
//                    v = super.getDropDownView(position, null, parent)
//                }
//
//                parent.isVerticalScrollBarEnabled = false
//                return v
//            }
        }
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCountry.setAdapter(dataAdapter2)
    }


    override fun onUnknownError(errorMessage: String) {

    }

    override fun onTimeout() {

    }

    override fun onNetworkError() {
    }

    override fun onSuccessfulRegistration(response: JsonObject) {
        if (response.has("message"))
        {
            Toast.makeText(this, response.get("message").asString, Toast.LENGTH_SHORT).show()

        }

        if (!response.get("error").asBoolean)
        {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("email", etEmail.text.toString())
            startActivity(intent)
            finish()

        }


    }

    override fun onLoadFinish() {
        showProgress(false)
    }

    override fun onCountriesLoaded(countries: ArrayList<Country>) {
        initCitySpinner(countries)
    }

}