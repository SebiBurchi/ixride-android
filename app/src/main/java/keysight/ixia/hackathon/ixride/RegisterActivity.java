package keysight.ixia.hackathon.ixride;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerDragListener {

    private EditText registerName;
    private EditText registerUserName;
    private EditText registerUserPassword;
    private EditText registerUserPhone;
    private SupportMapFragment adressMap;
    private CheckBox registerCarOwnerCheckBox;
    private TextView registerCarSeatsLabel;
    private Spinner registerCarSeatsSpinner;
    private EditText registerCarPlate;
    private Button registerBtn;

    private GoogleMap googleMap;
    LocationRequest locationRequest;
    Location lastLocation;
    Marker currLocationMarker;
    FusedLocationProviderClient fusedLocationClient;
    LatLng adressPoint;

    private static final List<Integer> seatsList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static final int REQUEST_LOCATION_CODE = 0;
    private static final String REQUEST_LOCATION_DENIED_MSG = "You will be unable to register!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        adressMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.adressMap);
        adressMap.getMapAsync(this);

        registerCarOwnerCheckBox = findViewById(R.id.registerCarOwnner);
        registerCarSeatsLabel = findViewById(R.id.registerCarSeatsLabel);
        registerCarSeatsSpinner = findViewById(R.id.registerCarSeats);
        registerCarPlate = findViewById(R.id.registerCarPlate);
        registerBtn = findViewById(R.id.registerButton);

        addListToSpinner();
        registerCarSeatsLabel.setEnabled(registerCarOwnerCheckBox.isChecked());
        registerCarSeatsSpinner.setEnabled(registerCarOwnerCheckBox.isChecked());
        registerCarPlate.setEnabled(registerCarOwnerCheckBox.isChecked());

        registerCarOwnerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                registerCarSeatsLabel.setEnabled(b);
                registerCarSeatsSpinner.setEnabled(b);
                registerCarPlate.setEnabled(b);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(120000); // two minute interval
        locationRequest.setFastestInterval(120000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_CODE);
        } else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMarkerDragListener(this);
        }
    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                lastLocation = location;
                if (currLocationMarker != null) {
                    currLocationMarker.remove();
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Pick up point");
                markerOptions.draggable(true);
                currLocationMarker = googleMap.addMarker(markerOptions);

                //move map camera
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            adressMap.getMapAsync(this);
        } else {
            Toast.makeText(this, REQUEST_LOCATION_DENIED_MSG, Toast.LENGTH_LONG).show();
        }
    }

    private void addListToSpinner() {
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, seatsList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registerCarSeatsSpinner.setAdapter(spinnerAdapter);
    }

    private void register() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        adressPoint = marker.getPosition();
        Log.i("MapsActivity", "Final location: " + adressPoint.latitude + " " + adressPoint.longitude);
    }
}
