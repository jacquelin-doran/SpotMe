package edu.gvsu.cis.spotme2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import java.lang.Exception

class AdvertisingActivity : AppCompatActivity() {
    private val STRATEGY = Strategy.P2P_CLUSTER


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertising)
        //startAdvertising()
        //startDiscovery()

        val advertise = findViewById<Button>(R.id.advertiseButton)

        advertise.setOnClickListener { v ->
            startAdvertising()
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
                Log.v("Nearby", "addOnSuccessListener")
            }
            .addOnFailureListener { a: Exception? ->
                Log.v("Nearby", "addOnFailureListener")
            }
    }

//    private fun startDiscovery() {
//        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
//        Nearby.getConnectionsClient(this)
//            .startDiscovery(localClassName, , discoveryOptions)
//            .addOnSuccessListener { a: Void? ->
//                Log.v("Nearby", "addOnSuccessDiscovery")
//            }
//            .addOnFailureListener { a: Exception? ->
//                Log.v("Nearby", "addOnFailureListener")
//            }
//    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(p0: String, p1: ConnectionInfo) {
            Log.v("Nearby", "onConnectionInitiated")
        }

        override fun onConnectionResult(p0: String, p1: ConnectionResolution) {
            Log.v("Nearby", "onConnectionResults")
        }

        override fun onDisconnected(p0: String) {
            Log.v("Nearby", "onDisconnected")
        }
    }

//    private val EndpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
//        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
//            // An endpoint was found. We request a connection to it.
//            //debug("mEndpointDiscoveryCallback.onEndpointFound $endpointId:$info:${info.endpointName}")
////
////            mConnectionsClient.requestConnection(
////                getNickName(),
////                endpointId,
////                mConnectionLifecycleCallback
////            )
//                .addOnSuccessListener {
//                    // We successfully requested a connection. Now both sides
//                    // must accept before the connection is established.
//                    //debug("Success requestConnection: $it")
//                }
//                .addOnFailureListener {
//                    // Nearby Connections failed to request the connection.
//                    // TODO: retry
//                    //debug("Failure requestConnection: $it")
//                }
//        }
//    }
}
