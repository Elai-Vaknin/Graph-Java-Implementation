# Overview
This project represents the structure of a directed weighted graph, also contains a pokemon game.

# NodeData
Represents a vertice in the graph.
Key: specific key associated with this node.
Tag: contains an integer, helps with graph algorithms.
Weight: the weight of the node.
Info: a string associated with the node, helps with graph algorithms.
Location: the location of the node in 3D.

# EdgeData
Represents a edge in the graph.
#Src#: source node.
Dest: destination node.
Tag: contains an integer, helps with graph algorithms.
Info: a string associated with the edge, helps with graph algorithms.
Weight: the weight of the edge from source to destination.

# DWGraph_DS
Represents the graph.
V: an HashMap contains all the vertices of the graph.
Edges: an HashMap contains all the edges of the graph.

# DWGraph_Algo
The class that contains the whole algorithms that can be used on a graph.
G: the graph that the algorithms being used on.

## Methods:
copy(): a deep copy of the graph return a new directed_weighted_graph object.
isConnected(): returns a boolean, if the graph is connected or not.
shortestPath(src, dest): return the shortest path from source node to destination.
shortestPathDist(src, dest): return the weight of the shortest path from source to destination.
calculateDist(src): calculates the distance from source node to any other node in the graph, helps to implement shortest path methods.
save(path): save a graph to json string file.
load(path): loads a graph from json to DWGraph_DS object.
convertJson(path, operation): the method that implements save and load, decided by the operation type.

# GeoLocation
Represents a location in 3D.
X: x value.
Y: y value.
Z: z value.

## Methods:
distance(point): calculates the distance between the instance to the point.

# InterfaceSerializer
A class the helps transfering json string into a graph, by deserializing the interfaces into specific classes.
