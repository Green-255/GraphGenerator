package Lab1;

import java.util.List;
import java.util.Map;

public class printGraph {
    Map<Integer, List<Integer>> listOfIns;
    Map<Integer, List<Integer>> listOfOuts;

    public printGraph(Graph graph) {
        listOfOuts = graph.getGraph();
        listOfIns = graph.getListOfIns();
        print();
    }

    public printGraph(Map<Integer, List<Integer>> listOfIns, Map<Integer, List<Integer>> listOfOuts) {
        this.listOfOuts = listOfOuts;
        this.listOfIns = listOfIns;
        print();
    }

    private void print() {

        System.out.println(Colors.green + "List Of Outs:" + Colors.reset);
        for (Map.Entry<Integer, List<Integer>> entry : listOfOuts.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println(Colors.green + "List Of Ins:" + Colors.reset);
        for (Map.Entry<Integer, List<Integer>> entry : listOfIns.entrySet()) {
            System.out.println(entry.getKey() + " <- " + entry.getValue());
        }
    }
}
