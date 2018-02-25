import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * Goal of the program is to have minimum gas stations in k distance of each other. Can start on the
 * node with the smallest out nodes and place one closest to 30 from it as possible, and move the
 * gas stations.
 * 
 * Uses a DFS to find the nodes within range of a node. Prioritizing the nodes with most
 * 
 * @author cheriejw
 */
public class GasStations {
    // GasStations private variables
    // private String[] nodes;
    private Node[] data;
    // dependency is the number of nodes depending on that station index.
    private int[] index, dependency;
    private ArrayList<String> stations;
    private int max;
    private boolean[] traveled;
    private List<Integer>[] inRange;

    // TODO: Remove/refactor any extra data structures
    @SuppressWarnings("unchecked")
    public GasStations(int k, int size) {
        this.max = k;
        // this.nodes = lines;
        this.data = new Node[size];
        this.stations = new ArrayList<String>();
        // this.initialize();
        this.traveled = new boolean[size];
        this.index = new int[size];
        this.dependency = new int[size];
        Arrays.fill(dependency, 0);
        Arrays.fill(traveled, false);

        // Storing in node object or not.
        this.inRange = (List<Integer>[]) new List[size];
        for (int i = 0; i < size; ++i) {
            inRange[i] = (new ArrayList<Integer>());
        }
    }

    public void reduce() {
        int count = 0;
        for (int i = 0; i < data.length; ++i) {
            stations.add("" + i);
        }

        // start removing from the front. those have less dependency so should be less significant.
        // nodes kofme are dependent on that node. if those nodes have at least one in station then
        // get rid of this.

        populateRange();
        reduceSort();

        // Can remove if nodes depending on that station has one of its kofme list is still in
        // stations list
        // nodes with >1 in range one of stations can be removed potentially.
        // place stations that have more dependent on it first.
        
        for(Node n : data){
            if(removable(n.getId())){
                //if there is a station in range
                stations.remove("" + n.getId());
            }
            //check to remove.
        }
    }
    
    private boolean removable(int id){
        for (int i : inRange[id]){
            if(stations.contains(i + "")){
                return true;
            }
        }
        return false;
    }
    
    /**
     * After reduce has been run, this method will move the stations around and see if it can remove even more.
     */
    public void minimize() {
         
    }

