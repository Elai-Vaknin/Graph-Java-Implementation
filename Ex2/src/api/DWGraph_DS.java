package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements Serializable, directed_weighted_graph {
    private HashMap<Integer, node_data> v;
    private HashMap<String, edge_data> edges;

    private int nodesize;
    private int edgesize;
    private int modecount;
	
    public DWGraph_DS() {
        this.nodesize = 0;
        this.edgesize = 0;
        this.modecount = 0;
		
        this.v = new HashMap<>();
        this.edges = new HashMap<>();
    }

    public node_data getNode(int key) {
        return this.v.get(key);
    }

    public edge_data getEdge(int src, int dest) {
        String vec = makeVector(src, dest);

        return edges.get(vec);
    }

    public void addNode(node_data n) {
        if(!this.v.containsKey(n.getKey())) {
            this.v.put(n.getKey(), n);

            this.nodesize++;
			this.modecount++;
        }
    }

    public void connect(int src, int dest, double w) {
        if( src == dest || !this.v.containsKey(src) || !this.v.containsKey(dest) || getEdge(src, dest) != null )
            return;

        edge_data e = new EdgeData(src, dest, w);

        String vec = makeVector(src, dest);

        edges.put(vec,e);
	
		this.modecount++;
        this.edgesize++;
    }

    public Collection<node_data> getV() {
        return this.v.values();
    }

    public Collection<edge_data> getE(int node_id) {
        ArrayList<edge_data> list = new ArrayList<>();

        if(!this.v.isEmpty()) {
            for (node_data n : this.v.values()) {
                String vec = makeVector(node_id, n.getKey());

                if (this.edges.containsKey(vec))
                    list.add(this.edges.get(vec));

            }
        }

        return list;
    }

    public node_data removeNode(int key) {
        if(!this.v.containsKey(key))
            return null;

        if(!this.v.values().isEmpty()) {
            for (node_data n : this.v.values()) {
                if(n.getKey() == key)
                    continue;

                String vec = makeVector(key, n.getKey());
                String vec2 = makeVector(n.getKey(), key);

                if(this.edges.containsKey(vec))
                    removeEdge(key, n.getKey());

                if(this.edges.containsKey(vec2))
                    removeEdge(n.getKey(), key);
            }
        }

        this.nodesize--;
		this.modecount++;
		
        return v.remove(key);
    }

    public edge_data removeEdge(int src, int dest) {
        String vec = makeVector(src,dest);

        if(!edges.containsKey(vec))
            return null;

        this.edgesize--;
		this.modecount++;
		
        return this.edges.remove(vec);
    }

    public int nodeSize() {
        return this.nodesize;
    }

    public int edgeSize() {
        return this.edgesize;
    }

    public int getMC() {
        return this.modecount;
    }

    /*
    Creates a string from src and dest with '-' inbetween
    @return String
    */

    public String makeVector(int src, int dest) {
        String vec = src + "-" + dest;

        return vec;
    }

    @Override
    public String toString() {
        return "["+ nodesize + "]Nodes: " + this.v.values().toString() + "\n["+ edgesize + "]Edges: " + this.edges.values().toString();
    }
}
