/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;

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

       // startLockConflictExperiment(stage, "Real conflicts frequency, 2 cores", 8);
       startEstimationExperiment(stage, "Time performance estimation of an elementary instruction");

    }
    
    public void startEstimationExperiment(Stage stage, String name) {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        series.add(getSeriesEstimation(10, 100));   
        series.add(getSeriesEstimation(10, 10000));
        createScene(stage, name, "Number of iterations", "Time performance, microseconds", series);
      //  series.add(getSeriesEstimation(7, 1000000));
        
    }

    public void startLockConflictExperiment(Stage stage, String name, int progon) throws Exception {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        series.add(getSeriesLockConflictFrequency(2, progon));
        series.add(getSeriesLockConflictFrequency(4, progon));
        series.add(getSeriesLockConflictFrequency(8, progon));
        series.add(getSeriesLockConflictFrequency(16, progon));
        createScene(stage, name, "Sleep delay in microsec", "Frequency", series);
    }

    public void startModelConflictExperiment(Stage stage, String name, int progon) throws Exception {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        series.add(getSeriesConflictFrequency(2, 2, 100.0, 100000000, progon));
        series.add(getSeriesConflictFrequency(4, 2, 100.0, 100000000, progon));
        series.add(getSeriesConflictFrequency(8, 2, 100.0, 100000000, progon));
        series.add(getSeriesConflictFrequency(16, 2, 100.0, 100000000, progon));
        createScene(stage, "Сonflicts frequency, 2 cores", "Ratio", "Frequency", series);
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

    public XYChart.Series getSeriesConflictFrequency(int friends, int cores, double delay, double timeMod, int progon) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        XYChart.Series seriesA = new XYChart.Series();

        double[] x = new double[10];
        x[0] = 0.00000001; // 0.00000001 - good result
        for (int j = 1; j < x.length; j++) {
            x[j] = x[j - 1] + 0.1;
        }

        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < progon; j++) {
                y[i] += this.getResult(friends, cores, delay, x[i], timeMod);

            }
            y[i] = y[i] / progon;
        }

        System.out.println("x " + "\t "
                + " y");
        for (int j = 0; j < x.length; j++) {
            System.out.println(x[j] + "\t"
                    + y[j]);
        }

        for (int j = 0; j < x.length; j++) {
            seriesA.getData().add(new XYChart.Data(x[j], y[j]));

        }
        return seriesA;
    }

    public double getResult(int numFriends, int cores, double delay, double ratio, double time) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriSim> list = new ArrayList<>();

        FriendPetri[] friends = new FriendPetri[numFriends];
        for (int j = 0; j < numFriends; j++) {
            friends[j] = new FriendPetri("Friend_" + j, 1000, cores, delay, ratio); // 1000 is the number of all bows (unsuccessful and successful)
        }
        for (int j = 0; j < numFriends; j++) {
            for (int i = 0; i < numFriends; i++) {
                if (i != j) {
                    friends[j].addFriend(friends[i]);
                }
            }
        }

        for (int j = 0; j < numFriends; j++) {
            list.add(friends[j]);
        }
        PetriObjModel model = new PetriObjModel(list);
        model.setIsProtokol(false);

        model.go(time);

        double res = 0.0;
        for (PetriSim sim : model.getListObj()) {
            res += ((FriendPetri) sim).getResult();
        }
        return res / model.getListObj().size();
    }

    public XYChart.Series getSeriesLockConflictFrequency(int numFriends, int progon) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        XYChart.Series seriesA = new XYChart.Series();
//          seriesA.setName("Conflict frequency for " + cores + " cores ");

        seriesA.setName("Real conflict frequency for " + numFriends + " friends ");

        long[] x = new long[4]; // x is sleep delay
        x[0] = 1;
        for (int j = 1; j < x.length; j++) {
            x[j] = x[j - 1] * 100;
        }

        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < progon; j++) {
                y[i] += this.getLockResult(numFriends, x[i]);

            }
            y[i] = y[i] / progon;
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

    public double getLockResult(int numFriends, long delaySleep) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        Friend[] friends = new Friend[numFriends];
        for (int j = 0; j < numFriends; j++) {
            friends[j] = new Friend("Friend_" + j);
        }
        ArrayList<Thread> threads = new ArrayList<>();

        for (int j = 0; j < numFriends; j++) {

            for (int i = 0; i < numFriends; i++) {

                if (i != j) {
                    threads.add(new Thread(new BowLoop(friends[j], friends[i], delaySleep)));
                }
            }
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
            double res = 0.0;
            for (Friend friend : friends) {
                res += friend.getResult();
            }

            return res / friends.length;

        } catch (InterruptedException ex) {
            Logger.getLogger(TestTryLockFriends.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;

    }
    
    public XYChart.Series getSeriesEstimation(int num, int warmedUp) {

        XYChart.Series seriesA = new XYChart.Series();
        seriesA.setName("Estimation, warmed up =  " + warmedUp + "instructions");

        int[] x = new int[num]; 
        x[0] = 1000;
        for (int j = 1; j < x.length; j++) {
            x[j] = x[j - 1] + 1000;
        }

        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < 4; j++) {
                y[i] += DelayEstimation.getEstResult(x[i], warmedUp);

            }
            y[i] = y[i] / 4;
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

    
    
    

    

}
