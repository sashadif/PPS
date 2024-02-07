package LibNet;

import PetriObj.ArcIn;
import PetriObj.ArcOut;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import java.util.ArrayList;
import java.util.Random;

public class NetLibrary {

    /**
     * Creates Petri net that describes the dynamics of system of the mass
     * service (with unlimited queue)
     *
     * @param numChannel the quantity of devices
     * @param timeMean the mean value of service time of unit
     * @param name the individual name of SMO
     * @throws ExceptionInvalidTimeDelay if one of net's transitions has no
     * input position.
     * @return Petri net dynamics of which corresponds to system of mass service
     * with given parameters
     * @throws PetriObj.ExceptionInvalidNetStructure
     */
    // часові затримки у мікросекундах
    private static final double d = 0.001;  // '+' is approximately 1000 ns = 1000*0.000001ms = 0.001 ms
    private static final double dlock = d*10; 
    private static final double dnumthread = d*1000; 
    private static final double dmodel = d*20; 
    
    public static PetriNet CreateNetSMOwithoutQueue(int numChannel, double timeMean, String name) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", numChannel));
        d_P.add(new PetriP("P3", 0));
        d_T.add(new PetriT("T1", timeMean));
        d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("SMOwithoutQueue" + name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    /**
     * Creates Petri net that describes the dynamics of arrivals of demands for
     * service
     *
     * @param timeMean mean value of interval between arrivals
     * @return Petri net dynamics of which corresponds to generator
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid
     * structure
     * @throws PetriObj.ExceptionInvalidNetStructure
     */
    public static PetriNet CreateNetGenerator(double timeMean) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> places = new ArrayList<>();
        ArrayList<PetriT> transitions = new ArrayList<>();
        ArrayList<ArcIn> arcsIn = new ArrayList<>();
        ArrayList<ArcOut> arcsOut = new ArrayList<>();
        places.add(new PetriP("P1", 1));
        places.add(new PetriP("P2", 0));
        transitions.add(new PetriT("T1", timeMean, Double.MAX_VALUE));
        transitions.get(0).setDistribution("exp", transitions.get(0).getTimeServ());
        transitions.get(0).setParamDeviation(0.0);
        arcsIn.add(new ArcIn(places.get(0), transitions.get(0), 1));
        arcsOut.add(new ArcOut(transitions.get(0), places.get(1), 1));
        arcsOut.add(new ArcOut(transitions.get(0), places.get(0), 1));
        PetriNet d_Net = new PetriNet("Generator", places, transitions, arcsIn, arcsOut);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    /**
     * Creates Petri net that describes the route choice with given
     * probabilities
     *
     * @param p1 the probability of choosing the first route
     * @param p2 the probability of choosing the second route
     * @param p3 the probability of choosing the third route
     * @return Petri net dynamics of which corresponds to fork of routs
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid
     * structure
     * @throws PetriObj.ExceptionInvalidNetStructure
     */
    public static PetriNet CreateNetFork(double p1, double p2, double p3) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_T.add(new PetriT("T1", 0.0, Double.MAX_VALUE));
        d_T.get(0).setProbability(p1);
        d_T.add(new PetriT("T2", 0.0, Double.MAX_VALUE));
        d_T.get(1).setProbability(p2);
        d_T.add(new PetriT("T3", 0.0, Double.MAX_VALUE));
        d_T.get(2).setProbability(p3);
        d_T.add(new PetriT("T4", 0.0, Double.MAX_VALUE));
        d_T.get(3).setProbability(1 - p1 - p2 - p3);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        PetriNet d_Net = new PetriNet("Fork", d_P, d_T, d_In, d_Out);

        return d_Net;
    }

    /**
     * Creates Petri net that describes the route choice with given
     * probabilities
     *
     * @param numOfWay quantity of possibilities in choice ("ways")
     * @param probabilities set of values probabilities for each "way"
     * @return Petri net dynamics of which corresponds to fork of routs
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid
     * structure
     * @throws PetriObj.ExceptionInvalidNetStructure
     */
    public static PetriNet CreateNetFork(int numOfWay, double[] probabilities) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();

        d_P.add(new PetriP("P0", 0));
        for (int j = 0; j < numOfWay; j++) {
            d_P.add(new PetriP("P" + (j + 1), 0));
        }

        for (int j = 0; j < numOfWay; j++) {
            d_T.add(new PetriT("вибір маршруту " + (j + 1), 0));
        }
        for (int j = 0; j < numOfWay; j++) {
            d_T.get(j).setProbability(probabilities[j]);
        }

        for (int j = 0; j < numOfWay; j++) {
            d_In.add(new ArcIn(d_P.get(0), d_T.get(j), 1));
        }

