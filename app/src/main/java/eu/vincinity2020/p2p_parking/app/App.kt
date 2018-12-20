package eu.vincinity2020.p2p_parking.app

import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.data.entities.User

class App : Application() {
    private lateinit var appComponent: AppComponent

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "p2p-db"

        fun get(context: Context): App {
            return context.applicationContext as App
        }

    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .dbModule(DbModule(this, "db.sqlite"))
                .build()
    }


    fun appComponent(): AppComponent {
        return appComponent
    }

    fun isLoggedIn(): Boolean {
        return getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .getBoolean(AppConstants.USER_LOGGED_IN, false)
    }

    fun setIsLoggedIn(loggedId: Boolean) {
        getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(AppConstants.USER_LOGGED_IN, loggedId)
                .apply()
    }

  fun getUserEmail(): Long? {
        return getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .getLong(AppConstants.USER_EMAIL, 0)
    }

    fun setUserEmail(email: Long) {
        getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putLong(AppConstants.USER_EMAIL, email)
                .apply()
    }


    fun getUser(): User?
    {
        return Gson().fromJson(getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(AppConstants.USER, ""), User::class.java)
    }

    fun  setUser(user: User)
    {
        getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().putString(AppConstants.USER, Gson().toJson(user))
                .apply()
    }









    //Temp code to logout client's user

    fun isLoggedOut(): Boolean
    {
        return (getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .getBoolean(AppConstants.IsLoggedOut, false))
    }

    fun  setIsLoggedOut(isLoggedOut: Boolean)
    {
        getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().putBoolean(AppConstants.IsLoggedOut,isLoggedOut)
                .apply()
    }



    fun hideKeyboard(currentFocus: View)
    {
        val inputManager: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)

    }
}