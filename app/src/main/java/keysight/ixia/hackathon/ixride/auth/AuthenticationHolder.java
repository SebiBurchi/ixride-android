package keysight.ixia.hackathon.ixride.auth;

import android.content.SharedPreferences;

import keysight.ixia.hackathon.ixride.model.RetroUser;

import static android.content.Context.MODE_PRIVATE;

public class AuthenticationHolder {

    private static AuthenticationHolder instance;


    public static AuthenticationHolder getInstance() {
        if (instance == null) {
            instance = new AuthenticationHolder();
        }

        return instance;
    }

    public void setAuthUser(RetroUser user, SharedPreferences sharedPreferences) {
        if (user == null) {
            return;
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("authUserKey", user.getId());
            editor.commit();
        }
    }

    public Long getAuthUser(SharedPreferences sharedPreferences) {

        return sharedPreferences.getLong("authUserKey", 0l);
    }

}
