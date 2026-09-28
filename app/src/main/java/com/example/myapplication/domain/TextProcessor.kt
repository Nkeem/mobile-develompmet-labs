package com.example.myapplication.domain

import com.example.myapplication.data.ResultFormatter
import java.text.Collator
import java.util.Locale

class TextProcessor {
    fun processText(text: String, length: Int): List<String> {
        return text.lowercase()
            .split(Regex("[^\\p{L}]+"))
            .filter { it.isNotEmpty() && it.length != length }
            .distinct()
            .sortedWith(Collator.getInstance(Locale.forLanguageTag("ru")))
    }

    fun runProcessing(text: String, lengthInput: String): String {
        val length = lengthInput.trim().toIntOrNull()
        if (length == null || length <= 0) {
            return "Введите положительное целое число."
        }
        return ResultFormatter.format(processText(text, length))
    }
}
