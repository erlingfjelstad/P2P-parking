package eu.vincinity2020.p2p_parking.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.text.Spannable
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import retrofit2.HttpException
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * An extension function to check active network connections
 * @receiver Context
 * @return Boolean
 */
//region: Context Extension
fun Context.isConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}

fun Context.hasPermissions(permissions: Array<String>): Boolean {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            permission
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
    }
    return true
}


fun Context.getManifestPermissions(): Array<String> {
    var packageInfo: PackageInfo? = null
    val list = ArrayList<String>(1)
    try {
        packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_PERMISSIONS
        )
    } catch (e: PackageManager.NameNotFoundException) {
        e.message?.let { error(it) }
    }

    if (packageInfo != null) {
        val permissions = packageInfo.requestedPermissions
        if (permissions != null) {
            Collections.addAll(list, *permissions)
        }
    }
    return list.toTypedArray()
}
//endregion

fun Context.getColorCompat(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * An Extension function that separates the items of array list into a new line
 *
 * @receiver ArrayList<String>
 * @return String
 */
fun ArrayList<String>.toLineSeparatedString(): String {
    val sb = StringBuilder("")
    this.forEach {
        sb.append("$it \n")
    }
    return sb.toString()
}

/**
 * An Extension function that separate the items of array list by comma
 *
 * @receiver ArrayList<String>
 * @return String
 */
fun ArrayList<String>.toCommaSeparatedString(): String {
    val sb = StringBuilder("")
    this.forEach {
        sb.append("$it, ")
    }
    sb.deleteCharAt(sb.length - 2)
    return sb.toString()
}

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

fun String.isOnlyIndianPhone(): Boolean {
    val p = "^[6-9]\\d{9}\$".toRegex()
    return matches(p)
}

fun String.isValidMobile(): Boolean {
    return android.util.Patterns.PHONE.matcher(this).matches()
}
//endregion


val EditText.value get() = text.toString()


fun <T: Any> T.TAG() = this::class.java.simpleName


fun String.ellipsize(at: Int): String {
    if (this.length > at) {
        return this.substring(0, at) + "..."
    }
    return this
}

//region: Random

fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) + start

fun Random.randomString(length: Int = 8): String {
    val base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var randomString = ""

    for (i in 1..length) {
        val randomValue = (0 until base.length).random()
        randomString += "${base[randomValue]}"
    }
    return randomString
}
//endregion


//region: Calendar, Date-Time Extensions
fun Calendar.getFormattedDate(format: String): String = SimpleDateFormat(format, Locale.ENGLISH)
        .format(this.timeInMillis)


fun Calendar.getDatePicker(
        context: Context,
        onDateSetListener: DatePickerDialog.OnDateSetListener
): DatePickerDialog {
    return DatePickerDialog(
            context, onDateSetListener,
            get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)
    )
}

fun Calendar.getTimePicker(
        context: Context,
        onTimeSetListener: TimePickerDialog.OnTimeSetListener,
        is24HourView: Boolean
): TimePickerDialog {
    return TimePickerDialog(
            context, onTimeSetListener,
            get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), is24HourView
    )
}


fun Long.getFormattedTime(format: String): String {
    return try {
        SimpleDateFormat(format, Locale.US).format(this)
    } catch (e: Exception) {
        ""
    }
}
//endregion


fun Date.convertTo(format: String): String? {
    var dateStr: String? = null
    val df = SimpleDateFormat(format)
    try {
        dateStr = df.format(this)
    } catch (ex: Exception) {
        Log.d("date", ex.toString())
    }

    return dateStr
}

// Converts current date to Calendar
fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun Date.isFuture(): Boolean {
    return !Date().before(this)
}

fun Date.isPast(): Boolean {
    return Date().before(this)
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

fun Date.today(): Date {
    return Date()
}

fun Date.yesterday(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    return cal.time
}

fun Date.tomorrow(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, 1)
    return cal.time
}

fun Date.hour(): Int {
    return this.toCalendar().get(Calendar.HOUR)
}

fun Date.minute(): Int {
    return this.toCalendar().get(Calendar.MINUTE)
}

fun Date.second(): Int {
    return this.toCalendar().get(Calendar.SECOND)
}

fun Date.month(): Int {
    return this.toCalendar().get(Calendar.MONTH) + 1
}

fun Date.monthName(locale: Locale? = Locale.getDefault()): String {
    return this.toCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)
}

fun Date.year(): Int {
    return this.toCalendar().get(Calendar.YEAR)
}

fun Date.day(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_MONTH)
}

fun Date.dayOfWeek(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_WEEK)
}

fun Date.dayOfWeekName(locale: Locale? = Locale.getDefault()): String {
    return this.toCalendar().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)
}

fun Date.dayOfYear(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_YEAR)
}

//region: Fragment Extensions
fun FragmentManager.addFragAllowingStateLoss(container: Int, fragment: Fragment) {
    this.beginTransaction()
            .add(container, fragment)
            .commitAllowingStateLoss()
}


fun FragmentManager.addFragment(@IdRes container: Int, fragment: Fragment) {
    this.beginTransaction()
            .add(container, fragment)
            .commit()
}

fun FragmentManager.addFragment(@IdRes container: Int, fragment: Fragment, tag: String?) {
    this.beginTransaction()
            .add(container, fragment)
            .addToBackStack(tag)
            .commit()
}