        for (int j = 0; j < numOfWay; j++) {
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(j + 1), 1));
        }

        PetriNet d_Net = new PetriNet("Fork ", d_P, d_T, d_In, d_Out);

        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }


    public static PetriNet CreateNetThread3() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("bow{", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("lockA", 1));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("failure++", 0));
        d_P.add(new PetriP("lockB", 1));
        d_P.add(new PetriP("bowA++", 0));
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("bowB++", 0));
        d_P.add(new PetriP("P15", 0));
        d_P.add(new PetriP("bowLoop{", 100));
        d_P.add(new PetriP("bow", 0));
        d_P.add(new PetriP("Core", 1));
        d_T.add(new PetriT("imp{", 0.1));
        d_T.add(new PetriT("tryLockA", 0.0));
        d_T.add(new PetriT("0&?", 0.0));
        d_T.add(new PetriT("tryLockB", 0.0));
        d_T.add(new PetriT("bowBack{", 0.1));
        d_T.add(new PetriT("unlockA", 0.0));
        d_T.add(new PetriT("0&1", 0.0));
        d_T.add(new PetriT("failure", 0.0));
        d_T.add(new PetriT("unlockAB", 0.1));
        d_T.add(new PetriT("unlockB", 0.1));
        d_T.add(new PetriT("for{", 0.0));
        d_T.add(new PetriT("for", 0.0));
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

        PetriNet d_Net = new PetriNet("friendThread", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetFriend() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        double delay = 100.0;
        double x = 0.00000001;
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        Random r = new Random();
        d_P.add(new PetriP("bow[", 0));
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
        d_P.add(new PetriP("bowLoop[", 10));
        d_P.add(new PetriP("bow]", 0));
        d_P.add(new PetriP("Core", 1));
        d_T.add(new PetriT("imp[", delay * x));// was delay
        d_T.get(0).setDistribution("norm", d_T.get(0).getTimeServ() * 0.1);
        d_T.add(new PetriT("tryLockA", delay * x, 1));//priority = 1
        d_T.get(1).setDistribution("norm", d_T.get(1).getTimeServ() * 0.1);
        d_T.add(new PetriT("0&?", 0.0));
        d_T.add(new PetriT("tryLockB", delay * x, 1));//priority = 1
        d_T.get(3).setDistribution("norm", d_T.get(3).getTimeServ() * 0.1);
        d_T.add(new PetriT("bowBack[]", delay)); //delay*x
        d_T.get(4).setDistribution("norm", d_T.get(4).getTimeServ() * 0.1);
        d_T.add(new PetriT("unlockA", 0.0));
        d_T.add(new PetriT("0&1", 0.0, 1)); //priority = 1
        d_T.add(new PetriT("failure", 0.0));
        d_T.add(new PetriT("unlockAB", 0.0));
        d_T.add(new PetriT("unlockB", 0.0));
        d_T.add(new PetriT("for[", delay)); //sleep
        d_T.get(10).setDistribution("norm", d_T.get(10).getTimeServ() * 0.1);
        d_T.add(new PetriT("for]", 0.0 + r.nextDouble()));
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
        PetriNet d_Net = new PetriNet("Friend ", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetTestInfArc() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 1));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_T.add(new PetriT("T1", 1.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(0), 1));
        d_In.get(1).setInf(true);
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(3), 1));
        PetriNet d_Net = new PetriNet("TestInfArc", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetTestStatistics() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 100));
        d_P.add(new PetriP("P2", 1));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 1));
        d_T.add(new PetriT("T1", 10.0));
        d_T.add(new PetriT("T2", 5.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        PetriNet d_Net = new PetriNet("TestStatistics", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetTask(double a) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1000));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("Resource", 0));
        d_T.add(new PetriT("T1", a));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        for (PetriT tr : d_T) {
            d_In.add(new ArcIn(d_P.get(2), tr, 1));
            d_Out.add(new ArcOut(tr, d_P.get(2), 1));

        }
        PetriNet d_Net = new PetriNet("Task", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetGeneratorInf() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P1", 0));
        d_T.add(new PetriT("T1", 2.0));
        d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(0), 1));
        d_In.get(1).setInf(true);
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
        PetriNet d_Net = new PetriNet("Generator", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetSimple() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 100));
        d_P.add(new PetriP("P2", 0));
        d_T.add(new PetriT("T1", 2.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("Simple", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetSMOgroup(int numInGroup, int numChannel, double timeMean, String name) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P0", 0));
        for (int j = 0; j < numInGroup; j++) {
            d_P.add(new PetriP("P" + (2 * j + 1), numChannel));
            d_P.add(new PetriP("P" + (2 * j + 2), 0));
            d_T.add(new PetriT("T" + (j), timeMean));
            d_T.get(j).setDistribution("exp", d_T.get(j).getTimeServ());
            d_T.get(j).setParamDeviation(0.0);
            d_In.add(new ArcIn(d_P.get(2 * j), d_T.get(j), 1));
            d_In.add(new ArcIn(d_P.get(2 * j + 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(2 * j + 1), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(2 * j + 2), 1));
        }
        PetriNet d_Net = new PetriNet(name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    public static PetriNet CreateNetMain(int numThreads, int complexity) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("mainStart", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("runSt", 0)); //P[3]
        d_P.add(new PetriP("runFin", 0)); //P[4]
        d_P.add(new PetriP("P6", 0));
        d_T.add(new PetriT("start", d * complexity * 20));
        d_T.add(new PetriT("for", d * numThreads * 3000));
        d_T.add(new PetriT("finish", d * 10));
//        for (PetriT tr: d_T){
//            tr.setDistribution("unif", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), numThreads));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), numThreads));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        PetriNet d_Net = new PetriNet("Untitled", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    public static PetriNet CreateNetMain(int cores, int numThreads, int complexity) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("mainStart", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("runSt", 0)); //P[3]
        d_P.add(new PetriP("runFin", 0)); //P[4]
        d_P.add(new PetriP("P6", 0));

        d_T.add(new PetriT("start", d * complexity * 20));
        d_T.add(new PetriT("createThread", d *numThreads * 500));
        d_T.add(new PetriT("finish", d * 10));
//        for (PetriT tr: d_T){
//            tr.setDistribution("unif", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), numThreads));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), numThreads));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("mainCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    public static PetriNet CreateNetGoUntil(int threadComplexity) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0)); //P[0]
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("tlocLESStlim", 0));//P[2]
        d_P.add(new PetriP("finish", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("if1", 0));
        d_P.add(new PetriP("outputST", 0));//P[6]
        d_P.add(new PetriP("outputFin", 0)); //P[7]
        d_P.add(new PetriP("tminLESStlim", 0));//P[8]
        d_P.add(new PetriP("if2", 0));
        d_P.add(new PetriP("P11", 0));
        d_P.add(new PetriP("tlimBIGGERtmod", 0)); //P[11]
        d_P.add(new PetriP("P13", 0));
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("lockNext", 0));
        d_P.add(new PetriP("unlockNext", 1)); //P[15]
        d_P.add(new PetriP("signalNext", 0)); //P[16]
        d_P.add(new PetriP("if3", 0));
        d_P.add(new PetriP("toRemove", 0)); //P[18]
        d_P.add(new PetriP("prev", 0));//P[19]
        d_P.add(new PetriP("P24", 0));
        d_P.add(new PetriP("P26", 0));
        d_P.add(new PetriP("lockPrev", 0)); //P[22]
        d_P.add(new PetriP("P28", 0));
        d_P.add(new PetriP("unlockPrev", 1)); //P[24]
        d_P.add(new PetriP("signalCondLimit", 0));//P[25]

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("while", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("input", d * 0.5*threadComplexity)); //0.1*threadComplexity));
        d_T.add(new PetriT("settLoc=tMin", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("T7", d));
        d_T.add(new PetriT("T8", d));
        d_T.get(7).setPriority(1);
        d_T.add(new PetriT("settloc=tmod", d));
        d_T.add(new PetriT("T10", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(10).setPriority(1);
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("T14", d));
        d_T.get(13).setPriority(1);
        d_T.add(new PetriT("T17", d));
        d_T.add(new PetriT("settloc=tlim", d));
        d_T.add(new PetriT("remove", d));
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d));
        d_T.add(new PetriT("T22", d)); //T[19]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.get(0).setInf(true);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(4), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(5), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.get(11).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(10), 1));
        d_In.get(15).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(12), 1));
        d_In.add(new ArcIn(d_P.get(17), d_T.get(13), 1));
        d_In.add(new ArcIn(d_P.get(19), d_T.get(13), 1));
        d_In.get(19).setInf(true);
        d_In.add(new ArcIn(d_P.get(17), d_T.get(14), 1));
        d_In.add(new ArcIn(d_P.get(20), d_T.get(16), 1));
        d_In.add(new ArcIn(d_P.get(21), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(22), d_T.get(18), 1));
        d_In.add(new ArcIn(d_P.get(23), d_T.get(19), 1));
        d_In.add(new ArcIn(d_P.get(24), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(18), d_T.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(14), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(13), d_P.get(18), 1));
        d_Out.add(new ArcOut(d_T.get(15), d_P.get(20), 1));
        d_Out.add(new ArcOut(d_T.get(16), d_P.get(21), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(22), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(23), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(24), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(25), 1));
        d_Out.add(new ArcOut(d_T.get(19), d_P.get(7), 1));
        PetriNet d_Net = new PetriNet("goUntil", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    public static PetriNet CreateNetGoUntil(int cores, int threadComplexity) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0)); //P[0]
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("tlocLESStlim", 0));//P[2]
        d_P.add(new PetriP("finish", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("if1", 0));
        d_P.add(new PetriP("outputST", 0));//P[6]
        d_P.add(new PetriP("outputFin", 0)); //P[7]
        d_P.add(new PetriP("tminLESStlim", 0));//P[8]
        d_P.add(new PetriP("if2", 0));
        d_P.add(new PetriP("P11", 0));
        d_P.add(new PetriP("tlimBIGGERtmod", 0)); //P[11]
        d_P.add(new PetriP("P13", 0));
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("lockNext", 0));
        d_P.add(new PetriP("unlockNext", 1)); //P[15]
        d_P.add(new PetriP("signalNext", 0)); //P[16]
        d_P.add(new PetriP("if3", 0));
        d_P.add(new PetriP("toRemove", 0)); //P[18]
        d_P.add(new PetriP("prev", 0));//P[19]
        d_P.add(new PetriP("P24", 0));
        d_P.add(new PetriP("P26", 0));
        d_P.add(new PetriP("lockPrev", 0)); //P[22]
        d_P.add(new PetriP("P28", 0));
        d_P.add(new PetriP("unlockPrev", 1)); //P[24]
        d_P.add(new PetriP("signalCondLimit", 0));//P[25]
        
        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("while", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("input", d*5*threadComplexity)); //0.1*threadComplexity));
        d_T.add(new PetriT("settLoc=tMin", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("T7", d));
        d_T.add(new PetriT("T8", d));
        d_T.get(7).setPriority(1);
        d_T.add(new PetriT("settloc=tmod", d));
        d_T.add(new PetriT("T10", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(10).setPriority(1);
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("T14", d));
        d_T.get(13).setPriority(1);
        d_T.add(new PetriT("T17", d));
        d_T.add(new PetriT("settloc=tlim", d));
        d_T.add(new PetriT("remove", d));
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d));
        d_T.add(new PetriT("T22", d)); //T[19]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.get(0).setInf(true);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(4), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(5), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.get(11).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(10), 1));
        d_In.get(15).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(12), 1));
        d_In.add(new ArcIn(d_P.get(17), d_T.get(13), 1));
        d_In.add(new ArcIn(d_P.get(19), d_T.get(13), 1));
        d_In.get(19).setInf(true);
        d_In.add(new ArcIn(d_P.get(17), d_T.get(14), 1));
        d_In.add(new ArcIn(d_P.get(20), d_T.get(16), 1));
        d_In.add(new ArcIn(d_P.get(21), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(22), d_T.get(18), 1));
        d_In.add(new ArcIn(d_P.get(23), d_T.get(19), 1));
        d_In.add(new ArcIn(d_P.get(24), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(18), d_T.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(14), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(13), d_P.get(18), 1));
        d_Out.add(new ArcOut(d_T.get(15), d_P.get(20), 1));
        d_Out.add(new ArcOut(d_T.get(16), d_P.get(21), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(22), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(23), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(24), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(25), 1));
        d_Out.add(new ArcOut(d_T.get(19), d_P.get(7), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("goUntilCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    public static PetriNet CreateNetOutput(int k) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("for", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("lockNext", 0)); //P[5]
        d_P.add(new PetriP("waitSt", 0)); //P[6]
        d_P.add(new PetriP("unlockNext", 1)); //P[7]
        d_P.add(new PetriP("signalNext", 0)); //P[8]
        d_P.add(new PetriP("signalNext2", 0)); //P[9]
        d_P.add(new PetriP("waitFin", 0)); //P[10]
        d_P.add(new PetriP("endFor", 0));
        d_P.add(new PetriP("finish", 0)); //P[12]
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("endWhile", 0));

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("for", d));
        d_T.add(new PetriT("trActout", d * 2));
        d_T.get(2).setPriority(2);
        d_T.get(2).setProbability(0.1);  // тільки 1 з 2 мають час виходу = поточний час 
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("noTrActout", d));
        d_T.get(5).setPriority(2);
        d_T.get(5).setProbability(0.9);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(8).setPriority(1); // added 23.02.2021
        d_T.add(new PetriT("doFor", d));
