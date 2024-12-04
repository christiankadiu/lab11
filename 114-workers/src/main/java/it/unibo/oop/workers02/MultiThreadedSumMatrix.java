package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public final class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    /**
     * 
     * @param nthread
     *                no. of thread performing the sum.
     */
    public MultiThreadedSumMatrix(final int nthread) {
        this.nthread = nthread;
    }

    private static class Worker extends Thread {
        private final double[] list;
        private final int startpos;
        private final int nelem;
        private long res;

        /**
         * Build a new worker.
         * 
         * @param list
         *                 the list to sum
         * @param startpos
         *                 the initial position for this worker
         * @param nelem
         *                 the no. of elems to sum up for this worker
         */
        Worker(final double[] list, final int startpos, final int nelem) {
            super();
            this.list = list;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            int size = 0;
            for (final double d : list) {
                size = size + 1;
            }
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < size && i < startpos + nelem; i++) {
                this.res += this.list[i];
            }
        }

        /**
         * Returns the result of summing up the integers within the list.
         * 
         * @return the sum of every element in the array
         */
        public long getResult() {
            return this.res;
        }

    }

    @Override
    public double sum(double[][] matrix) {
        int sum = 0;
        for (double[] ds : matrix) {
            for (double d : ds) {
                sum = sum + 1;
            }
        }
        final int size = sum % nthread + sum / nthread;
        /*
         * Build a list of workers
         */
        final List<Worker> workers = new ArrayList<>(nthread);
        for (double[] ds : matrix) {
            for (int start = 0; start < matrix.length; start += size) {
                workers.add(new Worker(ds, start, size));
            }
        }
        /*
         * Start them
         */
        for (final Worker w : workers) {
            w.start();
        }
        /*
         * Wait for every one of them to finish. This operation is _way_ better done by
         * using barriers and latches, and the whole operation would be better done with
         * futures.
         */
        long somma = 0;
        for (final Worker w : workers) {
            try {
                w.join();
                somma += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        /*
         * Return the sum
         */
        return somma;
    }

}
