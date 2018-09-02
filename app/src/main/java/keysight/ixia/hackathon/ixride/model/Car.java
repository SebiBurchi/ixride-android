package keysight.ixia.hackathon.ixride.model;

import com.google.gson.annotations.SerializedName;

public class Car {
    @SerializedName("id")
    private Long id;
    @SerializedName("licensePlate")
    private String licensePlate;
    @SerializedName("seatsNumber")
    private int seatsNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
    }
}
