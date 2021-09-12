package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager ;
    LocationListener locationListener;

    TextView latitudeTextView , longitudeTextView , altitudeTextView , accuracyTextView , addressTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        addressTextView = findViewById(R.id.addressTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String latitude= String.valueOf(location.getLatitude());
                String longitude= String.valueOf(location.getLongitude());
                String altitude= String.valueOf(location.getAltitude());
                String accuracy=String.valueOf(location.getAccuracy());

                latitudeTextView.setText(latitude);
                longitudeTextView.setText(longitude);
                altitudeTextView.setText(altitude);
                accuracyTextView.setText(accuracy);

                Geocoder geocoder = new Geocoder(getApplicationContext() , Locale.getDefault());

                try {
                    List<Address> listAdressess = geocoder.getFromLocation(location.getLatitude() , location.getLongitude() , 1);

                    if(listAdressess!=null && listAdressess.size() > 0){
                        String address="";

                        if(listAdressess.get(0).getFeatureName()!= null){
                            address += listAdressess.get(0).getFeatureName() + ",\n";
                        }
                        if(listAdressess.get(0).getSubAdminArea()!= null){
                            address += listAdressess.get(0).getSubAdminArea() + ",\n";
                        }

                        if(listAdressess.get(0).getAdminArea()!= null){
                            address += listAdressess.get(0).getAdminArea() + ",\n";
                        }

                        if(listAdressess.get(0).getPostalCode()!= null){
                            address += listAdressess.get(0).getPostalCode() + ",\n";
                        }

                        if(listAdressess.get(0).getCountryName()!= null){
                            address += listAdressess.get(0).getCountryName() ;
                        }

                        addressTextView.setText(address);
                        Log.i("place info",address);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }
}