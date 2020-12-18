## Overview
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
Src: source node.
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
