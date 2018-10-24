package eu.vincinity2020.p2p_parking.registeruser

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.widget.DatePicker
import android.widget.TextView
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.common.BaseActivity
import kotlinx.android.synthetic.main.activity_register_user.*
import java.util.*

class RegisterUserActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    companion object {
        private const val ARG_LOGGED_IN = "arg:isLoggedIn"

        fun loggedInLaunchIntent(context: Context, isLoggedIn: Boolean): Intent {
            val intent = Intent(context, RegisterUserActivity::class.java)
            intent.putExtra(ARG_LOGGED_IN, isLoggedIn)

            return intent
        }
    }

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
        validateEmail()
        validateFirstName()
        validateSurname()
        validateDateOfBirth()
        validateGender()

    }

    private fun validateGender(): Boolean {
        var error = false
        gender_spinner_register_user.selectedItem.toString().let {
            if (it.length <= 3) {
                val errorView = gender_spinner_register_user.selectedView as TextView
                errorView.text = invalidInput
                errorView.setTextColor(Color.RED)
                errorView.error = invalidInput
                error = true
            }
        }

        if (gender_spinner_register_user.selectedItem == null) {
            dob_text_view_register_user.error = fieldRequired
            val errorView = gender_spinner_register_user.selectedView as TextView
            errorView.text = invalidInput
            errorView.setTextColor(Color.RED)
            errorView.error = invalidInput
            error = true
        }

        return error
    }

    private fun validateDateOfBirth(): Boolean {
        dob_text_view_register_user.error = null
        var error = false
        dob_text_view_register_user.text?.let {
            if (it.length <= 3) {
                dob_text_view_register_user.error = invalidInput
                error = true
            }
        }

        if (dob_text_view_register_user.text == null) {
            dob_text_view_register_user.error = fieldRequired
            error = true
        }

        return error
    }

    private fun validateEmail(): Boolean {
        var error = false
        email_edittext_register_user.error = null
        email_edittext_register_user.text?.let {
            if (!it.contains("@")) {
                email_edittext_register_user.error = invalidEmail
                error = true
            }

            if (it.length <= 3) {
                email_edittext_register_user.error = invalidEmail
                error = true
            }
        }

        if (email_edittext_register_user.text == null) {
            email_edittext_register_user.error = invalidEmail
            error = true
        }

        return error
    }

    private fun validateFirstName(): Boolean {
        var error = false
        first_name_edittext_register_user.error = null
        first_name_edittext_register_user.text?.let {
            if (it.length <= 3) {
                first_name_edittext_register_user.error = invalidInput
                error = true
            }
        }

        if (first_name_edittext_register_user.text == null) {
            first_name_edittext_register_user.error = fieldRequired
            error = true
        }

        return error
    }

    private fun validateSurname(): Boolean {
        surname_edittext_register_user.error = null
        var error = false
        surname_edittext_register_user.text?.let {
            if (it.length <= 3) {
                surname_edittext_register_user.error = invalidInput
                error = true
            }
        }

        if (surname_edittext_register_user.text == null) {
            surname_edittext_register_user.error = fieldRequired
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}