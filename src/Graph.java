// Java Program to print Vertex Cover of a given undirected graph
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// This class represents an undirected graph using adjacency list
class Graph {
    private int V; // No. of vertices

    // Array of lists for Adjacency List Representation
    private LinkedList<Integer> adj[];

    // Constructor
    @SuppressWarnings("unchecked")
    Graph(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i)
            adj[i] = new LinkedList<Integer>();
    }

    // Function to add an edge into the graph
    void addEdge(int v, int w) {
        adj[v].add(w); // Add w to v's list.
        adj[w].add(v); // Graph is undirected
    }

    // The function to print vertex cover
    void printVertexCover() throws FileNotFoundException {
        // Initialize all vertices as not visited.
        boolean visited[] = new boolean[V];
        for (int i = 0; i < V; i++)
            visited[i] = false;

        Iterator<Integer> i;

        // Consider all edges one by one
        for (int u = 0; u < V; u++) {
            // An edge is only picked when both visited[u]
            // and visited[v] are false
            if (visited[u] == false) {
                // Go through all adjacents of u and pick the
                // first not yet visited vertex (We are basically
                // picking an edge (u, v) from remaining edges.
                i = adj[u].iterator();
                while (i.hasNext()) {
                    int v = i.next();
                    if (visited[v] == false) {
                        // Add the vertices (u, v) to the result
                        // set. We make the vertex u and v visited
                        // so that all edges from/to them would
                        // be ignored
                        visited[v] = true;
                        visited[u] = true;
                        break;
                    }
                }
            }
        }

        // Print the vertex cover
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (int j = 0; j < V; j++) {
            if (visited[j]) {
                sb.append(j + "x");
                ++count;
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        try (PrintWriter out =
                new PrintWriter("/home/cheriejw/workspace/java/CECS428/Museum/src/cover.txt")) {
            out.print(sb.toString());
        }
        //System.out.println(sb.toString());
        
        System.out.println(count);
    }

    // Driver method
    public static void main(String args[]) throws FileNotFoundException {
        int vertex;
        File input = new File("/home/cheriejw/workspace/java/CECS428/Museum/src/graph.txt");
        Scanner scan = new Scanner(input);
        String line = scan.next();

        // Create a graph given in the above diagram
        Graph g = new Graph(line.length() - line.replace(":", "").length());

        String[] vertecies = line.split("x");

        // string v is 2:3,4,5
        for (String v : vertecies) {
            vertex = Integer.parseInt(v.split(":")[0]);
            List<Integer> edges = Arrays.stream((v.split(":")[1].split(","))).map(String::trim)
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

            for (int edge : edges) {
                g.addEdge(vertex, edge);
            }
        }
        g.printVertexCover();

        scan.close();
    }
}
