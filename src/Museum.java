import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Naive solution to sort ones with the most vertex in array of list.
 * 
 * @author cheriejw
 *
 */
public class Museum {

    private int[] graphR;
    private ArrayList<Pair> graph;
    // connection from v1 to v2. smaller one first.
    // private Set<String> validEdges;
    private ArrayList<Integer> gaurds;

    public Museum(int size) {
        this.graphR = new int[size];
        this.graph = new ArrayList<Pair>();
        // this.validEdges = new HashSet<String>();
        this.gaurds = new ArrayList<Integer>();
    }

    // the one with the biggest size get to get connected first.
    public void lnaive() {
        // sort so that the smallest to biggest
        sort();
        index();

        // list of edges that will remove when
        // edge is made by two vert.
        // remove that vert when put on the stack.

    }
    
    public void approxAlg(){
        
    }

    public void naive() {
        sort();
        index();

        int i;

        // rid of must haves
        for (i = 0; graph.get(i).getV().size() == 1; ++i) {
            System.out.println(
                    "Removing: " + graph.get(i).getK() + " with " + graph.get(i).getV().get(0));
            // for all end edge add one before. elbow.
            addGaurd(graph.get(i).getV().get(0));
            // graph.remove(i);
            // System.out.println("Removed: " + graph.get(i));
        }

        // i is where you start for size == 2.

        // sort and index then all of the added will be at then because they have 0.
        System.out.println(gaurds.size());
        System.out.println(graph.get(i).getV().size());
        // + " " + graph.get(i).getV().get(0) + " " + graph.get(i).getK() + " i is " + i);

        sort();
        index();
        System.out.println(graph.get(49999).getV().size() + " curr is " + i);

        for (i = graph.size() - 1; (!(graph.get(i).getV().size() == 0)); --i) {
            System.out.println(
                    "Removing: " + graph.get(i).getK() + " with " + graph.get(i).getV().get(0));
            // for all end edge add one before. elbow.
            addGaurd(graph.get(i).getV().get(0));
            // graph.remove(i);
            // System.out.println("Removed: " + graph.get(i));
        }

        System.out.println(gaurds.size());
        System.out.println(graph.get(i).getV().size());
        
        sort();
        index();
        
        while (!(graph.get(graph.size()-1).getV().size() == 0)){
            
            System.out.println(graph.get(49999).getV().size() + " curr is " + i);

            for (i = graph.size() - 1; (!(graph.get(i).getV().size() == 0)); --i) {
                System.out.println(
                        "Removing: " + graph.get(i).getK() + " with " + graph.get(i).getV().get(0));
                // for all end edge add one before. elbow.
                addGaurd(graph.get(i).getV().get(0));
                // graph.remove(i);
                // System.out.println("Removed: " + graph.get(i));
            }

            System.out.println(gaurds.size());
            System.out.println(graph.get(i).getV().size());
            sort();
            index();
        }

    }


    public void addGaurd(int index) {
        gaurds.add(index);
        // System.out.println("Removing: " + index);
        // remove from valid edges.
        graph.get(graphR[index]).printV();
        for (int node : graph.get(graphR[index]).getV()) {
            System.out.print(node + " : ");
            graph.get(graphR[node]).printV();
            graph.get(graphR[node]).removeV(index);
            System.out.print(
                    "Removed: " + index + " from " + graph.get(graphR[node]).getK() + " now has ");
            for (int nodes : graph.get(graphR[node]).getV()) {
                System.out.print(nodes + " ");
            }
            System.out.println(" ");
            // validEdges.remove(hash(node, index));
        }
        System.out.println(index + " " + graph.get(graphR[index]).getV().size());
        graph.get(graphR[index]).removeAll();
        // System.out.println(index + " " + graph.get(graphR[index]).getV().size());
        // graph.remove(graphR[index]);
        // remove from graph travel to nodes connected to elbow and remove elbow from them.
    }

    // pass in a string formatted as such: "2:1,3,4")
    public void add(String v) {
        // String edgeHash;
        int vertex = Integer.parseInt(v.split(":")[0]);
        List<Integer> edges = Arrays.stream((v.split(":")[1].split(","))).map(String::trim)
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        // graphR[vertex] = Arrays.stream((v.split(":")[1].split(","))).map(String::trim)
        // .mapToInt(Integer::parseInt).toArray();
        // System.out.println(vertex);
        graph.add(new Pair(vertex, edges));
    }

    private void sort() {
        Collections.sort(graph, new Comparator<Pair>() {
            public int compare(Pair p1, Pair p2) {
                return Integer.compare(p1.getV().size(), p2.getV().size());
            }
        });

    }

    private void index() {
        for (int i = 0; i < graph.size(); ++i) {
            graphR[graph.get(i).getK()] = i;
        }
    }

    public List<Integer> get(int index) {
        return graph.get(index).getV();
        // return graph[index];
    }

    public void printGaurds() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int g : gaurds) {
            sb.append(g + "x");
        }
        //sb.deleteCharAt(sb.length() - 1);

        try (PrintWriter out = new PrintWriter("/home/cheriejw/workspace/java/CECS428/Museum/src/cover.txt")) {
            out.print(sb.toString());
        }
        System.out.println(gaurds.size());
    }

    public static void main(String[] args) throws IOException {
        File input = new File("/home/cheriejw/workspace/java/CECS428/Museum/src/graph.txt");
        Scanner scan = new Scanner(input);
        String line = scan.next();

        // System.out.println(line.length() - line.replace(":", "").length());
        Museum instance = new Museum(line.length() - line.replace(":", "").length());
        String[] vertecies = line.split("x");

        for (String v : vertecies) {
            instance.add(v);
        }

        // System.out.println(instance.get(49999)[0]);
        instance.naive();
        instance.printGaurds();
        
        scan.close();
    }
}
