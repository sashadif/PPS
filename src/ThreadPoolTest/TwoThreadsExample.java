/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

import java.util.Arrays;

/**
 *
 * @author innastetsenko
 */
public class TwoThreadsExample {
    
    public static void main(String[] args) throws InterruptedException{
        double[] arr = new double[1000000];
        double[] arrCopy = new double[1000000];
        int n = Integer.MAX_VALUE;
        
        for(int j=0; j<arr.length; j++){
            arr[j] = arr.length-j;
            arrCopy[j] = arr[j];
        }      
   
        Thread a = new Thread(()->{
            Arrays.sort(arr,0,arr.length/2);
        });
        Thread b = new Thread(()->{
           Arrays.sort(arr, arr.length/2, arr.length);
        });
       

        a.start();
        b.start();
  
        a.join();
        b.join();
       
        
        System.out.println(arr[0]+"\t"+arr[arr.length-1]);
        Arrays.sort(arr);
        System.out.println(arr[0]+"\t"+arr[arr.length-1]);
       
  
    }
    
    
    
    
 
}
