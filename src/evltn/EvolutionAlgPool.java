/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evltn;

import PetriObj.ExceptionInvalidNetStructure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class EvolutionAlgPool {

    private int numInPopulation;
    private final int progon;
    private final Random random;
    private int numRepeat;
    private ArrayList<IndividualPool> searchHistory;

    public EvolutionAlgPool() {
        this(20, 1);
    }

    public EvolutionAlgPool(int numIndividuals, int progon) {
        this.numInPopulation = numIndividuals;
        this.numRepeat = 10*this.numInPopulation;
        this.progon = progon;
        random = new Random();
        searchHistory = new ArrayList<>();
    }

    public IndividualPool[] createPopulationByCombinatorial() { // обидва параметри рівномірно розкидуються і варіюються кожний з кожним
        // 0 - 01234, 1-01234,2-01234, 3-01234, 4-01234
        System.out.println("--------START population---------");
        IndividualPool[] population = new IndividualPool[numInPopulation];
        int num = (int) Math.sqrt(numInPopulation) + 1; // кількість значень від min до max що варіюються

        int next = 0;
        for (int i = 0; i < num; i++) { // для 20 в популяції достатньо 5, для 100 в популяції - 10
            for (int k = 0; k < num; k++) {
                population[next] = new IndividualPool(progon, i, k, num - 1); // 4 інтервали для 5 точок
                
                population[next].calcFit(progon);
                
                if (next == 0) {
                    getSearchHistory().add(population[next]);
                } else {
                    if (!population[next].isIdentical(searchHistory)) {
                        getSearchHistory().add(population[next]);
                    }
                }
                next++;
                if (next >= numInPopulation) {
                    break;
                }
            }
            if (next >= numInPopulation) {
                break;
            }
        }
       // System.out.println("Sorting by fit ... ");
       sortIndividuals(population);
       
        return population;
    }

    public IndividualPool[] createNextPopulation(IndividualPool[] previousPopulation, double s, int variation) {
        
        IndividualPool[] newPopul = new IndividualPool[numInPopulation]; // популяція завжди відсортована за зростанням фітнес-функції
        System.out.println("searching the best elements  ...");
        newPopul[0] = previousPopulation[0];
        
        newPopul[0].print();
        int k = 1;
        // відкидаємо за умовою "набагато гірший", а не за кількістю
       
        while (previousPopulation[k].getFit() - previousPopulation[0].getFit() < s) {
            newPopul[k] = previousPopulation[k];
            
            previousPopulation[k].print();
            k++;
            if (k == numInPopulation) { //k==previousPopulation.length-1
                break;
            }     
        }
        int bests = k;// динамічно змінюється кількість найкращих

        System.out.println(bests + " elements have been identified as the best ");
        // створюємо нові щоб набрати популяцію
        if(bests==numInPopulation){
            System.out.println("All elements are the best ");
            for(int j=0; j<numInPopulation; j++){
                 newPopul[j] = previousPopulation[j];
                 
//                 newPopul[j].calcFit(progon); // залишаємо раніше розраховане значення
                 
            }
        }  else if(bests == 1){
            System.out.println("Only one element is the best"); // що робити? чи підкидати нові варіанти?
            for (int j = k; j < numInPopulation; j++) {               
                   newPopul[j] = previousPopulation[0].
                           childIndividual(previousPopulation[1], progon, variation); //з наступним схрещуємо              
                        if (!newPopul[j].isIdentical(searchHistory)) {
               //             System.out.println(" unique has been found by mutation ");
                            getSearchHistory().add(newPopul[j]);
                            newPopul[j].calcFit(progon); 
                            
                        } else{
              //             System.out.println("the same....., best=1");
                           newPopul[j] = previousPopulation[j]; // залишаємо старий варіант, оскільки нового не знайдено  
                        }     
            }
        } else 
        //if (k < numInPopulation)
         {
            int half = (bests) / 2;
            int one, other;
           boolean isBreak = false;
            for (int j = k; j < numInPopulation; j++) {
                isBreak = false;
                for(int i=0; i<numRepeat; i++){ // шукаємо унікальний елемент
                    
                    // щоб різні числа були згенеровані одне шукаємо серед парних, а інше - серед непарних
                    one = 2 * random.nextInt(half + 1);
                    other = 1 + 2 * (random.nextInt(half + 1));
                    
                    if (other >= bests) {
                        other = 1;
                    }
                    
                    newPopul[j] = previousPopulation[one].
                                childIndividual(previousPopulation[other], progon, variation); // схрещування                    
                    
                    if (!newPopul[j].isIdentical(searchHistory)) {
              //          System.out.println(" unique has been found ");
                        newPopul[j].calcFit(progon);
                 
                        getSearchHistory().add(newPopul[j]);
              //          System.out.println("repeat has been break "+i);
                        isBreak = true;
                        break;
                    }
                   
                }
                if (!isBreak) { // якщо не вдалось відшукати унікальний
                    newPopul[j] = previousPopulation[j].trivialMutation(); // +- 1 обов'язково
                   if (!newPopul[j].isIdentical(searchHistory)) {
              //          System.out.println(" unique has been found by mutation ");
                        getSearchHistory().add(newPopul[j]);
                        newPopul[j].calcFit(progon); 
                    } else{
               //        System.out.println("the same....., repeat = "+(numRepeat-1));
                       newPopul[j] = previousPopulation[j]; // залишаємо старий варіант, оскільки нового не знайдено 
//                       if(numRepeat>2)
//                        numRepeat=numRepeat/2; // скорочуємо кількість спроб для пошуку унікального 
                   }                      
                }                
            }  
        }
//як позбавитись від однакових в популяції?
        if(bests==numInPopulation && bests>=4){
            numInPopulation/=2;
        }
        sortIndividuals(newPopul);
        return newPopul;

    }
    
    public boolean isParametersIdentical(int[] parameters){
        boolean s;
        for(IndividualPool ind: searchHistory){
            s=true;
            for(int j=0; j<parameters.length; j++){
                    if(parameters[j]!=ind.getParams()[j]){
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
    
    
    
    public static IndividualPool[] sortIndividualsByParameters(IndividualPool[] population) {
        Arrays.sort(population, (o1, o2) -> {
            if (o1.getParams()[0] > o2.getParams()[0]) {
                    
                return 1; //сортування у зростаючому порядку
            } else if (o1.getParams()[0] < o2.getParams()[0]) {
                return -1;
            } else {
                 if(o1.getParams()[1] > o2.getParams()[1]){
                     return 1;                    
                 } else if(o1.getParams()[1] < o2.getParams()[1]){
                     return -1;
                 }
                return 0;
            }
        });
        return population;
    }
    

    public static IndividualPool[] sortIndividuals(IndividualPool[] population) {
        Arrays.sort(population, (o1, o2) -> {
            if (o1.getFit() > o2.getFit()) {
                return 1; //сортування у зростаючому порядку
            } else if (o1.getFit() < o2.getFit()) {
                return -1;
            } else {
                return 0;
            }
        });
        return population;
    }

    public IndividualPool evolution() {
        int steps=20; // максимальна кількість кроків (популяцій) еволюційного алгоритму
        
        ArrayList<Double> fitValues = new ArrayList<>(); // згодяться для спостереження динаміки наближення
        IndividualPool[] popul = this.createPopulationByCombinatorial();
        //IndividualPool[] popul = this.createPopulation();
        
        this.print(popul);
        numInPopulation/=2;
        int counter = 0;
        fitValues.add(popul[0].getFit());
      
       // this.numInPopulation = this.numInPopulation/2; // після початкової популяції зменшуємо
        
       
        for (int n = 0; n < steps; n++) {
            System.out.println("----------NEXT----------");
            //*fitValues.get(n - 1) - щоб нормувати значення точності, останній аргумент - параметр варіації для мутації, тим більше delta для мутації
            popul = this.createNextPopulation(popul, 0.1 * fitValues.get(n) / (n + 1), 2); // поріг для відкидання з кожним поколінням зменшується
                                                                                                    //  (n + 1) delta для мутації - зменшується 
            this.print(popul);
            fitValues.add(popul[0].getFit());  // n+1 значення у списку        

            System.out.println("Fitness value improvement    " + (fitValues.get(n + 1) - fitValues.get(n)));
            // *fitValues.get(0) - щоб нормувати значення точності 
            if ((n > 0) && (fitValues.get(n + 1) - fitValues.get(n) < 0.1 * fitValues.get(0) / (n + 1))) { // треба щоб підряд кілька разів, а не взагалі в усьому пошуку
                counter++;
            } else {
                counter = 0; // тому починаємо відлік наново
            }

            if (counter > steps/2) {
                System.out.println("----------THE BEST HAS BEEN FOUND----------");

                break;
            }
            
        }
        if (counter <= steps/2) {
            System.out.println("----------THE BEST IN THE LAST POPULATION----------");
        }

        return popul[0];
    }

    public static void warmedUp(int n) {
        for (int j = 0; j < n; j++) {
            Math.random(); // warmed up
        }
    }

    public static void main(String[] args) throws ExceptionInvalidNetStructure {
        // третій параметр - можливо тип пулу ?
        int[] mins = {2, 0}; //[0]-threads, [1]-tasks
        int[] maxs = {16, 8};
        IndividualPool.setMaxs(maxs); // тепер ці значення використовуватимуться для створення нових елементів та мутації
        IndividualPool.setMins(mins);
        EvolutionAlgPool ev = new EvolutionAlgPool(40, 1); // 1 progon

        //warmedUp(1000000); // розігрів для моделі не потрібний, але потрібний при експериментуванні з реальною програмкою
        // проте навіть 1000000 не допомагає вирішити проблему розігріву...
        IndividualPool best = ev.evolution();
        System.out.println("----------THE BEST----------");
        best.print();
        System.out.println("----------SEARCH HISTORY----------  "+ev.getSearchHistory().size()); 
        ev.getSearchHistory().sort((o1, o2) -> {
            if (o1.getParams()[0] > o2.getParams()[0]) {
                    
                return 1; //сортування у зростаючому порядку
            } else if (o1.getParams()[0] < o2.getParams()[0]) {
                return -1;
            } else {
                 if(o1.getParams()[1] > o2.getParams()[1]){
                     return 1;                    
                 } else if(o1.getParams()[1] < o2.getParams()[1]){
                     return -1;
                 }
                return 0;
            }
        });
        ev.printHistory();

    }

    public void print(IndividualPool[] popul) {
        IndividualPool[] printedArr = Arrays.copyOf(popul, popul.length);
        sortIndividualsByParameters(printedArr); // для зручності сприйняття інформації
        for (int j = 0; j < numInPopulation; j++) {
            printedArr[j].print();
        }
    }
    
     public void printHistory() {
 
        for (int j = 0; j < searchHistory.size(); j++) {
            searchHistory.get(j).print();
        }
    }

    /**
     * @return the searchHistory
     */
    public ArrayList<IndividualPool> getSearchHistory() {
        return searchHistory;
    }

    /**
     * @param searchHistory the searchHistory to set
     */
    public void setSearchHistory(ArrayList<IndividualPool> searchHistory) {
        this.searchHistory = searchHistory;
    }
    
    
    

}
