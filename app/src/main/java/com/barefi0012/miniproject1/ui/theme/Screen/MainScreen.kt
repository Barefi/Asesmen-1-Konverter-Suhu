package com.barefi0012.miniproject1.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.barefi0012.miniproject1.R
import com.barefi0012.miniproject1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(id = R.string.menu_about)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var temperatur by rememberSaveable { mutableStateOf("") }
    var temperaturError by rememberSaveable { mutableStateOf(false) }

    val radioOptions = listOf("Celcius", "Fahrenheit", "Kelvin", "Reamur")
    var selectedUnit by rememberSaveable { mutableStateOf(radioOptions[0]) }
    var hasilKonversi by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_header),
            contentDescription = stringResource(id = R.string.menu_about),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(150.dp)
        )

        OutlinedTextField(
            value = temperatur,
            onValueChange = {
                temperatur = it
                temperaturError = false
            },
            label = { Text(text = stringResource(id = R.string.label_input)) },
            trailingIcon = { if (temperaturError) Icon(Icons.Filled.Warning, null) },
            isError = temperaturError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )


        Column(modifier = Modifier.fillMaxWidth()) {
            radioOptions.forEach { text ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedUnit == text),
                            onClick = { selectedUnit = text },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (selectedUnit == text), onClick = null)
                    Text(text = text, modifier = Modifier.padding(start = 12.dp))
                }
            }
        }

        Button(
            onClick = {
                val inputDouble = temperatur.toDoubleOrNull()
                if (inputDouble == null) {
                    temperaturError = true
                } else {
                    hasilKonversi = hitungSuhu(inputDouble, selectedUnit)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.btn_convert))
        }

        if (hasilKonversi.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = hasilKonversi, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


private fun hitungSuhu(nilai: Double, dari: String): String {
    val c = when (dari) {
        "Fahrenheit" -> (nilai - 32) * 5 / 9
        "Kelvin" -> nilai - 273.15
        "Reamur" -> nilai * 5 / 4
        else -> nilai
    }
    return "C: %.2f | F: %.2f | K: %.2f | R: %.2f".format(c, (c * 9 / 5) + 32, c + 273.15, c * 4 / 5)
}