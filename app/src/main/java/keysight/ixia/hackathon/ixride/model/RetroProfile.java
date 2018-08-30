package keysight.ixia.hackathon.ixride.model;

import com.google.gson.annotations.SerializedName;

public class RetroProfile {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("addressLongitude")
    private double addressLongitude;

    @SerializedName("addressLatitude")
    private double addressLatitude;

    @SerializedName("isDriver")
    private boolean isDriver;

    public RetroProfile() {
    }

    public RetroProfile(String name, String phone, double addressLongitude, double addressLatitude, boolean isDriver) {
        this.name = name;
        this.phone = phone;
        this.addressLongitude = addressLongitude;
        this.addressLatitude = addressLatitude;
        this.isDriver = isDriver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(double addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public double getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(double addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }
}
