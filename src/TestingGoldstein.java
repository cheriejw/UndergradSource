import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TestingGoldstein {
    public static void main(String[] args) throws FileNotFoundException{
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < 50000; ++i) {
                sb.append(i + "x");
        }
        sb.deleteCharAt(sb.length() - 1);
        try (PrintWriter out =
                new PrintWriter("/home/cheriejw/workspace/java/CECS428/Museum/src/cover.txt")) {
            out.print(sb.toString());
        }
        
        
        //---------------------------------------------------------
        
//        File input = new File("/home/cheriejw/workspace/java/CECS428/Museum/src/graph.txt");
//        Scanner scan = new Scanner(input);
//        String line = scan.next();
//        StringBuilder sb = new StringBuilder();
//        
//        String[] lines = line.split("x");
//        for (String l : lines){
//            sb.append((l.split(":")[0]) + "x");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        
//        try (PrintWriter out =
//                new PrintWriter("/home/cheriejw/workspace/java/CECS428/Museum/src/cover.txt")) {
//            out.print(sb.toString());
//        }
//        
//        scan.close();

    }
}
