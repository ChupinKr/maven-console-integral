package main;

import java.util.ArrayList;

import static main.Main.finishResult;

public class Calculator extends Thread {

    int n;
    double a;
    double b;
    ArrayList<Double> results = new ArrayList<Double>();
    ArrayList<Calculator> threads = new ArrayList<Calculator>();
    double mainResult = 0;
    double mainThread = 0;

    double result = 0;

    synchronized void addToResult(double result){
        finishResult += result;
    }

    public Calculator(double a, double b, int n) {
        this.a = a;
        this.b = b;
        this.n = n;
    }
    // основная функция
    public double inFunc(double x){
        return x * x;
    }

    private boolean threadIsDeeperCalc(int i) {
        double h = (b - a) / n;
        if (Math.abs((Math.abs(inFunc(a + h * i))) - Math.abs(inFunc(a + h * (i + 1)))) > 0.01) {
            return true;
        }
        return false;
    }


    //Чтобы итог из тела потока можно было бы получить, я вынес этот кусок кода в отдельный метод.
    @Override
    public void run() {
        double dx = (b - a) / n;
        //по условию создаем поток или высчитываем результат через среднее значение на отрезке
        for (int i = 0; i < n ; i++) {
            if(threadIsDeeperCalc(i)) {
                threads.add(new Calculator( a + dx * i, a + dx * (i + 1), n));
            }else{
                results.add(inFunc(a + dx * (i + 0.5)));
            }
        }
        //стартуем потоки созданные ранее
        for(Calculator calculator: threads){
            calculator.start();
            try {
                calculator.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //берем результаты для текущего потока и записывает в синх. переменную
        if(results.size() > 0) {
            for (double result : results) {
                mainResult += result;
            }
            mainResult *= dx;
        }

        //складывает результаты по промежуткам текущего потока и его подпотоков
        addToResult(mainResult);

    }
}