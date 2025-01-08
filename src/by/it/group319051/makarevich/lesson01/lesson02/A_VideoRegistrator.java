package by.it.group319051.makarevich.lesson01.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class A_VideoRegistrator {

    public static void main(String[] args)  {
        A_VideoRegistrator instance=new A_VideoRegistrator();
        double[] events=new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts=instance.calcStartTimes(events,1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }
    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration)  {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result;
        result = new ArrayList<>();
        Arrays.sort(events);//Сортировка массива по возрастанию
        //1,1.1,1.6,2.2,2.4,2.7,3.7,3.9,5.5,8.1,9.1};
        result.add(events[0]);
        for(int i = 0; i < events.length; i++) {

            //цикл пробегает по сыбытиям , когда регистратор не выключается( интервал между событиями меньше 1)
            double sum = 0;
            while (sum <= workDuration && i < events.length - 1) {
                sum += (events[i + 1] - events[i]);//находим сумму разностей интервалов, чтобы понять, когда закончить цикл
                i++;// i = i + 1
            }

            //регистратор выключается
            //и Проверяем, последнее событие соталось?
            if (i != events.length - 1) {
                result.add(events[i]);//добавляем последнее событие
            } else {
                result.add(events[i - 1]);//предыдущее, когда регистратор не выключался
            }

        }

        //i - это индекс события events[i]
        //комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)

        //пока есть незарегистрированные события
        //получим одно событие по левому краю
        //и запомним время старта видеокамеры
        //вычислим момент окончания работы видеокамеры
        //и теперь пропустим все покрываемые события
        //за время до конца работы, увеличивая индекс
        return result;                        //вернем итог
    }
}



