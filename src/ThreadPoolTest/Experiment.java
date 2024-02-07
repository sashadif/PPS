/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

import TestFriends.*;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 *
 * @author innastetsenko
 */
public class Experiment extends Application {
    
    public static void main(String[] args) {
        launch("null");
    }

    @Override
    public void start(Stage stage) throws Exception {

       startEstimationExperiment(stage, "Real thread pool time performance");

    }
    
    public void startEstimationExperiment(Stage stage, String name) {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        
        for(int j=0; j<1000; j++){
            Math.random(); // warmed up
        }
        series.add(getSeriesPoolReal_tasksImpactSpeedup(1, 2000000000, 7, 20));
        series.add(getSeriesPoolReal_tasksImpactSpeedup(2, 2000000000, 7, 20));
        series.add(getSeriesPoolReal_tasksImpactSpeedup(4, 2000000000, 7, 20));
//        getSeriesPoolReal_tasksImpact(8, 2000000000, 10, 20);
//        getSeriesPoolReal_tasksImpact(16, 2000000000, 10, 20);
        
       // series.add(getSeriesPoolReal(2, 2000000000, 9, 20)); 

//        series.add(getSeriesPoolReal(300000000, 20, 20)); 
//        series.add(getSeriesPoolReal(100000000, 20, 20)); 
       
        createScene(stage, name, "Number of tasks", "Time performance, seconds", series);
        
    }

    public Scene createScene(Stage stage, String title, String xLabel, String yLabel, ArrayList<XYChart.Series> series) {
        stage.setTitle(title);
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);// "Generator delay");
        yAxis.setLabel(yLabel);//"Probability rejected");
        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<>(xAxis, yAxis);
        for (XYChart.Series s : series) {
            lineChart.getData().add(s);
        }
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
        return scene;
    }
    public XYChart.Series getSeriesPoolRealSpeedup(int threads, long total, int numPoints, int repetitions) { // task complexity
        XYChart.Series seriesA = new XYChart.Series();
        seriesA.setName("thread pool  time performance, for " + total + " instructions ");
        
        long[] x = new long[numPoints]; // x is the number of instructions per task: total/x[i] = number of tasks
        x[0] = 1000000;
        for (int j = 1; j < x.length; j++) {
            x[j] = x[j - 1] +1000000;
        }
        
        
        double[] y = new double[x.length];
       
        for (int i = 0; i < y.length; i++) {
            
            for (int j = 0; j < repetitions; j++) {
                
                y[i] += PoolRealTest.getTimePerformance(threads,total/x[i], total)*1000000000;

            }
            y[i] = y[i] / repetitions;
        }
        double seq = PoolRealTest.getTimePerformanceSeq(total);
        System.out.println("seq = "+seq);
        System.out.println("x " + "\t " + " y");
        
        for (int j = 0; j < x.length; j++) {
            System.out.println(x[j] + "\t" + seq/y[j]);
          

        }

        for (int j = 0; j < x.length; j++) {
            seriesA.getData().add(new XYChart.Data(x[j], y[j]));

        }
        return seriesA;
    }
    
    
     public XYChart.Series getSeriesPoolReal(int threads, long total, int numPoints, int repetitions) { // task complexity
        XYChart.Series seriesA = new XYChart.Series();
        seriesA.setName("thread pool  time performance, for " + total + " instructions ");
        
        long[] x = new long[numPoints]; // x is the number of instructions per task: total/x[i] = number of tasks
        x[0] = 1000000;
        for (int j = 1; j < x.length; j++) {
            x[j] = x[j - 1] +1000000;
        }
        
        
        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < repetitions; j++) {
                
                y[i] += PoolRealTest.getTimePerformance(threads,total/x[i], total);

            }
            y[i] = y[i] / repetitions;
        }
        System.out.println("x " + "\t " + " y");

        for (int j = 0; j < x.length; j++) {
            System.out.println(x[j] + "\t" + y[j]);
          

        }

        for (int j = 0; j < x.length; j++) {
            seriesA.getData().add(new XYChart.Data(x[j], y[j]));

        }
        return seriesA;
    }
    
    
    public XYChart.Series getSeriesPoolReal_tasksImpactSpeedup(int threads, int total, int numPoints, int repetitions) {
        XYChart.Series seriesA = new XYChart.Series();
        seriesA.setName("thread pool  time performance, for " + total + " instructions ");
        
        int[] x = new int[numPoints]; 
        x[0] = 2;
        for (int j = 1; j < x.length; j++) {
            x[j] = x[j - 1] * 2;
        }
        
        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < repetitions; j++) {
                y[i] += PoolRealTest.getTimePerformance(threads,x[i], total);

            }
            y[i] = y[i] / repetitions;
        }
        System.out.println("x " + "\t " + " y");
        
        double seq = PoolRealTest.getTimePerformanceSeq(total);

        for (int j = 0; j < x.length; j++) {
            System.out.println(x[j] + "\t" + seq/y[j]);

        }

        for (int j = 0; j < x.length; j++) {
            seriesA.getData().add(new XYChart.Data(x[j], y[j]));

        }
        return seriesA;
    }
    
    

}
