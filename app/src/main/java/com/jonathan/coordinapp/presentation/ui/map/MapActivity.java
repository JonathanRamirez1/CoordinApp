package com.jonathan.coordinapp.presentation.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jonathan.coordinapp.R;

import dagger.android.support.DaggerAppCompatActivity;

public class MapActivity extends DaggerAppCompatActivity implements OnMapReadyCallback {

    private static final int REQ_LOC = 10;
    private GoogleMap gMap;
    private double latDest, lngDest;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_map);

        latDest = getIntent().getDoubleExtra("lat", 0);
        lngDest = getIntent().getDoubleExtra("lng", 0);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(bundle);
        mapView.getMapAsync(this);
        ImageButton close = findViewById(R.id.btnClose);
        close.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        LatLng dest = new LatLng(latDest, lngDest);
        gMap.addMarker(new MarkerOptions().position(dest).title("Destino"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 15f));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOC);
        } else {
            drawLineToCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOC && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            drawLineToCurrentLocation();
        }
    }

    private void drawLineToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOC
            );
            return;
        }

        FusedLocationProviderClient loc = LocationServices.getFusedLocationProviderClient(this);
        loc.getLastLocation().addOnSuccessListener(last -> {
            if (last != null) {
                plotAndLine(last);
            } else {
                loc.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        null
                ).addOnSuccessListener(this::plotAndLine);
            }
        });
    }

    private void plotAndLine(Location myLoc) {
        if (myLoc == null || gMap == null) return;

        LatLng me = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
        gMap.addMarker(new MarkerOptions()
                .position(me)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Mi posición"));

        LatLng dest = new LatLng(latDest, lngDest);

        gMap.addPolyline(new PolylineOptions()
                .add(me, dest)
                .color(ContextCompat.getColor(this, R.color.blueCoordinadora))
                .width(6f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
