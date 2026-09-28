package com.example.myapplication

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
}
