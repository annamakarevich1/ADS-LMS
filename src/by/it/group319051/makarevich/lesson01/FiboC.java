package by.it.group319051.makarevich.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m.
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 10;
        int m = 2;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    long fasterC(long n, int m) {
        //Решение сложно найти интуитивно
        //возможно потребуется дополнительный поиск информации
        //см. период Пизано
        long pisanoPeriod = pisanoPeriod(m);
        n = n % pisanoPeriod;
        return fibonacciMod(n, m);
    }

    // Функция для нахождения периода Пизано
    private long pisanoPeriod(int m) {
        long previous = 0;
        long current = 1;
        for (long i = 0; i < m * m; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;

            if (previous == 0 && current == 1) {
                return i + 1;
            }
        }
        return 0;
    }

    // Функция для вычисления n-го числа Фибоначчи по модулю m
    private long fibonacciMod(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        long previous = 0;
        long current = 1;
        for (long i = 2; i <= n; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;
        }
        return current;
    }
}