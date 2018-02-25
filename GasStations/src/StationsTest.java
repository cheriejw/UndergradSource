import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;

import org.junit.Test;

public class StationsTest {

    @Test
    public void test() throws IOException {
        String input = "/home/cheriejw/workspace/java/CECS428/GasStations/bin/test_network.txt";

        Scanner file = new Scanner(new File(input));
        LineNumberReader lnr = new LineNumberReader(new FileReader(input));
        lnr.skip(Long.MAX_VALUE);


        String[] lines = new String[lnr.getLineNumber() - 1];

        assertEquals(lnr.getLineNumber(), 7);

        int k = file.nextInt();
        assertEquals(k, 10);

        for (int i = 0; i < lines.length; ++i) {
            lines[i] = file.nextLine();
        }

        String[] splitHelper, edge;
        Node container;
        int key;

        GasStations instance = new GasStations(k, lines.length);

        for (int i = 0; i < lines.length; ++i) {
            splitHelper = lines[i].split(":");
            key = Integer.parseInt(splitHelper[0]);
            container = new Node(key);
            splitHelper = splitHelper[1].split("\\[\\]|\\[|\\]");
            for (String s : splitHelper) {
                if (!s.equals("")) {
                    edge = s.split(",");
                    // System.out.println(edge[0] + " " + edge[1]);
                    container.add(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
                }
            }
            instance.add(key, container);
        }
        instance.probe();

        instance.print();
        // instance.print("/home/cheriejw/workspace/java/CECS428/GasStations/bin/stations.txt");

        file.close();
        lnr.close();
    }

}
