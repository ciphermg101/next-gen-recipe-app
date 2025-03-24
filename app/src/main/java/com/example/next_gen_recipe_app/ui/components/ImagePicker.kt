package com.example.next_gen_recipe_app.ui.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import java.io.InputStream

/**
 * Composable that allows the user to pick an image from their gallery.
 * When an image is selected, it converts the URI to a Bitmap and calls [onImageSelected].
 *
 * Robust error handling is implemented in [loadBitmapFromUri] to log any issues during image loading.
 */
@Composable
fun ImagePicker(
    onImageSelected: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Manage permission state (must be defined before using it)
    var permissionGranted by remember { mutableStateOf(false) }

    // Launcher to request permission.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    // Launcher for picking image from the gallery.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = loadBitmapFromUri(context, it)
            if (bitmap != null) {
                onImageSelected(bitmap)
            } else {
                Log.e("ImagePicker", "Failed to load image from URI: $it")
            }
        }
    }

    LaunchedEffect(Unit) {
        // Check for permission (both READ_EXTERNAL_STORAGE and READ_MEDIA_IMAGES for Android 13+)
        permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED

        // Request permission if not granted.
        if (!permissionGranted) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    Box(modifier = modifier) {
        Button(onClick = {
            if (permissionGranted) {
                launcher.launch("image/*")
            } else {
                Log.e("ImagePicker", "Required permission not granted.")
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }) {
            Text("Pick an Image")
        }
    }
}

/**
 * Converts a given [Uri] to a [Bitmap] using the ContentResolver.
 * Logs errors if any exceptions occur and returns null in such cases.
 *
 * @param context The context used to access the ContentResolver.
 * @param uri The URI of the image.
 * @return The decoded Bitmap, or null if an error occurs.
 */
private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use {
            BitmapFactory.decodeStream(it)
        }
    } catch (e: Exception) {
        Log.e("ImagePicker", "Error loading bitmap from URI: ${e.localizedMessage}", e)
        null
    }
}
