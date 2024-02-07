/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

import LibNet.NetLibrary;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author innastetsenko
 */
public class PoolModel {
    public static void main(String[] args) throws InterruptedException, ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        int[] threads = {1, 2, 4, 8, 16};
        int[] complexity = {100000, 1000000, 10000000, 100000000,1000000000};
        int[] factors = {2, 10, 100, 1000, 10000};
        PoolSimulationModel model = createModel(2, factors[1], threads[3], 0.013, 70.0, complexity[4]);
        model.setIsProtokol(false);
        model.go(10000000);
        System.out.println("Current time at the final moment of simulation = "+model.getCurrentTime()+", no events in model = " + model.isStop());

    }
    
    public static PoolSimulationModel createModel(int m, int w, int th, 
                                            double delayCreate,   
                                            double delayAwait,
                                            double c)
        throws ExceptionInvalidNetStructure,ExceptionInvalidTimeDelay{
        int numW = w*th; // total number of workers in th threads; w - number of workers per thread;
        double delayExecute = 500.0/(w)/1000.0; //microseconds to miliseconds
        //double delayRun = 16.0/1000000.0*c/1000.0; //c - comlexity
        double delayRun = ((56.0/th)*c)/(w*100000*1000.0);
        System.out.println("delayExecute:"+delayExecute);
        System.out.println("delayRun:"+delayRun);
        ArrayList<PetriSim> list = new ArrayList<>();
        PetriSim main = new PetriSim(NetLibrary.CreateNetMainPool(m));
        main.setName("main");
        PetriSim pool = new PetriSim(NetLibrary.CreateNetThPool(numW, th, delayCreate, delayExecute, delayAwait));
        PetriSim[] tasks = new PetriSim[numW];
        for (int j = 0; j < numW; j++){
            tasks[j] = new PetriSim(NetLibrary.CreateNetRunnableSimple(delayRun));
            tasks[j].getNet().getListP()[0] = pool.getNet().getListP()[5];
            tasks[j].getNet().getListP()[1] = pool.getNet().getListP()[6];
            tasks[j].getNet().getListP()[2] = main.getNet().getListP()[4];
            
            
        }
        pool.getNet().getListP()[0] = main.getNet().getListP()[1];
   

        pool.getNet().getListP()[9] = main.getNet().getListP()[2];
        pool.getNet().getListP()[10] = main.getNet().getListP()[4];
       //System.out.println("p " + pool.getNet().getListP().length+"");
        list.add(main);
        list.add(pool);
        for(int j=0;j<numW; j++){
            list.add(tasks[j]);
        }
        
        return new PoolSimulationModel(list);
    }

    public static double getTimePerformance(int cores, double tmodel, int numThreads,int workers, int complexity) throws ExceptionInvalidNetStructure{
             
//        ParallelSimulationModel model = getModel(tmodel, 2, 100, 3);
         // ParallelSimulationModel model = getModelCore(cores,tmodel, numThreads, threadComplexity, limit);   
          PetriObjModel model = null;
        try {
            model = createModel(cores, workers, numThreads, 0.013, 70.0, complexity);
        } catch (ExceptionInvalidTimeDelay ex) {
            Logger.getLogger(PoolModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        model.setIsProtokol(false);
//       modelA.printStatistics();

        double simTime = tmodel*100000;  //дати можливість імітаційному алгоритму відтворити роботу паралельного алгоритму
       
        model.go(simTime);
        
//        System.out.println("Current time at the final moment of simulation = "+model.getCurrentTime()+", no events in model = " + model.isStop());
        return model.getCurrentTime();
    }
}
