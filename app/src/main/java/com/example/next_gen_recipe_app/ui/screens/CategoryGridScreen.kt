package com.example.next_gen_recipe_app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryGridScreen(
    categories: List<String>, // Example: list of category names
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp)
    ) {
        items(categories) { category ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onCategoryClick(category) }
            ) {
                Text(text = category, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
