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

    var isChosen = false

    fun setOnToggleListener(clickHandler: (isSelected: Boolean) -> Unit){
        super.setOnClickListener {
            if(isChosen){
                setTileNotChosen()
            }else{
                setTileChosen()
            }
            clickHandler(isChosen)
        }
    }

    private fun setTileChosen() {
        isChosen = true
        setBackgroundResource(R.drawable.tile_not_selected_background)
    }

    private fun setTileNotChosen() {
        isChosen = false
        setBackgroundResource(R.drawable.tile_selected_background)
    }



}