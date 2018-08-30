package keysight.ixia.hackathon.ixride.retrofit;

import java.util.List;

import keysight.ixia.hackathon.ixride.model.RetroUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPIInterface {

    @GET("/users")
    Call<List<RetroUser>> getAllUsers();

    @POST("/auth")
    Call<RetroUser> authorizeUser(@Body RetroUser user);
}
