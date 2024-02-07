/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParallelAlgorithmModel;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author innastetsenko
 */
public class Experiment extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        ArrayList<XYChart.Series> series = new ArrayList<>();
        
//        series.add(getSeriesTimePerformance(2, 100, 200, 1));
//        series.add(getSeriesTimePerformance(2, 100, 400, 1));
//      series.add(getSeriesTimePerformance(2, 100, 800, 1));
//      series.add(getSeriesTimePerformance(2, 100, 1000, 1));
//        series.add(getSeriesTimePerformance(2,100, 400, 1));
//              series.add(getSeriesTimePerformance(4,100, 400, 1));
//                    series.add(getSeriesTimePerformance(8,100, 400, 1));
//                     series.add(getSeriesTimePerformance(16,100, 400, 1));
//        series.add(getSeriesTimePerformance(2,100, 400, 1));
//        series.add(getSeriesTimePerformance(2,10000, 800, 1));

//       createScene(stage, "Thread complexity impact", "Events per thread", "Performance time estimation", series);
//            createScene(stage, "Cores impact", "Events per thread", "Performance time", series);
//        series = getSeriesXY();
//        createScene(stage, "Cores impact", "Events per thread", "Performance time estimation", series);
            // limit buffer
//             series.add(getSeriesLimitBufferImpact(2, 100, 400, 10, 1));
//              series.add(getSeriesLimitBufferImpact(4, 100, 400, 10, 1));
//               series.add(getSeriesLimitBufferImpact(8, 100, 400, 10, 1));
//                series.add(getSeriesLimitBufferImpact(16, 100, 400, 10, 1));
//             createScene(stage, "Limit buffer of external events impact", "Events per thread", "Performance time", series);
             
             // formula
//             series = getSeriesFormula();
//        createScene(stage, "Thread complexity impact", "Events per thread", "Computational complexity", series);
    
        // experiment
       // series = getSeriesAlgPerformance();
      //  createScene(stage, "Thread complexity impact", "Events per thread", "Performance time estimation", series);
// два параметри досліджуємо

//series.add(getSeriesTimePerformance(2, 100, 400, 3, 1)); // 5 = limitBuffer
//series.add(getSeriesTimePerformance(2, 100, 400, 10, 1));
//series.add(getSeriesTimePerformance(2, 100, 400, 100, 1));
//series.add(getSeriesTimePerformance(2, 100, 400, 1000, 1));
    ArrayList<XYChart.Series> seriesArray = new ArrayList<>();
    seriesArray = getSeriesAlgPerformance();
    for (XYChart.Series s: seriesArray){
        series.add(s);
    }
createScene(stage, "Parameters impact", "Events per thread", "Performance time estimation", series);


// при збільшенні ліміту до 10 зменшення є, а потім зменшується несуттєво
//
//     series.add(getSeriesLimitBufferImpact(2, 100, 400, 2, 1));
//     series.add(getSeriesLimitBufferImpact(2, 100, 400, 4, 1));
//     series.add(getSeriesLimitBufferImpact(2, 100, 400, 10, 1));
//     series.add(getSeriesLimitBufferImpact(2, 100, 400, 30, 1));
//     series.add(getSeriesLimitBufferImpact(2, 100, 400, 50, 1));
//     createScene(stage, "Parameters impact", "Limit buffer of external events", "Performance time estimation", series);
      
      
    }

    public Scene createScene(Stage stage, String title, String xLabel, String yLabel, ArrayList<XYChart.Series> series) {
        stage.setTitle(title);
        
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);// "Events per obj");
        yAxis.setLabel(yLabel);//"Performance time");
        Font font = Font.font(18);
        xAxis.setTickLabelFont(font);
        yAxis.setTickLabelFont(font);
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

    public double[] createListParameters(double min, double delta, int n) {
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = min + i * delta;
        }
        return data;
    }
    public XYChart.Series getSeriesTimePerformance( int cores, double timeMod, int totalComplexity, int limitBuffer, int progon) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        XYChart.Series seriesA = new XYChart.Series();
//        seriesA.setName("Total complexity:"+totalComplexity);
        seriesA.setName("Limit Buffer :"+limitBuffer);
        

       double[] x = {2, 4, 10, 20, 30, 50};//{2, 5, 10, 20, 50};
        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < progon; j++) {
                int threadComplexity = (int)x[i];
                y[i] += Model.getTimePerformance(cores,timeMod,totalComplexity/threadComplexity,threadComplexity,limitBuffer); 
            }
            y[i] = y[i] / progon;
        }

        System.out.println("x " + "\t "
                + " y");
        for (int j = 0; j < x.length; j++) {
            System.out.println((int) x[j] + "\t"
                    + y[j]);
        }

        for (int j = 0; j < x.length; j++) {
        
            seriesA.getData().add(new XYChart.Data(x[j], y[j]));

        }
        return seriesA;
    }
    
    public XYChart.Series getSeriesTimePerformance( int cores, double timeMod, int totalComplexity, int progon) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        XYChart.Series seriesA = new XYChart.Series();
//        seriesA.setName("Total complexity:"+totalComplexity);
        seriesA.setName("Cores:"+totalComplexity);

       double[] x = {2, 5, 10, 20, 50};//{5, 10, 20, 50, 100};
        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < progon; j++) {
                int threadComplexity = (int)x[i];
                y[i] += Model.getTimePerformance(cores,timeMod,totalComplexity/threadComplexity,threadComplexity,20); 
            }
            y[i] = y[i] / progon;
        }

        System.out.println("x " + "\t "
                + " y");
        for (int j = 0; j < x.length; j++) {
            System.out.println((int) x[j] + "\t"
                    + y[j]);
        }

        for (int j = 0; j < x.length; j++) {
        
            seriesA.getData().add(new XYChart.Data(x[j], y[j]));

        }
        return seriesA;
    }

    public XYChart.Series getSeriesLimitBufferImpact( int cores, double timeMod, int totalComplexity, int threadComplexity, int progon) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        XYChart.Series seriesA = new XYChart.Series();
       seriesA.setName("Thread complexity:"+threadComplexity);
