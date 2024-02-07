/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Inna Stetsenko
 */
public class TestTryLockFriends {

    public static void main(String[] args) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {

        int num = 4;
        Friend[] friends = new Friend[num];
        for (int j = 0; j < num; j++) {
            friends[j] = new Friend("Friend_" + j);
        }
        ArrayList<Thread> threads = new ArrayList<>();
        
//        for (int j = 0; j < num; j++) {  // to test why deсreasing frequency
//            Friend other = new Friend("other_Friend_" + j);
//
//            threads.add(new Thread(new BowLoop(friends[j], other, 1))); // sleep 1
//            threads.add(new Thread(new BowLoop(other, friends[j], 1))); // sleep 1
//        }

        for (int j = 0; j < num; j++) {
            for (int i = 0; i < num; i++) {
                if (i != j) {
                    threads.add(new Thread(new BowLoop(friends[j], friends[i], 1))); // sleep 1
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
               // friend.print();
                
            }
            friends[0].print();
            
            System.out.println(res / friends.length);

        } catch (InterruptedException ex) {
            Logger.getLogger(TestTryLockFriends.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
