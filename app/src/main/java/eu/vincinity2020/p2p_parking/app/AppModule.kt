package eu.vincinity2020.p2p_parking.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.common.ApplicationScope

@Module
class AppModule(application: Application) {
    private val context: Context

    init {
        this.context = application
    }

    @ApplicationScope
    @Provides
    fun providesAppContext(): Context {
        return context
    }

    @ApplicationScope
    @Provides
    fun providesDbModule(): DbModule {
        return DbModule(context, App.DB_NAME)
    }

    @ApplicationScope
    @Provides
    fun providesNetworkModule(): NetworkModule {
        return NetworkModule()
    }
}
