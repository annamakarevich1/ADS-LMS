package by.it.group310951.makarevich.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Пример: A -> B, A -> C, B -> D, C -> D
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Убедимся, что все вершины есть в графе
        for (String node : inDegree.keySet()) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        // Очередь по лексикографическому порядку
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.offer(node);
            }
        }

        List<String> topoOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            topoOrder.add(current);
            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        if (topoOrder.size() != inDegree.size()) {
            System.out.println("Граф содержит цикл!");
        } else {
            for (String node : topoOrder) {
                System.out.print(node + " ");
            }
        }
    }
}