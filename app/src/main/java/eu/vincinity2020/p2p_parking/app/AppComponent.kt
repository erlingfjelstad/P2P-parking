package eu.vincinity2020.p2p_parking.app

import android.content.Context
import dagger.Component
import eu.vincinity2020.p2p_parking.book.BookParkingSpotComponent
import eu.vincinity2020.p2p_parking.car.MyCarsComponent
import eu.vincinity2020.p2p_parking.login.LoginComponent
import eu.vincinity2020.p2p_parking.map.MapComponent
import eu.vincinity2020.p2p_parking.registeruser.RegisterUserComponent
import eu.vincinity2020.p2p_parking.search.SearchComponent
import eu.vincinity2020.p2p_parking.trip.TripComponent

@Component(modules = [AppModule::class, DbModule::class, NetworkModule::class])
interface AppComponent {
    fun context(): Context

    fun dbModule(): DbModule

    fun loginComponentBuilder(): LoginComponent.Builder

    fun registerUserComponentBuilder(): RegisterUserComponent.Builder

    fun mapComponentBuilder(): MapComponent.Builder

    fun searchComponentBuilder(): SearchComponent.Builder

    fun myCarsComponentBuilder(): MyCarsComponent.Builder

    fun bookParkingSpotComponentBuilder(): BookParkingSpotComponent.Builder

    fun tripComponentBuilder(): TripComponent.Builder
}