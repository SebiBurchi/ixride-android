package keysight.ixia.hackathon.ixride.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {
    @SerializedName("overview_polyline")
    private OverviewPolyLine overviewPolyLine;

    private List<Leg> legs;

    public OverviewPolyLine getOverviewPolyLine() {
        return overviewPolyLine;
    }

    public List<Leg> getLegs() {
        return legs;
    }
}

