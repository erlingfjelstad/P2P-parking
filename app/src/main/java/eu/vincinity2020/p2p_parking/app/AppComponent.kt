package eu.vincinity2020.p2p_parking.app

import android.content.Context
import dagger.Component
import eu.vincinity2020.p2p_parking.app.network.NetworkModule
import eu.vincinity2020.p2p_parking.ui.auth.login.LoginComponent
import eu.vincinity2020.p2p_parking.ui.auth.registeruser.RegisterUserComponent
import eu.vincinity2020.p2p_parking.ui.book.BookParkingSpotComponent
import eu.vincinity2020.p2p_parking.ui.bookings.ViewBookingComponent
import eu.vincinity2020.p2p_parking.ui.car.MyCarsComponent
import eu.vincinity2020.p2p_parking.ui.map.MapComponent
import eu.vincinity2020.p2p_parking.ui.mylocations.addlocation.AddLocationComponent
import eu.vincinity2020.p2p_parking.ui.mylocations.locationlist.MyLocationListComponent
import eu.vincinity2020.p2p_parking.ui.places.addplaces.AddPlacesComponent
import eu.vincinity2020.p2p_parking.ui.places.listplaces.PlacesListComponent
import eu.vincinity2020.p2p_parking.ui.profile.edit.part1.EditProfile1Component
import eu.vincinity2020.p2p_parking.ui.profile.edit.part2.EditProfile2Component
import eu.vincinity2020.p2p_parking.ui.profile.edit.part3.EditProfile3Component
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.EditUserComponent
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.body.BodyComponent
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.info.InfoComponent
import eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.role.RoleComponent
import eu.vincinity2020.p2p_parking.ui.profile.view.ViewProfileComponent
import eu.vincinity2020.p2p_parking.ui.search.SearchComponent
import eu.vincinity2020.p2p_parking.ui.trip.TripComponent
import eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle.AddVehicleComponent
import eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist.VehicleListComponent

@Component(modules = [AppModule::class, DbModule::class, NetworkModule::class])
interface AppComponent/*: AndroidInjector<App>*/ {
    fun context(): Context

    fun dbModule(): DbModule

    fun loginComponentBuilder(): LoginComponent.Builder

    fun registerUserComponentBuilder(): RegisterUserComponent.Builder

    fun mapComponentBuilder(): MapComponent.Builder

    fun viewProfileComponentBuilder(): ViewProfileComponent.Builder


    fun editProfile1ComponentBuilder(): EditProfile1Component.Builder

    fun editProfile2ComponentBuilder(): EditProfile2Component.Builder

    fun editProfile3ComponentBuilder(): EditProfile3Component.Builder


    fun searchComponentBuilder(): SearchComponent.Builder

    fun myCarsComponentBuilder(): MyCarsComponent.Builder

    fun bookParkingSpotComponentBuilder(): BookParkingSpotComponent.Builder

    fun viewBookingsComponentBuilder(): ViewBookingComponent.Builder


    fun addVehicleComponentBuilder(): AddVehicleComponent.Builder

    fun viewVehiclesComponentBuilder(): VehicleListComponent.Builder


    fun tripComponentBuilder(): TripComponent.Builder


    fun myLocationListComponentBuilder(): MyLocationListComponent.Builder

    fun addLocationComponentBuilder(): AddLocationComponent.Builder

    fun myPlacesComponentBuilder(): AddPlacesComponent.Builder

    fun placeListComponentBuilder(): PlacesListComponent.Builder

    fun roleComponentBuilder(): RoleComponent.Builder

    fun bodyComponentBuilder(): BodyComponent.Builder

    fun infoComponentBuilder(): InfoComponent.Builder

    fun editUserComponentBuilder(): EditUserComponent.Builder

    /*  @Component.Builder
      interface Builder {
          @BindsInstance
          fun application(app: Application): Builder

          fun build(): AppComponent
      }

      override fun inject(instance: App?)*/
}