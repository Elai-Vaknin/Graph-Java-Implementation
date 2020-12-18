package api;

import java.io.Serializable;

public class EdgeData implements Serializable, edge_data {
    private int src;
    private int dest;
    private int tag;
    private String info;
    private double weight;

    public EdgeData(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public int getSrc() {
        return this.src;
    }

    public int getDest() {
        return this.dest;
    }

    public double getWeight() {
        return this.weight;
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
        return this.src + "\u001B[34m==" + String.format("%.2f", this.weight) + "==>\u001B[0m" + this.dest;
    }
}