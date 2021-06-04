package com.example.gaswatcher

import android.content.Context
import android.location.Location
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_watcher.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.security.auth.callback.Callback

class HarborManager {

    var harborList : ArrayList<Harbor> = ArrayList<Harbor>()

    interface Callback {
        fun onSuccess(result: Boolean?)
    }

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

    private fun setHarborPrice(harborObject : Harbor, harborGazole : String, harborSp98 : String, harborJour : String) {
        harborObject.harborGazole = harborGazole
        harborObject.harborSp98 = harborSp98
        harborObject.harborJour = harborJour
    }


    fun getPriceDatas (context: Context, harbor_id: String, closeHarbor : Harbor) : Harbor{
        val queue = Volley.newRequestQueue(context)
        val url: String = "https://www.teleobjet.fr/Ports/portprix.php/"+harbor_id
        Log.d("API", "on a la queue et la string prix")
        Log.d("API", "url : "+url)
        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d("API", "recuperation des donnees prix")
                val obj: JSONObject = JSONObject(response)
                Log.d("API", obj.getString("id"))
                setHarborPrice(
                    closeHarbor,
                    obj.getString("gazole"),
                    obj.getString("sp98"),
                    obj.getString("jour")
                )
                Log.d("API", "le gazole : "+closeHarbor.harborGazole.toString())
               // param.onSuccess(true)

            },
            Response.ErrorListener {
                Log.d("API", "erreur")
            })
        queue.add(stringReq)

        return closeHarbor
    }

    fun getCloserHarborList(context : Context, myPosition : Location, harborList : ArrayList<Harbor>, param: Callback) : ArrayList<Harbor> {
        var closerHarborList : ArrayList<Harbor> = ArrayList<Harbor>()
        var result : FloatArray = FloatArray(1)
        for(i in 0 until harborList.size) {
            var closeHarbor = harborList[i]
            Location.distanceBetween(myPosition.latitude, myPosition.longitude,
                closeHarbor.harborLat.toDouble(), closeHarbor.harborLon.toDouble(), result)
            if (result.first() * 0.001 < 16) {

               closeHarbor = getPriceDatas(context, closeHarbor.harborId, closeHarbor)
                closerHarborList.add(closeHarbor)

            }
        }
        param.onSuccess(true)
        Log.d("API", "liste closer : "+closerHarborList.size.toString())
        return closerHarborList
    }




    fun addHarbor() : Boolean {

        //TODO

        return true
    }


}