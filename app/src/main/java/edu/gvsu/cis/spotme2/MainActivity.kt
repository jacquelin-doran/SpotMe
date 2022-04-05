package edu.gvsu.cis.spotme2

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Strategy

class MainActivity : AppCompatActivity() {

    private val STRATEGY = Strategy.P2P_CLUSTER
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                //permission is granted
                println(0)
            } else {
                println(1)
                //explain
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        println(resultCode)

        val bluetooth = findViewById<Button>(R.id.permissions)

        bluetooth.setOnClickListener { v ->
            checkPermission()
            println("Button pressed")
        }
    }

    fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED -> {
                //use api that requires permission
                //enable bluetooth()?
                println("performAction")
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
            }
            explainPermissions() -> {

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
            }
        }

    }


    fun explainPermissions(): Boolean {
        println("Allow Permissions")
        val accepted = true
        return accepted
    }
}