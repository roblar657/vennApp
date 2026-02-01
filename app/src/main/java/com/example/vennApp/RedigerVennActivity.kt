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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.isBlank

/**
 * Aktivitet som endrer fornavn, etternavn og/eller fødselsdato
 *
 * Input:
 * @param fornavn  Opprinnelig fornavn fra intent
 * @param etternavn Opprinnelig etternavn fra intent
 * @param fodselsdato  Opprinnelig fødselsdato fra intent
 *
 * Output:
 * @return RESULT_OK med oppdaterte verdier i intent, hvis endringer lagres
 */
@OptIn(ExperimentalMaterial3Api::class)
class RedigerVennActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Fornavn som vises i fornavn tekstfelt, når en starter aktivitet
        val initFornavn = intent.getStringExtra("fornavn") ?: ""
        //Etternavn som vises i etternavn tekstfelt, når en starter aktivitet
        val initEtternavn = intent.getStringExtra("etternavn") ?: ""
        //Fødselsdato som vises i fodselsdato tekstfelt, når en starter aktivitet
        val initFodselsdato = intent.getStringExtra("fodselsdato") ?: ""
        val initPos = intent.getIntExtra("pos",-1)

        setContent {
            RedigerCompose(
                initFornavn = initFornavn,
                initEtternavn = initEtternavn,
                initFodselsdato = initFodselsdato,
                initPos = initPos,

                onReturn= {
                    finish() },

                onEdit = { fornavn, etternavn, fodselsdato,pos ->
                    val resultIntent   = Intent()
                    resultIntent.putExtra("fornavn", fornavn)
                    resultIntent.putExtra("etternavn", etternavn)
                    resultIntent.putExtra("fodselsdato", fodselsdato)
                    resultIntent.putExtra("pos", pos)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun RedigerCompose(
    initFornavn: String,
    initEtternavn: String,
    initFodselsdato: String,
    onReturn: () -> Unit,
    onEdit: (fornavn: String, etternavn: String, fodselsdato: String, pos:Int) -> Unit,
    initPos: Int
) {
    var fornavn by remember { mutableStateOf(TextFieldValue(initFornavn)) }
    var etternavn by remember { mutableStateOf(TextFieldValue(initEtternavn)) }
    var fodselsdato by remember { mutableStateOf(initFodselsdato) }
    var pos by remember { mutableStateOf(initPos) }


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
        containerColor = Color(0xFFFFECB3)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

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
                        placeholder = { Text("DD/MM/YYYY") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
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
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = {
                        fornavn = TextFieldValue(initFornavn)
                        etternavn = TextFieldValue(initEtternavn)
                        fodselsdato = initFodselsdato
                        errorFornavn = null
                        errorEtternavn = null
                        errorFodselsdato = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Reset")
                }


                Button(
                    onClick = {
                        errorFornavn = if (fornavn.text.isBlank()) "Skriv inn fornavn" else null
                        errorEtternavn = if (etternavn.text.isBlank()) "Skriv inn etternavn" else null
                        errorFodselsdato = if (fodselsdato.isBlank()) "Velg en fødselsdato" else null

                        if (errorFornavn == null && errorEtternavn == null && errorFodselsdato == null) {
                            onEdit(fornavn.text, etternavn.text, fodselsdato,pos)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("Endre")
                }
            }
        }
    }
}