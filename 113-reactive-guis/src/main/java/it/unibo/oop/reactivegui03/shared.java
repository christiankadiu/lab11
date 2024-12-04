package it.unibo.oop.reactivegui03;

public class shared {
    private int counter = 0;
    private boolean stop;

    public synchronized void stopCounting() {
        this.stop = true;
    }

    public synchronized void inc() {
        this.counter++;
    }

    public synchronized void dec() {
        this.counter--;
    }

    public synchronized boolean getStop() {
        return this.stop;
    }

    public synchronized int getCounter() {
        return this.counter;
    }
}
