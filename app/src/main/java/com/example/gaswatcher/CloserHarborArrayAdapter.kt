package com.example.gaswatcher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.NonNull

class CloserHarborArrayAdapter(context : Context, private val closerHarborList : ArrayList<Harbor>) : ArrayAdapter<String>(context, R.layout.closer_harbor_row) {
    private lateinit var harborNom: TextView
    private lateinit var harborGazole: TextView
    private lateinit var harborSp98: TextView
    private lateinit var harborJour: TextView

    override fun getCount(): Int {
        return closerHarborList.size
    }

    @NonNull
    override fun getItem(position: Int): String? {
        return position.toString()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.closer_harbor_row, parent, false)
        //**** recuperation des elements du layout
        harborNom = convertView.findViewById(R.id.tv_harborName)
        harborGazole = convertView.findViewById(R.id.tv_gazole)
        harborSp98 = convertView.findViewById(R.id.tv_sp98)
        harborJour = convertView.findViewById(R.id.tv_date)
        //**** affectation des elements du layout
        harborNom.text = closerHarborList[position].harborNom
        harborGazole.text = "Gazole : " + closerHarborList[position].harborGazole
        harborSp98.text = "SP98 : " + closerHarborList[position].harborSp98
        harborJour.text = "Date : "+closerHarborList[position].harborJour
        return convertView
    }

}

