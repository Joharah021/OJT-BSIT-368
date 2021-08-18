package com.example.cakeshop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapquest.mapping.MapQuest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapSearch extends AppCompatActivity implements LocationListener {
    private final double minZoomLevel = 10.0;
    private DataBase dbutilities;
    private LocationManager locationManager;
    private Location location;
    private LatLng MYLOC;
    private MapView mapview;
    private MapboxMap mapboxMap;
    private double latitude;
    private double longitude;
    private Geocoder geocoder;
    private HashMap<LatLng,String> markTracker;
    private HashMap<String,String> storeLocationTracker;
    private ArrayList<String> storeLocations;
    private Spinner searchNearBy;
    private ArrayAdapter<String> adapter;

    private void addListeners(){

        /*
        * adds listeners to map and dropdown search
        * */
        try {
            this.mapview.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap _mapboxMap) {
                    mapboxMap = _mapboxMap;
                    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MYLOC, minZoomLevel));
                    mapboxMap.setMinZoomPreference(minZoomLevel);
                    addMarker(mapboxMap,MYLOC,"You","-1");
                    markNearBy();
                    addMarkerEvent();
                    showMarkersInfo();
                    System.out.println("Reached Bottom!");
                }
            });


            this.adapter = new ArrayAdapter<>(MapSearch.this, android.R.layout.simple_spinner_dropdown_item,this.storeLocations);
            this.searchNearBy.setAdapter(this.adapter);
            this.searchNearBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0){
                        //Please modify message
                        showToast("Please select or search store");
                    }else{
                        System.out.println("Key: "+Integer.toString(position)+" VALUE: "+storeLocationTracker.get(Integer.toString(position)));
                        Intent vendorstore = new Intent(MapSearch.this,VendorStore.class);
                        vendorstore.putExtra("VENDORID",storeLocationTracker.get(Integer.toString(position)));
                        startActivity(vendorstore);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }catch (Exception e){
            //Please modify error message
            this.showToast("MapSearchClassError > addListeners : "+e.toString());
        }
    }
    private void initWidgets(Bundle savedInstanceState) {
        /**
         *
         *  initialize widgets eg: Maps | controls
         *
         * */

        this.searchNearBy = findViewById(R.id.selectMonth);
        this.storeLocations = new ArrayList<String>();
        this.storeLocationTracker = new HashMap<String, String>();
        this.storeLocations.add("Find store");
        this.markTracker = new HashMap<LatLng, String>();
        this.geocoder = new Geocoder(this, Locale.getDefault());
        this.dbutilities = new DataBase(this,null);
        MapQuest.start(getApplicationContext());
        this.mapview = (MapView) findViewById(R.id.mapquestMapView);
        this.mapview.onCreate(savedInstanceState);
        this.MYLOC = this.myLocation();
        this.latitude = 0;
        this.longitude = 0;
    }

    private LatLng getGeolocation(String _address){
        try {
            List<Address> addresses = this.geocoder.getFromLocationName(_address,1);
            if(!addresses.isEmpty()){
                return new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
            }
        }catch (IOException e){
            return null;
        }
        return null;
    }
    private boolean isNearBy(double distance_in_kilometer,LatLng loc){
        /**
        * considered nearby if distance is distance_in_kilometer away from current location
        * */
        if((this.MYLOC.getLatitude() == loc.getLatitude()) && (this.MYLOC.getLongitude() == loc.getLongitude())){
            // considered arrived at given location
            return true;
        }else{
            final int R = 6371;
            double latDistance = Math.toRadians(loc.getLatitude() - this.MYLOC.getLatitude());
            double lonDistance = Math.toRadians(loc.getLongitude() - this.MYLOC.getLongitude());
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(this.MYLOC.getLatitude())) * Math.cos(Math.toRadians(loc.getLatitude()))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000;
            double height = this.MYLOC.getAltitude() - loc.getAltitude();
            distance = Math.pow(distance, 2) + Math.pow(height, 2);
            distance = Math.sqrt(distance) / 1000;
            return (distance<=distance_in_kilometer)?true:false;
        }
    }
    private void markNearBy(){
        /**
        * adds vendors store as nearby store to map
         *
         * ORDER:
         *      0 = id
         *      1 = username
         *      2 = password
         *      3 = Firstname
         *      4 = lastname
         *      5 = email
         *      6 = Shop name
         *      7 = contact number
         *      8 = address
        * */
        String data[][] = this.dbutilities.getAllVendorStore();
        int position = 1;
        for(int i=0;i<data.length;i++){
            if(data[i][8]!=null){
                LatLng loc = this.getGeolocation(data[i][8]);
                if(loc!=null){
                    this.storeLocationTracker.put(Integer.toString(position),data[i][0]);
                    System.out.println("Key: "+Integer.toString(position)+" VALUE: "+data[i][0]);
                    this.storeLocations.add(data[i][6]);
                    // considered nearby if the store is within 50km radius from your current location | change if you want
                    if(isNearBy(50,loc)){
                        this.addMarker(this.mapboxMap,loc,data[i][6],data[i][0]);
                    }
                    position++;
                }
            }
        }
    }
    private void addMarkerEvent(){
        /**
        * adds event listeners on Marker eg: click"
        * */
        this.mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!(marker.getTitle() =="You" && marker.getSnippet() == "-1")){

                    Intent vendorstore = new Intent(MapSearch.this,VendorStore.class);
                    System.out.println("DATA: "+markTracker.get(marker.getPosition()));
                    vendorstore.putExtra("VENDORID",markTracker.get(marker.getPosition()));
                    startActivity(vendorstore);

                }
                marker.showInfoWindow(mapboxMap,mapview);
                return true;
            }
        });
        this.mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                hideMarkersInfo();
            }
        });
        this.mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                showMarkersInfo();
            }
        });
        this.mapboxMap.setPrefetchesTiles(true);
        return;
    }
    private void showToast(String message){
        /**
         * display's notification in a short period of time
         * */
        try {
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            // handle error
        }
    }
    private LatLng myLocation() {
        /**
        *  Gets current location using Google's GEOLOCATION service
        *
        * */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           /**
            *   Request location if not granted!
            **/
            ActivityCompat.requestPermissions(
                    this,
                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                69);
            //Please modify message
            this.showToast("Location error! location access denied.");
            //returns last recorded geolocation if permission is denied
            return new LatLng(TemporaryState.latitude, TemporaryState.longitude);
        }else{
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = String.valueOf(this.locationManager.getBestProvider(criteria,true)).toString();
            this.location = this.locationManager.getLastKnownLocation(bestProvider);
            if(this.location!=null){
                this.latitude = this.location.getLatitude();
                this.longitude = this.location.getLongitude();
                TemporaryState.latitude=this.latitude;
                TemporaryState.longitude=this.longitude;
                return new LatLng(this.latitude, this.longitude);
            }else{
                // if location is null request new location
                this.locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                return new LatLng(TemporaryState.latitude, TemporaryState.longitude);
            }
        }
    }
    private void showMarkersInfo(){
        /*
        * display marker info
        * */
        List<Marker> markers = this.mapboxMap.getMarkers();
        for(Marker marker: markers){
            marker.showInfoWindow(mapboxMap,mapview);
        }
    }
    private void hideMarkersInfo(){
        /*
         * hide display marker info
         * */
        List<Marker> markers = this.mapboxMap.getMarkers();
        for(Marker marker: markers){
            if(marker.isInfoWindowShown()){
                marker.hideInfoWindow();
            }
        }
    }
    private void addMarker(MapboxMap mapbox,LatLng geolocation,String title,String vendorid){
        /**
        *
        * adds marker to map
        * */
        this.markTracker.put(geolocation,vendorid);// marker iteration is equal to vendorid
        MarkerOptions options = new MarkerOptions();
        options.title(title);
        //options.snippet(vendorid);
        options.setPosition(geolocation);
        mapbox.addMarker(options);
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_search);
        this.initWidgets(savedInstanceState);
        this.addListeners();
    }
    // LocationListener

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            //if location has changed | update both lat and long
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            TemporaryState.latitude=latitude;
            TemporaryState.longitude=longitude;
        }catch (Exception e){
            //Please modify error messaage
            this.showToast("Unable to fetch location. restart app");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        this.showToast(provider+" is enabled");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        this.showToast(provider+" is disabled");
    }

    @Override
    protected void onDestroy() {
        //destroy database and map link
        this.dbutilities.destroy();
        this.mapview.onDestroy();
        super.onDestroy();
    }
}
