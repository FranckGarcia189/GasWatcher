package com.example.gaswatcher

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class HarborManager {

    var harborList : ArrayList<Harbor> = ArrayList<Harbor>()
    var onComplete : (()-> Unit)? = null

    fun initHarborList(context: Context, param: WatcherActivity.ServerCallback) {
        val queue = Volley.newRequestQueue(context)
        val url: String = "https://www.teleobjet.fr/Ports/port.php"
        Log.d("API", "on a la queue et la string")
        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d("API", "recuperation des donnees")
                val responseList : JSONArray = JSONArray(response)
                for (i in 0 until responseList.length()) {
                    val innerBlock: JSONObject = responseList.getJSONObject(i)
                    Log.d("API", innerBlock.getString("id"))
                    addToHarborList(
                        innerBlock.getString("id"),
                        innerBlock.getString("nom"),
                        innerBlock.getString("lat"),
                        innerBlock.getString("lon")
                    )
                }

               param.onSuccess(true)

            },
            Response.ErrorListener {
                Log.d("API", "erreur")
            })
        queue.add(stringReq)

    }

    fun addToHarborList (id : String, nom : String, lat : String, lon : String) {
        var currentHarbor= Harbor()
        currentHarbor.harborId = id
        currentHarbor.harborNom = nom
        currentHarbor.harborLat = lat
        currentHarbor.harborLon = lon
        currentHarbor.harborGazole = ""
        currentHarbor.harborSp98 = ""
        currentHarbor.harborJour = ""

        Log.d("API", "Ajout de : "+currentHarbor.harborNom)
        harborList.add(currentHarbor)

        Log.d("API", "la liste : "+ harborList.size.toString())


    }

    fun setup()
    {
        //setup your object
        onComplete?.invoke()
    }

    fun completeHarborList () {

    }

    fun addHarbor() : Boolean {

        //TODO

        return true
    }


}