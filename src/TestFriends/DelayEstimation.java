/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import java.util.function.LongSupplier;

/**
 *
 * @author innastetsenko
 */
public class DelayEstimation {
    private double sum;
    private int repeat;
    
    public DelayEstimation(){
        repeat=10000; // біда, чим більше кількість повторів, тим менший час на одну дію )))
        // оскільки loop = 1000, то вимірюємо на 1000?
    }
    
    
    
    public double estimate() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay{
       Friend my =   new Friend("my");
       Friend other = new Friend("other");
       
       sum=0;
       double t;
       for(int j=0;j<repeat;j++){
       t = System.nanoTime();
       my.bowBack(other);
       sum+=System.nanoTime()-t;
       }
       return sum/repeat;  
    }
    
    public static double estimate(LongSupplier s, int repetitions){
       double total=0;
       for(int j=0;j<repetitions;j++){
            total+=s.getAsLong();
       }
       return total/repetitions;  
    }
    
    
    public static double getEstResult(int num, int warmedUp){
     double mean=0.0;
        for (int j = 0; j < warmedUp; j++) { // розiгрів
            estimate(() -> {
                double c = 0;
                long t = System.nanoTime();
                c+=1111.11;
                return System.nanoTime() - t;
            }, 1000); // 1000 замірів буде зроблено
        }

        for (int j = 0; j < num; j++) {
            mean += estimate(() -> {
                double c = 0;
                long t = System.nanoTime();
                c+=1111.11;
                return System.nanoTime() - t;
            }, 1000);
        }
       return  mean / num;
    }
    
    
    
    public static void main(String[] args) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        
        double mean = 0.0;
        int num = 10000;
        for (int j = 0; j < 1000; j++) { // розгон?
            estimate(() -> Friend.getTimePerformance(), 1000);
        }

        for (int j = 0; j < num; j++) {
        //     mean+=estimate(()->Friend.getTimePerformance(), 1000);
            mean += estimate(() -> {
                double c = 0;
                long t = System.nanoTime();
                c+=1111.11;
                return System.nanoTime() - t;
            }, 1000);
        }
        mean = mean / num;

        System.out.println(mean / num * 1000 + " us");
    }

}
