package Lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Algorithm {
    private Map<Integer, List<Integer>> listOfOuts = new HashMap<>();
    private List<Integer> reachableVirteces = new ArrayList<Integer>();
    private int givenVirtex;

    public Algorithm(int givenVirtex, Map<Integer, List<Integer>> listOfOuts) {
        this.listOfOuts = listOfOuts;
        this.givenVirtex = givenVirtex;
        BFS();
    }

    private void BFS() {
        StringBuilder sb = new StringBuilder();
        reachableVirteces.addAll(listOfOuts.get(givenVirtex));

        sb.append(Colors.green).append(givenVirtex).append(Colors.red).append(" -> ").append(Colors.yellow)
                .append(reachableVirteces)
                .append(Colors.reset).append("\n");

        Set<Integer> visited = new HashSet<>(givenVirtex);
        Set<Integer> enqueued = new HashSet<>(reachableVirteces);
        Queue<Integer> queue = new LinkedList<>(reachableVirteces); /// buvo tikrinama ar queue contains, kas yra o(V)

        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();
            visited.add(currentVertex);
            sb.append(Colors.green).append(currentVertex).append(Colors.red).append(" -> ").append(Colors.yellow)
                    .append("[");

            for (int neighbor : listOfOuts.get(currentVertex)) {
                if (!visited.contains(neighbor) && !enqueued.contains(neighbor)) {
                    queue.add(neighbor);
                    enqueued.add(neighbor);
                    reachableVirteces.add(neighbor);
                    sb.append(neighbor).append(", ");
                }
            }
            if (sb.charAt(sb.length() - 2) == ',') {
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
                sb.append("]").append(Colors.reset).append("\n");
            } else if (sb.charAt(sb.length() - 1) == '[') {
                while (sb.charAt(sb.length() - 1) != ']') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("\n");
            }
        }
        System.out.println(sb.toString());
        System.out.println(Colors.green + reachableVirteces + Colors.reset);
    }

    public List<Integer> getReachableVirteces() {
        return reachableVirteces;
    }
}
