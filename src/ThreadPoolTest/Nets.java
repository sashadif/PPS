/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

import TestFriends.*;
import PetriObj.ArcIn;
import PetriObj.ArcOut;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author innastetsenko
 */
public class Nets {

    public static PetriNet CreateNetRunnableSimple(double timeMean)
            throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> places = new ArrayList<>();
        ArrayList<PetriT> transitions = new ArrayList<>();
        ArrayList<ArcIn> arcsIn = new ArrayList<>();
        ArrayList<ArcOut> arcsOut = new ArrayList<>();
        places.add(new PetriP("runSt", 0));
        places.add(new PetriP("runEnd", 0));
        places.add(new PetriP("cores", 0));
        transitions.add(new PetriT("run", timeMean, Double.MAX_VALUE));
        transitions.get(0).setDistribution("exp",
                transitions.get(0).getTimeServ());
        transitions.get(0).setParamDeviation(0.0);
        arcsIn.add(new ArcIn(places.get(0), transitions.get(0), 1));
        arcsIn.add(new ArcIn(places.get(2), transitions.get(0), 1));
        arcsOut.add(new ArcOut(transitions.get(0), places.get(1), 1));
        arcsOut.add(new ArcOut(transitions.get(0), places.get(2), 1));
        PetriNet d_Net = new PetriNet("Runnable",
                places,
                transitions,
                arcsIn,
                arcsOut);
        return d_Net;
    }

    public static PetriNet CreateNetMain() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("mainStart", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("runStA", 0));
        d_P.add(new PetriP("runStB", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("mainEnd", 0));
        d_P.add(new PetriP("runEndA", 0));
        d_P.add(new PetriP("runEndB", 0));
        d_T.add(new PetriT("createTreadA", 0.0));
        d_T.add(new PetriT("createTreadB", 0.0));
        d_T.add(new PetriT("startA", 0.0));
        d_T.add(new PetriT("startB", 0.0));
        d_T.add(new PetriT("joinA", 0.0));
        d_T.add(new PetriT("joinB", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        PetriNet d_Net = new PetriNet("Main", d_P, d_T, d_In, d_Out);

        return d_Net;
    }

    public static PetriNet CreateNetMain(double delayCreate,
            double delayStart,
            double delayJoin)
            throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("mainStart", 1));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("runStA", 0));
        d_P.add(new PetriP("runStB", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("mainEnd", 0));
        d_P.add(new PetriP("runEndA", 0));
        d_P.add(new PetriP("runEndB", 0));
        d_T.add(new PetriT("createThreadA", delayCreate));
        d_T.add(new PetriT("createThreadB", delayCreate));
        d_T.add(new PetriT("startA", delayStart));
        d_T.add(new PetriT("startB", delayStart));
        d_T.add(new PetriT("joinA", delayJoin));
        d_T.add(new PetriT("joinB", delayJoin));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        d_P.add(new PetriP("cores", 2));
        for (PetriT transition : d_T) {
            transition.setDistribution("unif",
                    transition.getTimeServ());
            transition.setParamDeviation(transition.getTimeServ() / 2);
        }
        for (PetriT transition : d_T) {
            d_In.add(new ArcIn(d_P.get(11), transition, 1));
            d_Out.add(new ArcOut(transition, d_P.get(11), 1));
        }
        PetriNet d_Net = new PetriNet("Main", d_P, d_T, d_In, d_Out);
        return d_Net;
    }
       public static PetriNet CreateNetFixedPool(int w, int k,
                                                double delayCreate, double delayExecute,
                                                double delayAwait) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("poolST", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("tasks", w));
        d_P.add(new PetriP("numTasks", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("runSt", 0));
        d_P.add(new PetriP("runEnd", 0));
        d_P.add(new PetriP("poolEnd", 0));
        d_T.add(new PetriT("poolCreate", delayCreate));
        d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(delayCreate/2);
        d_T.add(new PetriT("execute", delayExecute));
        d_T.get(1).setDistribution("unif", d_T.get(1).getTimeServ());
        d_T.get(1).setParamDeviation(delayExecute/2);
        d_T.get(1).setPriority(6);
        d_T.add(new PetriT("tunSt", 0.0));
        d_T.add(new PetriT("shutdown", 0.0));
        d_T.add(new PetriT("runEnd", 0.0));
        d_T.add(new PetriT("awaitTermination", delayAwait));
        d_T.get(5).setDistribution("unif", d_T.get(5).getTimeServ());
        d_T.get(5).setParamDeviation(delayAwait/2);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(5), w));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(3), k));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(3), 1));
        PetriNet d_Net = new PetriNet("FixedPool", d_P, d_T, d_In, d_Out);
        return d_Net;
    }
       

}
