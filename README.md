# Overview

This project represents the structure of a directed weighted graph, also contains a pokemon game.

# NodeData

Represents a vertice in the graph.<br/>

<b>Key</b>: specific key associated with this node.<br/>
<b>Tag:</b> contains an integer, helps with graph algorithms.<br/>
<b>Weight:</b> the weight of the node.<br/>
<b>Info:</b> a string associated with the node, helps with graph algorithms.<br/>
<b>Location:</b> the location of the node in 3D.<br/>

# EdgeData

Represents a edge in the graph.<br/>

<b>Src:</b> source node.<br/>
<b>Dest:</b> destination node.<br/>
<b>Tag:</b> contains an integer, helps with graph algorithms.<br/>
<b>Info:</b> a string associated with the edge, helps with graph algorithms.<br/>
<b>Weight:</b> the weight of the edge from source to destination.<br/>

# DWGraph_DS

Represents the graph.<br/>

<b>V:</b> an HashMap contains all the vertices of the graph.<br/>
<b>Edges:</b> an HashMap contains all the edges of the graph.<br/>
<b>modecount:</b> counts the changes in the graph.<br/>
<b>edgesize:</b> the amount of edges.<br/>
<b>nodeisze:</b> the amount of nodes.<br/>

## Methods:
<b>getNode(int key):</b> returns a node_data by key in the graph.<br/>
<b>getEdge(int src, int dest):</b> returns a edge_data by src and dest.<br/>
<b>addNode(node_data n):</b> add node to the graph.<br/>
<b>connect(int src, int dest, double w):</b> connects a new edge from source to destination with weight w.<br/>
<b>getE(int node_id):</b> returns a collection of all edges which node_id is their source.<br/>
<b>removeNode(int key):</b> removes a now with specific key.<br/>
<b>removeEdge(int src, int dest):</b> removes edge from source to destination if exists<br/>

# DWGraph_Algo

The class that contains the whole algorithms that can be used on a graph.<br/>

<b>G:</b> the graph that the algorithms being used on.<br/>

## Methods:
<b>copy():</b> a deep copy of the graph return a new directed_weighted_graph object.<br/>
<b>isConnected():</b> returns a boolean, if the graph is connected or not.<br/>
<b>shortestPath(src, dest):</b> return the shortest path from source node to destination.<br/>
<b>shortestPathDist(src, dest):</b> return the weight of the shortest path from source to destination.<br/>
<b>calculateDist(src):</b> calculates the distance from source node to any other node in the graph, helps to implement shortest path methods.<br/>
<b>save(path):</b> save a graph to json string file.<br/>
<b>load(path):</b> loads a graph from json to DWGraph_DS object.<br/>
<b>convertJson(path, operation):</b> the method that implements save and load, decided by the operation type.<br/>

# GeoLocation

Represents a location in 3D.<br/>

<b>X:</b> x value.<br/>
<b>Y:</b> y value.<br/>
<b>Z:</b> z value.<br/>
## Methods
<b>distance(point):</b> calculates the distance between the instance to the point.<br/>

# InterfaceSerializer

A class the helps transfering json string into a graph, by deserializing the interfaces into specific classes.<br/>
