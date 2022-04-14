package edu.gvsu.cis.spotme2

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val PERMISSION_CODE = 1
    private val STRATEGY = Strategy.P2P_CLUSTER
//    val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission())
//        { isGranted: Boolean ->
//            if (isGranted) {
//                //permission is granted
//                println(0)
//            } else {
//                println(1)
//                //explain
//            }
//        }
    val positivButtonClick = { dialog: DialogInterface, which: Int ->
    Toast.makeText(applicationContext,
        "Yes", Toast.LENGTH_SHORT).show()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
         "No", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        println(resultCode)

        val bluetooth = findViewById<Button>(R.id.permissions)
        val start = findViewById<Button>(R.id.start)

        bluetooth.setOnClickListener { v ->
            checkPermission()
            println("Button pressed")
        }
        start.setOnClickListener { v ->
            val intent = Intent(this@MainActivity, WorkoutActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
            == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "You have already granted this permission", Toast.LENGTH_SHORT).show()
            requestStoragePermission()
        }
                //use api that requires permission
                //enable bluetooth()?
//                println("performAction")
//                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
//            explainPermissions() -> {

            else {
                requestStoragePermission()
            //requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
            }
        }
    private fun requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("This permission is needed because I said so")
                .setPositiveButton("Ok", DialogInterface.OnClickListener(function = positivButtonClick))
                .setNegativeButton("Cancel", negativeButtonClick)
        }
        else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
        }
     }
    private fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        Nearby.getConnectionsClient(this)
            .startAdvertising(localClassName, "edu.gvsu.cis.spotme2",
                connectionLifecycleCallback, advertisingOptions)
            .addOnSuccessListener {
                a: Void? ->  Log.v("Nearby", "addOnSuccessListener")
            }
        .addOnFailureListener{
            a : Exception? -> Log.v("Nearby", "addOnFailureListener")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_CODE){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
            }
        }
    }
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback(){
        override fun onConnectionInitiated(p0: String, p1: ConnectionInfo) {
            Log.v("Nearby", "onConnectionIniated")
        }

        override fun onConnectionResult(p0: String, p1: ConnectionResolution) {
            Log.v("Nearby", "onConnectionResults")
        }

        override fun onDisconnected(p0: String) {
            Log.v("Nearby", "onDisconnected")
        }
    }
}


    fun explainPermissions(): Boolean {
        println("Allow Permissions")
        val accepted = true
        return accepted
    }