fun FragmentManager.replaceFragAllowingStateLoss(
        container: Int, fragment: Fragment,
        addToBackStack: Boolean
) {
    if (addToBackStack) {
        this.beginTransaction()
                .replace(container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    } else {
        this.beginTransaction()
                .replace(container, fragment)
                .commitAllowingStateLoss()
    }

}

/**
 * An Extension function that replaces the fragment
 *
 * @receiver FragmentManager
 * @param container Int
 * @param fragment Fragment
 * @param addToBackStack Boolean
 */
fun FragmentManager.replaceFragment(
        @IdRes container: Int,
        fragment: Fragment,
        addToBackStack: Boolean = false
) {
    if (addToBackStack) {
        this.beginTransaction()
                .replace(container, fragment)
                .addToBackStack(null)
                .commit()
    } else {
        this.beginTransaction()
                .replace(container, fragment)
                .commit()
    }

}

fun FragmentManager.addFragmentIfNotAlreadyAdded(
        @IdRes container: Int,
        fragment: Fragment
) {
    val currentFragment = findFragmentById(container)
    if (currentFragment == null || currentFragment::class.java.name != fragment::class.java.name) {
        addFragment(container, fragment)
    }
}

fun FragmentManager.replaceFragmentIfNotAlreadyVisible(
        @IdRes container: Int,
        fragment: Fragment,
        addToBackStack: Boolean = false
) {
    val currentFragment = findFragmentById(container)
    if (currentFragment == null || currentFragment::class.java.name != fragment::class.java.name) {
        replaceFragment(container, fragment, addToBackStack)
    }
}

fun String.toWords() = trim().splitToSequence(' ').filter { it.isNotEmpty() }.toList()
//endregion


//region: View Visibility extensions
fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.invisible(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun ArrayList<View>.gone() {
    this.forEach {
        it.gone()
    }
}

fun ArrayList<View>.show() {
    this.forEach {
        it.show()
    }
}

fun ArrayList<View>.enable() {
    this.forEach {
        it.enable()
    }
}

fun ArrayList<View>.disable() {
    this.forEach {
        it.disable()
    }
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

//endregion


//region: DisplayMetrics
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

//endregion


/**
 * An Extension function to get the text of the selected checked button
 *
 * @receiver RadioGroup
 * @return String
 */

//region: UI
fun RadioGroup.getCheckedButtonText(): String {
    val radioButtonID = this.checkedRadioButtonId
    val radioButtonView = this.findViewById<View>(radioButtonID)
    val idx = this.indexOfChild(radioButtonView)
    if (idx == -1) {
        return ""
    }
    val radioButton = this.getChildAt(idx) as RadioButton
    return radioButton.text.toString()
}
//endregion


//region: TextView

fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}

fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, color)), start,
                start + substring.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannable
    } catch (e: Exception) {
        Log.e(
                "ViewExtensions", "exception in setColorOfSubstring, text=$text, substring=$substring", e
        )
    }
}

fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

fun ArrayList<EditText>.clearAll() {
    this.forEach {
        it.clear()
    }
}

fun EditText.clear() {
    setText("")
}

//endregion

fun Throwable.getErrorMessage(): String {
    if (this is HttpException && code() == 401) {
        return App.applicationContext().getString(R.string.invalid_credentials)
    }

    if (this is HttpException) {
        return App.applicationContext().getString(R.string.error_msg_server_error)
    }

    if (this is ConnectException || this is SocketTimeoutException || this is UnknownHostException) {
        return if (App.applicationContext().isConnected()) {
            App.applicationContext().getString(R.string.error_msg_cannot_connect)
        } else {
            App.applicationContext().getString(R.string.error_msg_no_internet)
        }
    }
    Log.e("Throwable", "Exception", this)
    //Crashlytics.logException(this)
    return App.applicationContext().getString(R.string.error_msg_generic)
}

fun IntArray.allPermissionsAccepted(): Boolean {
    forEach {
        if (it == -1) return false
    }
    return true
}

private fun getFileExtension(file: File): String {
    val fileName = file.name
    return if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        fileName.substring(fileName.lastIndexOf(".") + 1)
    else
        ""
}

fun File.getMimeType(): String {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(this.absolutePath)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type ?: ""
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Long.toCompoundDuration(): String {
    if (this < 0L) return "" // task doesn't ask for negative integers to be converted
    if (this == 0L) return "0 seconds"
    val weeks  : Long
    val days   : Long
    val hours  : Long
    val minutes: Long
    val seconds: Long
    var divisor: Long = 7 * 24 * 60 * 60
    var rem    : Long
    var result = ""

    weeks = this / divisor
    rem   = this % divisor
    divisor /= 7
    days  = rem / divisor
    rem  %= divisor
    divisor /= 24
    hours = rem / divisor
    rem  %= divisor
    divisor /= 60
    minutes = rem / divisor
    seconds = rem % divisor

    if (weeks > 0)   result += "$weeks weeks, "
    if (days > 0)    result += "$days days, "
    if (hours > 0)   result += "$hours hours, "
    if (minutes > 0) result += "$minutes minutes, "
    if (seconds > 0)
        result += "$seconds sec"
    else
        result = result.substring(0, result.length - 2)
    return result
}