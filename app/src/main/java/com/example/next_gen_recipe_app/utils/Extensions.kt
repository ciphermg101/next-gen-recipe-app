package com.example.next_gen_recipe_app.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A simple extension to apply a fade animation effect to a Modifier.
 */
fun Modifier.fade(alpha: Float): Modifier = this.alpha(alpha)

/**
 * An extension function to draw a dashed line on a canvas.
 */
fun DrawScope.drawDashedLine(
    color: Color,
    startX: Float,
    startY: Float,
    stopX: Float,
    stopY: Float,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp
) {
    val dashPx = dashLength.toPx()
    val gapPx = gapLength.toPx()
    val totalLength = kotlin.math.hypot((stopX - startX), (stopY - startY))
    var currentLength = 0f
    while (currentLength < totalLength) {
        val startFraction = currentLength / totalLength
        val endFraction = ((currentLength + dashPx).coerceAtMost(totalLength)) / totalLength
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(
                x = startX + (stopX - startX) * startFraction,
                y = startY + (stopY - startY) * startFraction
            ),
            end = androidx.compose.ui.geometry.Offset(
                x = startX + (stopX - startX) * endFraction,
                y = startY + (stopY - startY) * endFraction
            ),
            strokeWidth = 2f
        )
        currentLength += dashPx + gapPx
    }
}
