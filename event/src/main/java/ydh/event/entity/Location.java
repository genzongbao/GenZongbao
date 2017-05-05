package ydh.event.entity;

/**
 * @auther 华清松
 * @time 2017/3/21 0021 14:19
 * @doc 描述：定位实体
 */
public class Location {
    private double latx;
    private double laty;
    private String locationName;

    public double getLatx() {
        return latx;
    }

    public void setLatx(double latx) {
        this.latx = latx;
    }

    public double getLaty() {
        return laty;
    }

    public void setLaty(double laty) {
        this.laty = laty;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
