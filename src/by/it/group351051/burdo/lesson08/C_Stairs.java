package by.it.group351051.burdo.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки.
Найдите максимальную сумму, которую можно получить, идя по лестнице
снизу вверх (от нулевой до n-й ступеньки), каждый раз поднимаясь на
одну или на две ступеньки.

Sample Input 1:
2
1 2
Sample Output 1:
3

Sample Input 2:
2
2 -1
Sample Output 2:
1

Sample Input 3:
3
-1 2 1
Sample Output 3:
3

*/

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // если лестница состоит из одной ступеньки
        if (n == 1) {
            return stairs[0];
        }

        // массив для хранения максимальной суммы до каждой ступеньки
        int[] maxSum = new int[n];

        // инициализация первых двух ступенек
        maxSum[0] = stairs[0];
        maxSum[1] = Math.max(stairs[0] + stairs[1], stairs[1]);
        // заполнение массива dp
        for (int i = 2; i < n; i++) {
            if (i == n -1) {
                maxSum[i] = Math.max(maxSum[i-1], maxSum[i-2]) + stairs[i];
            }
            maxSum[i] = Math.max(maxSum[i-1], maxSum[i-2]) + stairs[i];
        }

        return maxSum[n-1];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    }


    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/group351051/burdo/lesson08/dataCTest.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
