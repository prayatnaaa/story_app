package com.prayatna.storyapp.helper.customView

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class RoundedBottomNavView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        setupBackground()
    }

    private fun setupBackground() {
        val shapeModel = ShapeAppearanceModel.Builder()
            .setTopLeftCornerSize(16f)
            .setTopRightCornerSize(16f)
            .setBottomLeftCornerSize(16f)
            .setBottomRightCornerSize(16f)
            .build()

        val backgroundDrawable = MaterialShapeDrawable(shapeModel).apply {
            setTint(Color.WHITE) // Set the background color
            elevation = 8f // Add shadow
        }

        // Set the background
        background = backgroundDrawable
        clipToOutline = true
    }
}
