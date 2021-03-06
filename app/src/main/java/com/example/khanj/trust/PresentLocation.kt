package com.example.khanj.trust

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class PresentLocation : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    internal var mRootRef = FirebaseDatabase.getInstance().reference
    internal var mConditionRef = mRootRef.child("test")
    internal var mchild1Ref: DatabaseReference=mConditionRef.child("경도")
    internal var mchild2Ref: DatabaseReference=mConditionRef.child("위도")


    var longitude:Double = 0.0
    var latitude:Double = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_present_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    override fun onStart() {
        super.onStart()

        mchild1Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var datas = dataSnapshot.getValue().toString()
                latitude= datas.toDouble()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        mchild2Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var datas = dataSnapshot.getValue().toString()
                longitude= datas.toDouble()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val x=LatLng(37.6007195267265,126.86528900355972)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x,14.toFloat()))
        Handler().postDelayed({
            val sydney = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(sydney).title("현재 위치"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15.toFloat()))
        }, 1500)
        // Add a marker in Sydney and move the camera

    }
    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }


}
