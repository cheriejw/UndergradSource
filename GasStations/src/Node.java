import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Stores the id of the node along with the edges and the edge weights. The connections can be
 * sorted to have the greatest weight be first on the connection List.
 * 
 * @author cheriejw
 */
public class Node {
    private int id;
    private List<Entry<Integer, Integer>> connections;
    
    //Remember to clear, the number it took relative to get to this node.
    public int count;
    private Boolean visited;

    public Node(int id) {
        this.id = id;
        this.visited = false;
        this.connections = new ArrayList<Entry<Integer, Integer>>();
    }

    public void setVisit(Boolean b){
        this.visited = b;
    }
    
    //primative boolean is reference or value?
    public Boolean getVisit(){
        return this.visited;
    }
    
    /**
     * Adding an edge to this node. Node and weight specified in the arguments.
     * 
     * @param key the node id of the node this node is connected to.
     * @param weight the weight of the line from this node to key node.
     */
    public void add(int key, int weight) {
        connections.add(new AbstractMap.SimpleEntry<Integer, Integer>(key, weight));
        this.sort();
    }

    /**
     * Sorts the connections List of Entry to have the greatest weighted edge be first.
     */
    private void sort() {
        Collections.sort(connections, new Comparator<Entry<Integer, Integer>>() {
            public int compare(Entry<Integer, Integer> p1, Entry<Integer, Integer> p2) {
                return (Integer.compare(p1.getValue(), p2.getValue()) * -1);
            }
        });
    }
    
    public List<Entry<Integer, Integer>> getConnections(){
        return connections;
    }
    
    /**
     * Gets the index of connections List<Entry<Integer, Integer>> requested.
     * @param index the index from the sorted connections List<Entry<Integer, Integer>>.
     * @return the Entry<Integer, Integer> at the requested index.
     */
    public Entry<Integer, Integer> get(int index){
        return connections.get(index);
    }
    
    public int getId(){
        return this.id;
    }

    /**
     * @return the number of edges that this node has.
     */
    public int numEdges() {
        return connections.size();
    }
}
