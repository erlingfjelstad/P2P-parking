package eu.vincinity2020.p2p_parking.utils.compoundviews

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.button.MaterialButton
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.ParkingSensor
import eu.vincinity2020.p2p_parking.ui.map.ParkingSpotAdapter

class ParkingSpotCardView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    @BindView(R.id.text_view_parking_spot_name)
    lateinit var parkingSpotNameTextView: AppCompatTextView

    @BindView(R.id.button_register_parking_spot)
    lateinit var registerParkingSpotButton: MaterialButton

    @BindView(R.id.image_button_close)
    lateinit var closeImageButton: AppCompatImageButton

    private var onParkingSpotClickedListener: ParkingSpotAdapter.OnParkingSpotClickedListener? = null

    init {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.view_parking_spot_card, this, true)

        ButterKnife.bind(this, view)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                    R.styleable.ParkingSpotCardView, 0, 0)
            val dismissable = typedArray.getBoolean(R.styleable
                    .ParkingSpotCardView_dismissable,
                    false)

            if (dismissable) {
                closeImageButton.visibility = View.VISIBLE
            } else {
                closeImageButton.visibility = View.GONE
            }

            typedArray.recycle()
        }

    }

    fun update(parkingSensor: ParkingSensor) {
        parkingSpotNameTextView.text = parkingSensor.status
        registerParkingSpotButton.setOnClickListener { onParkingSpotClickedListener?.onParkingSpotClicked(parkingSensor) }
        closeImageButton.setOnClickListener { onParkingSpotClickedListener?.onCloseButtonClicked() }
    }

    fun setOnParkingSpotClickedListener(onParkingSpotClickedListener: ParkingSpotAdapter.OnParkingSpotClickedListener) {
        this.onParkingSpotClickedListener = onParkingSpotClickedListener
    }


}