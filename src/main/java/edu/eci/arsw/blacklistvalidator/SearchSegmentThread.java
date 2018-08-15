/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

/**
 *
 * @author 2104784
 */
public class SearchSegmentThread extends Thread{
    
    int lima,limb;
    
    int ocurrencesCount=0;
    
    public SearchSegmentThread(int a, int b){
        this.lima=a;
        this.limb=b;
    }
    
    @Override
    public void run(){
        if (lima < 0 || limb < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        
        
    }
    
    public int instances(){
        
        return ocurrencesCount;
    }
}
