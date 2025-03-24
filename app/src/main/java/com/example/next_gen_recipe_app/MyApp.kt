package com.example.next_gen_recipe_app

import android.app.Application
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize ML Kit Text Recognition
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }
}