//        for (PetriT tr: d_T){
//            tr.setDistribution("exp", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), k));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(8), 1));
        d_In.get(10).setInf(true);
        d_In.add(new ArcIn(d_P.get(4), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), k));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("Output", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    public static PetriNet CreateNetOutput(int cores, int threadComplexity) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("for", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("lockNext", 0)); //P[5]
        d_P.add(new PetriP("waitSt", 0)); //P[6]
        d_P.add(new PetriP("unlockNext", 1)); //P[7]
        d_P.add(new PetriP("signalNext", 0)); //P[8]
        d_P.add(new PetriP("signalNext2", 0)); //P[9]
        d_P.add(new PetriP("waitFin", 0)); //P[10]
        d_P.add(new PetriP("endFor", 0));
        d_P.add(new PetriP("finish", 0)); //P[12]
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("endWhile", 0));
        double probability  = 1.0/threadComplexity; // only 1 of all could perform act in the current moment
        if(probability>=1.0){
            System.out.println("The model has got the wrong value of probability" +probability);
        }
//        System.out.println("=========" +probability);
        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("for", d*2));
        d_T.add(new PetriT("trActout", d ));
        d_T.get(2).setPriority(2);
        d_T.get(2).setProbability(probability);  // тільки 1 з 2 мають час виходу = поточний час 
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("noTrActout", d));
        d_T.get(5).setPriority(2);
        d_T.get(5).setProbability(1-probability);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(8).setPriority(1); // added 23.02.2021
        d_T.add(new PetriT("doFor", d));
