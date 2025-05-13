package by.it.group310951.makarevich.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Пример: C->B, C->I, ...
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        // Построение графа и его транспонированной версии
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Добавим вершины в список всех узлов
            nodes.add(from);
            nodes.add(to);
        }

        // Убедимся, что каждая вершина есть в графах
        for (String node : nodes) {
            graph.putIfAbsent(node, new ArrayList<>());
            reverseGraph.putIfAbsent(node, new ArrayList<>());
        }

        // Шаг 1: обычный DFS и сохраняем порядок выхода из вершин
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        List<String> sortedNodes = new ArrayList<>(nodes);
        Collections.sort(sortedNodes); // Лексикографический порядок

        for (String node : sortedNodes) {
            if (!visited.contains(node)) {
                dfs1(graph, node, visited, order);
            }
        }

        // Шаг 2: обратный DFS по транспонированному графу
        visited.clear();
        List<List<String>> sccs = new ArrayList<>();

        while (!order.isEmpty()) {
            String node = order.pollLast();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfs2(reverseGraph, node, visited, component);
                Collections.sort(component); // Лексикографический порядок внутри компоненты
                sccs.add(component);
            }
        }

        // Вывод компонентов
        for (List<String> component : sccs) {
            for (String v : component) {
                System.out.print(v);
            }
            System.out.println();
        }
    }

    private static void dfs1(Map<String, List<String>> graph, String node,
                             Set<String> visited, Deque<String> order) {
        visited.add(node);
        List<String> neighbors = graph.getOrDefault(node, new ArrayList<>());
        neighbors.sort(String::compareTo); // Чтобы порядок был лексикографический
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs1(graph, neighbor, visited, order);
            }
        }
        order.addLast(node);
    }

    private static void dfs2(Map<String, List<String>> reverseGraph, String node,
                             Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        List<String> neighbors = reverseGraph.getOrDefault(node, new ArrayList<>());
        neighbors.sort(String::compareTo);
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs2(reverseGraph, neighbor, visited, component);
            }
        }
    }
}