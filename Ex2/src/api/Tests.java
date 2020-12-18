package api;


import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class Tests {
    private static final int nodesRange = 1000000;
    private static final int edgesRange = 10000000;
    private static final int weightRange = 100;

    @Test
    public void TestGraph() {
        directed_weighted_graph graph = new DWGraph_DS();

        node_data node;

        for(int i = 0; i < nodesRange; i++) {
            node = new NodeData(i);

            graph.addNode(node);
        }

        while(graph.edgeSize() < edgesRange) {
            int a = (int)(Math.random() * nodesRange);
            int b = (int)(Math.random() * nodesRange);

            graph.connect(a,b,Math.random() * weightRange);
        }

        assertEquals(graph.nodeSize(), nodesRange);
        assertEquals(graph.edgeSize(), edgesRange);
    }

    @Test
    public void TestAlgo() {
        directed_weighted_graph graph = new DWGraph_DS();

        final int range = 100;

        for(int i = 0; i < range; i++) {
            node_data node = new NodeData(i);

            graph.addNode(node);
        }

        for(int i = 1; i < range; i++) {
            graph.connect(i-1, i, 1);
            graph.connect(i, i-1, 1);
        }

        dw_graph_algorithms algo = new DWGraph_Algo(graph);

        assertTrue(algo.isConnected());

        for(int i = 0; i < range; i++) {
            for(int j = 0; j < range; j++) {
                if(i == j) {
                    assertEquals((int) (algo.shortestPathDist(i, j)), 0);
                    assertEquals(algo.shortestPath(i,j), null);
                }
                else {
                    assertEquals((int) algo.shortestPathDist(i, j), Math.abs(i - j));
                    assertNotEquals(algo.shortestPath(i,j), null);
                }
            }
        }

        for(int i = 0; i < range; i++) {
            int rand = Math.max((int) (Math.random() * range), 1);

            graph.removeEdge(rand, rand - 1);
            graph.removeEdge(rand - 1, rand);

            assertFalse(algo.isConnected());
        }

        algo.save("test.json");

        directed_weighted_graph graph2 = new DWGraph_DS();

        algo.init(graph2);

        algo.load("test.json");

        graph2 = algo.getGraph();

        assertEquals(graph.nodeSize(), graph2.nodeSize());
        assertEquals(graph.edgeSize(), graph2.edgeSize());
    }

    @Test
    public void TestGeo() {
        final int range = 100;

        for(int i = 1; i < range; i++) {
            geo_location loc1 = new GeoLocation(0, i, 0);
            geo_location loc2 = new GeoLocation(i, 0, 0);

            System.out.println(Math.pow(loc1.distance(loc2), 2));
            assertEquals(Math.round(Math.pow(loc1.distance(loc2), 2)), i*i*2);
        }
    }
}
