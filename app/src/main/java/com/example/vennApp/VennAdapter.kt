package com.example.vennApp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.vennApp.model.Venn


class VennAdapter(
    private val context: Context,
    private val venner: MutableList<Venn>,
    private val onEdit: (Venn, Int) -> Unit,
    private val onDelete: (Venn, Int) -> Unit
) : BaseAdapter() {

    private val bakgrunnsfarger = listOf(
        Color.parseColor("#A5D6A7"),
        Color.parseColor("#81D4FA")
    )

    override fun getCount(): Int = venner.size

    override fun getItem(position: Int): Any = venner[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.venn_item, parent, false)

        val venn = venner[position]

        val container = view as LinearLayout
        container.setBackgroundColor(bakgrunnsfarger[position % bakgrunnsfarger.size])


        val textNavn = view.findViewById<TextView>(R.id.textNavn)
        val textDato = view.findViewById<TextView>(R.id.textDato)
        val btnEdit = view.findViewById<Button>(R.id.buttonEdit)
        val btnDelete = view.findViewById<Button>(R.id.buttonDelete)


        textNavn.text = "${venn.fornavn} ${venn.etternavn}"
        textDato.text = venn.fodselsdato


        btnEdit.setOnClickListener {
            onEdit(venn, position)
        }

        btnDelete.setOnClickListener {
            onDelete(venn, position)
        }

        return view
    }
}
