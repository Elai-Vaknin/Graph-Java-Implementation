package api;

import java.io.Serializable;

public class NodeData implements Serializable, node_data {
    private int key;
    private int tag;
    private double weight;
    private String info;
    private geo_location location;

    public NodeData(int key) {
        this.tag = 0;
        this.info = "";
        this.key = key;
        this.weight = 0.0;
        this.location = new GeoLocation();
    }

    public NodeData(int key, geo_location location) {
        this.tag = 0;
        this.info = "";
        this.key = key;
        this.weight = 0.0;
        this.location = location;
    }

    public NodeData(int key, int tag, String info, double weight, geo_location location) {
        this.tag = tag;
        this.info = info;
        this.key = key;
        this.weight = weight;
        this.location = location;
    }

    public int getKey() {
        return this.key;
    }

    public geo_location getLocation() {
        return this.location;
    }

    public void setLocation(geo_location p) { this.location = p; }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double w) {
        this.weight = w;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String s) {
        this.info = s;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public String toString() {
        //return "[\u001B[31mK:"+key+"\u001B[0m I:"+info+" T:"+tag+" W:"+weight+" L:"+location+"]";
        return key + "";
    }
}