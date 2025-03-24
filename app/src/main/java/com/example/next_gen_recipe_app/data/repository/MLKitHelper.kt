package com.example.next_gen_recipe_app.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.tasks.await

/**
 * Helper class that leverages ML Kit's Object Detection & Tracking API
 * to detect objects in an image and filter out those likely representing food.
 */
class MLKitHelper {

    /**
     * Processes a [Bitmap] image using ML Kit's object detection.
     * Returns a list of detected object labels that are likely food-related.
     *
     * This method:
     * - Configures the object detector in single image mode with multiple object detection and classification enabled.
     * - Processes the image and awaits detection results.
     * - Filters detected objects based on a predefined list of food-related keywords.
     *
     * @param bitmap The image to process.
     * @return A list of labels (String) that are likely food-related, or an empty list if detection fails.
     */
    suspend fun detectFoodObjectsFromImage(bitmap: Bitmap): List<String> {
        var objectDetector = ObjectDetection.getClient(
            ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification() // Enables object classification.
                .build()
        )

        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val detectedObjects = objectDetector.process(image).await()

            val foodLabels = mutableListOf<String>()
            // Define a basic list of food-related keywords to filter results.
            val foodKeywords = listOf("food", "fruit", "vegetable", "dish", "meal", "snack")

            detectedObjects.forEach { detectedObject ->
                // Each detected object can have multiple classification labels.
                detectedObject.labels.forEach { label ->
                    val labelText = label.text.lowercase()
                    // Check if the detected label contains any food-related keyword.
                    if (foodKeywords.any { keyword -> labelText.contains(keyword) }) {
                        foodLabels.add(label.text)
                    }
                }
            }
            foodLabels
        } catch (e: Exception) {
            Log.e("MLKitHelper", "Error detecting objects: ${e.localizedMessage}", e)
            emptyList()
        } finally {
            try {
                objectDetector.close()
            } catch (e: Exception) {
                Log.e("MLKitHelper", "Error closing object detector: ${e.localizedMessage}", e)
            }
        }
    }
}
