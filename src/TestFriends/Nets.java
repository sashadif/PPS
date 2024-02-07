/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;

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

    public static PetriNet CreateNetFriendUsingCores(String name, int loop, int cores, double delay, double x) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        Random r = new Random();
        d_P.add(new PetriP("bow{", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("lock", 1));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("failure++", 0));
        d_P.add(new PetriP("lockOther", 1));
        d_P.add(new PetriP("bowA++", 0));
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("bowB++", 0));
        d_P.add(new PetriP("P15", 0));
        d_P.add(new PetriP("bowLoop{", loop));
        d_P.add(new PetriP("bow}", 0));
        d_P.add(new PetriP("Cycle", 1));
        d_P.add(new PetriP("Core", cores));
        d_T.add(new PetriT("imp{", delay * x));// // was delay*x //was delay
        d_T.get(0).setDistribution("norm", d_T.get(0).getParametr());
        d_T.get(0).setParamDeviation(d_T.get(0).getParametr() * 0.1);
        d_T.add(new PetriT("tryLockA", delay * x, 1));//priority = 1
        d_T.get(1).setDistribution("norm", d_T.get(1).getParametr());
        d_T.get(1).setParamDeviation(d_T.get(1).getParametr() * 0.1);
        d_T.add(new PetriT("0&?", 0.0));
        d_T.add(new PetriT("tryLockB", delay * x, 1));//priority = 1
        d_T.get(3).setDistribution("norm", d_T.get(3).getParametr());
        d_T.get(3).setParamDeviation(d_T.get(3).getParametr() * 0.1);
        d_T.add(new PetriT("bowBack{}", delay * x));
        d_T.get(4).setDistribution("norm", d_T.get(4).getParametr());
        d_T.get(4).setParamDeviation(d_T.get(4).getParametr() * 0.1);
        d_T.add(new PetriT("unlockA", 0.0));
        d_T.add(new PetriT("0&1", 0.0, 1)); //priority = 1
        d_T.add(new PetriT("failure", 0.0));
        d_T.add(new PetriT("unlockAB", 0.0));
        d_T.add(new PetriT("unlockB", 0.0));
        d_T.add(new PetriT("for{", delay)); //sleep // was delay
        d_T.get(10).setDistribution("norm", d_T.get(10).getParametr());
        d_T.get(10).setParamDeviation(d_T.get(10).getParametr() * 0.1);
        d_T.add(new PetriT("for}", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(6), 1));
        for (PetriT transition : d_T) { // Core bound
            d_In.add(new ArcIn(d_P.get(15), transition, 1));
            d_Out.add(new ArcOut(transition, d_P.get(15), 1));
        }

        PetriNet d_Net = new PetriNet("Friend " + name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetFriendWithoutCores(String name, int loop, double delay, double x) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        Random r = new Random();
        d_P.add(new PetriP("bow{", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("lock", 1));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("failure++", 0));
        d_P.add(new PetriP("lockOther", 1));
        d_P.add(new PetriP("bowA++", 0));
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("bowB++", 0));
        d_P.add(new PetriP("P15", 0));
        d_P.add(new PetriP("bowLoop{", loop));
        d_P.add(new PetriP("bow}", 0));
        d_P.add(new PetriP("Cycle", 1));
        d_T.add(new PetriT("imp{", delay * x));// // was delay*x //was delay
        d_T.get(0).setDistribution("norm", d_T.get(0).getParametr());
        d_T.get(0).setParamDeviation(d_T.get(0).getParametr() * 0.1);
        d_T.add(new PetriT("tryLockA", delay * x, 1));//priority = 1
        d_T.get(1).setDistribution("norm", d_T.get(1).getParametr());
        d_T.get(1).setParamDeviation(d_T.get(1).getParametr() * 0.1);
        d_T.add(new PetriT("0&?", 0.0));
        d_T.add(new PetriT("tryLockB", delay * x, 1));//priority = 1
        d_T.get(3).setDistribution("norm", d_T.get(3).getParametr());
        d_T.get(3).setParamDeviation(d_T.get(3).getParametr() * 0.1);
        d_T.add(new PetriT("bowBack{}", delay * x));
        d_T.get(4).setDistribution("norm", d_T.get(4).getParametr());
        d_T.get(4).setParamDeviation(d_T.get(4).getParametr() * 0.1);
        d_T.add(new PetriT("unlockA", 0.0));
        d_T.add(new PetriT("0&1", 0.0, 1)); //priority = 1
        d_T.add(new PetriT("failure", 0.0));
        d_T.add(new PetriT("unlockAB", 0.0));
        d_T.add(new PetriT("unlockB", 0.0));
        d_T.add(new PetriT("for{", delay)); //sleep // was delay
        d_T.get(10).setDistribution("norm", d_T.get(10).getParametr());
        d_T.get(10).setParamDeviation(d_T.get(10).getParametr() * 0.1);
        d_T.add(new PetriT("for}", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(6), 1));
        PetriNet d_Net = new PetriNet("Friend " + name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetBowingFriend(String name, int loop, int cores, double sleepDelay) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("bows", 0));
        d_P.add(new PetriP("next", 1));
        d_P.add(new PetriP("lockMy", 1));
        d_P.add(new PetriP("failures", 0));
        d_P.add(new PetriP("lockOther", 1));
        d_P.add(new PetriP("end", 0));
        d_P.add(new PetriP("bowLoop", loop));
        d_P.add(new PetriP("impSt", 0));
        d_P.add(new PetriP("check", 0));
        d_P.add(new PetriP("ownerMy", 0));
        d_P.add(new PetriP("ownerOther", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("", 0));
        d_P.add(new PetriP("cores", cores));
        
        d_T.add(new PetriT("tryLockMy", 1.0));
        d_T.get(0).setDistribution("norm", d_T.get(0).getParametr());
        d_T.get(0).setParamDeviation(d_T.get(0).getParametr() * 0.1);
        d_T.get(0).setPriority(6);       
        d_T.add(new PetriT("falseMy", 0.0));
        
        d_T.add(new PetriT("bow", 1.0));
        d_T.get(2).setDistribution("norm", d_T.get(2).getParametr());
        d_T.get(2).setParamDeviation(d_T.get(2).getParametr() * 0.1);
        d_T.add(new PetriT("falseOther", 0.0));
        d_T.add(new PetriT("unlockBoth", 1.0));
        d_T.get(4).setDistribution("norm", d_T.get(4).getParametr());
        d_T.get(4).setParamDeviation(d_T.get(4).getParametr() * 0.1);
        d_T.add(new PetriT("trylockOther", 1.0));
        d_T.get(5).setDistribution("norm", d_T.get(5).getParametr());
        d_T.get(5).setParamDeviation(d_T.get(5).getParametr() * 0.1);
        d_T.get(5).setPriority(6);
        
        d_T.add(new PetriT("forSt", 0.0));
        d_T.add(new PetriT("impTrue", 0.0));
        d_T.get(7).setPriority(6);
        d_T.add(new PetriT("unlockOther", 1.0));
        d_T.get(8).setDistribution("norm", d_T.get(8).getParametr());
        d_T.get(8).setParamDeviation(d_T.get(8).getParametr() * 0.1);
        d_T.add(new PetriT("unlockMy", 1.0));
        d_T.get(9).setDistribution("norm", d_T.get(9).getParametr());
        d_T.get(9).setParamDeviation(d_T.get(9).getParametr() * 0.1);
        d_T.add(new PetriT("impFalse", 0.0));
        d_T.get(10).setPriority(1);
        d_T.add(new PetriT("noUnlock", 0.0));
        d_T.add(new PetriT("sleepDelay", sleepDelay)); // не потребує ядра для виконання
        d_T.add(new PetriT("sleepEnd", 0.0));
        for (int j=0; j<1;j++) {
         d_In.add(new ArcIn(d_P.get(16), d_T.get(j), 1));
         d_Out.add(new ArcOut(d_T.get(j), d_P.get(16), 1));
        }
       
//        for (PetriT transition : d_T) { // Core bound           
//            //if (!transition.getName().equalsIgnoreCase("sleepDelay")) {
//            System.out.println(d_P.get(16).getName()+" -> "+transition.getName());
//                d_In.add(new ArcIn(d_P.get(16), transition, 1));
//                d_Out.add(new ArcOut(transition, d_P.get(16), 1));
//            //}
//        }

        d_In.add(new ArcIn(d_P.get(11), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(15), d_T.get(13), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(7), 1));
        d_In.get(6).setInf(true);
        d_In.add(new ArcIn(d_P.get(12), d_T.get(7), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(12), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(13), d_P.get(10), 1));
        PetriNet d_Net = new PetriNet("BowingFriend "+ name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    public static PetriNet CreateNetFriend111() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("bows",0));
	d_P.add(new PetriP("forEnd",1));
	d_P.add(new PetriP("lockMy",1));
	d_P.add(new PetriP("failures",0));
	d_P.add(new PetriP("lockOther",1));
	d_P.add(new PetriP("end",0));
	d_P.add(new PetriP("bowLoop[",100));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("check",0));
	d_P.add(new PetriP("ownerMy",0));
	d_P.add(new PetriP("ownerOther",0));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("",0));
	d_T.add(new PetriT("tryLockMy",1.0));
	d_T.get(0).setDistribution("norm", d_T.get(0).getTimeServ());
	d_T.get(0).setParamDeviation(0.1);
	d_T.get(0).setPriority(6);
	d_T.add(new PetriT("falseMy",0.0));
	d_T.add(new PetriT("bow",1.0));
	d_T.get(2).setDistribution("norm", d_T.get(2).getTimeServ());
	d_T.get(2).setParamDeviation(0.1);
	d_T.add(new PetriT("falseOther",0.0));
	d_T.add(new PetriT("unlockBoth",1.0));
	d_T.get(4).setDistribution("norm", d_T.get(4).getTimeServ());
	d_T.get(4).setParamDeviation(0.1);
	d_T.add(new PetriT("trylockOther",1.0));
	d_T.get(5).setDistribution("norm", d_T.get(5).getTimeServ());
	d_T.get(5).setParamDeviation(0.1);
	d_T.get(5).setPriority(6);
	d_T.add(new PetriT("forSt",0.0));
	d_T.add(new PetriT("impTrue",0.0));
	d_T.get(7).setPriority(6);
	d_T.add(new PetriT("unlockOther",1.0));
	d_T.get(8).setDistribution("norm", d_T.get(8).getTimeServ());
	d_T.get(8).setParamDeviation(0.1);
	d_T.add(new PetriT("unloclMy",1.0));
	d_T.get(9).setDistribution("norm", d_T.get(9).getTimeServ());
	d_T.get(9).setParamDeviation(0.1);
	d_T.add(new PetriT("impFalse",0.0));
	d_T.get(10).setPriority(1);
	d_T.add(new PetriT("noUnock",0.0));
	d_T.add(new PetriT("sleepDelay",100.0));
	d_T.add(new PetriT("sleepEnd",0.0));
	d_In.add(new ArcIn(d_P.get(11),d_T.get(10),1));
	d_In.add(new ArcIn(d_P.get(15),d_T.get(13),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(13),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(12),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(11),d_T.get(7),1));
	d_In.add(new ArcIn(d_P.get(13),d_T.get(7),1));
	d_In.get(6).setInf(true);
	d_In.add(new ArcIn(d_P.get(12),d_T.get(7),1));
	d_In.get(7).setInf(true);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(9),d_T.get(6),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(6),1));
	d_In.add(new ArcIn(d_P.get(14),d_T.get(12),1));
	d_In.add(new ArcIn(d_P.get(10),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(11),1));
	d_In.add(new ArcIn(d_P.get(10),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(12),d_T.get(9),1));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(9),1));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(8),1));
	d_In.add(new ArcIn(d_P.get(13),d_T.get(8),1));
	d_Out.add(new ArcOut(d_T.get(10),d_P.get(8),1));
	d_Out.add(new ArcOut(d_T.get(10),d_P.get(6),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(7),d_P.get(0),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(11),1));
	d_Out.add(new ArcOut(d_T.get(6),d_P.get(14),1));
	d_Out.add(new ArcOut(d_T.get(12),d_P.get(15),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(11),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(13),1));
	d_Out.add(new ArcOut(d_T.get(11),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(12),1));
	d_Out.add(new ArcOut(d_T.get(9),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(9),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(8),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(8),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(13),d_P.get(10),1));
	PetriNet d_Net = new PetriNet("Friend111",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}

}
