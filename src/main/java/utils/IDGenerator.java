package utils;

public class IDGenerator {
    public static int counter = 0;

    public static synchronized int getCounter() {
        counter++;
        return counter;
    }
}
