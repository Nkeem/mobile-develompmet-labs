package com.example.myapplication.ui.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.SourceText
import com.example.myapplication.domain.TextProcessor

@Composable
fun TextProcessingScreen(modifier: Modifier = Modifier) {
    var lengthInput by remember { mutableStateOf("3") }
    var result by remember { mutableStateOf("") }
    val processor = remember { TextProcessor() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Лабораторная №1. Вариант 14")
        Text("Исходный текст:")
        Text(SourceText.text)

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
            result = processor.runProcessing(SourceText.text, lengthInput)
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
