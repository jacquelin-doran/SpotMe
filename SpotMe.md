
# Using The Nearby Connections API To Connect Devices Through Bluetooth
## Overview
Nearby Connections at its' heart is a peer-to-peer networking API that provides functionality for discovering, connecting, and exchanging data with nearby devices, all in real-time. Connections using Nearby Connections are high-bandwidth, low-latency, and fully encrypted meaning the connection is fast and secure.

Nearby Connections utilizes a combination of Bluetooth, BLE, and Wifi hotspots, supporting the strengths of each while mitigating their respective weaknesses. What this means is, this platform is performant and broad so that you can focus on the features that matter the most for your implementation.

Nearby Connections can be used to create:
* Collaborative Whiteboard: Create a shared virtual whiteboard for you and nearby participants to work collaboratively.
* Local Multiplayer Gaming: Utilize peer-to-peer connections to play a game that others nearby can join.
* Multi-Screen Gaming: Using your phone or tablet as a game controller and a screen or projector as the game display. Think Kahoot!
* Offline File Transfer: Share photos, videos, or any other type of data quickly and indepentant of a network connection.

### API Rundown
The Nearby Connections API falls into two phases: pre-connection and post-connection. In the pre-connection phase, the Advertiser (device to be connected) advertises themself as looking for a connection request, while Discoverers (device making the connection) sends a connection request to the advertiser. Connection requests from a Discoverer to and Advertiser initiates a symmetric authentication flow resutling in both sides independently accepting or rejection the connection request. A connect is established once both sides accept the connection request.

**Advertising and Discovery**:
On the Advertiser side, begin by invoking the *startAdvertising()* method, which takes a *ConnectionLifecycleCallback* that is notified whenever a Discoverer wants to connect via the *onConnectionInitiated()* callback.
On the Discoverer side, begin by invoking the *startDiscovery()* method, which takes an *EndpointDiscoveryCallback* that is notified whenever a nearby Advertiser is found via the *onEndpointFound()* callback.

**Establishing Connections**:
When the Discoverer wants to make a connection to a nearby Advertiser, the Discoverer invokes *requestConnection()*, passing its own *ConnectionLifecycleCallback*.
Both sides, via the *ConnectionLifecycleCallback.onConnectionInitiated()* callback, are notified of the connection initation process and most now choose whether to accept or deny the connection request via either the *acceptConnection()* and *rejectConnection()* methods.
Apps are able to optionally prompt the user to accept the connection, or have this process done automatically. To alert the user in an *AlertDialog*, use the following override function:
```
@Override
public void onConnectionInitiated(String endpointId, ConnectionInfo info) {
  new AlertDialog.Builder(context)
      .setTitle("Accept connection to " + info.getEndpointName())
      .setMessage("Confirm the code matches on both devices: " + info.getAuthenticationDigits())
      .setPositiveButton(
          "Accept",
          (DialogInterface dialog, int which) ->
              // The user confirmed, so we can accept the connection.
              Nearby.getConnectionsClient(context)
                  .acceptConnection(endpointId, payloadCallback))
      .setNegativeButton(
          android.R.string.cancel,
          (DialogInterface dialog, int which) ->
              // The user canceled, so we should reject the connection.
              Nearby.getConnectionsClient(context).rejectConnection(endpointId))
      .setIcon(android.R.drawable.ic_dialog_alert)
      .show();
}
```
*ConnectionLifecycleCallback.onConnectionResult()* is invoked once both sides have responded to the connection request. Upon acception, the *ConnectionResolution* provided in the callback will return succesful, indicating the connection is considered established, and the transfer of Payloads (data) can begin.

**Exchanging Data**: The connection is now established, so there is no longer need for the Advertiser vs. Discoverer distinction. Both sides can now exchange data via the *Payload* object. *Payload* objects can be:
* *Bytes*: Byte arrays with upper limit size of 32k; used to encapsulate metadata or control messages.
* *File*: Files of size any; transfered from the application to the network interface with minimal file copying across boundaries.
* *Stream*: A stream of any size; usually generated on the fly, as in the case of recorded audio/video.

