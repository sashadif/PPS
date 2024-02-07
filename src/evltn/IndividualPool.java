/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evltn;

import PetriObj.ExceptionInvalidNetStructure;
import ThreadPoolTest.PoolModel;
import ThreadPoolTest.PoolRealTest;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IndividualPool {
        
    private static int numParams;
    private final int[] params;
    private static int[] mins;
    private static int[] maxs;
    private double fit;
    private final Random random=new Random();
    

    public IndividualPool() { // БЕЗ розрахунку фіт-значення
        numParams = mins.length;
        params = new int[numParams];
        for (int i = 0; i < numParams; i++) {
            if (maxs[i] == mins[i]) {
                this.params[i] = mins[i];
            } else {
                this.params[i] = mins[i] + random.nextInt(maxs[i] - mins[i]);
            }
        }
        fit = Double.MAX_VALUE;
    }
    
    public IndividualPool(int progon) { // З розрахунком фіт-значення
        numParams = mins.length;
        params = new int[numParams];
        for (int i = 0; i < numParams; i++) {
            if (maxs[i] == mins[i]) {
                this.params[i] = mins[i];
            } else {
                this.params[i] = mins[i] + random.nextInt(maxs[i] - mins[i]);
            }
        }
        calcFit(progon);
    }
    // цей метод створює зазростанням одного індексу j
    public IndividualPool(int progon, int j, int numInPopulation) { // рівномірно розподіляємо
        numParams = mins.length;
        params = new int[numParams];
        for (int i = 0; i < numParams; i++) {
            if (maxs[i] == mins[i]) {
                this.params[i] = mins[i];
            } else {
                this.params[i] = mins[i] + (int) Math.rint(j * (maxs[i] - mins[i]) / numInPopulation); // 20 в популяції
            }
        }
    //    calcFit(progon);
//        System.out.print("[ " + j + " ]");
//        this.print();
    }

    // цей метод для випадку двох параметрів і варіюються два індекси j k
    public IndividualPool(int progon, int j, int k, int num) { // рівномірно розподіляємо
        numParams = mins.length;
        params = new int[numParams];
        if (maxs[0] == mins[0]) {
            this.params[0] = mins[0];
        } else {
            this.params[0] = mins[0] + (int) Math.rint(j * (maxs[0] - mins[0]) / num); // 4 інтервали для 5 точок
        }
        if (maxs[1] == mins[1]) {
            this.params[1] = mins[1];
        } else {

            this.params[1] = mins[1] + (int) Math.rint(k * (maxs[1] - mins[1]) / num); // 4 інтервали
        }
  //      calcFit(progon);

//        System.out.print("[ "+j+","+k+" ]");
       // this.print();
    }
    
     public IndividualPool childIndividual(IndividualPool other, int progon, int variation) {
        IndividualPool child = new IndividualPool(progon);

        for (int j = 0; j < getNumParams(); j++) {
                child.params[j] = (this.getParams()[j] + other.getParams()[j]) / 2; // десь посередині
                child.params[j] = generateMutationValue(child.params[j],
                        Math.min(this.getParams()[j], other.getParams()[j]),
                        Math.max(this.getParams()[j], other.getParams()[j]),
                        variation);
        }
        
     //   child.calcFit(progon);
//        System.out.print("child "+child.isIdentical(history));
//       child.printParams();
        return child;
    }

         public int generateMutationValue(int value, int min, int max, int variation) {
      
            double rr = random.nextDouble();
            int delta = Math.max(1, (max - min) / variation); // величина варіації при мутації 1...10
            int d = random.nextInt(delta + 1); // величина мутації
            if (rr < 0.33) {
                    value += d;                     
                    if (value > max) {
                        value = max;
                    }
                } else {
                    if (rr < 0.66) {
                        value -= d;
                        if (value < min) {
                            value = min;
                        }

                    } 
                }
        return value;
    }
    public IndividualPool trivialMutation() { // величина мутації = 1
        IndividualPool child = new IndividualPool();
        double rr;
        for (int j = 0; j < getNumParams(); j++) {
            rr = random.nextDouble();

            if (rr < 0.5) {
                child.getParams()[j] =this.getParams()[j] + 1;
            } else {
//                if (rr < 0.66) {
                   child.getParams()[j] =this.getParams()[j] - 1;
                    if (child.getParams()[j] < 1) {
                        child.getParams()[j] = 1;
                    }

//                }
            }
        }        
        return child;
    }
    
    // for pool model oprimization
     public final void calcFit(int progon) {
        double f = 0;
        int totalComplexity = 2000000000;
        int cores = 2;
        double tMod = 100;

        for (int i = 0; i < progon; i++) {
            try {
                f += PoolModel.getTimePerformance(cores, tMod, this.params[0], (int)Math.pow(2, this.params[1]), totalComplexity);//[0]-threads , [1]-tasks, [2] - total, time in nanoseconds //
            } catch (ExceptionInvalidNetStructure ex) {
                Logger.getLogger(IndividualPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fit = f / progon;
    //    fit = f / progon / 1000000000; // time in seconds

    }

   /* // for real pool oprimization
    public final void calcFit(int progon) {
        double f = 0;
        int totalComplexity = 2000000000;

        for (int i = 0; i < progon; i++) {
            f += PoolRealTest.getTimePerformance(this.params[0], this.params[0]*(int)Math.pow(2, this.params[1]), totalComplexity);//[0]-threads , [1]-tasks, [2] - total, time in nanoseconds //
        }

        fit = f / progon / 1000000000; // time in seconds

    }
*/
    public double getFit() {

        return fit;
    }
    
    public int[] getParams(){
        return params;
    }
    public void printParams(){
        System.out.println(this.getParams()[0]+"\t"+this.getParams()[1]);  
    }
    
    public void print(){
        System.out.println(this.getParams()[0]+"\t"+this.getParams()[1]+"\t "+this.getFit());  
    }
    
    public boolean isIdentical(ArrayList<IndividualPool> hystoric){
        boolean s;
        for(IndividualPool ind: hystoric){
            s=true;
            for(int j=0; j<params.length; j++){
                    if(this.params[j]!=ind.params[j]){
                        s=false;
                        break;
                    }
            }
            if(s){
//                System.out.print("Indiidiual "+this.getParams()[0]+"\t"+this.getParams()[1]+"\t "+
//                        "is identical to the "+ ind.getParams()[0]+"\t"+ind.getParams()[1]);
                return true;            
            }          
        }
        return false;
    }
    
    

    /**
     * @return the mins
     */
    public static int[] getMins() {
        return mins;
    }

    /**
     * @param aMins the mins to set
     */
    public static void setMins(int[] aMins) {
        mins = aMins;
    }

    /**
     * @return the maxs
     */
    public static int[] getMaxs() {
        return maxs;
    }

    /**
     * @param aMaxs the maxs to set
     */
    public static void setMaxs(int[] aMaxs) {
        maxs = aMaxs;
    }

    /**
     * @return the numParams
     */
    public static int getNumParams() {
        return numParams;
    }

}
