package api;

import java.io.Serializable;

public class EdgeLocation implements Serializable, edge_location {
    private edge_data edge;
    private double ratio;

    public EdgeLocation(edge_data edge, double ratio) {
        this.edge = edge;
        this.ratio = ratio;
    }

    public edge_data getEdge() {
        return this.edge;
    }

    public double getRatio() {
        return this.ratio;
    }
}
