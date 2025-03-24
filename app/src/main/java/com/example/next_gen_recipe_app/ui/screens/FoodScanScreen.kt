package com.example.next_gen_recipe_app.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.next_gen_recipe_app.data.repository.MLKitHelper
import com.example.next_gen_recipe_app.ui.components.ImagePicker

@Composable
fun FoodScanScreen(
    mlKitHelper: MLKitHelper,
    onFoodScanResult: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var scannedFoods by remember { mutableStateOf<List<String>>(emptyList()) }
    var isScanning by remember { mutableStateOf(false) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val coroutineScope = rememberCoroutineScope()  // For launching background jobs

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Scan Food Items", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Image Picker
        ImagePicker(onImageSelected = { bitmap: Bitmap ->
            selectedBitmap = bitmap
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Show selected image preview
        selectedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scan button
        Button(
            onClick = {
                selectedBitmap?.let { bitmap ->
                    isScanning = true
                    coroutineScope.launch {
                        scannedFoods = mlKitHelper.detectFoodObjectsFromImage(bitmap)
                        onFoodScanResult(scannedFoods)
                        isScanning = false
                    }
                }
            },
            enabled = selectedBitmap != null && !isScanning
        ) {
            Text("Scan Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show progress indicator when scanning
        if (isScanning) {
            CircularProgressIndicator()
        }

        // Display scanned food results
        scannedFoods.forEach { food ->
            Text(text = food, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
