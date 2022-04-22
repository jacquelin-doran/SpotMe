package edu.gvsu.cis.spotme2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import java.lang.Exception
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.sql.Connection
import kotlin.text.Charsets.UTF_8

class AdvertisingActivity : AppCompatActivity() {
    private val STRATEGY = Strategy.P2P_CLUSTER
    var ITEMS = ArrayList<String>()
    var remoteEndpointId: String = ""
    lateinit var connectionsClient: ConnectionsClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertising)


        val plan = findViewById<TextView>(R.id.textView4)

        if(intent.hasExtra("Plans")){
            plan.text = intent.getStringExtra("Plans")
            var item = intent.getStringExtra("Plans")
            item = "$item"
            ITEMS.add(item)
        }

        val advertise = findViewById<Button>(R.id.advertiseButton)

        advertise.setOnClickListener { v ->
            requestFineLocationPermission()
            startAdvertising()
            startDiscovery()
        }

    }

    private fun requestFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            Log.i("info", "No fine location permissions")

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }
    private fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        Nearby.getConnectionsClient(this)
            .startAdvertising(
                localClassName, "edu.gvsu.cis.spotme2",
                connectionLifecycleCallback, advertisingOptions
            )
            .addOnSuccessListener { a: Void? ->
                Log.v("Nearby ADVERTISE", "addOnSuccessListener")
            }
            .addOnFailureListener { a: Exception? ->
                Log.v("Nearby ADVERTISE", "addOnFailureListener")
            }
    }

    private fun startDiscovery() {
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        Nearby.getConnectionsClient(this)
            .startDiscovery("edu.gvsu.cis.spotme2", endpointDiscoveryCallback, discoveryOptions)
            .addOnSuccessListener { a: Void? ->
                Log.v("Nearby", "addOnSuccessDiscovery")
            }
            .addOnFailureListener { a: Exception? ->
                Log.v("Nearby", "addOnFailureListener")
            }
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            //Automatically accept the connection on both sides.
            Nearby.getConnectionsClient(this@AdvertisingActivity).acceptConnection(endpointId, payloadCallback)
            println("Nearby onConnectionInitiated")
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            Log.v("Nearby", "onConnectionResults")
            when(result.status.statusCode){
                ConnectionsStatusCodes.STATUS_OK -> {
                    //we're connected!
                    remoteEndpointId = endpointId
                    Log.v("Connection Status", "Success")
                    sendString("Hello")

                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    //connection rejected

                    Log.v("Connection Status", "Rejected")
                }
                ConnectionsStatusCodes.STATUS_ERROR -> {
                    Log.v("Connection Status", "Error")
                }
            }
        }

        override fun onDisconnected(p0: String) {
            Log.v("Nearby", "onDisconnected")
        }
    }

    private fun sendString(content: String){
        connectionsClient.sendPayload(remoteEndpointId,
        Payload.fromBytes(content.toByteArray()))
    }
    private val payloadCallback = object : PayloadCallback(){
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            debug("payloadCallback.onPayloadRecieved $endpointId")

            when(payload.type){
                Payload.Type.BYTES ->{
                    val data = payload.asBytes()!!
                    debug("Payload.Type.Bytes: $data")
                }
//                Payload.Type.FILE -> {
//                    val file = payload.asFile()!!
//                    debug("Payload.Type.File : $file")
//                }
//                Payload.Type.STREAM -> {
//                    val stream = payload.asStream()!!
//                    debug("Payload.Type.STREAM : $stream")
//                }
            }
        }

        override fun onPayloadTransferUpdate(endpoinId: String, update: PayloadTransferUpdate) {
            //Bytes payloads are sent as a single chunck
            //after the call to onPayloadRecieved
        }
    }
    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            // An endpoint was found. We request a connection to it.
                Nearby.getConnectionsClient(this@AdvertisingActivity)
                 .requestConnection(localClassName, endpointId, connectionLifecycleCallback)
                 .addOnSuccessListener { a: Void? ->
                    // We successfully requested a connection. Now both sides
                    // must accept before the connection is established.
                    //debug("Success requestConnection: $it")
                }
                .addOnFailureListener { a: Exception? ->
                    // Nearby Connections failed to request the connection.
                    // TODO: retry
                    //debug("Failure requestConnection: $it")
                }
        }

        override fun onEndpointLost(p0: String) {
            TODO("Not yet implemented")
        }
    }

    private fun debug(message :String){
        Log.v("",message)
    }
}
