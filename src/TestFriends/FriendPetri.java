/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import PetriObj.PetriP;
import PetriObj.PetriSim;

/**
 *
 * @author innastetsenko
 */
public class FriendPetri extends PetriSim {

    public FriendPetri(String name, int loop, 
                    int cores, double delay) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay{
        super(Nets.CreateNetBowingFriend(name, loop,cores,delay)); //the new net for friend is loading
        
    }    
        
        
    public FriendPetri(String name, int loop, 
                    int cores, double delay, double ratio)
                        throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        super(Nets.CreateNetFriendUsingCores(name, loop, cores, delay, ratio)); // x=ratio
      
    
    }
    public FriendPetri(String name)
                        throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        super(Nets.CreateNetFriendUsingCores(name, 1000, 2, 100, 0.01)); // x=ratio
      
    
    }
    
     public FriendPetri(String name, int loop, 
                     double delay, double ratio)
                        throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        super(Nets.CreateNetFriendWithoutCores(name, loop, delay, ratio)); // x=ratio
      
    
    }

    public FriendPetri(String name, int loop) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        super(Nets.CreateNetFriendUsingCores(name, loop, 2, 100, 0.8)); // 2 cores, delay=100,x=0.8
    }

   
    public int getFailures(){
        return this.getNet().getListP()[6].getMark() ;
    }
    
    public int getBows(){
        return this.getNet().getListP()[8].getMark();
    }
    

    public void addFriend(FriendPetri other) {
        this.getNet().getListP()[7] = other.getNet().getListP()[2]; //lockOther = lock
        this.getNet().getListP()[15] = other.getNet().getListP()[15]; // coresOther = cores

    }
    
    public PetriP getLock(){
        return this.getNet().getListP()[15];
    }
    
     public void addBowingFriend(FriendPetri other) {
        this.getNet().getListP()[7] = other.getNet().getListP()[5]; //lockOther = lock
        this.getNet().getListP()[16] = other.getNet().getListP()[16]; // coresOther = cores

    }
    
    public void addFriendWithoutCores(FriendPetri other) {
        this.getNet().getListP()[7] = other.getNet().getListP()[2]; //lockOther = lock
    }
    
    public void printDelayTransition(){
        System.out.println(this.getNet().getListT()[0].getParametr());
    }
    
    public double getResult() { // for net using cores
            return (double) getFailures() / ((double) getFailures() + (double) getBows());
        }

    
     public void print() {
            System.out.println(this.getName() + " ,failures " + getFailures() + ", bows " + getBows());

            //   System.out.println (sumBowTime/1000+",  "+ sumBackTime/bow+",  "+sumLockTime/1000);
        }
   

}
