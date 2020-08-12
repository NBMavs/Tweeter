package com.example.tweeter_v1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

class ViewMapActivity: Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var Location: String
    private lateinit var client2: GoogleApiClient
    private var Latitude = 0.00
    private var Longitude = 0.00


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.view_map, container, false)
        // Gets the MapView from the XML layout and creates it
        mapView = v.findViewById(R.id.mapview) as MapView
        mapView.onCreate(savedInstanceState)
        // Gets to GoogleMap from the MapView and does initialization stuff
        //map = mapView.getMapAsync()
        map.getUiSettings().setMyLocationButtonEnabled(false)
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        map.setMyLocationEnabled(true)
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try
        {
            MapsInitializer.initialize(this.getActivity())
        }
        catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
        // Updates the location and zoom of the MapView
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(43.1, -87.9), 10F)
        map.animateCamera(cameraUpdate)
        return v
    }
    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(p0: GoogleMap?) {
        TODO("Not yet implemented")
    }
}