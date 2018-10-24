package eu.vincinity2020.p2p_parking.car

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.common.BaseActivity
import eu.vincinity2020.p2p_parking.entities.Car
import kotlinx.android.synthetic.main.activity_my_cars.*
import javax.inject.Inject


class MyCarsActivity : BaseActivity(), MyCarsView, MyCarsAdapter.OnCarClicked {

    @Inject
    lateinit var myCaPresenter: MyCarsPresenter

    lateinit var myCarsAdapter: MyCarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cars)

        App.get(this)
                .appComponent()
                .myCarsComponentBuilder()
                .myCarsModule(MyCarsModule())
                .build()
                .inject(this)

        button_confirm_my_cars
                .setOnClickListener {
                    myCaPresenter.registerCar(registration_number_edittext_my_cars
                            .text.toString())

                    registration_number_edittext_my_cars.text?.clear()
                }

        setUpRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        myCaPresenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        myCaPresenter.detach()
    }

    private fun setUpRecyclerView() {
        recycler_view_my_cars.layoutManager = LinearLayoutManager(recycler_view_my_cars.context)
        myCarsAdapter = MyCarsAdapter(arrayListOf(), this, this)
        recycler_view_my_cars.adapter = myCarsAdapter
        recycler_view_my_cars.addItemDecoration(DividerItemDecoration(
                recycler_view_my_cars.context,
                LinearLayoutManager.VERTICAL))
    }

    override fun onCarDefaulted(car: Car) {
        myCaPresenter.setDefault(car)
    }

    override fun onCarAdded() {
        Snackbar
                .make(linear_layout_my_cars, getString(R.string.car_added), Snackbar.LENGTH_LONG)
                .show()

    }

    override fun onCarDefaulted() {
        Snackbar
                .make(linear_layout_my_cars, getString(R.string.car_defaulted), Snackbar.LENGTH_LONG)
                .show()
    }

    override fun onCarsLoaded(cars: List<Car>) {
        myCarsAdapter.swapData(cars)
    }

    override fun onUnknownError(errorMessage: String) {

    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }

}