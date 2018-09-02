package keysight.ixia.hackathon.ixride;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import keysight.ixia.hackathon.ixride.auth.AuthenticationHolder;
import keysight.ixia.hackathon.ixride.model.Car;
import keysight.ixia.hackathon.ixride.model.DirectionResults;
import keysight.ixia.hackathon.ixride.model.Driver;
import keysight.ixia.hackathon.ixride.model.Location;
import keysight.ixia.hackathon.ixride.model.PreliminaryRoute;
import keysight.ixia.hackathon.ixride.model.Route;
import keysight.ixia.hackathon.ixride.model.RoutePoint;
import keysight.ixia.hackathon.ixride.model.Step;
import keysight.ixia.hackathon.ixride.retrofit.RetrofitAPIService;
import keysight.ixia.hackathon.ixride.retrofit.RetrofitRouteClient;
import keysight.ixia.hackathon.ixride.retrofit.RetrofitRouteInterface;
import keysight.ixia.hackathon.ixride.util.RouteDecoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;
    private List<LatLng> markerPoints = new ArrayList<>();
    private static final String API_KEY = "AIzaSyA-qYiBuJcp8YvhXFDueLv-QzvQvhlVuIg";
    private static final String googleRoutesUrl = "https://maps.googleapis.com/maps/api/directions/";

    private RetrofitRouteInterface retrofitRouteService;
    private RetrofitAPIService retrofitAPIService;

    private LatLng userLatLng;
    private Driver driver;
    private Car car;
    private Long currentUserId;

    private TextView routeMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationHolder authHolder = AuthenticationHolder.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        currentUserId = authHolder.getAuthUser(sharedPreferences);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitAPIService = RetrofitAPIService.aRetrofitApiService();
                populateRouteInfo(currentUserId);
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        retrofitRouteService = RetrofitRouteClient.getRetrofitRouteClient().create(RetrofitRouteInterface.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        routeMessage = findViewById(R.id.routeMessage);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (markerPoints.size() != 0) {

            //add start point
            MarkerOptions startMarkerOptions = new MarkerOptions();
            startMarkerOptions.position(markerPoints.get(0));
            startMarkerOptions.title("Start");
            if (markerPoints.get(0).equals(userLatLng)) {
                startMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            mMap.addMarker(startMarkerOptions);

            //add stop point
            MarkerOptions stopMarkerOptions = new MarkerOptions();
            stopMarkerOptions.position(markerPoints.get(markerPoints.size() - 1));
            stopMarkerOptions.title("Stop");
            if (markerPoints.get(markerPoints.size() - 1).equals(userLatLng)) {
                stopMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            mMap.addMarker(stopMarkerOptions);

            //zoom on stop
            mMap.moveCamera(CameraUpdateFactory.newLatLng(markerPoints.get(0)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPoints.get(0), 9));

            String originQueryParam = markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;
            String destinationQueryParam = markerPoints.get(markerPoints.size() - 1).latitude + "," + markerPoints.get(markerPoints.size() - 1).longitude;
            String wayPointsQueryParam = new String("");

            //construct waypoints and add them on map
            for (int i = 1; i <= (markerPoints.size() - 2); i++) {
                wayPointsQueryParam = wayPointsQueryParam + "via:" + markerPoints.get(i).latitude + "," + markerPoints.get(i).longitude;
                if (i != (markerPoints.size() - 2)) {
                    wayPointsQueryParam = wayPointsQueryParam + "|";
                }
                MarkerOptions wayPointMarkerOptions = new MarkerOptions();
                wayPointMarkerOptions.position(markerPoints.get(i));
                wayPointMarkerOptions.title("Way point");
                if (markerPoints.get(i).equals(userLatLng)) {
                    wayPointMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                }
                mMap.addMarker(wayPointMarkerOptions);
            }
            String apiKeyQueryParam = API_KEY;
        Call<DirectionResults> call = retrofitRouteService.getRoute(originQueryParam, destinationQueryParam, wayPointsQueryParam, apiKeyQueryParam);
        call.enqueue(new Callback<DirectionResults>() {
            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                ArrayList<LatLng> routeList = new ArrayList<LatLng>();
                List<Route> dirResults = response.body().getRoutes();
                if (dirResults.size() > 0) {
                    ArrayList<LatLng> decodelist;
                    Route route = dirResults.get(0);
                    Log.i("MainActivity", "Legs length : " + route.getLegs().size());
                    if (route.getLegs().size() > 0) {
                        List<Step> steps = route.getLegs().get(0).getSteps();
                        Log.i("MainActivity", "Steps size :" + steps.size());
                        Step step;
                        Location location;
                        String polyline;
                        for (int i = 0; i < steps.size(); i++) {
                            step = steps.get(i);
                            location = step.getStart_location();
                            routeList.add(new LatLng(location.getLat(), location.getLng()));
                            Log.i("MainActivity", "Start Location :" + location.getLat() + ", " + location.getLng());
                            polyline = step.getPolyline().getPoints();
                            decodelist = RouteDecoder.decodePoly(polyline);
                            routeList.addAll(decodelist);
                            location = step.getEnd_location();
                            routeList.add(new LatLng(location.getLat(), location.getLng()));
                            Log.i("MainActivity", "End Location :" + location.getLat() + ", " + location.getLng());
                        }
                    }
                }

                if (routeList.size() > 0) {
                    PolylineOptions rectLine = new PolylineOptions().width(10).color(
                            Color.BLUE);

                    for (int i = 0; i < routeList.size(); i++) {
                        rectLine.add(routeList.get(i));
                    }
                    // Adding route on the map
                    mMap.addPolyline(rectLine);
                }

                Log.d("CallBack", " response is " + response);
            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
            if (!currentUserId.equals(driver.getId())) {
                Log.i("MainActivity", "Your driver is: " + driver.getName() + ". Phone number is: " + driver.getPhone() + " and car plate is: " + car.getLicensePlate() + ".");
                routeMessage.setText("Your driver is: " + driver.getName() + ". Phone number is: " + driver.getPhone() + " and car plate is: " + car.getLicensePlate() + ".");
            } else {
                Log.i("MainActivity", "You will pick up " + (markerPoints.size() - 2) + " people.");
                routeMessage.setText("You will pick up " + (markerPoints.size() - 2) + " people.");
            }
        } else {
            routeMessage.setText("No routes found!");
        }
    }

    private void populateRouteInfo(Long userId) {
        PreliminaryRoute preliminaryRoute = retrofitAPIService.getPreliminaryRoute(userId);
        if (preliminaryRoute.getRoutePoints() != null) {
            List<RoutePoint> routePoints = preliminaryRoute.getRoutePoints();
            for (int i = 0; i < routePoints.size(); i++) {
                markerPoints.add(new LatLng(routePoints.get(i).getLatitude(), routePoints.get(i).getLongitude()));
                if (routePoints.get(i).getProfileId().equals(userId)) {
                    userLatLng = new LatLng(routePoints.get(i).getLatitude(), routePoints.get(i).getLongitude());
                }
            }
            driver = preliminaryRoute.getDriver();
            car = preliminaryRoute.getCar();
        }
    }
}
