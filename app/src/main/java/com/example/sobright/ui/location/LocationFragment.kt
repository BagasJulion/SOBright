package com.example.sobright.ui.location


import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.sobright.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class LocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var searchView: SearchView
    private var lastClickedMarker: Marker? = null

    // Data class to represent location information
    data class LocationData(val name: String, val position: LatLng, val address: String)

    private val locations: List<LocationData> = listOf(
        LocationData("Special Region of Yogyakarta", LatLng(-7.79918, 110.37200), "Special Region of Yogyakarta"),
        LocationData("Surakarta", LatLng(-7.757591, 110.82338), "Surakarta"),
        LocationData("Kebumen", LatLng(-7.67097, 109.65740), "Kebumen")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        searchView = view.findViewById(R.id.searchView)
        setupSearchView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnPoiClickListener { pointofInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointofInterest.latLng)
                    .title(pointofInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            poiMarker?.showInfoWindow()
        }
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val locationName = marker.title
        Toast.makeText(requireContext(), "Sending data from $locationName", Toast.LENGTH_SHORT).show()

        if (marker == lastClickedMarker) {
            lastClickedMarker = null
        } else {
            lastClickedMarker = marker
        }
        return true
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        val filteredLocations = locations.filter {
            it.name.contains(query, true) || it.address.contains(query, true)
        }

        filteredLocations.forEach { location ->
            addMarker(location.name, location.position, location.address)
        }

        if (filteredLocations.isNotEmpty()) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(filteredLocations.first().position, 12f))
        } else {
            Toast.makeText(requireContext(), "No matching locations found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addMarker(title: String, position: LatLng, snippet: String) {
        val markerOptions = MarkerOptions()
            .position(position)
            .title(title)
            .snippet(snippet)

        when (title) {
            "Special Region of Yogyakarta" -> markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            "Surakarta" -> markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            "Kebumen" -> markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        }

        mMap.addMarker(markerOptions)
    }
}