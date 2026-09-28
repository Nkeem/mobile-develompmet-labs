package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.LabTheme

class MainActivity : ComponentActivity() {
    private val processor = TextProcessor()
    private val sourceText = "Кот, море, дом, река, море, лес. " +
        "Солнце, книга, река, школа, кот, ёлка."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabTheme {
                var lengthInput by remember { mutableStateOf("3") }
                var result by remember { mutableStateOf("") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Лабораторная №1. Вариант 14")
                        Text("Исходный текст:")
                        Text(sourceText)

                        OutlinedTextField(
                            value = lengthInput,
                            onValueChange = {
                                lengthInput = it
                                result = ""
                            },
                            label = { Text("Длина удаляемых слов") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(onClick = {
                            val length = lengthInput.trim().toIntOrNull()
                            result = if (length == null || length <= 0) {
                                "Введите положительное целое число."
                            } else {
                                processor.processText(sourceText, length)
                                    .joinToString("\n")
                                    .ifEmpty { "Слов не осталось." }
                            }
                        }) {
                            Text("Обработать")
                        }

                        OutlinedTextField(
                            value = result,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Результат") },
                            minLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
