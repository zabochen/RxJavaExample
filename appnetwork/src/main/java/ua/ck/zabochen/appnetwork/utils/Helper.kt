package ua.ck.zabochen.appnetwork.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.support.annotation.ColorInt

class Helper {

    companion object {


        // Material Color
        fun getRandomMaterialColor(context: Context, typeColor: String): Int {

            @ColorInt
            var color = Color.BLACK

            val colorArrayId = context.resources.getIdentifier("mdcolor_$typeColor", "array", context.applicationContext.packageName)

            if (colorArrayId != 0) {
                val colorArray: TypedArray = context.resources.obtainTypedArray(colorArrayId)
                val colorIndex: Int = (Math.random() * colorArray.length()).toInt()
                color = colorArray.getColor(colorIndex, Color.BLACK)
                colorArray.recycle()
            }

            return color
        }

    }

}