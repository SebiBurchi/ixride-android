package keysight.ixia.hackathon.ixride;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

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

    private static final List<Integer> seatsList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

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
}
