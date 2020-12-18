package api;

import com.google.gson.*;
import java.io.*;
import java.util.*;

import static api.InterfaceSerializer.interfaceSerializer;

public class DWGraph_Algo implements dw_graph_algorithms {
    enum Operation {
        Save,
        Load
    }

    directed_weighted_graph g;

    public DWGraph_Algo(directed_weighted_graph g) {
        init(g);
    }

    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    public directed_weighted_graph getGraph() {
        return this.g;
    }

    public directed_weighted_graph copy() {
        directed_weighted_graph copy = new DWGraph_DS();

        Collection<node_data> ver = this.g.getV();

        if(!ver.isEmpty()) {
            for (node_data n : ver) {
                node_data temp = new NodeData(n.getKey(), n.getTag(), n.getInfo(), n.getWeight(), n.getLocation());

                copy.addNode(temp);
            }
        }

        if(!ver.isEmpty()) {
            for (node_data n : ver) {
                Collection<edge_data> edges = this.g.getE(n.getKey());

                if(!edges.isEmpty()) {
                    for (edge_data e : edges) {
                        copy.connect(e.getSrc(), e.getDest(), e.getWeight());
                    }
                }
            }
        }

        return copy;
    }

    public boolean isConnected() {
        Collection<node_data> ver = g.getV();

        if(ver.isEmpty())
            return true;

        boolean[] visited = new boolean[getHighestKey()+1];

        int i = 1;

        Stack<node_data> stack = new Stack<>();

        node_data start = ver.iterator().next();
        System.out.println("Start: " + start.getKey());

        stack.push(start);

        visited[start.getKey()] = true;

        while(!stack.isEmpty()) {
            node_data node = stack.pop();

            Collection<edge_data> edges = g.getE(node.getKey());

            if(!edges.isEmpty()) {
                for (edge_data neighbourEdge : edges) {
                    int neighbourKey = neighbourEdge.getDest();
                    System.out.println("Continue: " + neighbourKey);
                    node_data neighbourData = g.getNode(neighbourKey);

                    if (!visited[neighbourKey]) {
                        stack.push(neighbourData);
                        visited[neighbourKey] = true;
                        i++;
                    }
                }
            }
        }

        if(i != g.nodeSize()) {
            System.out.println(i + " " + g.nodeSize());
            return false;
        }
        return true;
    }

    /* Finding highest node's key in the graph */

    public int getHighestKey() {
        Collection<node_data> ver = g.getV();

        if(ver.isEmpty())
            return -1;

        int maxKey = -1;

        for(node_data n : ver) {
            if(n.getKey() > maxKey)
                maxKey = n.getKey();
        }

        return maxKey;
    }
    public double shortestPathDist(int src, int dest) {
        if(src == dest)
            return 0;

        CalculateDist(src);

        node_data result = g.getNode(dest);

        if(result == null)
            return -1;

        String info = result.getInfo();
        String[] args = info.split(",");

        double path = Double.parseDouble(args[1]);

        return path == 0 ? -1 : path;
    }

    public List<node_data> shortestPath(int src, int dest) {
        if(src == dest)
            return null;

        CalculateDist(src);

        node_data destNode = g.getNode(dest);
        node_data srcNode = g.getNode(src);

        if(destNode == null || srcNode == null)
            return null;

        Stack<node_data> stack = new Stack<>();
        ArrayList<node_data> result = new ArrayList<>();

        stack.add(destNode);

        node_data temp = destNode;

        while(temp.getKey() != srcNode.getKey()) {
            String info = temp.getInfo();
            String[] args = info.split(",");

            int key = Integer.parseInt(args[0]);

            node_data node = g.getNode(key);

            if(node != null) {
                stack.add(node);
                temp = node;
            }
        }

        while(!stack.isEmpty())
            result.add(stack.pop());

        return result;
    }

    /* Calculates distance from all nodes to specific source */

    public void CalculateDist(int src) {
        node_data srcNode = g.getNode(src);

        if(srcNode == null)
            return;

        for(node_data n : g.getV())
            n.setInfo(src+","+0);

        Stack<Integer> stack = new Stack<>();

        boolean[] visited = new boolean[getHighestKey()+1];

        stack.add(src);

        while(!stack.isEmpty()) {
            int id = stack.pop();

            node_data node = g.getNode(id);

            if(node == null)
                continue;

            if(!visited[id]) {
                visited[id] = true;

                Collection<edge_data> edges = g.getE(id);

                if(edges.isEmpty())
                    continue;

                for (edge_data edge : edges) {
                    int id2 = edge.getDest();

                    stack.add(id2);
                    node_data temp = g.getNode(id2);

                    String tempData = temp.getInfo();
                    String nodeData = node.getInfo();

                    String[] tempArgs = tempData.split(",");
                    String[] nodeArgs = nodeData.split(",");

                    double path = edge.getWeight();
                    double dist = Double.parseDouble(tempArgs[1]);
                    double distsrc = Double.parseDouble(nodeArgs[1]);

                    if (distsrc + path < dist || dist == 0) {
                        temp.setInfo(node.getKey() + "," + (distsrc + path));
                        visited[temp.getKey()] = false;
                    }
                }
            }
        }
    }
    
    public boolean save(String file) {
        return convertJson(file, Operation.Save);
    }

    public boolean load(String file) {
        return convertJson(file, Operation.Load);
    }

    /* Converts json string into a saved file or loads it into a graph object */

    public boolean convertJson(String file, Operation t) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(node_data.class, interfaceSerializer(NodeData.class))
                .registerTypeAdapter(directed_weighted_graph.class, interfaceSerializer(DWGraph_DS.class))
                .registerTypeAdapter(geo_location.class, interfaceSerializer(GeoLocation.class))
                .registerTypeAdapter(edge_data.class, interfaceSerializer(EdgeData.class))
                .registerTypeAdapter(edge_location.class, interfaceSerializer(EdgeLocation.class))
                .create();

        switch(t) {
            case Save: {
                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(this.g, writer);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            case Load: {
                try (Reader reader = new FileReader(file)) {
                    this.g = gson.fromJson(reader, directed_weighted_graph.class);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        return false;
    }
}