package keysight.ixia.hackathon.ixride;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import keysight.ixia.hackathon.ixride.auth.AuthenticationHolder;
import keysight.ixia.hackathon.ixride.model.RetroCar;
import keysight.ixia.hackathon.ixride.model.RetroProfile;
import keysight.ixia.hackathon.ixride.model.RetroUser;
import keysight.ixia.hackathon.ixride.retrofit.RetrofitAPIService;

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
    private Intent intent;

    private GoogleMap googleMap;
    LocationRequest locationRequest;
    Location lastLocation;
    Marker currLocationMarker;
    FusedLocationProviderClient fusedLocationClient;
    LatLng adressPoint;
    LatLng storedLocation;

    private static final List<Integer> seatsList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static final int REQUEST_LOCATION_CODE = 0;
    private static final String REQUEST_LOCATION_DENIED_MSG = "You will be unable to register!";

    private RetroUser updateUser;
    private RetroProfile updateProfile;
    private RetroCar updateCar;

    private String actionMade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.registerButton);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        adressMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.adressMap);
        adressMap.getMapAsync(this);

        registerUserPassword = findViewById(R.id.registerUserPassword);
        registerUserName = findViewById(R.id.registerUserName);
        registerUserPhone = findViewById(R.id.registerUserPhone);
        registerName = findViewById(R.id.registerName);

        registerCarOwnerCheckBox = findViewById(R.id.registerCarOwnner);
        registerCarSeatsLabel = findViewById(R.id.registerCarSeatsLabel);
        registerCarSeatsSpinner = findViewById(R.id.registerCarSeats);
        registerCarPlate = findViewById(R.id.registerCarPlate);


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
                if (registerBtn.getText().equals("Update")) {
                    updateRegister();
                } else {
                    register();
                }
            }
        });

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            actionMade = (String) b.get("action");
            if (actionMade.equals("edit")) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        populateForm();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateFields();
                            }
                        });
                    }
                });
                thread.start();

            }
        }
    }

    private void updateAccount() {
        RetrofitAPIService retrofitAPIService = RetrofitAPIService.aRetrofitApiService();
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        String registerusername = registerUserName.getText().toString();
        String registeruserpassword = registerUserPassword.getText().toString();
        RetroUser userToUpdate = new RetroUser(registerusername, registeruserpassword);
        RetroUser userUpdated = retrofitAPIService.updateUser(userToUpdate, AuthenticationHolder.getInstance().getAuthUser(sharedPreferences));
        if (userUpdated != null) {
            String registername = registerName.getText().toString();
            String registeruserphone = registerUserPhone.getText().toString();
            Boolean registerisdriver = registerCarOwnerCheckBox.isChecked();
            RetroProfile profileToUpdate = new RetroProfile(registername, registeruserphone, adressPoint.longitude, adressPoint.latitude, registerisdriver);
            RetroProfile profileUpdated = retrofitAPIService.updateProfile(profileToUpdate, userUpdated.getId());
            if (profileUpdated != null) {
                if (profileUpdated.isDriver()) {
                    String registerlicenseplate = registerCarPlate.getText().toString();
                    Integer carSeatsNo = Integer.parseInt(registerCarSeatsSpinner.getSelectedItem().toString());
                    RetroCar carToUpdate = new RetroCar(registerlicenseplate, carSeatsNo);
                    RetroCar carUpdated = retrofitAPIService.updateCar(carToUpdate, profileUpdated.getId());
                    if (carUpdated != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Error updating the profile!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error updating the profile!", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {
            Toast.makeText(getApplicationContext(), "Error updating the profile!", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    public void populateForm() {
        RetrofitAPIService retrofitAPIService = RetrofitAPIService.aRetrofitApiService();
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        Long currentUserId = AuthenticationHolder.getInstance().getAuthUser(sharedPreferences);
        updateUser = retrofitAPIService.getUserById(currentUserId);
        if (updateUser != null) {
            updateProfile = retrofitAPIService.getProfileByUser(updateUser.getId());
            if (updateProfile != null) {
                if (updateProfile.isDriver()) {
                    updateCar = retrofitAPIService.getCarByProfile(updateProfile.getId());
                    if (updateCar != null) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Error getting the car!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error getting profile!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(getApplicationContext(), "The account can not be edited!", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    public void updateFields() {
        if (updateUser != null && updateProfile != null) {
            registerUserName.setText(updateUser.getUsername() != null ? updateUser.getUsername() : "");
            registerUserPassword.setText(updateUser.getPassword() != null ? updateUser.getPassword() : "");
            registerUserPhone.setText(updateProfile.getPhone() != null ? updateProfile.getPhone() : "");
            registerName.setText(updateProfile.getName() != null ? updateProfile.getName() : "");
            storedLocation = new LatLng(updateProfile.getAddressLatitude(), updateProfile.getAddressLongitude());
            adressPoint = storedLocation;


            if (updateProfile.isDriver()) {
                registerCarOwnerCheckBox.setChecked(true);
                if (updateCar != null) {
                    registerCarPlate.setText(updateCar.getLicensePlate() != null ? updateCar.getLicensePlate() : "");
                    ArrayAdapter<Integer> adapter = (ArrayAdapter<Integer>) registerCarSeatsSpinner.getAdapter();
                    int spinnerPosition = adapter.getPosition(updateCar.getSeatsNumber());
                    registerCarSeatsSpinner.setSelection(spinnerPosition);

                }
            }

            registerBtn.setText("Update");
        }
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
                LatLng latLng;
                if (storedLocation != null && actionMade.equals("edit")) {
                    latLng = storedLocation;
                } else {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
                adressPoint = latLng;
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
        clearErrors();

        boolean cancel = validateFields();
        if (cancel) {
            Toast.makeText(getApplicationContext(), "Complete all fields!", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    createAccount();
                }
            }).start();

        }

    }

    private void updateRegister() {
        clearErrors();

        boolean cancel = validateFields();
        if (cancel) {
            Toast.makeText(getApplicationContext(), "Complete all fields!", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    updateAccount();
                }
            }).start();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem itemDelete = menu.findItem(R.id.action_delete_account);
        MenuItem itemLogout = menu.findItem(R.id.action_logout);
        MenuItem itemEdit = menu.findItem(R.id.action_edit_profile);
        MenuItem itemHome = menu.findItem(R.id.action_home);
        itemDelete.setVisible(false);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String action = "";
        if (b != null) {
            action = (String) b.get("action");
        }
        if (!action.equals("edit")) {
            itemHome.setVisible(false);
            itemLogout.setTitle("Sign In");
        } else {
            itemLogout.setVisible(false);
        }
        itemEdit.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.action_logout:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
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

    private void clearErrors() {
        registerName.setError(null);
        registerUserName.setError(null);
        registerUserPassword.setError(null);
        registerUserPhone.setError(null);

    }

    private boolean validateFields() {
        boolean cancel = false;

        String registername = registerName.getText().toString();
        String registerusername = registerUserName.getText().toString();
        String registeruserpassword = registerUserPassword.getText().toString();
        String registeruserphone = registerUserPhone.getText().toString();
        Boolean registerisdriver = registerCarOwnerCheckBox.isChecked();

        if (TextUtils.isEmpty(registername)) {
            registerName.setError("This field is required!");
            cancel = true;
        }

        if (TextUtils.isEmpty(registerusername)) {
            registerUserName.setError("This field is required!");
            cancel = true;
        }

        if (TextUtils.isEmpty(registeruserpassword)) {
            registerUserPassword.setError("This field is required!");
            cancel = true;
        }

        if (TextUtils.isEmpty(registeruserphone)) {
            registerUserPhone.setError("This field is required!");
            cancel = true;
        }

        if (registerisdriver) {
            String registerlicenseplate = registerCarPlate.getText().toString();
            if (TextUtils.isEmpty(registerlicenseplate)) {
                registerCarPlate.setError("This field is required!");
                cancel = true;
            }
        }

        return cancel;

    }

    private void createAccount() {
        RetrofitAPIService retrofitAPIService = RetrofitAPIService.aRetrofitApiService();

        String registername = registerName.getText().toString();
        String registerusername = registerUserName.getText().toString();
        String registeruserpassword = registerUserPassword.getText().toString();
        String registeruserphone = registerUserPhone.getText().toString();
        boolean registerisdriver = registerCarOwnerCheckBox.isChecked();

        RetroUser user = new RetroUser(registerusername, registeruserpassword);
        RetroUser insertedUser = retrofitAPIService.addNewUser(user);
        if (insertedUser != null) {
            RetroProfile retroProfile = new RetroProfile();
            retroProfile.setAddressLatitude(adressPoint.latitude);
            retroProfile.setAddressLongitude(adressPoint.longitude);
            retroProfile.setDriver(registerisdriver);
            retroProfile.setName(registername);
            retroProfile.setPhone(registeruserphone);
            RetroProfile insertedProfile = retrofitAPIService.addNewProfile(insertedUser.getId(), retroProfile);

            if (insertedProfile != null) {
                if (retroProfile.isDriver()) {
                    String carlicenseplate = registerCarPlate.getText().toString();
                    RetroCar car = new RetroCar();
                    car.setLicensePlate(carlicenseplate);
                    car.setSeatsNumber(Integer.parseInt(registerCarSeatsSpinner.getSelectedItem().toString()));
                    RetroCar insertedRetroCar = retrofitAPIService.addNewCar(insertedProfile.getId(), car);
                    if (insertedRetroCar != null) {
                        switchIntent();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to register! Retry", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    switchIntent();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Unable to register! Retry", Toast.LENGTH_SHORT).show();
            }

        }


    }

    public void switchIntent() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("action", "createAccount");
        startActivity(intent);

    }

}
