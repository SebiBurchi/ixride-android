package keysight.ixia.hackathon.ixride.retrofit;

import java.util.List;

import keysight.ixia.hackathon.ixride.model.RetroCar;
import keysight.ixia.hackathon.ixride.model.RetroProfile;
import keysight.ixia.hackathon.ixride.model.RetroUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitAPIInterface {

    @GET("/users")
    Call<List<RetroUser>> getAllUsers();

    @POST("/auth")
    Call<RetroUser> authorizeUser(@Body RetroUser user);

    @POST("/users")
    Call<RetroUser> addNewUser(@Body RetroUser user);

    @POST("/users/{userId}/profiles")
    Call<RetroProfile> addNewProfile(@Path("userId") Long userId, @Body RetroProfile profile);

    @POST("/profiles/{profileId}/cars")
    Call<RetroCar> addNewCar(@Path("profileId") Long profileId, @Body RetroCar car);

    @DELETE("/users/{userId}")
    Call<Long> deleteUser(@Path("userId") Long userId);

    @GET("/users/{userId}")
    Call<RetroUser> getUserById(@Path("userId") long userId);

    @GET("/users/{userId}/profile")
    Call<RetroProfile> getProfileByUser(@Path("userId") long userId);

    @GET("/profiles/{profileId}/car")
    Call<RetroCar> getCarByProfile(long profileId);
}