//        for (PetriT tr: d_T){
//            tr.setDistribution("exp", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), threadComplexity));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(8), 1));
        d_In.get(10).setInf(true);
        d_In.add(new ArcIn(d_P.get(4), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), threadComplexity));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(2), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("outputCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    public static PetriNet CreateNetRun() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 1));
        d_P.add(new PetriP("while", 0)); //P[2]
        d_P.add(new PetriP("tlocLESStmod", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("waitSt", 0));
        d_P.add(new PetriP("waitFin", 0));
        d_P.add(new PetriP("runFinish", 0)); //P[7]
        d_P.add(new PetriP("prev", 0));  //P[8]
        d_P.add(new PetriP("tlocLESStlim", 0)); //P[9]
        d_P.add(new PetriP("goUntilSt", 0)); //P[10]
        d_P.add(new PetriP("goUntilFin", 0)); //P[11]
        d_P.add(new PetriP("condCheck", 0)); //P[12]
        d_P.add(new PetriP("tlocEQtlimAndPrev", 0)); //P[13]
        d_P.add(new PetriP("P14", 0));
        d_P.add(new PetriP("unlockPrev", 1)); // P[15]
        d_P.add(new PetriP("lockPrev", 0)); //P[16]
        d_P.add(new PetriP("signalCondLimit", 0)); // P[17]

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("T2", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("waitSt", d));
        d_T.get(2).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("notWait", d));
        d_T.add(new PetriT("goUntilSt", d));
        d_T.get(5).setPriority(2);
        d_T.add(new PetriT("return", d));
        d_T.add(new PetriT("goUntilFin", d));
        d_T.add(new PetriT("updateCond", 0.0));
        d_T.add(new PetriT("remove", d));
        d_T.get(9).setPriority(1);
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d)); //T[11]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(2).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(2), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(5), 1));
        d_In.get(8).setInf(true);
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(9), 1));
        d_In.get(14).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(16), d_T.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(2), 1)); // edit
        PetriNet d_Net = new PetriNet("run", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    public static PetriNet CreateNetRun(int cores) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 1));
        d_P.add(new PetriP("while", 0)); //P[2]
        d_P.add(new PetriP("tlocLESStmod", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("waitSt", 0));
        d_P.add(new PetriP("waitFin", 0));
        d_P.add(new PetriP("runFinish", 0)); //P[7]
        d_P.add(new PetriP("prev", 0));  //P[8]
        d_P.add(new PetriP("tlocLESStlim", 0)); //P[9]
        d_P.add(new PetriP("goUntilSt", 0)); //P[10]
        d_P.add(new PetriP("goUntilFin", 0)); //P[11]
        d_P.add(new PetriP("condCheck", 0)); //P[12]
        d_P.add(new PetriP("tlocEQtlimAndPrev", 0)); //P[13]
        d_P.add(new PetriP("P14", 0));
        d_P.add(new PetriP("unlockPrev", 1)); // P[15]
        d_P.add(new PetriP("lockPrev", 0)); //P[16]
        d_P.add(new PetriP("signalCondLimit", 0)); // P[17]

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("T2", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("waitSt", d));
        d_T.get(2).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("notWait", d));
        d_T.add(new PetriT("goUntilSt", d));
        d_T.get(5).setPriority(2);
        d_T.add(new PetriT("return", d));
        d_T.add(new PetriT("goUntilFin", d));
        d_T.add(new PetriT("updateCond", 0.0));
        d_T.add(new PetriT("remove", d));
        d_T.get(9).setPriority(1);
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d)); //T[11]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(2).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(2), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(5), 1));
        d_In.get(8).setInf(true);
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(9), 1));
        d_In.get(14).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(16), d_T.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(2), 1)); // edit

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("runCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    } 
    
    // відділено спочатку not waiting потім захоплення/очікування локера
    public static PetriNet CreateNetWait() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("unlock", 1));
        d_P.add(new PetriP("condCheck", 0));
        d_P.add(new PetriP("notCond", 0));
        d_P.add(new PetriP("waiting", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("finish", 0));
        d_P.add(new PetriP("getSignal", 0));
        d_P.add(new PetriP("P8", 0));
        d_P.add(new PetriP("P9", 0));

        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("check", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("wait", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("getSignal", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("isNotWaiting", d));
        d_T.add(new PetriT("catch locker", d * 10));
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(3).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("wait", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetWait(int cores) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("unlock", 1));
        d_P.add(new PetriP("condCheck", 0));
        d_P.add(new PetriP("notCond", 0));
        d_P.add(new PetriP("waiting", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("finish", 0));
        d_P.add(new PetriP("getSignal", 0));
        d_P.add(new PetriP("P8", 0));
        d_P.add(new PetriP("P9", 0));

        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("check", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("wait", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("getSignal", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("isNotWaiting", d));
        d_T.add(new PetriT("catch locker", d * 10));
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(3).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(2), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("waitCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
//    public static PetriNet CreateNetMain() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
//        ArrayList<PetriP> d_P = new ArrayList<>();
//        ArrayList<PetriT> d_T = new ArrayList<>();
//        ArrayList<ArcIn> d_In = new ArrayList<>();
//        ArrayList<ArcOut> d_Out = new ArrayList<>();
//        d_P.add(new PetriP("mainStart", 0));
//        d_P.add(new PetriP("", 0));
//        d_P.add(new PetriP("", 0));
//        d_P.add(new PetriP("", 0));
//        d_P.add(new PetriP("", 0));
//        d_P.add(new PetriP("runStA", 0));
//        d_P.add(new PetriP("runStB", 0));
//        d_P.add(new PetriP("", 0));
//        d_P.add(new PetriP("mainEnd", 0));
//        d_P.add(new PetriP("runEndA", 0));
//        d_P.add(new PetriP("runEndB", 0));
//        d_T.add(new PetriT("createTreadA", 0.0));
//        d_T.add(new PetriT("createTreadB", 0.0));
//        d_T.add(new PetriT("startA", 0.0));
//        d_T.add(new PetriT("startB", 0.0));
//        d_T.add(new PetriT("joinA", 0.0));
//        d_T.add(new PetriT("joinB", 0.0));
//        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
//        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
//        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
//        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
//        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
//        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
//        d_In.add(new ArcIn(d_P.get(9), d_T.get(4), 1));
//        d_In.add(new ArcIn(d_P.get(10), d_T.get(5), 1));
//        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
//        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
//        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
//        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
//        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
//        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
//        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
//        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
//        PetriNet d_Net = new PetriNet("Main", d_P, d_T, d_In, d_Out);
//        PetriP.initNext();
//        PetriT.initNext();
//        ArcIn.initNext();
//        ArcOut.initNext();
//
//        return d_Net;
//    }

    public static PetriNet CreateNetPool() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("tasksQueue", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("end", 0));
        d_P.add(new PetriP("P7", 0));
        d_P.add(new PetriP("numWorkers", 0));
        d_T.add(new PetriT("T1", 0.1));
        d_T.add(new PetriT("newRunnable", 0.1));
        d_T.add(new PetriT("invoke", 0.1));
        d_T.add(new PetriT("run", 0.2));
        d_T.add(new PetriT("T5", 0.1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 5));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(6), 2));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(7), 5));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("NewPool", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetThreadPool(int w, int th, double delayCreate, double delayExecute, double delayAwait) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("poolSt",0));
	d_P.add(new PetriP("numThreads",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("tasks",w));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("runSt",0));
	d_P.add(new PetriP("runEnd",0));
	d_P.add(new PetriP("P8",0));
	d_P.add(new PetriP("P9",0));
	d_P.add(new PetriP("poolEnd",0));
        d_P.add(new PetriP("cores",0));
	d_T.add(new PetriT("poolCreate", delayCreate));
        d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(delayCreate/2);
	d_T.add(new PetriT("execute",delayExecute));
        d_T.get(1).setDistribution("unif", d_T.get(1).getTimeServ());
        d_T.get(1).setParamDeviation(delayExecute/2);
	d_T.get(1).setPriority(9);
	d_T.add(new PetriT("shutdown",0.0));
	d_T.add(new PetriT("runSt",0.0));
	d_T.add(new PetriT("runEnd",0.0));
	d_T.add(new PetriT("awaitTermination",delayAwait));
        d_T.get(5).setDistribution("unif", d_T.get(5).getTimeServ());
        d_T.get(5).setParamDeviation(delayAwait/2);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(5),w));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),th));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(9),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(8),1));
        for (PetriT tr: d_T){
            d_In.add(new ArcIn(d_P.get(d_P.size()-1),tr,1));
            d_Out.add(new ArcOut(tr, d_P.get(d_P.size()-1),1));
        }
	PetriNet d_Net = new PetriNet("ThreadPool",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
    }
    
    public static PetriNet CreateNetMain(int m) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("mainStart",1));
	d_P.add(new PetriP("poolStart",0));
	d_P.add(new PetriP("poolEnd",0));
	d_P.add(new PetriP("mainEnd",0));
	d_P.add(new PetriP("cores",m));
	d_T.add(new PetriT("start",0.1));
	d_T.add(new PetriT("finish",0.1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("Main",d_P,d_T,d_In,d_Out);
        PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();
	return d_Net;
    }
    public static PetriNet CreateNetRunnableSimple(double timeMean) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> places = new ArrayList<>();
        ArrayList<PetriT> transitions = new ArrayList<>();
        ArrayList<ArcIn> arcsIn = new ArrayList<>();
        ArrayList<ArcOut> arcsOut = new ArrayList<>();
        places.add(new PetriP("runSt",0));
        places.add(new PetriP("runEnd",0));
        places.add(new PetriP("cores",0));
        transitions.add(new PetriT("run", timeMean,Double.MAX_VALUE));
        transitions.get(0).setDistribution("exp",
        transitions.get(0).getTimeServ());
        arcsIn.add(new ArcIn(places.get(0),transitions.get(0),1));
        arcsIn.add(new ArcIn(places.get(2),transitions.get(0),1));
        arcsOut.add(new ArcOut(transitions.get(0),places.get(1),1));
        arcsOut.add(new ArcOut(transitions.get(0),places.get(2),1));
        PetriNet d_Net = new PetriNet("Runnable", places, transitions, arcsIn, arcsOut);	
        PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();
	return d_Net;
    }

    public static PetriNet CreateNetThrPool(int w, int k) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("poolSt",0));
	d_P.add(new PetriP("numThreads",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("tasks",w));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("runSt",0));
	d_P.add(new PetriP("runEnd",0));
	d_P.add(new PetriP("P8",0));
	d_P.add(new PetriP("P9",0));
	d_P.add(new PetriP("poolEnd",0));
	d_P.add(new PetriP("cores",0));
	d_T.add(new PetriT("poolCreate",0.0));
	d_T.add(new PetriT("execute",0.0));
	d_T.get(1).setPriority(9);
	d_T.add(new PetriT("shutdown",0.0));
	d_T.add(new PetriT("runSt",0.0));
	d_T.add(new PetriT("runEnd",0.0));
	d_T.add(new PetriT("awaitTermination",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(5),w));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),k));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(9),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(8),1));
        for (PetriT tr: d_T){
            d_In.add(new ArcIn(d_P.get(d_P.size()-1),tr,1));
            d_Out.add(new ArcOut(tr, d_P.get(d_P.size()-1),1));
        }
	PetriNet d_Net = new PetriNet("ThPool",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
    }
    
    public static PetriNet CreateNetThPool(int w, int th, double delayCreate, double delayExecute, double delayAwait) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("poolSt",0));
	d_P.add(new PetriP("numThreads",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("tasks",w));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("runSt",0));
	d_P.add(new PetriP("runEnd",0));
	d_P.add(new PetriP("P8",0));
	d_P.add(new PetriP("P9",0));
	d_P.add(new PetriP("poolEnd",0));
        d_P.add(new PetriP("cores",0));
	d_T.add(new PetriT("poolCreate", delayCreate));
        //d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
       // d_T.get(0).setParamDeviation(delayCreate/4);
	d_T.add(new PetriT("execute",delayExecute));
        d_T.get(1).setDistribution("unif", d_T.get(1).getTimeServ());
        d_T.get(1).setParamDeviation(delayExecute/4);
	d_T.get(1).setPriority(9);
	d_T.add(new PetriT("shutdown",0.0));
	d_T.add(new PetriT("runSt",0.0));
	d_T.add(new PetriT("runEnd",0.0));
	d_T.add(new PetriT("awaitTermination",delayAwait));
        d_T.get(5).setDistribution("unif", d_T.get(5).getTimeServ());
        d_T.get(5).setParamDeviation(delayAwait/4);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(5),w));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),th));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(9),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(8),1));
        for (PetriT tr: d_T){
            d_In.add(new ArcIn(d_P.get(d_P.size()-1),tr,1));
            d_Out.add(new ArcOut(tr, d_P.get(d_P.size()-1),1));
        }
	PetriNet d_Net = new PetriNet("ThreadPool",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
    }
    
    public static PetriNet CreateNetMainPool(int m) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("mainStart",1));
	d_P.add(new PetriP("poolStart",0));
	d_P.add(new PetriP("poolEnd",0));
	d_P.add(new PetriP("mainEnd",0));
	d_P.add(new PetriP("cores",m));
	d_T.add(new PetriT("start",0.0));
	d_T.add(new PetriT("finish",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("Main",d_P,d_T,d_In,d_Out);
        PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();
	return d_Net;
    }
    
    public static PetriNet CreateNetFor(int m) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("numIteration",m));
	d_P.add(new PetriP("computaionStart",0));
	d_P.add(new PetriP("computationEnd",0));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("for",1));
	d_T.add(new PetriT("forStart",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_T.add(new PetriT("forFinish",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(2),m));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(5),1));
	PetriNet d_Net = new PetriNet("For",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
    }
    public static PetriNet CreateNetWhile() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("whileStart",0));
	d_P.add(new PetriP("conditiion",1));
	d_P.add(new PetriP("computationStart",0));
	d_P.add(new PetriP("computationEnd",0));
	d_P.add(new PetriP("whileIsFinished",0));
	d_T.add(new PetriT("whileStart",0.0));
	d_T.get(0).setPriority(9);
	d_T.add(new PetriT("whileFinish",0.0));
	d_T.add(new PetriT("T3",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(0),1));
	d_In.get(2).setInf(true);
	d_In.add(new ArcIn(d_P.get(3),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(0),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("While",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
    }
    public static PetriNet CreateNetIf() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("condition",1));
	d_P.add(new PetriP("ifStart",0));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("",0));
	d_T.add(new PetriT("If",0.0));
	d_T.get(0).setPriority(5);
	d_T.add(new PetriT("else",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.get(0).setInf(true);
	d_In.add(new ArcIn(d_P.get(1),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("If",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
    }
public static PetriNet CreateNetCreateThread() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("runStart",0));
	d_P.add(new PetriP("runEnd",0));
	d_P.add(new PetriP("P6",0));
	d_P.add(new PetriP("P7",0));
	d_T.add(new PetriT("createThread",0.0));
	d_T.add(new PetriT("start",0.0));
	d_T.add(new PetriT("end",0.0));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(6),1));
	PetriNet d_Net = new PetriNet("CreateThread",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}

public static PetriNet CreateNetSleep() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("threadB.runEnd",0));
	d_T.add(new PetriT("sleepStart",0.0));
	d_T.add(new PetriT("sleepDelay",0.0));
	d_T.add(new PetriT("sleepEnd",0.0));
	d_T.add(new PetriT("threadB.join",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("Sleep",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetSyncMethod() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("monitor",1));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("owner",0));
	d_T.add(new PetriT("syncMethodStart",0.0));
	d_T.add(new PetriT("syncMethodEnd",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("SyncMethod",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetReentrantLock() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("lockingStart",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("owner",0));
	d_P.add(new PetriP("holdCount",0));
	d_P.add(new PetriP("lock",1));
	d_P.add(new PetriP("lockingEnd",0));
	d_P.add(new PetriP("P7",0));
	d_T.add(new PetriT("lock",0.0));
	d_T.add(new PetriT("asOwner",0.0));
	d_T.add(new PetriT("release",0.0));
	d_T.get(2).setPriority(9);
	d_T.add(new PetriT("unlock",0.0));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.get(7).setInf(true);
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(1),1));
	PetriNet d_Net = new PetriNet("ReentrantLock",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetTryLock() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("lockingStart",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("lockingEnd",0));
	d_P.add(new PetriP("lock",1));
	d_P.add(new PetriP("owner",0));
	d_T.add(new PetriT("tryLockTrue",0.0));
	d_T.get(0).setPriority(9);
	d_T.add(new PetriT("tryLockFalse",0.0));
	d_T.add(new PetriT("unlock",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(5),1));
	PetriNet d_Net = new PetriNet("TryLock",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetTryReentrantLock() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("lockingStart",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("lockingEnd",0));
	d_P.add(new PetriP("lock",1));
	d_P.add(new PetriP("owner",0));
	d_P.add(new PetriP("holdCount",0));
	d_T.add(new PetriT("tryLockTrue",0.0));
	d_T.get(0).setPriority(9);
	d_T.add(new PetriT("tryLockFalse",0.0));
	d_T.add(new PetriT("unlock",0.0));
	d_T.add(new PetriT("tryAsOwner",0.0));
	d_T.get(3).setPriority(5);
	d_T.add(new PetriT("release",0.0));
	d_T.get(4).setPriority(9);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(3),1));
	d_In.get(9).setInf(true);
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(6),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(3),1));
	PetriNet d_Net = new PetriNet("TryReentrantLock",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetSharedAccess() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("P6",0));
	d_P.add(new PetriP("lock",1));
	d_P.add(new PetriP("owner",0));
	d_T.add(new PetriT("lock",0.0));
	d_T.add(new PetriT("read",0.0));
	d_T.add(new PetriT("modify",0.0));
	d_T.add(new PetriT("write",0.0));
	d_T.add(new PetriT("unlock",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(4),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(6),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(7),1));
	PetriNet d_Net = new PetriNet("SharedAccess",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetWaitNotify() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("condCheck",0));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("condition",0));
	d_P.add(new PetriP("signalTo",0));
	d_P.add(new PetriP("monitor",1));
	d_P.add(new PetriP("",0));
	d_P.add(new PetriP("owner",0));
	d_P.add(new PetriP("waiting",0));
	d_P.add(new PetriP("signalFrom",0));
	d_P.add(new PetriP("",0));
	d_T.add(new PetriT("syncStart",0.0));
	d_T.add(new PetriT("check",0.0));
	d_T.get(1).setPriority(9);
	d_T.add(new PetriT("notifyAll",0.0));
	d_T.add(new PetriT("syncEnd",0.0));
	d_T.add(new PetriT("catchMonitor",0.0));
	d_T.add(new PetriT("wait",0.0));
	d_T.add(new PetriT("getSignal",0.0));
	d_T.get(6).setPriority(9);
	d_T.add(new PetriT("isNotWaiting",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(1),1));
	d_In.get(4).setInf(true);
	d_In.add(new ArcIn(d_P.get(7),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(10),d_T.get(6),1));
	d_In.add(new ArcIn(d_P.get(11),d_T.get(7),1));
	d_In.add(new ArcIn(d_P.get(11),d_T.get(6),1));
	d_In.add(new ArcIn(d_P.get(9),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(9),d_T.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(6),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(9),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(9),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(10),1));
	d_Out.add(new ArcOut(d_T.get(6),d_P.get(8),1));
	d_Out.add(new ArcOut(d_T.get(7),d_P.get(12),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(7),1));
	PetriNet d_Net = new PetriNet("WaitNotify",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
}