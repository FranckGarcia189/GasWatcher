package com.example.gaswatcher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE

import android.widget.Button
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Error
import java.lang.System.exit
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var username : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButton()
    }

    fun initButton() {
        // Create account button
        frame_account.visibility = INVISIBLE
        load_create_account.setOnClickListener {
            layout_connect.visibility = INVISIBLE
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_account, CreateAccount.newInstance(), "CreateAccount")
                .commit()
            frame_account.visibility = VISIBLE
        }

        // Connection Button
        btn_connect.setOnClickListener() {
            if (et_username.text.toString() != "" || et_password.text.toString() != "" ) {
                var check : Boolean = checkUser(et_username.text.toString(), et_password.text.toString())
                if (check) {
                    val intentWatcher = Intent(this, WatcherActivity::class.java).apply {
                        putExtra("username", username)
                    }
                    startActivity(intentWatcher)
                }
            }
            else {
                tv_error_connect.text = getString(R.string.miss_data)
                tv_error_connect.visibility = VISIBLE
            }
        }
    }

    fun checkUser(pseudo : String, password : String) : Boolean {
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://www.teleobjet.fr/Ports/user.php?pseudo="+pseudo+"&password="+password+""
        // Request a string response from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    tv_error_connect.visibility = INVISIBLE
                },
                Response.ErrorListener { error ->
                    if (error.networkResponse.statusCode == 404) {
                        tv_error_connect.text = getString(R.string.error_connect)
                        tv_error_connect.visibility = VISIBLE
                    }
                    Log.d("API", error.networkResponse.statusCode.toString())
                })
        queue.add(stringReq)
        return stringReq.toString() != ""
    }




}