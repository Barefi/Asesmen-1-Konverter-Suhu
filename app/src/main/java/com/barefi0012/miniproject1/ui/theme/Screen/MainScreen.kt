package com.barefi0012.miniproject1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.barefi0012.miniproject1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var temperatur by remember { mutableStateOf("") }
    var temperaturError by remember { mutableStateOf(false) }

    val radioOptions = listOf("Celcius", "Fahrenheit", "Kelvin", "Reamur")
    var selectedUnit by remember { mutableStateOf(radioOptions[0]) }

    var hasilKonversi by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = temperatur,
            onValueChange = {
                temperatur = it
                temperaturError = false
            },
            label = { Text(text = stringResource(R.string.label_input)) },
            trailingIcon = { IconPicker(temperaturError) },
            supportingText = { ErrorHint(temperaturError) },
            isError = temperaturError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Pilih Satuan Asal:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            radioOptions.forEach { text ->
                UnitOption(
                    label = text,
                    isSelected = (selectedUnit == text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedUnit == text),
                            onClick = { selectedUnit = text },
                            role = Role.RadioButton
                        )
                )
            }
        }

        Button(
            onClick = {
                temperaturError = (temperatur == "" || temperatur.toDoubleOrNull() == null)
                if (temperaturError) return@Button

                val nilai = temperatur.toDoubleOrNull() ?: 0.0
                hasilKonversi = hitungSuhu(nilai, selectedUnit)
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.btn_convert))
        }

        if (hasilKonversi.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
            Text(text = "Hasil Konversi:", style = MaterialTheme.typography.titleMedium)
            Text(
                text = hasilKonversi,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun UnitOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun IconPicker(isError: Boolean) {
    if (isError) Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) Text(text = "Input tidak valid!", color = MaterialTheme.colorScheme.error)
}

private fun hitungSuhu(nilai: Double, dari: String): String {
    val c = when (dari) {
        "Fahrenheit" -> (nilai - 32) * 5 / 9
        "Kelvin" -> nilai - 273.15
        "Reamur" -> nilai * 5 / 4
        else -> nilai
    }
    val f = (c * 9 / 5) + 32
    val k = c + 273.15
    val r = c * 4 / 5


    return "Celcius: %.2f\nFahrenheit: %.2f\nKelvin: %.2f\nReamur: %.2f".format(c, f, k, r)
}