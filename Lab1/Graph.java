package Lab1;

import java.util.*;

public class Graph {

    private int numVertices;
    private int maxIn, minIn, maxOut, minOut;
    private Map<Integer, List<Integer>> listOfOuts = new HashMap<>();
    private Map<Integer, List<Integer>> listOfIns = new HashMap<>();
    private Random rand = new Random();

    public Graph(Map<Integer, List<Integer>> listOfOuts, Map<Integer, List<Integer>> listOfIns, int numVertices,
            int maxIn, int maxOut, int minIn, int minOut) {
        this.numVertices = numVertices;
        this.maxIn = maxIn;
        this.minIn = minIn;
        this.maxOut = maxOut;
        this.minOut = minOut;
        this.listOfOuts = listOfOuts;
        this.listOfIns = listOfIns;
    }

    public Graph(int numVertices, int maxIn, int maxOut, int minIn, int minOut) {
        this.numVertices = numVertices;
        this.maxIn = maxIn;
        this.minIn = minIn;
        this.maxOut = maxOut;
        this.minOut = minOut;
        generateRandomGraph();
    }

    public void generateRandomGraph() {

        /// sugeneruojamos virsunes
        for (int i = 0; i < numVertices; i++) {
            listOfOuts.put(i, new ArrayList<Integer>());
            listOfIns.put(i, new ArrayList<Integer>());
        }

        for (int i = 0; i < numVertices; i++) {
            int outDegree = rand.nextInt(maxOut - minOut + 1) + minOut;
            for (int j = 0; j < outDegree; j++) {
                int target;
                do {
                    target = rand.nextInt(numVertices);
                } while (target == i || listOfOuts.get(i).contains(target) || listOfIns.get(target).size() >= maxIn);
                listOfOuts.get(i).add(target);
                listOfIns.get(target).add(i);
            }
        }
    }

    /// validuojama ar nepazeistos taisykles
    public void graphValidator() {
        for (int i = 0; i < numVertices; i++) {
            int size = listOfOuts.get(i).size();
            int sizeOfIns = listOfIns.get(i).size();
            if (size < minOut) {
                System.out.println(Colors.red + "minOut rule violated for " + Colors.yellow + i + Colors.reset + ".");
                minOutViolationFix(i, size);
            } else if (size > maxOut) {
                System.out.println(Colors.red + "maxOut rule violated for " + Colors.yellow + i + Colors.reset + ".");
            } else if (sizeOfIns < minIn) {
                System.out.println(Colors.red + "minIn rule violated for " + Colors.yellow + i + Colors.reset + ".");
                minInViolationFix(i, sizeOfIns);
            } else if (sizeOfIns > maxIn) {
                System.out.println(Colors.red + "maxIn rule violated for " + Colors.yellow + i + Colors.reset + ".");
            }
            for (int j = 0; j < size; j++) {
                int target = listOfOuts.get(i).get(j);
                if (!listOfIns.get(target).contains(i)) {
                    System.out.println(Colors.yellow + i + Colors.red + " points to " + Colors.yellow
                            + target + Colors.red + " but " + Colors.yellow + target
                            + Colors.red + " does not have In of " + Colors.yellow + i + Colors.reset + ".");
                    outInNotMatchingFix(i, target);
                }
            }
            for (int j = 0; j < sizeOfIns; j++) {
                int in = listOfIns.get(i).get(j);
                if (!listOfOuts.get(in).contains(i)) {
                    System.out.println(Colors.yellow + i + Colors.red + " has In of " + Colors.yellow + in
                            + Colors.red + " but " + Colors.yellow + in + Colors.red
                            + " does not point to " + Colors.yellow + i + Colors.reset + ".");
                    inOutNotMatchingFix(i, in);
                }
            }
        }
    }

    private void outInNotMatchingFix(int x, int y) {
        if (listOfIns.get(y).size() >= maxIn) {
            if (listOfOuts.get(y).size() == minOut) {
                System.out.println(Colors.red + "Cannot fix, " + Colors.yellow + y
                        + Colors.red + " already has maximum number of vertices pointing to it." + Colors.reset);
                new printGraph(listOfIns, listOfOuts);
                System.exit(0);
            }
            listOfOuts.get(y).remove(listOfOuts.get(y).indexOf(x));
            System.out.println(y + " -> " + x + " removed");
        }
        listOfIns.get(y).add(x);
        System.out.println(y + " <- " + x);
    }

    private void inOutNotMatchingFix(int x, int y) {
        if (listOfOuts.get(y).size() >= maxOut) {
            if (listOfIns.get(y).size() == minIn) {
                System.out.println(Colors.red + "Cannot fix, " + Colors.yellow + y
                        + Colors.red + " already points to maximum number of vertices." + Colors.reset);
                new printGraph(listOfIns, listOfOuts);
                System.exit(0);
            }
            listOfIns.get(y).remove(listOfIns.get(y).indexOf(x));
            System.out.println(y + " <- " + x + " removed");
        }
        listOfOuts.get(y).add(x);
        System.out.println(y + " -> " + x);
    }