Send a *Payload* object via the *sendPayload()* invokation. The *sendPayload()* method guarantees in-order delivery, so multiple *sendPayload()* method calls will be queued until the first *Payload* is done.
Receivers can expect the *PayloadCallback.onPayloadReceived()* to be invoked every time an incoming *Payload* is received.
Both the Sender(s) and Receiver(s) in the connection can expect the *PayloadCallback.onPayloadTransferUpdate()* callback to be used for progress updates on both outgoing and incoming *Payloads*, respectively.

**Disconnect From Connection**: To disconnect from the connection, invoke either the *disconnectFromEndpoint()* method which disconnects from a specified enpoint, or the *stopAllEndpoints()* method which disconnects from all connected endpoints. Call *ConnectionLifecycleCallback.onDisconnected()* to notify remote endpoints of the disconnection.

## Getting Started
### What You Will Need
* *[Android Studios: Version 32](https://developer.android.com/studio#downloads)*
* *[Google Play Services SDK](https://developer.android.com/google/play-services/setup.html)*
* *[Nearby Connections API](https://github.com/android/connectivity-samples)*
## Step-By-Step Instructions: Create a File Sharing App Using Nearby Connections
### Request Permissions
We must begin by selecting an appropriate permissions for the *Strategy* we inted to use. We will be using the *P2P_Cluster* Strategy. In order to use this Strategy, Location must be turned on and the app must have the following permissions declared:

    * BLUETOOTH
    * BLUETOOTH_ADMIN
    * ACCESS_WIFI_STATE
    * CHANGE_WIFI_STATE
    * ACCESS_COARSE_LOCATION

On devices running Q (and onwards), FINE_LOCATION is required in place of COARSE location.

    * ACCESS_FINE_LOCATION

Additionally, on devices running S (and onwards), replace BLUETOOTH and BLUETOOTH_ADMIN permissions with the following:

    * BLUETOOTH_ADVERTISE
    * BLUETOOTH_CONNECT
    * BLUETOOTH_SCAN

Add the prior explained permissions to your AndroidManifest.xml as such:
```
<!-- Required for Nearby Connections -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<!-- Only required for apps targeting Android 12 and higher -->
<uses-permission android:name="android.permission.BLUETOOTH_ADVERTISE" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<!-- Optional: only required for FILE payloads -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
To allow the system to manage the request code that's associated with a permissions request, add dependencies on the following libraries in your module's build.gradle file:

   * [androidx.activity](https://developer.android.com/jetpack/androidx/releases/activity#declaring_dependencies), version 1.2.0 or later.
   * [androidx.fragment](https://developer.android.com/jetpack/androidx/releases/fragment#declaring_dependencies), version 1.3.0 or later.

You can then use one of the following classes:

To request a single permission, use [RequestPermission](https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.RequestPermission).
To request multiple permissions at the same time, use [RequestMultiplePermissions](https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.RequestMultiplePermissions).

The following steps show how to use the *RequestPermission* contract. The process is nearly the same for the *RequestMultiplePermissions* contract.

  1. In your activity or fragment's initialization logic, pass in an implementation of [ActivityResultCallback](https://developer.android.com/reference/androidx/activity/result/ActivityResultCallback) into a call to [registerForActivityResult()](https://developer.android.com/reference/androidx/activity/result/ActivityResultCaller#registerForActivityResult(androidx.activity.result.contract.ActivityResultContract%3CI,%20O%3E,%20androidx.activity.result.ActivityResultCallback%3CO%3E)). The ActivityResultCallback defines how your app handles the user's response to the permission request. Keep a reference to the return value of registerForActivityResult(), which is of type [ActivityResultLauncher](https://developer.android.com/reference/androidx/activity/result/ActivityResultLauncher).

  2. To display the system permissions dialog when necessary, call the [launch()](https://developer.android.com/reference/androidx/activity/result/ActivityResultLauncher#launch(I)) method on the instance of ActivityResultLauncher that you saved in the previous step. After launch() is called, the system permissions dialog appears. When the user makes a choice, the system asynchronously invokes your implementation of ActivityResultCallback, which you defined in the previous step.
### Advertise and Discover

## Summary And Resources
