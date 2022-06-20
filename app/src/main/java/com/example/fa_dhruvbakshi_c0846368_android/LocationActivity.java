package com.example.fa_dhruvbakshi_c0846368_android;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "LocationActivity";
    private GoogleMap mMap;
    public final int REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    LocationCallback locationCallback;
    LocationRequest locationRequest;
    DatabaseActivity DB = new DatabaseActivity(LocationActivity.this);
    String name, desc, con, id;
    TextView update;
    FloatingActionButton home, direction, delete;
    String lati, lng;

    double a, b, lati2, lng2;
    private Marker src, dest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_main);
        home = findViewById(R.id.homeBtn);
        direction = findViewById(R.id.directBtn);
        delete = findViewById(R.id.deleteBtn);
        direction.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);

        update = findViewById(R.id.updateView);
        update.setVisibility(View.INVISIBLE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // initializing the location request
        locationRequest = new LocationRequest();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
        getLocation();


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                startActivity(intent);
            }


        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setTitle(name);
                builder.setMessage("You wanted to Delete this location?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DB.onDelete(id);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();


            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//            Uri uri = Uri.parse("https://www.google.com/maps/dir/"+src.getPosition()+","+dest.getPosition());
//            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//            intent.setPackage("com.google.android.apps.maps");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
                drawLine();
            }

            private void drawLine() {
                PolylineOptions options = new PolylineOptions()
                        .color(Color.GREEN)
                        .add(src.getPosition(), dest.getPosition())
                        .width(6);
                mMap.addPolyline(options);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        mMap = googleMap;

        getIntentData();
        if (!checkPermission())
            requestPermission();
        else
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper());


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                setMarker(latLng);
            }


        });
        mMap.setOnMarkerDragListener(this);


    }


    private void setMarker(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.i(TAG, "onLocationResult: " + addresses.get(0));
                String address = "";
                if (addresses.get(0).getPremises() != null) {
                    name = addresses.get(0).getPremises();
                } else if (addresses.get(0).getThoroughfare() != null) {
                    name = addresses.get(0).getPremises();
                } else {
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    name = formattedDate;
                }



            }

        } catch (IOException e) {
            e.printStackTrace();
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            name = formattedDate;
        }
        if (name == null){
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            name = formattedDate;
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet(desc + ", " + con);
        mMap.addMarker(options);

        DB.addItem(name,latLng.latitude,latLng.longitude);

    }

    private boolean checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }
    }

    private void getLocation() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location: locationResult.getLocations()) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    src = mMap.addMarker(new MarkerOptions().position(userLocation).title("You were here!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));



                }
            }
        };
    }

    void getIntentData()
    {
        if(  (getIntent().hasExtra("id"))&&(getIntent().hasExtra("name"))&&(getIntent().hasExtra("lati")) &&(getIntent().hasExtra("lng"))      )
        {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            lati = getIntent().getStringExtra("lati");
            lng = getIntent().getStringExtra("lng");
            a=Double.parseDouble(lati);
            b=Double.parseDouble(lng);
            LatLng l=new LatLng(a,b);

            setMarker2(l,name);
            direction.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);

        }else{

        }

    }
    private void setMarker2(LatLng latLng,String n) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.i(TAG, "onLocationResult: " + addresses.get(0));
                String address = "";

                if (addresses.get(0).getLocality() != null) {
                    desc = addresses.get(0).getLocality();
                }
                if (addresses.get(0).getCountryName() != null)
                    con = addresses.get(0).getCountryName();

                if (addresses.get(0).getPremises() != null)
                    address += addresses.get(0).getPremises() + " ";
                if (addresses.get(0).getCountryName() != null)
                    address += addresses.get(0).getCountryName() + " ";
                if (addresses.get(0).getLocality() != null)
                    address += addresses.get(0).getLocality() + " ";
                if (addresses.get(0).getPostalCode() != null)
                    address += addresses.get(0).getPostalCode() + " ";
                if (addresses.get(0).getThoroughfare() != null)
                    address += addresses.get(0).getThoroughfare();
                Toast.makeText(LocationActivity.this, address, Toast.LENGTH_SHORT).show();


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(n)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .draggable(true)
                .snippet(desc + ", " + con);
        dest = mMap.addMarker(options);


    }

    @Override
    public void onMarkerDrag (@NonNull Marker marker){

        }
    @Override
    public void onMarkerDragEnd (@NonNull Marker marker){
            lati2=marker.getPosition().latitude;
            lng2=marker.getPosition().longitude;

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lati2,lng2, 1);
                if (addresses != null && addresses.size() > 0) {
                    Log.i(TAG, "onLocationResult: " + addresses.get(0));
                    if (addresses.get(0).getPremises() != null) {
                        name = addresses.get(0).getPremises();
                    } else if (addresses.get(0).getThoroughfare() != null) {
                        name = addresses.get(0).getPremises();
                    } else {
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        name = formattedDate;
                    }



                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (name == null){
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                name = df.format(c);
            }

            try{DB.updateData(id, name,lati2,lng2);
                marker.setTitle(name);}
            catch(Exception e){
                Toast.makeText(this, " Failed Updating !!", Toast.LENGTH_SHORT).show();

            }

        }
    @Override public void onMarkerDragStart (@NonNull Marker marker){ }

}





