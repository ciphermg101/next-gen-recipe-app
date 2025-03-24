package com.example.next_gen_recipe_app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedTimer(
    modifier: Modifier = Modifier,
    durationMillis: Int = 60000 // one-minute timer as an example
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val color = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMillis)
        ) { value, _ ->
            progress = value
        }
    }

    Canvas(modifier = modifier.size(100.dp)) {
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = progress * 360,
            useCenter = false,
            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
