package by.it.group351051.burdo.lesson03;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

//Lesson 3. A_Huffman.
//Разработайте метод encode(File file) для кодирования строки (код Хаффмана)

// По данным файла (непустой строке ss длины не более 104104),
// состоящей из строчных букв латинского алфавита,
// постройте оптимальный по суммарной длине беспрефиксный код.

// Используйте Алгоритм Хаффмана — жадный алгоритм оптимального
// безпрефиксного кодирования алфавита с минимальной избыточностью.

// В первой строке выведите количество различных букв kk,
// встречающихся в строке, и размер получившейся закодированной строки.
// В следующих kk строках запишите коды букв в формате "letter: code".
// В последней строке выведите закодированную строку. Примеры ниже

//        Sample Input 1:
//        a
//
//        Sample Output 1:
//        1 1
//        a: 0
//        0

//        Sample Input 2:
//        abacabad
//
//        Sample Output 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111

public class A_Huffman {

    //Изучите классы Node InternalNode LeafNode
    abstract class Node implements Comparable<Node> {
        //абстрактный класс элемент дерева
        //(сделан abstract, чтобы нельзя было использовать его напрямую)
        //а только через его версии InternalNode и LeafNode
        private final int frequence; //частота символов

        //генерация кодов (вызывается на корневом узле
        //один раз в конце, т.е. после построения дерева)
        abstract void fillCodes(String code);

        //конструктор по умолчанию
        private Node(int frequence) {
            this.frequence = frequence;
        }

        //метод нужен для корректной работы узла в приоритетной очереди
        //или для сортировок
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //расширение базового класса до внутреннего узла дерева
    private class InternalNode extends Node {
        //внутренный узел дерева
        Node left;  //левый ребенок бинарного дерева
        Node right; //правый ребенок бинарного дерева

        //для этого дерева не существует внутренних узлов без обоих детей
        //поэтому вот такого конструктора будет достаточно
        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////
    //расширение базового класса до листа дерева
    private class LeafNode extends Node {
        //лист
        char symbol; //символы хранятся только в листах

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            //добрались до листа, значит рекурсия закончена, код уже готов
            //и можно запомнить его в индексе для поиска кода по символу.
            codes.put(this.symbol, code);
        }
    }

    //индекс данных из листьев
    static private Map<Character, String> codes = new TreeMap<>();


    //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(File file) throws FileNotFoundException {
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(file);
        String s = scanner.next();
        //System.out.println(s);

        Map<Character, Integer> count = new HashMap<>();
        //1. переберем все символы по очереди и рассчитаем их частоту в Map count
            //для каждого символа добавим 1 если его в карте еще нет или инкремент если есть.
        char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            char symbol = charArray[i];
            if (count.get(symbol) == null) {
                count.put(symbol, 1);
            } else {
                count.put(symbol, count.get(symbol)+1);
            }
        }
        //count.forEach((key, value) -> System.out.println(key + " " + value));

        //2. перенесем все символы в приоритетную очередь в виде листьев
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        for (char symbol : count.keySet()) {
            Integer frequence = count.get(symbol);

            LeafNode leaf = new LeafNode(frequence, symbol);
            priorityQueue.add(leaf);
        }
        //3. вынимая по два узла из очереди (для сборки родителя)
        if (priorityQueue.size()==1) {
            // обработка случая с одним символом
            Node leaf1 = priorityQueue.poll();
            leaf1.fillCodes("0");
            priorityQueue.add(leaf1);
        }
        else {
            while (priorityQueue.size() >= 2) {
                Node leaf1 = priorityQueue.poll();
                leaf1.fillCodes("0");
                Node leaf2 = priorityQueue.poll();
                leaf2.fillCodes("1");
                Node internalNode = new InternalNode(leaf1, leaf2);
                //и возвращая этого родителя обратно в очередь
                //построим дерево кодирования Хаффмана.
                //У родителя частоты детей складываются.
                priorityQueue.add(internalNode);
                //System.out.println(priorityQueue.poll().frequence);
            }
        }

        //4. последний из родителей будет корнем этого дерева
        //это будет последний и единственный элемент оставшийся в очереди priorityQueue.

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            sb.append(codes.get(charArray[i]));
        }

        return sb.toString();
        //01001100100111
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        File f = new File(root + "by/it/group351051/burdo/lesson03/dataHuffmanTest.txt");
        A_Huffman instance = new A_Huffman();
        long startTime = System.currentTimeMillis();
        String result = instance.encode(f);
        long finishTime = System.currentTimeMillis();
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

}
