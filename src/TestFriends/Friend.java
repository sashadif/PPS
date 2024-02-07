/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author innastetsenko
 */
public class Friend {
     private final ReentrantLock lock = new ReentrantLock();
        private int bow = 0;
        private int bowBack = 0;
        private int failure = 0;
        private String name;
        
        
        public Friend(String name){
            this.name = name;
        }
   

        public boolean impendingBow(Friend bower) {
            Boolean myLock = false;
            Boolean yourLock = false;
            try {
                myLock = getLock().tryLock(); //true if lock was acquired
                yourLock = bower.getLock().tryLock(); //true if lock was acquired

            } finally {
                if (!(myLock && yourLock)) {
                    if (myLock) {
                        getLock().unlock(); // release this lock
                    }
                    if (yourLock) {
                        bower.getLock().unlock();
                    }
                }
            }
            return myLock && yourLock;
        }

        public void bow(Friend bower) {

            if (impendingBow(bower)) {
                try {
                    bow++;

                    bower.bowBack(this);

                } finally {
                    getLock().unlock();
                    bower.getLock().unlock();
                }
            } else {
                failure++;

            }
        }

        public void bowBack(Friend bower) {
//            try {
//                
//                Thread.sleep(1); // cause great impact on failures
//            } catch (InterruptedException ex) {
//                Logger.getLogger(LockFriends.class.getName()).log(Level.SEVERE, null, ex);
//            }
            bowBack++; // delay in bow Back cause increasing failures

        }

        public double getResult() {
            return (double) failure / ((double) failure + (double) bow);
        }

        public void print() {
            System.out.println(this.getName() + " ,failures " + failure + ", bows " + bow);

            //   System.out.println (sumBowTime/1000+",  "+ sumBackTime/bow+",  "+sumLockTime/1000);
        }

    /**
     * @return the lock
     */
    public ReentrantLock getLock() {
        return lock;
    
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    public static Long getTimePerformance(){
        Friend peter = new Friend("Peter");
        Friend other = new Friend("Other");
        Long t= System.nanoTime();
        peter.bowBack(other);
        return System.nanoTime()-t;
    }
}
