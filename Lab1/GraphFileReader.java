package Lab1;

import java.io.*;
import java.util.*;

public class GraphFileReader {
    private Map<Integer, List<Integer>> listOfOuts = new HashMap<>();
    private Map<Integer, List<Integer>> listOfIns = new HashMap<>();

    public GraphFileReader() {
        main();
    }

    public void main() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader("C:\\Main Files\\Programing\\Grafai\\Lab1\\CustomGraph.txt"))) {
            String line;
            boolean isOuts = true;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("List Of Outs:")) {
                    isOuts = true;
                    continue;
                } else if (line.startsWith("List Of Ins:")) {
                    isOuts = false;
                    continue;
                }

                String[] parts = line.split(" ");
                int vertex = Integer.parseInt(parts[0]);
                List<Integer> vertices = new ArrayList<>();

                for (int i = 1; i < parts.length; i++) {
                    vertices.add(Integer.parseInt(parts[i]));
                }

                if (isOuts) {
                    listOfOuts.put(vertex, vertices);
                } else {
                    listOfIns.put(vertex, vertices);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, List<Integer>> getListOfOuts() {
        return listOfOuts;
    }

    public Map<Integer, List<Integer>> getListOfIns() {
        return listOfIns;
    }
}
