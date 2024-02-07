/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadPoolTest;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import java.util.ArrayList;

/**
 *
 * @author innastetsenko
 */
public class PoolModelTest {
    public static void main(String[] args) throws InterruptedException {
    
    }
    
    public static PetriObjModel createModel() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriSim> list= new ArrayList<>();   
       // PetriSim pool = new PetriSim(CreateNetFixedPool());
    
        return new PetriObjModel(list);
    }
}