    private void minOutViolationFix(int violatedVertice, int size) {
        List<Integer> unavalable = new ArrayList<Integer>(listOfOuts.get(violatedVertice));
        unavalable.add(violatedVertice);
        int newOut;
        int sizeOfIns;
        do {
            do {
                newOut = rand.nextInt(numVertices);
                sizeOfIns = listOfIns.get(newOut).size();
                if (unavalable.contains(newOut)) { //// reikejo dar vieno ifo, nes .add() prideda duplikatus
                    continue;
                } else if (sizeOfIns >= maxIn) {
                    unavalable.add(newOut);
                }
                if (unavalable.size() == numVertices) {
                    System.out.println(Colors.red
                            + "Graph cannot be generated, " + Colors.yellow
                            + "as there is minOut violation and  all Vertices already have maxOut."
                            + Colors.reset);
                    new printGraph(listOfIns, listOfOuts);
                    System.exit(0);
                }
            } while (sizeOfIns >= maxIn
                    || unavalable.contains(newOut));

            listOfOuts.get(violatedVertice).add(newOut);
            System.out.println(violatedVertice + " -> " + newOut);
            listOfIns.get(newOut).add(violatedVertice);
            System.out.println(newOut + " <- " + violatedVertice);
            size++;
            unavalable.add(newOut);
        } while (size < minOut);
    }

    private void minInViolationFix(int violatedVertice, int sizeOfIns) {
        List<Integer> unavalable = new ArrayList<Integer>(listOfIns.get(violatedVertice));
        unavalable.add(violatedVertice);
        int newIn;
        int sizeOfOuts;
        do {
            do {
                newIn = rand.nextInt(numVertices);
                sizeOfOuts = listOfOuts.get(newIn).size();
                if (unavalable.contains(newIn)) {
                    continue;
                } else if (sizeOfOuts >= maxOut) {
                    unavalable.add(newIn);
                }
                if (unavalable.size() == numVertices) {
                    System.out.println(Colors.red
                            + "Graph cannot be generated, " + Colors.yellow
                            + "as there is minIn violation and all Vertices already have maxIn."
                            + Colors.reset);
                    new printGraph(listOfIns, listOfOuts);
                    System.exit(0);
                }
            } while (sizeOfOuts >= maxOut
                    || unavalable.contains(newIn));

            listOfIns.get(violatedVertice).add(newIn);
            System.out.println(violatedVertice + " <- " + newIn);
            listOfOuts.get(newIn).add(violatedVertice);
            System.out.println(newIn + " -> " + violatedVertice);
            sizeOfIns++;
            unavalable.add(newIn);
        } while (sizeOfIns < minIn);

    }

    public void check(int x, int y) {
        if (listOfOuts.get(x).contains(y)) {
            System.out.println(Colors.green + "Edge exists." + Colors.reset);
        } else {
            System.out.println(Colors.yellow + "Edge does not exist." + Colors.reset);
        }
    }

    public boolean add(int x, int y) {
        if (listOfOuts.get(x).contains(y)) {
            System.out.println(Colors.yellow + "Edge already exists." + Colors.reset);
            return false;
        } else if (listOfOuts.get(x).size() == maxOut) {
            System.out.println(Colors.yellow + x + Colors.red
                    + " already points to maximum number of vertices." + Colors.reset);
            return false;
        } else if (listOfIns.get(y).size() == maxIn) {
            System.out.println(Colors.yellow + y + Colors.red
                    + " already has maximum number of vertices pointing to it." + Colors.reset);
            return false;
        }
        listOfOuts.get(x).add(y);
        listOfIns.get(y).add(x);
        System.out.println(Colors.green + "Edge added." + Colors.reset);
        return true;
    }

    public boolean remove(int x, int y) {
        if (!listOfOuts.get(x).contains(y)) {
            System.out.println(Colors.yellow + x + Colors.red
                    + " does not point to " + Colors.yellow + y + Colors.reset + ".");
            return false;
        } else if (listOfOuts.get(x).size() == minOut) {
            System.out.println(Colors.yellow + x + Colors.red
                    + " already points to minimum number of vertices." + Colors.reset);
            return false;
        } else if (listOfIns.get(y).size() == minIn) {
            System.out.println(Colors.yellow + y + Colors.red
                    + " already has minimum number of vertices pointing to it." + Colors.reset);
            return false;
        }
        listOfOuts.get(x).remove(listOfOuts.get(x).indexOf(y));
        listOfIns.get(y).remove(listOfIns.get(y).indexOf(x));
        System.out.println(Colors.green + "Edge removed." + Colors.reset);
        return true;
    }

    public Map<Integer, List<Integer>> getGraph() {
        return listOfOuts;
    }

    public Map<Integer, List<Integer>> getListOfIns() {
        return listOfIns;
    }
}
