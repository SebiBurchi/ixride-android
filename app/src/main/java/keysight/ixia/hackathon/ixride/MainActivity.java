package keysight.ixia.hackathon.ixride;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import keysight.ixia.hackathon.ixride.auth.AuthenticationHolder;
import keysight.ixia.hackathon.ixride.retrofit.RetrofitAPIService;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem itemHome = menu.findItem(R.id.action_home);
        itemHome.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_edit_profile:
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("action", "edit");
                startActivity(intent);
                break;

            case R.id.action_logout:
                AuthenticationHolder.getInstance().setAuthUser(null, null);
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("action", "Logout");
                startActivity(intent);
                break;

            case R.id.action_delete_account:
                RetrofitAPIService retrofitAPIService = RetrofitAPIService.aRetrofitApiService();
                new AlertDialog.Builder(this)
                        .setTitle("Delete account")
                        .setMessage("Do you really want to delete this profile?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                                        Long removedUserId = retrofitAPIService.removeUser(AuthenticationHolder.getInstance().getAuthUser(sharedPreferences));
                                        if (removedUserId != null) {
                                            intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            intent.putExtra("action", "deleteAccount");
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "The account was not deleted!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                thread.start();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;
        }

        return super.onOptionsItemSelected(item);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
