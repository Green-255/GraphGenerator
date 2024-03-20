package Lab1;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/////// reiktu pasidometi Edge klase

public class Main {
    private static int vertices = 0;
    private static int maxIn = 0;
    private static int maxOut = 0;
    private static int minIn = 0;
    private static int minOut = 0;
    private static Map<Integer, List<Integer>> listOfOuts;
    private static Map<Integer, List<Integer>> listOfIns;

    public static void main(String[] args) throws InterruptedException {
        System.out.print(Colors.reset);
        Graph graph = null;
        Scanner scanner = new Scanner(System.in);
        String regex = "^(y|n)$";
        Pattern pattern = Pattern.compile(regex);
        System.out.println("Generate random graph? (y/n)");
        while (true) {
            String input = scanner.nextLine();
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                if (input.equals("n")) {
                    graph = fileInputs();
                } else if (input.equals("y")) {
                    // graph = automaticUserInputs();
                    graph = userInputs(scanner);
                }
                break;
            }
        }

        graph.graphValidator();

        new printGraph(graph);

        userActionInputs(graph, scanner);
        scanner.close();
    }

    private static Graph automaticUserInputs() throws InterruptedException {
        vertices = 10;
        minOut = 8;
        maxOut = 9;
        minIn = 8;
        maxIn = 9;
        Graph graph = null;

        for (int i = 0; i < 10000; i++) {
            System.out.println("Generating random graph " + (i + 1) + "/100");
            graph = new Graph(vertices, maxIn, maxOut, minIn, minOut);
            graph.graphValidator();
            new printGraph(graph);
            // Thread.sleep(400);
        }
        return graph;
    }

    private static Graph userInputs(Scanner scanner) {
        String regex = "^\\d+\\s\\d+\\s\\d+\\s\\d+\\s\\d+$";
        Pattern pattern = Pattern.compile(regex);
        System.out.println("Enter the number of vertices, minOut, maxOut, minIn, maxIn:");
        Matcher matcher;
        do {
            String inputs = scanner.nextLine();
            matcher = pattern.matcher(inputs);
            if (!matcher.matches()) {
                System.out.println(Colors.red + "Invalid input patern." + Colors.reset);
                continue;
            } else {
                String[] inputsArray = inputs.split(" ");
                vertices = Integer.parseInt(inputsArray[0]);
                minOut = Integer.parseInt(inputsArray[1]);
                maxOut = Integer.parseInt(inputsArray[2]);
                minIn = Integer.parseInt(inputsArray[3]);
                maxIn = Integer.parseInt(inputsArray[4]);
            }
        } while (!inputValidation());
        return new Graph(vertices, maxIn, maxOut, minIn, minOut);
    }

    private static Graph fileInputs() {
        vertices = 10;
        maxIn = 9;
        maxOut = 6;
        minIn = 3;
        minOut = 3;

        if (!inputValidation()) {
            System.out.println(Colors.yellow + "Invalid hardcoded parameters." + Colors.reset);
        }

        GraphFileReader graphFileReader = new GraphFileReader();
        listOfIns = graphFileReader.getListOfIns();
        listOfOuts = graphFileReader.getListOfOuts();
        return new Graph(listOfOuts, listOfIns, vertices, maxIn, maxOut, minIn, minOut);
    }

    private static void userActionInputs(Graph graph, Scanner scanner) {
        System.out.println("Choose action:\n(check x y; add x y; remove x y; exit; alg x).");
        String actionType;
        String regex = "^(add|remove|check)\\s\\d+\\s\\d+$|^exit|^alg\\s\\d+$";
        Pattern pattern = Pattern.compile(regex);
        while (true) {
            String actionInput = scanner.nextLine();
            Matcher matcher = pattern.matcher(actionInput);
            if (!matcher.matches()) {
                System.out.println("Invalid action input: " + actionInput);
                continue;
            }

            String[] actionArray = actionInput.split(" ");
            actionType = actionArray[0];
            if (actionType.equals("exit")) {
                break;
            } else if (actionType.equals("alg")) {
                int x = Integer.parseInt(actionArray[1]);
                if (x >= vertices || x < 0) {
                    System.out.println(Colors.red + "Invalid virtex input for algorithm." + Colors.reset);
                    continue;
                }
                new Algorithm(x, graph.getGraph());
                continue;
            }
            int x = Integer.parseInt(actionArray[1]);
            int y = Integer.parseInt(actionArray[2]);

            if (x >= vertices || y >= vertices || x < 0 || y < 0 || x == y) {
                System.out.println(Colors.red + "Invalid virtices' input." + Colors.reset);
                continue;
            }

            if (actionType.equals("check")) {
                graph.check(x, y);
            } else if (actionType.equals("add")) {
                if (graph.add(x, y)) {
                    new printGraph(graph);
                }
            } else if (actionType.equals("remove")) {
                if (graph.remove(x, y)) {
                    new printGraph(graph);
                }
            }
        }

    }

    private static boolean inputValidation() {
        if (maxIn < minIn || maxOut < minOut) {
            System.out.println(Colors.red + "Max degree cannot be less than min degree." + Colors.reset);
            return false;
        }
        if (vertices <= 0 || maxIn <= 0 || maxOut <= 0 || minIn <= 0 || minOut <= 0) {
            System.out.println(Colors.red + "All the parameters must be greater than 0." + Colors.reset);
            return false;
        }
        if (maxIn >= vertices || maxOut >= vertices) {
            System.out.println(Colors.red
                    + "Degrees cannot be greater than or equal to the number of vertices." + Colors.reset);
            return false;
        }
        return true;
    }
}
