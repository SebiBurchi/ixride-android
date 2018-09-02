package keysight.ixia.hackathon.ixride.retrofit;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import keysight.ixia.hackathon.ixride.model.DirectionResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitRouteInterface {
    @GET("/maps/api/directions/json")
    Call<DirectionResults> getRoute(@Query("origin") String origin, @Query("destination") String destination, @Query("waypoints") String waypoints, @Query("key") String apiKey);
}
