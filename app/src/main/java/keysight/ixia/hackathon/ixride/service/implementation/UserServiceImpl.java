package keysight.ixia.hackathon.ixride.service.implementation;

import java.util.List;

import keysight.ixia.hackathon.ixride.model.RetroUser;
import keysight.ixia.hackathon.ixride.retrofit.RetrofitAPIService;
import keysight.ixia.hackathon.ixride.service.UserService;

public class UserServiceImpl implements UserService {

    private RetrofitAPIService retrofitAPIService;

    public UserServiceImpl() {
        this.retrofitAPIService = RetrofitAPIService.aRetrofitApiService();
    }

    @Override
    public List<RetroUser> getAllUsers() {
        return retrofitAPIService.getAllUsers();
    }
}
