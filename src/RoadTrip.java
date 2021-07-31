

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import roadtrip.Graph;

public class RoadTrip {

    // Initializing graph class object for storing graph nodes
    static Graph graph;
    // Hashmap for convering string vertices from the text file into integer value nodes
    HashMap<String,Integer> vertices = new HashMap();
    // Arraylist to generate adjacency matrix for making computation of shortest path algorithm possible
    private ArrayList<ArrayList<Integer>> adj;

    /**
     * @param args the command line arguments
     */
    
    // Constructor taking file name as input which contains graph data
    RoadTrip(String name) throws FileNotFoundException {
        // Initializing adjacency matrix arraylist with size 49 as number of vertices in given data are 49.
        adj = new ArrayList<ArrayList<Integer>>(49);
        
        // Initialzing nested array in adjacency matrix to make it accessibe in future,
        for (int i = 0; i < 49; i++) {
            adj.add(new ArrayList<Integer>());
        }
        // Initializing graph object
        this.graph = new Graph();
        
        // Opening file object to make it accessible.
        File file = new File(name);
        
        // Initializing scanner module to read data from file object
        Scanner sc = new Scanner(file);
        
        // Looping the file data till the last line
        while (sc.hasNextLine()) {
            // Splitting each line to seperate the each vertex for putting it into list of vertices
            String s[] = sc.nextLine().split(" ");
            String vertex1 = s[0];
            String vertex2 = s[1];
            // If vertex or city name doesn't already exists in vertices collection, then add it
            if (!vertices.containsKey(vertex1)) {
                // Put city name as key and its entry no in list as key
                vertices.put(vertex1, vertices.size());
                // Add vertex in graph too
                this.graph.addVertex((int) vertices.get(vertex1));
            }
            // Above same operation goes for vertex2 found from file line.
            if (!vertices.containsKey(vertex2)) {
                vertices.put(vertex2, vertices.size());
                this.graph.addVertex((int) vertices.get(vertex2));

            }
            
            // Putting adjacent values for vertex1 and vertex2, indicating them connected to each other.
            adj.get((int) vertices.get(vertex1)).add((int) vertices.get(vertex2));
            adj.get((int) vertices.get(vertex2)).add((int) vertices.get(vertex1));

            
            // Making an edge in graph regardig given two vertices.
            this.graph.addEdge((int) vertices.get(vertex1), (int) vertices.get(vertex2));

        }
        // Closing the scanner object after using
        sc.close();
    }

    
    // Method for finding path between given source and destination
    String getPath(String source, String destination) {
        
        // Getting corresponding node values for given source and destination cities to find them in adjacency matrix
        int vertex1 = (int) vertices.get(source);
        int vertex2 = (int) vertices.get(destination);

        
        // Array used in process of breadth first search to track predecessor of each vertex
        int predecessorOfVertex[] = new int[graph.vertices()];
        
        // Array used in process of breadth first search to track distance of each vertex from source
        int distanceFromVertex[] = new int[graph.vertices()];

        // If breadth first return false, it means there is no available path between source and destination
        if (breadthFirstSearch(adj, vertex1, vertex2, graph.vertices(), predecessorOfVertex, distanceFromVertex) == false) {

            return "There is no path from " + source + " to " + destination;
        }

        
        // Linked list to store path found by breadfirst searh
        LinkedList<Integer> path = new LinkedList<Integer>();
        
        // Loop through predecessors array to add all the predecessors to the path list
        int crawl = vertex2;
        path.add(crawl);
        while (predecessorOfVertex[crawl] != -1) {
            path.add(predecessorOfVertex[crawl]);
            crawl = predecessorOfVertex[crawl];
        }

        // String for storing output expression
        String pathOutput = "Path is : [";
        for (int i = path.size() - 1; i >= 0; i--) {
            // Get city name regarding given node value in path
            String vertex = getKeysByValue(vertices, path.get(i)).toString();
            // Adding vertex name in output expression with sligh modifications
            pathOutput += vertex.substring(1, vertex.length()-1);
            // If it isn't last city in the path, then put arrow otherwise don't 
            if (i != 0) {
                pathOutput += " -> ";
            }
        }
        // returning required message with source, destination and path other output values
        return "There is a path from " + source + " to " + destination + "\nThe shortest path involves crossing " + distanceFromVertex[vertex2] + " borders\n" + pathOutput+"]";
    }

    
    // Method for finding shortest path between source and destination
    // Taking adjacency list, source node value, destination node value, predecessors and distance array as input
    private static boolean breadthFirstSearch(ArrayList<ArrayList<Integer>> adj, int src,
            int dest, int v, int predecessorOfVertex[], int distanceFromVertex[]) {
        
        // A queue to maintain tracks of vertices whose adjacency matrix is to be visited
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // boolean array to track which vertex nodes are visited in BFS process
        boolean visitedVertices[] = new boolean[v];

        // Initially all vertices are unvisited, so making each element of visited array false, 
        //and initially distance between related vertices is infinity too, until calculated.
        for (int i = 0; i < v; i++) {
            visitedVertices[i] = false;
            distanceFromVertex[i] = Integer.MAX_VALUE;
            predecessorOfVertex[i] = -1;
        }
        
        // Indicating source as visited, as search starts from it

        visitedVertices[src] = true;
        
        // Setting distance of source from source as 0.
        distanceFromVertex[src] = 0;
        queue.add(src);

        //BFS algorithm for visiting each node's adjacent nodes 
        while (!queue.isEmpty()) {
            // Getting a node from queue
            int u = queue.remove();
            // Looping to visit u's adjacent nodes
            for (int i = 0; i < adj.get(u).size(); i++) {
                // if some node is unvisited then go for the process
                if (visitedVertices[adj.get(u).get(i)] == false) {
                    // Change visited value to true
                    visitedVertices[adj.get(u).get(i)] = true;
                    // Incrementing total distance of current vertex from source
                    distanceFromVertex[adj.get(u).get(i)] = distanceFromVertex[u] + 1;
                    // Adding predecessor of u in the array
                    predecessorOfVertex[adj.get(u).get(i)] = u;
                    queue.add(adj.get(u).get(i));
                    
                    // If have reached the destination node, return true
                    if (adj.get(u).get(i) == dest) {
                        return true;
                    }
                }
            }
        }
        // If doesn't found destination node in adjacent nodes then return false.
        return false;
    }

    // Method for getting name of vertex key by using vertex node value
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        
        Set<T> keys = new HashSet<T>();
        // Looping through the collection of vertices
        for (Entry<T, E> entry : map.entrySet()) {
            // checking if vertex's value matches the current vertex's value
            if (Objects.equals(value, entry.getValue())) {
                // Adds key of corresponding vertex name to the set
                keys.add(entry.getKey());
            }
        }
        // Returning vertex name aka key of given node
        return keys;
    }

}
