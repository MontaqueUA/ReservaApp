package com.example.reservabuses

import android.content.Context
import com.google.android.gms.location.LocationServices

class Geofence private constructor(context: Context) {
    private val geofencingClient = LocationServices.getGeofencingClient(context)

}