package eu.vincinity2020.p2p_parking.app

import android.app.Application
import android.content.Context
import eu.vincinity2020.p2p_parking.common.AppConstants

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

    fun setIsLoggedIn() {
        getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(AppConstants.USER_LOGGED_IN, true)
                .apply()
    }


}