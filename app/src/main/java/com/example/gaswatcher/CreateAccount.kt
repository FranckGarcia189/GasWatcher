package com.example.gaswatcher

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_create_account.view.*
import org.json.JSONObject
import java.sql.Time
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 * Use the [CreateAccount.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccount : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_create_account, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.load_connection.setOnClickListener() {
            view.visibility = INVISIBLE
            activity?.layout_connect?.visibility = VISIBLE
        }

        view.btn_create_account.setOnClickListener() {
            if(checkDatas(et_create_email.text.toString(), et_create_username.text.toString(), et_create_password.text.toString(), et_confirm_password.text.toString())) {
                if (createUser(et_create_email.text.toString(), et_create_username.text.toString(), et_create_password.text.toString())) {
                    val myToast = Toast.makeText(view.context,"Inscription enregistrée !",Toast.LENGTH_SHORT)
                    myToast.show()
                    TimeUnit.SECONDS.sleep(5)
                    this.activity?.recreate()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() : CreateAccount {
            return CreateAccount()
        }

    }

    private fun checkDatas(email : String, pseudo : String, password : String, confirmPassword : String) : Boolean {
        var checkdata = ""
        if (email != "" || pseudo != "" || password != "" || confirmPassword != "" ) {
            if (password == confirmPassword) {
                checkdata = "ok"
            }
        }
        return checkdata != ""

    }

    private fun createUser(email : String, pseudo : String, password : String) : Boolean {
        val queue = Volley.newRequestQueue(this.context)
        val url = "https://www.teleobjet.fr/Ports/user.php"
        var userJsonObject = getUserJsonObject(email, pseudo , password)
        val putRequest = JsonObjectRequest(
            Request.Method.PUT, url, userJsonObject,
                Response.Listener { response ->
                    Log.d("API", "OK!")
                },
                Response.ErrorListener { error ->
                    Log.d("API", error.toString())
                }
            )
        queue.add(putRequest)

        return true
    }

    fun getUserJsonObject(email : String, pseudo : String, password : String) : JSONObject {
        val obj = JSONObject()
        obj.put("pseudo", pseudo)
        obj.put("mailadr",email)
        obj.put("password",password)

        return obj
    }


}