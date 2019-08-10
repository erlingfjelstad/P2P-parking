package eu.vincinity2020.p2p_parking.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.drawable.DrawableCompat
import android.os.Build
import androidx.core.content.ContextCompat
import android.graphics.drawable.Drawable


class AndroidUtils {

    companion object {
        fun attachFragment(fragmentManager: FragmentManager,
                           fragment: Fragment,
                           viewId: Int,
                           tag: String,
                           addToStack: Boolean) {
            var transaction = fragmentManager.beginTransaction().replace(viewId, fragment, tag);
            if (addToStack)
                transaction.addToBackStack(tag)
            transaction.commit()
        }
    }


}

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    var drawable = ContextCompat.getDrawable(context, drawableId)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable!!).mutate()
    }

    val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}