package com.example.gaswatcher

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_watcher.*


class WatcherActivity : AppCompatActivity() {

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var harborManager : HarborManager

    interface ServerCallback {
        fun onSuccess(result: Boolean?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watcher)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        harborManager = HarborManager()

        harborManager.initHarborList(applicationContext,
            object : ServerCallback {
                override fun onSuccess(result: Boolean?) {
                    initHarborSpinner(spin_harbor, harborManager.harborList)
                }
            }
        )

        Toast.makeText(this,R.string.welcomeback, Toast.LENGTH_SHORT).show()

        btn_my_position.setOnClickListener() {
            getLocationUser(mFusedLocationClient)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationUser(mFusedLocationClient:FusedLocationProviderClient ) {
        if (checkPermissions()) {
            mFusedLocationClient.lastLocation.addOnCompleteListener {
                    val location : Location? = it.result
                    if (location == null) {
                        requestLocationData {
                            tv_position.text = "Ma position"
                            tv_my_position.text = "lat : "+it.latitude.toString() + " | lon : " + it.longitude.toString()
                        }
                        Log.d("API", "No location")
                    } else {
                        tv_position.text = "Ma position"
                        tv_my_position.text = "lat : "+location.latitude.toString() + " | lon : " + location.longitude.toString()
                        Log.d("API", "Location")
                    }
            }
            Log.d("API", "ok permission")

        } else {
            Toast.makeText(this, "Autoriser l'application à accéder à votre localisation", Toast.LENGTH_LONG).show()
            val intent : Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationData(onLocationResult: (Location) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
        }
        val locationCallback: LocationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult)
            {
                val location: Location = locationResult.lastLocation
                onLocationResult.invoke(location)
                mFusedLocationClient.removeLocationUpdates(this)
            }
        }
        if (checkPermissions()) {
            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()!!
            )
        }
    }


    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }



    private fun initHarborSpinner(spinner : Spinner, harborList : ArrayList<Harbor>) {
        var itemList : ArrayList<String> = ArrayList<String>()
        itemList.add("Choisir")
        if (spinner != null) {
            for (i in 0 until harborList.size) {
                var currentHarbor : Harbor = harborList[i]
                itemList.add(""+currentHarbor.harborNom)
            }
            Log.d("API", "remplir le spinner")
            val adapter = ArrayAdapter (this,
                android.R.layout.simple_spinner_item, itemList)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (spin_harbor.selectedItem.toString() != "Choisir"){
                        tv_position.text = "Position : "+spin_harbor.selectedItem.toString()
                        var harborPosition : Harbor? = harborManager.harborList.find { it.harborNom == spin_harbor.selectedItem.toString() }
                        if (harborPosition != null) {
                            tv_my_position.text = "lat : "+harborPosition.harborLat+" | lon : "+harborPosition.harborLon
                        }
                    }
                }

            }
        }
    }

}
