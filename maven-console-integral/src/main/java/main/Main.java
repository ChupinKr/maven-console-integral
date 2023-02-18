package main;



public class Main {

    public static double finishResult = 0;
    public static void main(String[] args) throws InterruptedException {
        double a = 0;
        double b = 10;
        int n = 1000;

        Calculator calc = new Calculator(a, b, n);
        calc.start();
        calc.join();
        System.out.println(finishResult);
    }
}