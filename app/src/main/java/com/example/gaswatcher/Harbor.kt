package com.example.gaswatcher

import org.json.JSONObject

data class Harbor(
    var harborId: String = "",
    var harborNom: String = "",
    var harborLat: String = "",
    var harborLon: String = "",
    var harborGazole: String = "",
    var harborSp98: String = "",
    var harborJour: String = ""
)