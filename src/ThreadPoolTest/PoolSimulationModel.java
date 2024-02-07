/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import java.util.ArrayList;

/**
 *
 * @author dyfuchyna
 */
public class PoolSimulationModel extends PetriObjModel{
    
    public PoolSimulationModel(ArrayList<PetriSim> listObj) {
        super(listObj);
    }
    
    @Override
    public boolean isFinishSimulation(){  // additional condition to stop simulation
        //System.out.println("isFinishedSimulation");
        PetriSim main = null;
        for(PetriSim o: this.getListObj()){
            if (o.getName().equalsIgnoreCase("main")){
                main=o;
                break;
            }
        }
        return main.getNet().getListP()[3].getMark()==1;
    }
}
