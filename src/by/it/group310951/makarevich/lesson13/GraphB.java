package by.it.group310951.makarevich.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Пример: 1 -> 2, 1 -> 3, 2 -> 3
        scanner.close();

        // Строим граф
        Map<String, List<String>> graph = new HashMap<>();

        String[] edges = input.split(",\\s*");
        Set<String> allNodes = new HashSet<>();

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }

        // Убедимся, что каждая вершина есть в графе
        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        boolean hasCycle = false;

        for (String node : allNodes) {
            if (!visited.contains(node)) {
                if (dfsHasCycle(graph, node, visited, recursionStack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsHasCycle(Map<String, List<String>> graph, String node,
                                       Set<String> visited, Set<String> stack) {
        visited.add(node);
        stack.add(node);

        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                if (dfsHasCycle(graph, neighbor, visited, stack)) {
                    return true;
                }
            } else if (stack.contains(neighbor)) {
                return true; // Обнаружен цикл
            }
        }

        stack.remove(node);
        return false;
    }
}