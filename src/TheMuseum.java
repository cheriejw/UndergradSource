import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TheMuseum {

    private int[] graphR; // edgeCount;
    private boolean[] traveled;
    private ArrayList<Pair> graph;
    private ArrayList<String> gaurds ,edgesMaster;

    public TheMuseum(int size) {
        this.graphR = new int[size];
        this.traveled = new boolean[size];
        // this.edgeCount = new int[size];
        //this.notTraveled = new ArrayList<String>();
        this.graph = new ArrayList<Pair>();
        this.gaurds = new ArrayList<String>();
        this.edgesMaster = new ArrayList<String>();
        Arrays.fill(traveled, false);
    }

    public void cover() {
        sort();
        index();

        // System.out.println(gaurds.size());

        // as a gaurd, gaurding only one hallway. if some is watching you you can leave. ie. if your
        // verticies exist on gaurd list you can leave.

        // for (int i = 0; graph.get(i).getV().size() < 2; ++i) {
        //
        // gaurds.remove("" + graph.get(i).getK());
        //
        // }

        boolean gaurdOff; // if it is false, gaurd cant be relieved of duty.
        // for (int i = 0; i < graphR.length; ++i) {

        Scanner s = new Scanner(System.in);
        Random r = new Random();
        int i;
        // 100 ok
        // 1000 ok
        // Uncovered hallway at 1000 - 10000
        // Uncovered hallway at 1000 - 2000
        // 1500 ok
        // 1700 ok 1800 ok
        // 1999 ok 3000 ok
        // 3k - 5k
        System.out.println("Gaurd with least hallways has " + graph.get(0).getV().size() + " hallways.");
        System.out.println("Gaurd with most hallways has " + graph.get(graph.size() - 1).getV().size() + " hallways.");

        // pre travel process
        for (i = 0; graph.get(i).getV().size() == 1; ++i) {
            for (int j = graph.get(i).getV().get(0); graph.get(graphR[j]).getV().size() != 1; j =
                    graph.get(graphR[j]).getV()
                            .get(r.nextInt(graph.get(graphR[j]).getV().size()))) {
                // j is the next node you go to to check if you can remove that gaurd.
                // if all gaurds are there in your neighbors you can remove yourself.


                gaurdOff = true;
                for (int neighbor : graph.get(graphR[j]).getV()) {
                    gaurdOff = gaurdOff && gaurds.contains("" + neighbor); // neighbor still on
                                                                           // duty...
                    // System.out.println(gaurds.contains("" + neighbor));
                }

                if (gaurdOff == true) {
                    // System.out.println("REMOVED: " + graph.get(i).getK());
                    gaurds.remove("" + j);
                }
            }
        }
        
        System.out.println("Number of gaurds on duty after walk through: " + gaurds.size() + " gaurds.");



        //running this after to make sure all are taken.
        for (i = 0; i < graph.size(); ++i) { // 3700
            gaurdOff = true;
            for (int neighbor : graph.get(i).getV()) {
                // if (neighbor != graph.get(graphR[neighbor]).getK()) {
                // System.out.println("ERROR PLEASE.");
                // }
                // graph(graphR[neighbor]) //getK should be neighbor NOT CHEKCED
                gaurdOff = gaurdOff && gaurds.contains("" + neighbor); // neighbor still on duty...
                // System.out.println(gaurds.contains("" + neighbor));
            }

            if (gaurdOff == true) {
                // System.out.println("REMOVED: " + graph.get(i).getK());
                gaurds.remove("" + graph.get(i).getK());
            } // else {
              // System.out.println("NOT REMOVED: " + graph.get(i).getK());
              // }
              // else you have to stay on duty...
        }

        // 3000 ok 5000 ok 10000 ok 20000 ok
        // for (i = (graph.size()-1); i > (graph.size()-30000); --i){
        // gaurdOff = true;
        // //check if i.getk is contained in gaurds.
        // for (int neighbor : graph.get(i).getV()) {
        // if (neighbor != graph.get(graphR[neighbor]).getK()) {
        // System.out.println("ERROR PLEASE.");
        // }
        // // graph(graphR[neighbor]) //getK should be neighbor NOT CHEKCED
        // gaurdOff = gaurdOff && gaurds.contains("" + neighbor); // neighbor still on duty...
        // // System.out.println(gaurds.contains("" + neighbor));
        // }
        //
        // if (gaurdOff == true) {
        // //System.out.println("REMOVED: " + graph.get(i).getK());
        // gaurds.remove("" + graph.get(i).getK());
        // } //else {
        // //System.out.println("NOT REMOVED: " + graph.get(i).getK());
        // //}
        // // else you have to stay on duty...
        // }
        System.out.println("Number of gaurds on duty after global check: " + gaurds.size() + " gaurds.");
        // System.out.println(edgesMaster.size());
        s.close();
    }
    
    public void desprate(){
        sort();
        index();

        boolean gaurdOff; // if it is false, gaurd cant be relieved of duty.
        // for (int i = 0; i < graphR.length; ++i) {

        Random r = new Random();
        int i;
        System.out.println("Gaurd with least hallways has " + graph.get(0).getV().size() + " hallways.");
        System.out.println("Gaurd with most hallways has " + graph.get(graph.size() - 1).getV().size() + " hallways.");

        // cant be wrong, all the ones looking at only one hall can leave.
        for (i = 0; graph.get(i).getV().size() == 1; ++i) {
            gaurds.remove("" + graph.get(i).getK());
            traveled[graph.get(i).getK()] = true;
            traveled[graph.get(i).getV().get(0)] = true;
        }
        
        System.out.println("Number of gaurds on duty after removing those gaurding one hallway: " + gaurds.size() + " gaurds.");

        int rand = r.nextInt(graphR.length), counter = 0;
        while (!allTraveled()){
            ++counter;
            if(counter > 200000){
                for(int k = 0; k < traveled.length; ++k){
                    if(!traveled[k]){
                        rand = k;
                    }
                }
            } else {
                while (traveled[rand]){
                    rand = r.nextInt(graphR.length);
                }
            }
            gaurdOff = true;
            for (int neighbor : graph.get(graphR[rand]).getV()) {
                gaurdOff = gaurdOff && gaurds.contains("" + neighbor);
            }

            if (gaurdOff == true) {
                gaurds.remove("" + rand);
            } 
        }
        System.out.println("Number of gaurds on duty after random cuts: " + gaurds.size() + " gaurds.");
        for (i = 0; i < graph.size(); ++i) { // 3700
            gaurdOff = true;
            for (int neighbor : graph.get(i).getV()) {
                gaurdOff = gaurdOff && gaurds.contains("" + neighbor);
            }

            if (gaurdOff == true) {
                gaurds.remove("" + graph.get(i).getK());
            } 
        }
        System.out.println("Number of gaurds on duty after global check: " + gaurds.size() + " gaurds.");
    }
    
    public boolean allTraveled()
    {
        for(boolean b : traveled) if(!b) return false;
        return true;
    }

    // pass in a string formatted as such: "2:1,3,4")
    public void add(String v) {
        // System.out.println("adding");
        int vertex = Integer.parseInt(v.split(":")[0]);
        List<Integer> edges = Arrays.stream((v.split(":")[1].split(","))).map(String::trim)
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        graph.add(new Pair(vertex, edges));
        gaurds.add("" + vertex);
        // edgeCount[vertex] = edges.size();
        for (int edg : edges) {
            if (edg < vertex) {
                edgesMaster.add(edg + "," + vertex);
            } else {
                edgesMaster.add(vertex + "," + edg);
            }
        }
    }

    public void tester(String testLine) {
        String[] gaurs = testLine.split("x");
        List<Integer> edges;
        List<String> edgeCk = new ArrayList<String>();
        // look gaurs up and add their edges
        for (String g : gaurs) {
            edges = graph.get(graphR[Integer.parseInt(g)]).getV();
            for (int edg : edges) {
                if (Integer.parseInt(g) < edg) {
                    edgeCk.add(g + "," + edg);
                } else {
                    edgeCk.add(edg + "," + g);
                }
            }
        }

        System.out.println("Edges added...");
        // check that edgeCk has all edgesMaster

        for (String e : edgesMaster) {
            if (!edgeCk.contains(e)) {
                System.out.print(e + " is uncovered edge!!");
            } // else
              // System.out.print(".");
        }

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
        for (String g : gaurds) {
            sb.append(g + "x");
        }
        // sb.deleteCharAt(sb.length() - 1);

        try (PrintWriter out =
                new PrintWriter("/home/cheriejw/workspace/java/CECS428/Museum/src/cover.txt")) {
            out.print(sb.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        File input = new File("/home/cheriejw/workspace/java/CECS428/Museum/src/graph.txt");
        Scanner scan = new Scanner(input);
        String line = scan.next();

        // System.out.println(line.length() - line.replace(":", "").length());
        TheMuseum instance = new TheMuseum(line.length() - line.replace(":", "").length());
        String[] vertecies = line.split("x");

        for (String v : vertecies) {
            instance.add(v);
        }

        // System.out.println(instance.get(49999)[0]);
        //instance.cover();
        instance.desprate();
        instance.printGaurds();

        // File check = new File("/home/cheriejw/workspace/java/CECS428/Museum/src/cover.txt");
        // Scanner scanT = new Scanner(check);
        // String testLine = scanT.next();
        // 1instance.tester(testLine);

        // scanT.close();
        scan.close();
    }
}
