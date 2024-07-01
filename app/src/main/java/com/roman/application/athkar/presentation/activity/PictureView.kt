package com.roman.application.athkar.presentation.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Picture
import android.util.AttributeSet
import android.view.View

class PictureView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var picture: Picture? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        picture?.let {
            canvas.drawPicture(it)
        }
    }
}