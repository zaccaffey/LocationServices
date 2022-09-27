package com.example.locationservices

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var MY_PERMISSIONS_REQUEST_LOCATION = 123;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    @SuppressLint("MissingPermission")
    fun getLocation(view: View) {
        // request the location permissions
        requestLocationPermission()


        // get the last location and log the lat and long
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.i(TAG, "latitude=" + location?.latitude + "--longitude=" + location?.longitude)

            // use the geocoder to get the address from the lat and long values
            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
            var addresses = geocoder.getFromLocation(location?.latitude!!, location?.longitude!!, 1)

            Log.i("address: ", addresses.get(0).getAddressLine(0))

            Toast.makeText(this, addresses.get(0).featureName, Toast.LENGTH_SHORT).show()

        }
    }

    private fun requestLocationPermission() {
        Log.d("Request Perms", "Setting Permissions")

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("Request code", "$requestCode")
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d("Granted", "Permission Granted!")
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->
                                if (location != null) {
                                    Log.d(
                                        "Location",
                                        "${location.latitude}--${location.longitude}"
                                    )

                                    // use your location object
                                    // get latitude , longitude and other info from this
                                    /*Toast.makeText(
                                        this,
                                        "${location.latitude}--${location.longitude}",
                                        Toast.LENGTH_LONG
                                    ).show()*/
                                }
                            }
                    } else {
                        Log.d("Not granted", "Permission was not granted")
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Denied", "Permission Denied")

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}