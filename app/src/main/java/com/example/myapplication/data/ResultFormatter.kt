package com.example.myapplication.data

object ResultFormatter {
    fun format(words: List<String>): String {
        return words.joinToString("\n").ifEmpty { "Слов не осталось." }
    }
}
