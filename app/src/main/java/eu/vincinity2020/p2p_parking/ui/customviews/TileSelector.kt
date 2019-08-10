package eu.vincinity2020.p2p_parking.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import eu.vincinity2020.p2p_parking.R

open class TileSelector : LinearLayout {

    init {
        setTileNotChosen()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attributeSet, defStyleAttr, defStyleRes)

    var state = -1

    fun setOnToggleListener(clickHandler: (isSelected: Boolean) -> Unit) {
        super.setOnClickListener {
            toggleState()
            clickHandler(state == 1)
        }
    }

    private fun toggleState() {
        if (state != 1 || state == -1) {
            setTileChosen()
        } else {
            setTileNotChosen()
        }
    }

    private fun setTileChosen() {
        state = 1
        setBackgroundResource(R.drawable.tile_not_selected_background)
    }

    fun setTileNotChosen() {
        state = 0
        setBackgroundResource(R.drawable.tile_selected_background)
    }

    fun isChosen():Boolean{
        return state == 1
    }

}