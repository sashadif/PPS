/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author innastetsenko
 */
public class BowLoop implements Runnable {

    private Friend a;
    private Friend b;
    private long delay;

    public BowLoop(Friend bower, Friend bowee, long delaySleep) {
        a = bower;
        b = bowee;
        delay = delaySleep;
    }

    @Override
    public void run() {
        for (int j = 0; j < 1000; j++) {            
            a.bow(b); // was b.bow(a)
            try {
                 TimeUnit.NANOSECONDS.sleep(delay);
//                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestTryLockFriends.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

   
       

}
