
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

public class MuseumTest {

    @Test
    public void test() throws FileNotFoundException {
        File input = new File("/home/cheriejw/workspace/java/CECS428/Museum/src/graph.txt");
        Scanner scan = new Scanner(input);
        String line = scan.next();

        int[] five = {18142, 14615, 37808, 24155, 31536};
        int[] twosixfive = {654, 10046, 28060, 26724, 22992, 49843, 8063, 37224, 17447, 29978};

        Museum instance = new Museum(line.length() - line.replace(":", "").length());
        String[] vertecies = line.split("x");

        for (String v : vertecies) {
            instance.add(v);
        }

        int temp = instance.get(5).get(0);
        assertEquals(five[0], temp);
        assertEquals(twosixfive[0], (int)instance.get(265).get(0));

        scan.close();
    }

}
