package com.example.master.iovadmin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import model.LocationObj;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference announRef = database.child("announcement");
    TextView textview;
    private GoogleMap mMap;
    static String TAG = "mainactivity";
    private SupportMapFragment mapFragment;
    //MArker
    private ArrayList<Marker> marker_list = new ArrayList<Marker>();
    private ChildEventListener markerListener;
    private Query locRef;
    private boolean zoom = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showMarkers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeMarkers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_exit:
                finish();
                break;
            case R.id.menu_list:
                startDetailActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startDetailActivity() {
        Intent detail = new Intent(this,ListActivity.class);
        startActivity(detail);
    }

    private void showMarkers() {
        String gKey = "location_data/pteF8V9oxhaspHMmKwm0M1kM7NE3";
        locRef = database.child(gKey);
        markerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LocationObj mLocObj = dataSnapshot.getValue(LocationObj.class);
                if(mLocObj != null){
                    updateMarker(mLocObj);}


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        locRef.addChildEventListener(markerListener);
    }

    private void updateMarker(LocationObj mLastlocationObj) {
        Log.d(TAG, "updateMarker: "+marker_list.size());
        if(mLastlocationObj.getmLastTime() != null)
        {   String vehicleNum = mLastlocationObj.getmVehicleNum();
            int pos = 0;
            boolean test = false;
            for (int i = 0; i< marker_list.size(); i++) {
                Marker check = marker_list.get(i);
                test = check.getTitle().equals(vehicleNum);
                if(test){
                    Log.d(TAG, "updateMarker: ");
                    pos = i;
                    break;
                }
            }
            if(!test){
                setMarker(mLastlocationObj);
            }
            else{
                LatLng position = new LatLng(mLastlocationObj.getmLatitude(),mLastlocationObj.getmLongitude());
                marker_list.get(pos).setPosition(position);
                marker_list.get(pos).setSnippet(mLastlocationObj.getmLastTime());
                Log.d(TAG, "updatedMarker: "+marker_list.get(pos).getTitle());
            }
        }
    }

    private void setMarker(LocationObj mLastlocationObj) {
        Log.d(TAG, "setMarker: ");
        LatLng position = new LatLng(mLastlocationObj.getmLatitude(),mLastlocationObj.getmLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(mLastlocationObj.getmLastTime())///to change
                .snippet(mLastlocationObj.getmLastTime()));
       /* if(mLastlocationObj.getmVehicleNum().equals(mFbUser.getDisplayName())){
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.userm));
            if(zoom){
                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(position , 9) );
                zoom = false;}
        }
        else {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.roundm));
        }*/
        Log.d(TAG, "setMarker: "+ marker.getTitle());
        marker_list.add(marker);
    }

    private void removeMarkers() {
        for (Marker marker: marker_list) {
            marker.remove();
        }
        marker_list.clear();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void setUpMapIfNeeded() {

        try {
            if (mMap == null) {

                Log.d(TAG, "setUpMapIfNeeded");

                mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapview);
                mapFragment.getMapAsync(this);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
