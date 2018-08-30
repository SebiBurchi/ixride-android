package keysight.ixia.hackathon.ixride.model;

import com.google.gson.annotations.SerializedName;

public class RetroCar {

    @SerializedName("id")
    private Long Id;

    @SerializedName("licensePlate")
    private String licensePlate;

    @SerializedName("seatsNumber")
    private int seatsNumber;

    public RetroCar() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
