package edu.gvsu.cis.spotme2

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.content.Context
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
import javax.sql.ConnectionEventListener

class MainActivity : AppCompatActivity() {
    private val PERMISSION_CODE = 1
    private val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    private val STRATEGY = Strategy.P2P_CLUSTER

        private val REQUIRED_PERMISSIONS = arrayOf(
        "android.permission.ACCESS_WIFI_STATE",
        "android.permission.CHANGE_WIFI_STATE",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.BLUETOOTH_ADVERTISE",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.BLUETOOTH_SCAN",
        "android.permission.READ_EXTERNAL_STORAGE"
    )
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

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
            for (permission in REQUIRED_PERMISSIONS){
                checkPermission(permission)
            }
//            if(allPermissionsGranted()){
//               println("Permissions Ok")
//            } else {
//                Log.v("Nearby Connections", "No permissions")
//            }
            println("Button pressed")
              val intent = Intent(this@MainActivity, WorkoutActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        promptEnableBluetooth()
    }

    @SuppressLint("MissingPermission")
    private fun promptEnableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            ENABLE_BLUETOOTH_REQUEST_CODE -> {
                if (resultCode != Activity.RESULT_OK){
                    promptEnableBluetooth()
                }
            }
        }
    }
//    private fun allPermissionsGranted(): Boolean{
//        for(permission in REQUIRED_PERMISSIONS){
//            if(ContextCompat.checkSelfPermission(
//                    this, permission) != PackageManager.PERMISSION_GRANTED)
//            {
//                requestStoragePermission(permission)
//            }
//        }
//        return true
//    }

    fun checkPermission(permission: String){
        //Always comes out to permission granted
        when {
            ContextCompat.checkSelfPermission(this, permission)
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
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun requestStoragePermission(permission: String) {
        val positivButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(
                applicationContext,
                "Yes", Toast.LENGTH_SHORT
            ).show()
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(
                applicationContext,
                "No", Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("This permission is needed because I said so")
                .setPositiveButton(
                    "Ok",
                    DialogInterface.OnClickListener(function = positivButtonClick)
                )
                .setNegativeButton("Cancel", negativeButtonClick)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission), PERMISSION_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
            } else {
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
