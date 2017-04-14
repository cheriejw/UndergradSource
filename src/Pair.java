import java.util.List;

public class Pair{
    private int key;
    private List<Integer> value;

    public Pair(int k, List<Integer> v) {
        this.key = k;
        this.value = v;
    }

    public int getK() {
        return key;
    }

    public List<Integer> getV() {
        return value;
    }
    
    public void removeV(int link) {
        int i;
        // if (this.value.contains(link)){
        // System.out.println("TRUE CONTAINS.");
        // printV();
        // }
        for(i = 0; this.value.get(i) != link; ++i){
            
        }
        // System.out.println(this.value.get(i));
        this.value.remove(i);
        
        //this.value.remove((Integer)link); //find and remove link int.
        //IntStream.of(a).anyMatch(x -> x == 4);
    }
    
    public void removeAll(){
        this.value.clear();
    }
    
    public void printV(){
        for (int v : this.value){
            System.out.print(v + " ");
        }
        System.out.println(" ");
    }

    public void setK(int k) {
        this.key = k;
    }

    public void setV(List<Integer> v) {
        this.value = v;
    }
    
}
