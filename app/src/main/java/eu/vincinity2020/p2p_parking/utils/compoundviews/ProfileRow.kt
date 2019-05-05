package eu.vincinity2020.p2p_parking.utils.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R

class ProfileRow
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    @BindView(R.id.image_view_profile_row)
    lateinit var imageViewStart: AppCompatImageView

    @BindView(R.id.text_view_profile_row)
    lateinit var textView: AppCompatTextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_profile_row, this, true)

        ButterKnife.bind(this, view)


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                    R.styleable.ProfileRow, 0, 0)
            val imageStartId = typedArray.getResourceId(R.styleable
                    .ProfileRow_imageStart,
                    -1)
            val imageStartIcon = AppCompatResources.getDrawable(context, imageStartId)

            imageViewStart.background = imageStartIcon

            val text = typedArray.getString(R.styleable.ProfileRow_text)
            textView.text = text

            typedArray.recycle()
        }
    }
}