//        seriesA.setName("Cores:"+cores);

       double[] x = {3, 10, 100, 1000, 2000};
        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < progon; j++) {
                
                y[i] += Model.getTimePerformance(cores,timeMod,totalComplexity/threadComplexity,threadComplexity,(int)x[i]); 
            }
            y[i] = y[i] / progon;
        }

        System.out.println("x " + "\t "
                + " y");
        for (int j = 0; j < x.length; j++) {
            System.out.println((int) x[j] + "\t"
                    + y[j]);
        }

        for (int j = 0; j < x.length; j++) {
        
            seriesA.getData().add(new XYChart.Data(x[j], y[j]));

        }
        return seriesA;
    }
    
    public ArrayList<XYChart.Series> getSeriesXY() {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        double[] x = {5, 10, 20, 50, 100};
         double[][] y = {{12536.023000605053, 5410.805000196245, 6338.125000231299, 11850.708000559118, 21869.878001123183},
             {8152.191000318455, 3085.502000048997, 3277.6260000423276, 6199.187000195732, 11327.869000514353},
             {5755.442000161724, 1976.8089999914228, 1792.7689999894615, 3260.5920000314595, 11269.660000510723},
             {4761.625000095421, 1425.4089999955063, 1025.2419999953727, 3256.690000030698, 11326.357000509508}
         
         };
         
         int[] cores = {2,4,8,16};
         XYChart.Series seriesA;
        for (int i = 0; i < 4; i++) {
            seriesA = new XYChart.Series();
            seriesA.setName("Cores:" + cores[i]);
            
            for (int j = 0; j < x.length; j++) {

                seriesA.getData().add(new XYChart.Data(x[j], y[i][j]));

            }
            series.add(seriesA);
        }
        
        
        return series;
    }
//     public ArrayList<XYChart.Series> getSeriesAlgPerformance() {
//        ArrayList<XYChart.Series> series = new ArrayList<>();
//        double[] x = {2, 5, 10, 50, 100};
//        // experimental results in case 2 core
//         double[][] y = {{2339.25,1184.75, 806, 2391.5, 5625.75},
//             {2712, 1450.5, 1367, 3940.75, 8434.75},
//             {6291.25, 2978, 2711.25, 6996.25, 14688.5},
//             {8318.75, 3710.5, 3287.25, 8644.5, 17390.25}
//         
//         };
//         
//         int[] complexity = {200,400,800,1000};
//         XYChart.Series seriesA;
//        for (int i = 0; i < 4; i++) {
//            seriesA = new XYChart.Series();
//            seriesA.setName("The total number of events: " + complexity[i]);
//            
//            for (int j = 0; j < x.length; j++) {
//
//                seriesA.getData().add(new XYChart.Data(x[j], y[i][j]));
//
//            }
//            series.add(seriesA);
//        }
//        
//        
//        return series;
//    }
    
    public ArrayList<XYChart.Series> getSeriesAlgPerformance() {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        //double[] x = {2, 4, 10, 20, 30, 50};
        double[] x = {3, 4, 6, 8, 10, 12, 15};
        // experimental results in case 2 core
        double[][] y = {{9500.502000045339, 6158.315000095876, 3977.7330001088603, 3570.536000072986, 3572.459000067294, 3657.428000062826, 4020.147000073015},
            {9080.670000016346, 5400.165000033616, 3497.950000078819, 3256.399000050082, 3334.3000000557386, 3510.2660000561036, 3878.0680000663747},
            {8878.614000001931, 5050.339000003831, 3239.4420000568216, 3127.911000050257, 3257.577000048118, 3463.935000052331, 3863.313000066019},
            {8868.784000001262, 5032.4160000023985, 3149.626000051676, 3071.45400004751, 3207.4430000492184, 3404.0780000508835, 3825.3140000642475}        
        };
         
        int[] limit = {3,10,100,1000};
        XYChart.Series seriesA = new XYChart.Series();
        for (int i = 0; i < 4; i++) {
            seriesA = new XYChart.Series();
            seriesA.setName("Limit Buffer: " + limit[i]);
            
            for (int j = 0; j < x.length; j++) {

                seriesA.getData().add(new XYChart.Data(x[j], y[i][j]));

            }
            series.add(seriesA);
        }
             
        return series;
    }
    
    public ArrayList<XYChart.Series> getSeriesFormula() {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        double[] x = {2, 5, 10, 20, 50, 100};
         double[][] y = new double[4][x.length];
         
         int[] complexity = {200,400,800,1000};
         XYChart.Series seriesA;
        for (int i = 0; i < 4; i++) {
            seriesA = new XYChart.Series();
            seriesA.setName("Total number of events:" + complexity[i]);
            
            for (int j = 0; j < x.length; j++) {
                y[i][j] = x[j]*(x[j]*100+100)+1000*complexity[i]/x[j];
                seriesA.getData().add(new XYChart.Data(x[j], y[i][j]));
            }
            series.add(seriesA);
        }
        
        
        return series;
    }
    
    
    

    



    public static void main(String[] args) {
        launch("null");
    }

}
