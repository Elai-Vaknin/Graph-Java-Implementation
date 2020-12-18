package api;

import java.io.Serializable;

public class GeoLocation implements Serializable, geo_location {
    private double x;
    private double y;
    private double z;

    public GeoLocation() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public GeoLocation(String s) {
        try {
            String[] a = s.split(",");
            x = Double.parseDouble(a[0]);
            y = Double.parseDouble(a[1]);
            z = Double.parseDouble(a[2]);
        }
        catch(IllegalArgumentException e) {
            System.err.println("Wrong format, should be x,y,z");

            throw(e);
        }
    }

    public GeoLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    public double distance(geo_location g)
    {
        double d = Math.pow((Math.pow(this.x - g.x(), 2) +
                Math.pow(this.y - g.y(), 2) +
                Math.pow(this.z - g.z(), 2) * 1.0), 0.5);

        return d;
    }

    @Override
    public String toString() {
        return "\u001B[35m("+this.x+","+this.y+","+this.z+")\u001B[0m";
    }
}
