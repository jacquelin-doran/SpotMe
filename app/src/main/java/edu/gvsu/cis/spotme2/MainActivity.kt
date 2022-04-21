package edu.gvsu.cis.spotme2

import android.Manifest
import android.Manifest.permission.*
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val PERMISSION_CODE = 1
    private val STRATEGY = Strategy.P2P_CLUSTER

    private val REQUIRED_PERMISSIONS = arrayOf(
        "android.permission.BLUETOOTH",
        "android.permission.BLUETOOTH_ADMIN",
        "android.permission.ACCESS_WIFI_STATE",
        "android.permission.CHANGE_WIFI_STATE",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.BLUETOOTH_ADVERTISE",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.BLUETOOTH_SCAN",
        "android.permission.READ_EXTERNAL_STORAGE"
    )

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                println("Permission OK")
                //permission is granted
            } else {
                println("Permissions not granted")

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

//            if(allPermissionsGranted()){
//                println("Permissions Ok")
//            } else {
//                Log.v("Nearby Connections", "No permissions")
//                //ActivityCompat.RequestPermission(this,  )
//                requestStoragePermission()
//            }
            println("Button pressed")
            val intent = Intent(this@MainActivity, WorkoutActivity::class.java)
            startActivity(intent)
        }

    }
//    private fun allPermissionsGranted(): Boolean{
//        for(permission in REQUIRED_PERMISSIONS){
//            if(ContextCompat.checkSelfPermission(
//                    this, permission) != PackageManager.PERMISSION_GRANTED)
//            {
//                return false
//            }
//        }
//        return true
//    }

    fun checkPermission() {
        //Always comes out to permission granted
        when {
            ContextCompat.checkSelfPermission(this, BLUETOOTH_ADMIN)
                    == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "You have already granted this permission", Toast.LENGTH_SHORT)
                    .show()
                //requestStoragePermission()
            }
            //use api that requires permission
            //enable bluetooth()?
//                println("performAction")
//                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
//            explainPermissions() -> {

            else -> {
                //requestStoragePermission()
                requestPermissionLauncher.launch(BLUETOOTH_ADMIN)
            }
        }
    }
    private fun requestSomePermission(){
        val positivButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                "Yes", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, arrayOf(BLUETOOTH_ADMIN), 1)
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                "No", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, BLUETOOTH_ADMIN)){
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("This permission is needed because I said so")
                .setPositiveButton("Ok", DialogInterface.OnClickListener(function = positivButtonClick))
                .setNegativeButton("Cancel", negativeButtonClick)
        }
        else {
            ActivityCompat.requestPermissions(this,
                arrayOf(BLUETOOTH), PERMISSION_CODE)
        }
     }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
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

}

    fun explainPermissions(): Boolean {
        println("Allow Permissions")
        val accepted = true
        return accepted
    }
