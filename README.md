# Overview

This project represents the structure of a directed weighted graph, also contains a pokemon game.

# NodeData

Represents a vertice in the graph.<br/>
Key: specific key associated with this node.<br/>
Tag: contains an integer, helps with graph algorithms.<br/>
Weight: the weight of the node.<br/>
Info: a string associated with the node, helps with graph algorithms.<br/>
Location: the location of the node in 3D.<br/>

# EdgeData

Represents a edge in the graph.<br/>
Src: source node.<br/>
Dest: destination node.<br/>
Tag: contains an integer, helps with graph algorithms.<br/>
Info: a string associated with the edge, helps with graph algorithms.<br/>
Weight: the weight of the edge from source to destination.<br/>

# DWGraph_DS

Represents the graph.<br/>
V: an HashMap contains all the vertices of the graph.<br/>
Edges: an HashMap contains all the edges of the graph.<br/>

# DWGraph_Algo

The class that contains the whole algorithms that can be used on a graph.<br/>
G: the graph that the algorithms being used on.<br/>
## Methods:
copy(): a deep copy of the graph return a new directed_weighted_graph object.<br/>
isConnected(): returns a boolean, if the graph is connected or not.<br/>
shortestPath(src, dest): return the shortest path from source node to destination.<br/>
shortestPathDist(src, dest): return the weight of the shortest path from source to destination.<br/>
calculateDist(src): calculates the distance from source node to any other node in the graph, helps to implement shortest path methods.<br/>
save(path): save a graph to json string file.<br/>
load(path): loads a graph from json to DWGraph_DS object.<br/>
convertJson(path, operation): the method that implements save and load, decided by the operation type.<br/>

# GeoLocation

Represents a location in 3D.<br/>
X: x value.<br/>
Y: y value.<br/>
Z: z value.<br/>
## Methods
distance(point): calculates the distance between the instance to the point.<br/>

# InterfaceSerializer

A class the helps transfering json string into a graph, by deserializing the interfaces into specific classes.<br/>
