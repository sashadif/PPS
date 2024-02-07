/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFriends;


import PetriObj.*;
import java.util.ArrayList;

/**
 *
 * @author innastetsenko
 */
public class TestFriendsModel {

    public static void main(String[] args) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay, InterruptedException {
       
    //    PetriObjModel model = getModel(16, 20, 100, 0.01);
        
    //   PetriObjModel model = getModelWithoutCores(16, 100, 0.0000001);
     PetriObjModel model = getBowingFriendModel(1,2,1);
       model.setIsProtokol(true);
//       PetriSim friend = model.getListObj().get(0);
//       PetriT tr = model.getListObj().get(0).getNet().getListT()[10];
//       System.out.println(tr.getParametr()+"\t   "+tr.getTimeServ());
       
   //    System.out.println(result(model, 100)); //100000000
       
       System.out.println("freq of failures = "+getResultBowingModel(model));
        
       for(PetriSim sim: model.getListObj()){
            ((FriendPetri)sim).print();
       }
       
    }
    
    public static double result(PetriObjModel model, double time){
        model.go(time);
        double res=0.0;
        for(PetriSim sim: model.getListObj()){
            res+=((FriendPetri)sim).getResult();
        }
        return res/model.getListObj().size();
    }
    
    
    public static double getResultBowingModel(PetriObjModel model){
        model.go(100);
        double res=0.0;
         for(PetriSim sim: model.getListObj()){
             FriendPetri f = (FriendPetri)sim;
             double failures = (double)f.getNet().getListP()[6].getMark();
             double bows = (double)f.getNet().getListP()[3].getMark();
             res+=failures/(failures+bows);
 //           System.out.println(f.getNet().getListP()[16].getName()+", "+f.getNet().getListP()[16].getMark());
//             System.out.println(f.getNet().getListP()[6].getMark()+", "+f.getNet().getListP()[3].getMark());
             
         }
         
         return res/model.getListObj().size();
    }
    
    
    public static PetriObjModel getBowingFriendModel(int numFriends, int cores, double delay) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay{
        ArrayList<PetriSim> list = new ArrayList<>();
   
        int num=numFriends;
        FriendPetri[] friends = new FriendPetri[num];
        for(int j=0;j<num;j++){
            friends[j] = new FriendPetri("Friend_"+j, 100, cores, delay); // 100 is the number of all bows (unsuccessful and successful)
        }
//        for(int j=0;j<num;j++){
//            for(int i=0;i<num;i++)
//                if(i!=j)
//                    friends[j].addBowingFriend(friends[i]);
//        }
        
        for(int j=0;j<num;j++){
            list.add(friends[j]);
        }
 
        return new PetriObjModel(list);
    }
    
    
    public static PetriObjModel getModel(int numFriends, int cores, double delay, double ratio) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriSim> list = new ArrayList<>();

        int num = numFriends;
        FriendPetri[] friends = new FriendPetri[num];
        for (int j = 0; j < num; j++) {
            friends[j] = new FriendPetri("Friend_" + j, 1000, cores, delay, ratio); // 1000 is the number of all bows (unsuccessful and successful)
        }
        for (int j = 0; j < num; j++) {
            for (int i = 0; i < num; i++) {
                if (i != j) {
                    friends[j].addFriend(friends[i]);
                }
            }
        }

        for (int j = 0; j < num; j++) {
            list.add(friends[j]);
        }

        return new PetriObjModel(list);
    }

    public static PetriObjModel getModelWithoutCores(int numFriends, double delay, double ratio) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay{
        ArrayList<PetriSim> list = new ArrayList<>();
   
        int num=numFriends;
        FriendPetri[] friends = new FriendPetri[num];
        for(int j=0;j<num;j++){
            friends[j] = new FriendPetri("Friend_"+j, 1000, delay, ratio); // 1000 is the number of all bows (unsuccessful and successful)
        }
        for(int j=0;j<num;j++){
            for(int i=0;i<num;i++)
                if(i!=j)
                    friends[j].addFriendWithoutCores(friends[i]);
        }
        
        for(int j=0;j<num;j++){
            list.add(friends[j]);
        }
 
        return new PetriObjModel(list);
    }
    
    
    public static PetriObjModel getModel() // four friends
            throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay{
        ArrayList<PetriSim> list = new ArrayList<>();
    
        FriendPetri friendA = new FriendPetri("A", 1000);
        FriendPetri friendB = new FriendPetri("B", 1000);

        FriendPetri friendC = new FriendPetri("C", 1000);
        FriendPetri friendD = new FriendPetri("D", 1000);

        friendA.addFriend(friendB);
        friendA.addFriend(friendC);
        friendA.addFriend(friendD);

        friendB.addFriend(friendA);
        friendB.addFriend(friendC);
        friendB.addFriend(friendD);

        friendC.addFriend(friendA);
        friendC.addFriend(friendB);
        friendC.addFriend(friendD);

        friendD.addFriend(friendA);
        friendD.addFriend(friendB);
        friendD.addFriend(friendC);

        list.add(friendA);
        list.add(friendB);
        list.add(friendC);
        list.add(friendD);
  
        return new PetriObjModel(list);
    }
    
    
    
}
