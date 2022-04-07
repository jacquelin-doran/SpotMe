package edu.gvsu.cis.spotme2

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Strategy

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

        bluetooth.setOnClickListener { v ->
            checkPermission()
            println("Button pressed")
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
}

    fun explainPermissions(): Boolean {
        println("Allow Permissions")
        val accepted = true
        return accepted
    }