    public void reduceSort() {
        Arrays.sort(data, new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return Integer.compare(dependency[n1.getId()], dependency[n2.getId()]);
                // -1 * so that goes other way and greater nodes are first, nodes with more
                // dependencies.
            }
        });

        for (int i = 0; i < data.length; ++i) {
            index[data[i].getId()] = i;
        }
    }

    private void populateRange() {
        for (int i = 0; i < data.length; ++i) { // 0 - 10647
            inRange[i] = kOfMe(i);
            // counting how many nodes depend on that variable.
            for (int nodeId : inRange[i]) {
                ++dependency[nodeId];
            }
        }
    }

    // have a gas on every node from node x.
    // node with least connections sorted first.
    public void probe() {

        int count = 0, i;
        long start;
        sort();

        // add all finger ends.
        for (i = 0; data[i].numEdges() == 1; ++i) {
            traveled[data[i].getId()] = true;
            stations.add(data[i].getId() + "");
        }

        start = System.currentTimeMillis();
        for (i = 0; !allTraveled();) {// if you can use for loop use it.
            // while not all thraveled explore until you find next base.
            // set base to this, base is last gas station placement.
            // if this i is not traveled proceed.
            // otherwise go to next i.

            if (allTraveled(data[i].getId())) {// if all nodes traveled go to the node not trvled

                for (int b = 0; b < traveled.length; ++b) {
                    if (traveled[b] == false) {
                        // System.out.println(
                        // "In the neighbors all traveled loop." + System.currentTimeMillis() + " "
                        // + index[b]);
                        i = index[b];
                    }
                }
            }
            // this loop just looks for the next one that is not traveled and travels one.
            // infinate loop in here
            traveled[data[i].getId()] = true;
            // System.out.println("BEFORE THE INFINATE LOOP.");
            for (int k = 0; k < data[i].numEdges(); ++k) { // THIS FORLOOP ENDS WHEN ALL THE
                                                           // EDGES OF BASE NODE IS EXPLORED.
                if (!traveled[data[i].get(k).getKey()]) { // if you havent traveled this edge.
                    // System.out.println(data[i].get(k).getKey() + " IS NOT TRAVELED.");
                    if (count + data[i].get(k).getValue() > max) { // if you cant make it to the
                                                                   // next station.
                        // if (data[index[data[i].get(k).getKey()]].numEdges() >
                        // data[i].numEdges()) {
                        stations.add(data[i].get(k).getKey() + "");
                        count = 0; // reset step
                        traveled[data[i].get(k).getKey()] = true;
                        i = index[data[i].get(k).getKey()];// BEACUSE I KEEP CHANGING i....
                        // }
                    } else {
                        count = count + data[i].get(k).getValue();
                        traveled[data[i].get(k).getKey()] = true;
                        i = index[data[i].get(k).getKey()];
                        // System.out.println("YOU CAN GO WITHOUT RUN OUT OF GAS.");
                        // you can travel to this one and potentially put a gas there.
                    }
                    // System.out.printf("One iteration of not traveled for loop. %d i value is
                    // %d\n",
                    // System.currentTimeMillis(), data[i].get(k).getKey());
                    break;
                } // else {
                  // System.out.println(data[i].get(k).getKey() + " IS TRAVELED.");
                  // }

                // if edge traveled go to next edge from node.
                // if put gas station down, break.
            }

            // stuck between 4 and 5
        }

        // for reassurance move gas stations, find high weights.

        // when adding to node object it auto puts edge with smalles mile first. go to last for
        // extreme;
        // start at the ones with one edge out. and for certain i am not going to put a gasa station
        // there.
        // if a node has many outbounds i may want to put one there.
        // if there is a road that is k distance i will have to put a gas station on each node... or
        // not as long as the city has a city near it that is that
    }

    private boolean allTraveled(int node) {
        for (Entry<Integer, Integer> neighbor : data[index[node]].getConnections())
            if (!traveled[neighbor.getKey()])
                return false;
        return true;
    }

    // Checks if all values are true, if false returns false.
    private boolean allTraveled() {
        for (boolean b : traveled)
            if (!b)
                return false;
        return true;
    }

    /**
     * Given a String[] nodes it turns each of the Strings in the nodes String[] into a Node in the
     * data Node[]. Basically a parsing method, instead of parsing yourself and adding.
     * 
     * @param nodes the String of the lines in this format : 2:[1,15][3,2][486,2][24,2]
     *        index:[edgeId,edgeWeight][edgeId,edgeWeight].
     */
    public void initialize(String[] nodes) {
        String[] splitHelper, edge;

        System.out.println(nodes.length);
        for (int i = 0; i < nodes.length; ++i) {
            System.out.println(nodes[i]);
            splitHelper = nodes[i].split(":");
            // Integer.parseInt(splitHelper[0]) should == i
            System.out.println(splitHelper.length + " INDEX" + splitHelper[0]);
            data[i] = new Node(i);
            splitHelper = splitHelper[1].split("\\[\\]|\\[|\\]");
            for (String s : splitHelper) {
                if (!s.equals("")) {
                    edge = s.split(",");

                    data[i].add(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
                }
            }
        }
    }

    public void add(int idx, Node n) {
        data[idx] = n;
    }

    public void print(String file) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (String s : stations) {
            sb.append(s + "x");
        }

        try (PrintWriter out = new PrintWriter(file)) {
            out.print(sb.toString());
        }

    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        for (String s : stations) {
            sb.append(s + "x");
        }
        System.out.println(sb.toString());
    }

    /**
     * Sorts the data Node[] in the order of Node with smallest amount of edges to the Node with the
     * greatest amount of edges, and indexes the Node's new position in the data Node[] with the
     * Node's id in the index int[].
     */
    private void sort() {
        Arrays.sort(data, new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return Integer.compare(n1.numEdges(), n2.numEdges());
            }
        });

        for (int i = 0; i < data.length; ++i) {
            index[data[i].getId()] = i;
        }
    }


    /**
     * Using DFS finds all the nodes k distance of the node you passed in.
     * 
     * @param node the integer id of the node you wish to evaluate.
     * @return an ArrayList<Integer> of the nodes that are within k of this node.
     */
    public ArrayList<Integer> kOfMe(int nodeId) {
        ArrayList<Integer> container = new ArrayList<Integer>();
        Entry<Integer, Integer> current;
        //node is reference
        data[index[nodeId]].count = 0;
        //TODO: change to BFS.

        Deque<Node> stack = new ArrayDeque<Node>();
        stack.add(data[index[nodeId]]);
        
        System.out.println(data[index[nodeId]].getVisit() + " " + data[index[nodeId]].getId() + " " + data[index[nodeId]].get(0).getKey() + " " + data[index[data[index[nodeId]].get(0).getKey()]].getVisit());
        
        data[index[nodeId]].setVisit(true); // data[index[nodeId]].setVisit(true);
        System.out.println(data[index[nodeId]]);
        System.out.println(data[index[nodeId]].getVisit() + " " + data[index[nodeId]].getId() + " " + data[index[nodeId]].get(0).getKey() + " " + data[index[data[index[nodeId]].get(0).getKey()]].getVisit());
        //data[5].getVisit() is false.
        while (!stack.isEmpty()) {
            Node element = stack.pop();
            //element is the same node the I put on in itial run
            System.out.println(element);
            // Get the neighbors of the element you just popped
            List<Entry<Integer, Integer>> neighbours = element.getConnections();

            // Exploring the node last popped's neighbors.
            for (int i = 0; i < neighbours.size(); i++) {
                // node last popped's neighbor's id and cost
                current = data[index[element.getId()]].get(i);

                // n is node last popped's neighbor.
                Node n = data[index[neighbours.get(i).getKey()]];
                // If you can travel there
                System.out.println(current.getValue());
                System.out.println((current.getValue() + element.count <= max) + " " + n.getVisit());
                if ((current.getValue() + element.count <= max) && !n.getVisit()) {
                    // add to stack to explore later.
                    stack.add(n);

                    // setting its relative cost.
                    n.count = current.getValue() + element.count;
                    n.setVisit(true);;
                }
            }
        }
        return container;
    }

    /**
     * Main program to run the code.
     * 
     * @param args are any arguments the user decides to pass into the program.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String input = "/home/cheriejw/workspace/java/CECS428/GasStations/bin/test_network.txt", option;

        Scanner file = new Scanner(new File(input));
        LineNumberReader lnr = new LineNumberReader(new FileReader(input));
        lnr.skip(Long.MAX_VALUE);
        String[] lines = new String[lnr.getLineNumber() - 1];
        Scanner keyboard = new Scanner(System.in);

        int k = Integer.parseInt(file.nextLine());

        for (int i = 0; i < lines.length; ++i) {
            lines[i] = file.nextLine();
        }

        String[] splitHelper, edge;
        Node container;
        int key;

        GasStations instance = new GasStations(k, lines.length);

        // THIS IS TERRIBLE SHOULD JUST MAKE IT AN ARRAYLIST TO ADD.
        for (int i = 0; i < lines.length; ++i) {
            splitHelper = lines[i].split(":");
            key = Integer.parseInt(splitHelper[0]);
            container = new Node(key);
            splitHelper = splitHelper[1].split("\\[\\]|\\[|\\]");
            // splitHelper contains some strings that are ""
            for (String s : splitHelper) {
                if (!s.equals("")) {
                    edge = s.split(",");
                    container.add(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
                }
            }
            instance.add(key, container);
        }

        for (int c : instance.kOfMe(0)) {
            System.out.println(c);
        }
        
        System.out.println("Enter command: (q, optimize, probe, reduce) ");
        
        while(true){
            option = keyboard.next();
            
            if(option.equals("q")) {
                break;
            } else if(option.equals("optimize")){
                instance.minimize();
                instance.print();
                instance.print("/home/cheriejw/workspace/java/CECS428/GasStations/bin/stations.txt");
            } else if(option.equals("probe")){
                instance.probe();
                instance.print();
                instance.print("/home/cheriejw/workspace/java/CECS428/GasStations/bin/stations.txt");
            } else if(option.equals("reduce")){
                instance.reduce();
                instance.print();
                instance.print("/home/cheriejw/workspace/java/CECS428/GasStations/bin/stations.txt");
            } else {
                System.out.println("Enter command: (q, optimize, probe, reduce) ");
            }
        }

        keyboard.close();
        file.close();
        lnr.close();
    }
}
