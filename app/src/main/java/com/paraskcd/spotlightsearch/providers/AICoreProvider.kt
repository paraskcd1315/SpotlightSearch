package com.paraskcd.spotlightsearch.providers

import android.content.Context
import com.google.ai.edge.aicore.GenerationConfig
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.generationConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AICoreProvider @Inject constructor(@ApplicationContext private val context: Context) {
    // Configuración del modelo pre-cargado (Gemini Nano)
    private val generativeModel: GenerativeModel by lazy {
        val generationConfig = generationConfig {
            context = this@AICoreProvider.context // required
            temperature = 0.2f
            topK = 16
            maxOutputTokens = 256
        }

        GenerativeModel(generationConfig)
    }

    fun getModel(): GenerativeModel = generativeModel
}