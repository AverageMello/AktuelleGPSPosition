package com.example.test123

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    val looperThread = LooperProzess()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var sollUebertragen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        looperThread.start()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<Button>(R.id.starten).setOnClickListener {
            sollUebertragen = true

            looperThread.handler!!.post {
                while (sollUebertragen){
                    positionBestimmen()
                    SystemClock.sleep(5000)
                    Log.d(TAG, "run: hi")
                }
            }
        }

        findViewById<Button>(R.id.stoppen).setOnClickListener{
            sollUebertragen = false
        }

    }


    private fun positionBestimmen() {
        //Berechtigung abfragen
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
           ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
           return
        }

        fusedLocationProviderClient.getCurrentLocation(100,null)
        val aufgabe = fusedLocationProviderClient.lastLocation

        aufgabe.addOnSuccessListener {
            if(it != null){
                findViewById<TextView>(R.id.breitengrad).text =  it.latitude.toString()
                findViewById<TextView>(R.id.laengengrad).text =  it.longitude.toString()
            }
        }
    }

}

class LooperProzess : Thread(){

    var looper: Looper? = null
    var handler: Handler? = null

    override fun run() {
        Looper.prepare()

        handler = Handler()

        looper = Looper.myLooper()

        Looper.loop()
    }
}