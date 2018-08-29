package keysight.ixia.hackathon.ixride.model;

import com.google.gson.annotations.SerializedName;

public class RetroUser {

    @SerializedName("id")
    private Long id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public RetroUser() {

    }

    public RetroUser(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public RetroUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
