package com.paraskcd.spotlightsearch.providers

import android.content.Context
import com.google.ai.edge.aicore.GenerationConfig
import com.google.ai.edge.aicore.GenerativeModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AICoreProvider @Inject constructor(@ApplicationContext private val context: Context) {
    // Configuración del modelo pre-cargado (Gemini Nano)
    private val generativeModel: GenerativeModel by lazy {
        val genConfig = GenerationConfig.Builder()
            .build()
        GenerativeModel(genConfig)
    }

    fun getModel(): GenerativeModel = generativeModel
}