/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

/**
 *
 * @author innastetsenko
 */
class Counter {
    private Object sync = new Object();
    private long c=0;
    private long q=0;
 
    public synchronized void  syncInc(){
            c++;  
    }
    public  void  asyncInc(){
            c++;
    }
    
    public void forSync(long n){
        for (int j = 0; j < n; j++) {
                syncInc();
            }
    }
    public void forAsync(long n){
        for (int j = 0; j < n; j++) {
                asyncInc();
            }
    }
    
    public long getForAsyncDelay(long n){
        long d = System.nanoTime();
        forAsync(n);
        return System.nanoTime() - d;
    }

    /**
     * @return the c
     */
    public long getC() {
        return c;
    }
    
    public void print(){
          System.out.println("Result is " +getC());
    }
    
}
