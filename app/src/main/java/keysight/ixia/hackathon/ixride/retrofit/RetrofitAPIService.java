package keysight.ixia.hackathon.ixride.retrofit;

import java.io.IOException;
import java.util.List;

import keysight.ixia.hackathon.ixride.model.PreliminaryRoute;
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

    public Long removeUser(Long userId) {
        Call<Long> call = retrofitAPIInterface.deleteUser(userId);
        try {
            Long body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public RetroUser getUserById(Long userId) {
        Call<RetroUser> call = retrofitAPIInterface.getUserById(userId);
        try {
            RetroUser body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public RetroProfile getProfileByUser(Long userId) {
        Call<RetroProfile> call = retrofitAPIInterface.getProfileByUser(userId);
        try {
            RetroProfile body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetroCar getCarByProfile(Long profileId) {
        Call<RetroCar> call = retrofitAPIInterface.getCarByProfile(profileId);
        try {
            RetroCar body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetroUser updateUser(RetroUser userToUpdate, Long userId) {
        Call<RetroUser> call = retrofitAPIInterface.updateUser(userToUpdate, userId);
        try {
            RetroUser body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetroProfile updateProfile(RetroProfile profileToUpdate, Long userId) {
        Call<RetroProfile> call = retrofitAPIInterface.updateProfile(profileToUpdate, userId);
        try {
            RetroProfile body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetroCar updateCar(RetroCar carToUpdate, Long profileId) {
        Call<RetroCar> call = retrofitAPIInterface.updateCar(carToUpdate, profileId);
        try {
            RetroCar body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreliminaryRoute getPreliminaryRoute(Long userId) {
        Call<PreliminaryRoute> call = retrofitAPIInterface.getPreliminaryRoute(userId);
        try {
            PreliminaryRoute body = call.execute().body();
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
