package com.example.vennApp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.vennApp.model.Venn
import com.example.oving3.ui.theme.Oving3Theme

/**
 * Viser en liste over venner, hvor en kan lage nye venner og endre gamle
 */
class MainActivity : ComponentActivity() {
    private var venner : MutableList<Venn> = listOf(
        Venn("Per", "Nako", "01/01/2000"),
        Venn("Taro", "Ned", "17/05/1995"),
        Venn("Lako", "Imba", "20/06/1998"),
        Venn("Tata", "Ti", "11/11/2001"),
        Venn("Novi", "Ma", "05/03/1999")
    ).sortedBy { it.fornavn }.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Oving3Theme {
                VennListeCompose(venner)
            }
        }
    }
}
@Composable
fun VennListeCompose(venner: MutableList<Venn>) {
    val vennListe = remember { venner }
    var adapter : VennAdapter? = remember { null }
    val context = LocalContext.current
    val activityForResultStarter = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data = result.data!!
            val pos = data.getIntExtra("pos", -1)
            val fornavn = data.getStringExtra("fornavn") ?: ""
            val etternavn = data.getStringExtra("etternavn") ?: ""
            val fodselsdato = data.getStringExtra("fodselsdato") ?: ""

            //Intent har en positiv posisjon, ettersom negativ posisjon ikke gir mmening
            //Dette skjer kun hvis en endrer en allerede eksisterende venn
            if (pos >= 0) {
                vennListe[pos].fornavn = fornavn
                vennListe[pos].etternavn = etternavn
                vennListe[pos].fodselsdato = fodselsdato

            }
            //Betyr at en ikke fikk tilbake noe posisjon, gjennom intent - er default satt lik -1
            //Når dette skjer, så legger en til en ny venn (som enda ikke har posisjon)
            else if(pos == -1) {
                vennListe.add(Venn(fornavn, etternavn, fodselsdato))

            }
            else {
                throw IllegalStateException("Error in comminication between activites")
            }
            vennListe.sortBy { it.fornavn }
            adapter?.notifyDataSetChanged()
        }
    }

    // Adapter husket via remember, initialiseres her
     adapter = VennAdapter(
            context,
            vennListe,
            onEdit = { venn, pos ->
                val intent = Intent(context, RedigerVennActivity::class.java)
                intent.putExtra("pos", pos)
                intent.putExtra("fornavn", venn.fornavn)
                intent.putExtra("etternavn", venn.etternavn)
                intent.putExtra("fodselsdato", venn.fodselsdato)
                activityForResultStarter.launch(intent)
            },
            onDelete = { _, pos ->
                vennListe.removeAt(pos)
                adapter?.notifyDataSetChanged()

            }
        )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 17.dp)
    ) {
        Text(text = "Mine venner", fontSize = 38.sp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Her kan en holde oversikt over venners bursdag!",
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val intent = Intent(context, NyVennActivity::class.java)
                activityForResultStarter.launch(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFA5D6A7),
                contentColor = Color.Black
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Legg til venn")
                Text(text = "Legg til venn", fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Liste over venner", fontSize = 24.sp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))

        AndroidView(
            factory = { context ->
                ListView(context).apply {
                    this.adapter = adapter
                }
            },
            modifier = Modifier.fillMaxSize().clipToBounds()
        )
    }
}
