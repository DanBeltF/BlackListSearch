/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

/**
 *
 * @author 2104784
 */
public class SearchSegmentThread extends Thread{
    
    int lima=0;
    int limb=0;
    String ipaddress;
    int checkedListsCount=0;
    LinkedList<Integer> blackListOcurrences= new LinkedList<>();
    HostBlacklistsDataSourceFacade skds;
    int ocurrencesCount;
    
    public SearchSegmentThread(int a, int b, String ipaddress){
        this.lima=a;
        this.limb=b;
        this.ipaddress=ipaddress;
        //this.checkedListsCount=0;
        //this.ocurrencesCount=0;
        //blackListOcurrences = new LinkedList<>();
        
    }
    
    @Override
    public void run(){
        if (lima < 0 || limb < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        skds=HostBlacklistsDataSourceFacade.getInstance();
        for (int i=lima;i<limb /*&& ocurrencesCount < HostBlackListsValidator.getBlackListAlarmCount()*/;i++){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        
        }        
    }
    
    public int getCheckedLists(){
        return checkedListsCount;
    }
    
    public int getOcurrences(){
        return ocurrencesCount;
    }
    
    public LinkedList<Integer> getBlackList(){
        return blackListOcurrences;
    }
}
