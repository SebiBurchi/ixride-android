package keysight.ixia.hackathon.ixride.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreliminaryRoute {
    @SerializedName("route")
    List<RoutePoint> routePoints;
    @SerializedName("driver")
    Driver driver;
    @SerializedName("car")
    Car car;

    public List<RoutePoint> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<RoutePoint> routePoints) {
        this.routePoints = routePoints;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
