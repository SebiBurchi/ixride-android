package keysight.ixia.hackathon.ixride.retrofit;

import java.io.IOException;
import java.util.List;

import keysight.ixia.hackathon.ixride.model.RetroCar;
import keysight.ixia.hackathon.ixride.model.RetroProfile;
import keysight.ixia.hackathon.ixride.model.RetroUser;
import retrofit2.Call;

public class RetrofitAPIService {

    private RetrofitAPIInterface retrofitAPIInterface;

    protected RetrofitAPIService() {
        retrofitAPIInterface = RetrofitAPIClient.getRetrofitClient().create(RetrofitAPIInterface.class);
    }

    public static RetrofitAPIService aRetrofitApiService() {
        return new RetrofitAPIService();
    }

    public List<RetroUser> getAllUsers() {
        Call<List<RetroUser>> call = retrofitAPIInterface.getAllUsers();
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetroUser authUser(RetroUser user) {

        Call<RetroUser> call = retrofitAPIInterface.authorizeUser(user);
        try {
            RetroUser body = call.execute().body();
            return body;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public RetroUser addNewUser(RetroUser user) {
        Call<RetroUser> call = retrofitAPIInterface.addNewUser(user);
        try {
            RetroUser body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetroProfile addNewProfile(Long userId, RetroProfile profile) {
        Call<RetroProfile> call = retrofitAPIInterface.addNewProfile(userId, profile);
        try {
            RetroProfile body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetroCar addNewCar(Long profileId, RetroCar car) {
        Call<RetroCar> call = retrofitAPIInterface.addNewCar(profileId, car);
        try {
            RetroCar body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
