package com.example.vennApp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
/**
 * Aktivitet som lager ny instanse av klassen Venn
 *
 * Output:
 * @return RESULT_OK med felt variabler til klassen Venn, i intent,gitt at en velger å lagre
 */
@OptIn(ExperimentalMaterial3Api::class)
class NyVennActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NyVennCompose(
                onReturn = { finish() },
                onSave = { fornavn, etternavn, fodselsdato ->
                    val resultIntent   = Intent()
                    resultIntent.putExtra("fornavn", fornavn)
                    resultIntent.putExtra("etternavn", etternavn)
                    resultIntent.putExtra("fodselsdato", fodselsdato)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NyVennCompose(
    onReturn: () -> Unit,
    onSave: (fornavn: String, etternavn: String, fodselsdato: String) -> Unit
) {
    var fornavn by remember { mutableStateOf("") }
    var etternavn by remember { mutableStateOf("") }
    var fodselsdato by remember { mutableStateOf("") }

    // Feilmeldinger
    var errorFornavn by remember { mutableStateOf<String?>(null) }
    var errorEtternavn by remember { mutableStateOf<String?>(null) }
    var errorFodselsdato by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormater = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tilbake") },
                navigationIcon = {
                    IconButton(onClick = { onReturn() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tilbake")
                    }
                }
            )
        },
        containerColor = Color(0xFFA5D6A7)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Fornavn", color = Color.Black, modifier = Modifier.padding(bottom = 4.dp))
                        OutlinedTextField(
                            value = fornavn,
                            onValueChange = {
                                fornavn = it
                                errorFornavn = null
                            },
                            isError = errorFornavn != null,
                            placeholder = { Text("Fornavn") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color.White
                            )
                        )
                        if (errorFornavn != null) {
                            Text(
                                text = errorFornavn ?: "",
                                color = Color.Red,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Etternavn", color = Color.Black, modifier = Modifier.padding(bottom = 4.dp))
                        OutlinedTextField(
                            value = etternavn,
                            onValueChange = {
                                etternavn = it
                                errorEtternavn = null
                            },
                            isError = errorEtternavn != null,
                            placeholder = { Text("Etternavn") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color.White
                            )
                        )
                        if (errorEtternavn != null) {
                            Text(
                                text = errorEtternavn ?: "",
                                color = Color.Red,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Fødselsdato", color = Color.Black, modifier = Modifier.padding(bottom = 4.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = fodselsdato,
                            onValueChange = { },
                            readOnly = true,
                            isError = errorFodselsdato != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White),
                            placeholder = { Text("DD/MM/YYYY") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Velg dato"
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorContainerColor = Color.White
                            )
                        )


                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    val dato = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            calendar.set(year, month, dayOfMonth)
                                            fodselsdato = dateFormater.format(calendar.time)
                                            errorFodselsdato = null
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    )
                                    dato.show()
                                }
                        )
                    }

                    if (errorFodselsdato != null) {
                        Text(
                            text = errorFodselsdato ?: "",
                            color = Color.Red,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                errorFornavn = if (fornavn.isBlank()) "Skriv inn fornavn" else null
                                errorEtternavn = if (etternavn.isBlank()) "Skriv inn etternavn" else null
                                errorFodselsdato = if (fodselsdato.isBlank()) "Velg en fødselsdato" else null


                                if (errorFornavn == null && errorEtternavn == null && errorFodselsdato == null) {
                                    onSave(fornavn, etternavn, fodselsdato)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(0.dp),
                        ) {
                            Text("Lagre")
                        }
                    }
                }
            }
        }
    }
}