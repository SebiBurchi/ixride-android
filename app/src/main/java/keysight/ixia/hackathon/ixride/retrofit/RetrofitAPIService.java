package keysight.ixia.hackathon.ixride.retrofit;

import java.io.IOException;
import java.util.List;

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


}
