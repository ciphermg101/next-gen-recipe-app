package com.example.next_gen_recipe_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.next_gen_recipe_app.R // Ensure a placeholder image exists
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * A composable that loads and displays the recipe image.
 * Shows a shimmer effect while loading and a fallback image in case of failure.
 *
 * @param recipeId The ID of the recipe.
 * @param recipeImage The image URL of the recipe.
 * @param modifier Optional Modifier to be applied to the image.
 */
@Composable
fun RecipeCardImage(
    recipeId: Int,  // Pass recipeId instead of full Recipe object
    recipeImage: String,  // Pass image URL
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(recipeImage)
                .crossfade(true)
                .placeholder(R.drawable.placeholder_image)  // Fallback while loading
                .error(R.drawable.placeholder_image)  // Error fallback
                .build(),
            contentDescription = "Recipe Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
