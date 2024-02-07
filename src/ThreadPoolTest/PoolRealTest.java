/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author innastetsenko
 */
public class PoolRealTest {

    public static long poolSyncTest(long numTasks, long total) {
        Counter counter = new Counter();
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (int j = 0; j < numTasks; j++) {
            pool.execute(() -> {
                counter.forSync(total / numTasks);
            });

        }
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
                pool.shutdownNow(); // Cancel currently executing factors    
                if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        return counter.getC();
    }

    public static void poolAsyncTest(int threads, long numTasks, long total) {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int j = 0; j < numTasks; j++) {
            pool.execute(() -> {
                new Counter().forAsync(total / numTasks);
            });

//           pool.execute(new TaskCounter(new Counter(), total/numTasks)); // немає різниці майже яким способом
        }
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
                pool.shutdownNow(); // Cancel currently executing factors    
                if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }

//    public static void poolAsyncTest(int threads, long numTasks, long total) {
//        long create=0;
//        long execute=0;
//        long exec=0;
//        long await=0;
//        long shutdown=0;
//        for (int i = 0; i < 4; i++){
//        long c = System.nanoTime();
//        ExecutorService pool = Executors.newFixedThreadPool(threads);
//        create+= System.nanoTime() - c; 
//        for (int j = 0; j < numTasks; j++) {
//            long t = System.nanoTime();
//            pool.execute(() -> {
//                new Counter().forAsync(total / numTasks);
//            });
//            exec+= System.nanoTime() - t;
//            
////           pool.execute(new TaskCounter(new Counter(), total/numTasks)); // немає різниці майже яким способом
//        }
//        execute+= exec/numTasks; 
//        long d = System.nanoTime();
//        pool.shutdown();
//        shutdown+= System.nanoTime() - d;
//        long a = System.nanoTime();
//        try {
//            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
//                pool.shutdownNow(); // Cancel currently executing factors    
//                if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
//                    System.err.println("Pool did not terminate");
//                }
//            }
//        } catch (InterruptedException ie) {
//            pool.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//        await+= System.nanoTime() - a;
//        }
//        System.out.println("Create delay: " +  create/4); 
//        System.out.println("execute delay: " + execute/4);
//        System.out.println("shutdown delay: " + shutdown/4);
//        System.out.println("awaitTermination delay: " + await/4);
//    }
    
    public static double getCreateDelay(int threads, long numTasks, long total) {
        long create;

        long c = System.nanoTime();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        create = System.nanoTime() - c; 
        
        for (int j = 0; j < numTasks; j++) {
            
            pool.execute(() -> {
                new Counter().forAsync(total / numTasks);
            });
        }    
                
        pool.shutdown();
        
        try {
            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
                pool.shutdownNow(); // Cancel currently executing factors    
                if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        //double res = Math.round(create)/1000.0;
        return create;
    }
    
    public static double getExecuteDelay(int threads, long numTasks, long total) {
        long execute;
        long exec=0;
        
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int j = 0; j < numTasks; j++) {
            long t = System.nanoTime();
            pool.execute(() -> {
                new Counter().forAsync(total / numTasks);
            });
            exec += System.nanoTime() - t;
        }    
        execute = exec/numTasks;
        
        pool.shutdown();
        
        try {
            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
                pool.shutdownNow(); // Cancel currently executing factors    
                if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        return execute;
    }
    
    public static double getShutdownDelay(int threads, long numTasks, long total) {
        long shutdown;
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int j = 0; j < numTasks; j++) {
            pool.execute(() -> {
                new Counter().forAsync(total / numTasks);
            });    
        }    
        
        long d = System.nanoTime();
        pool.shutdown();
        
        shutdown = System.nanoTime() - d;
        
        try {
            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
                pool.shutdownNow(); // Cancel currently executing factors    
                if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        return shutdown;
    }
    
    public static double getAwaitDelay(int threads, long numTasks, long total) {
        long await;
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int j = 0; j < numTasks; j++) {
            pool.execute(() -> {
                new Counter().forAsync(total / numTasks);
            });    
        }    
        
        
        pool.shutdown();
        
        long d = System.nanoTime();
        try {
            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
                pool.shutdownNow(); // Cancel currently executing factors    
                if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        await = System.nanoTime() - d;
        return await;
    }
    
    public static double getRunDelay(int threads, long numTasks, long total) {
        long run = 0;
        Runnable th;
        for (int j = 0; j < numTasks; j++) {           
            th = new TaskCounter(new Counter(), total/numTasks);
            long t = System.nanoTime();
            th.run();
            run += System.nanoTime() - t;
        }  
              
        return run/numTasks;
    }
    
    public static double getCounterDelay(int threads, long numTasks, long total) {
        long run = 0;
        Counter c = new Counter();
        for (int j = 0; j < numTasks; j++) {           
            long t = System.nanoTime();
            c.forAsync(total/numTasks);
            run += System.nanoTime() - t;
        }  
              
        return run/numTasks;
    }
    
    public static long getTimePerformance(int threads, long numTasks, long total) {
        long t = System.nanoTime();

        poolAsyncTest(threads, numTasks, total);
        return System.nanoTime() - t;
    }

    public static long getTimePerformanceSeq(long total) {
        long t = System.nanoTime();
        new Counter().forAsync(total);

        return System.nanoTime() - t;
    }

// public static void main(String[] args) throws InterruptedException {
//     poolAsyncTest(2, 100, 10000000);
// }
    public static void main(String[] args) throws InterruptedException {
        int[] threads = {1, 2, 4, 8, 16};
        int[] complexity = {100000, 1000000, 10000000, 100000000,1000000000};
        int[] factors = {2, 10, 100, 1000, 10000};
        //double[][] res = new double[threads.length][factors.length];
        double[][] res = new double[complexity.length][factors.length];
        int repetitions = 20;
        for(int j=0; j<1000; j++){
            Math.random(); // warmed up
        }
        //double seq = getTimePerformanceSeq(2000000000);
        
          for(int th=0; th<threads.length; th++){
            System.out.println("\n" + "Threads: "+threads[th] + "\n");
            for (int i = 0; i < complexity.length; i++) {
                System.out.println("____________________________");
                System.out.println("complexity"+"\t"+complexity[i]+"\n");
                for (int j = 0; j < factors.length; j++) {
                    
                    res[i][j] =0.0;
                    for(int r=0; r<repetitions; r++){
                        res[i][j] += getTimePerformance(threads[th], threads[th]*factors[j], complexity[i]);
                    }
                    res[i][j]/=repetitions;
                    System.out.println(factors[j]+"\t"+ Math.round(res[i][j])/1000.0);
                }
            }
        }
    }
    
/*    public static void main(String[] args) throws InterruptedException {
        int[] threads = {1,2,4,8,16};
        int[] complexity = {100000, 1000000, 10000000, 100000000,1000000000};
        int[] factors = {2, 10, 100, 1000, 10000};
//        double[][] res = new double[threads.length][factors.length];
        double[][] res = new double[complexity.length][factors.length];
        int repetitions = 20;
        for(int j=0; j<1000; j++){
            Math.random(); // warmed up
        }
//        double seq = getTimePerformanceSeq(2000000000);
//       System.out.println("Delay on creating newFixedThreadPool with 4 threads, microseconds");
//        System.out.println("Delay on executing task, microseconds");
//        System.out.println("Delay on shutdown, microseconds");
//        System.out.println("Delay on await termination, microseconds");
        System.out.println("Delay on task's running, microseconds");
        System.out.println("tasks"+"\t"+"\t"+"delay");
        for(int th=0; th<threads.length; th++){
            System.out.println("\n" + "Threads: "+threads[th] + "\n");
            for (int i = 0; i < complexity.length; i++) {  
                System.out.println("____________________________");
                System.out.println("complexity"+"\t"+complexity[i]+"\n");
                for (int j = 0; j < factors.length; j++) {               
                    res[i][j] = 0.0;
                    for(int r=0; r<repetitions; r++){
    //                    res[i][j] += getCreateDelay(threads[th], threads[th]*factors[j], complexity[i]);
    //                   res[i][j] += getExecuteDelay(threads[th], threads[th]*factors[j], complexity[i]);
    //                    res[i][j] += getShutdownDelay(threads[th], threads[th]*factors[j], complexity[i]);
    //                    res[i][j] += getAwaitDelay(threads[th], threads[th]*factors[j], complexity[i]);
                        res[i][j] += getCounterDelay(threads[th], threads[th]*factors[j], complexity[i]);

                    }
                    res[i][j]/=repetitions;
                    System.out.println(factors[j]+"\t"+"\t"+Math.round(res[i][j])/1000.0);
                }
            }
        }
    }*/
}
