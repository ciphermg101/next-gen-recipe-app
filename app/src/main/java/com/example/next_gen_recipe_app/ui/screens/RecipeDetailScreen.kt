package com.example.next_gen_recipe_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.next_gen_recipe_app.data.local.entities.Recipe

@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineSmall
        )
        VerticalSpacer(8)

        Text(
            text = recipe.summary,
            style = MaterialTheme.typography.bodyMedium
        )
        VerticalSpacer(8)

        recipe.readyInMinutes?.let { minutes ->
            Text(
                text = "Ready in: $minutes minutes",
                style = MaterialTheme.typography.bodySmall
            )
            VerticalSpacer(4)
        }

        recipe.servings?.let { servings ->
            Text(
                text = "Servings: $servings",
                style = MaterialTheme.typography.bodySmall
            )
            VerticalSpacer(4)
        }

        recipe.sourceUrl?.let { url ->
            Text(
                text = "Source: $url",
                style = MaterialTheme.typography.bodySmall
            )
            VerticalSpacer(8)
        }

        recipe.steps.joinToString(separator = "\n") { "• $it" }.let {
            if (it.isNotBlank()) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun VerticalSpacer(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}
