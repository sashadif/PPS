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
public class TaskCounter implements Runnable{
    private Counter counter;
    private long num;
    
    public TaskCounter(Counter c, long numFor){
        counter = c;
        num = numFor;
    }
    
    @Override
    public void run() {
            counter.forAsync(num);
    }
    
}
