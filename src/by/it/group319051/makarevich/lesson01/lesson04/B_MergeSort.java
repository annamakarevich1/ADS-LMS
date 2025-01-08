package by.it.group319051.makarevich.lesson01.lesson04;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {


    int[] mergeArr(int[] current, int[] first, int[] second, int left, int right)
    {
        int middle = left + (right - left) / 2;
        int l_cur = left;
        int r_cur = middle + 1;

        //склейка двух отсортированных массива
        for ( int i = left; i <= right; i++)
        {

            if (l_cur <= middle && r_cur <= right)
            {
                //если элемент из первого массива меньше элемента из второго,
                if (first[l_cur] < second[r_cur])
                {
                    current[i] = first[l_cur];//добавляем из перовго в наш новый массив
                    l_cur++;//увеличиваем индекс на 1 (индекс элемента первого массива)
                }
                else
                {
                    current[i] = second[r_cur];//добавляем из второго в наш новый массив
                    r_cur++;//увеличиваем индекс на 1 (индекс элемента второго массива)
                }
            }
            else if (l_cur <= middle)
            {
                current[i] = first[l_cur];
                l_cur++;
            }
            else
            {
                current[i] = second[r_cur];
                r_cur++;
            }
        }
        return current;
    }

    //int[] - массив в java
    int[] merge_sort(int[] arr, int[] tempArr,  int left, int right)
    {
        if (left == right)
        {
            tempArr[left] = arr[left];
            return tempArr;
        }

        int middle = left + (right - left) / 2;

        // разделяй и сортируй
        int[] first = merge_sort(arr, tempArr, left, middle);
        int[] second= merge_sort(arr,tempArr, middle + 1, right);

        // слияние двух отсортированных половин

        int[] current;
        if(first == arr) {
            current = tempArr;
        }
        else {
            current = arr;
        }


        current = mergeArr(current,first,second, left, right);
        return current;
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a=new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        int[] temp = new int[n];
        merge_sort(a, temp, 0, n-1);
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием






        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson04/dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result=instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index:result){
            System.out.print(index+" ");
        }
    }


}