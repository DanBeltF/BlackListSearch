/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @param n quantity of threads
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int n){
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        int ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int checkedListsCount=0;
        
        int div=skds.getRegisteredServersCount()/n;
        int mod=skds.getRegisteredServersCount()%n;
        
        int inic=0;
        
        SearchSegmentThread[] sst = new SearchSegmentThread[n];
        
        for(int i=0;i<=n-1;i++){
            if(i!=n-1){
                //sst[i]= new SearchSegmentThread(inic+i*div,div,ipaddress);
                sst[i]= new SearchSegmentThread(inic, skds.getRegisteredServersCount(), ipaddress);
                //sst[i].start();
            }
            else{
                //sst[i]= new SearchSegmentThread(inic+i*div,div+mod,ipaddress);
                sst[i]= new SearchSegmentThread(inic, inic+div, ipaddress);
                //sst[i].start();
                inic+=div;
            }
        }
        
        for (int i=0;i<n;i++){
            sst[i].start();
        }
        
        for(int i=0;i<=n-1;i++){
            try {
                sst[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HostBlackListsValidator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(SearchSegmentThread s : sst){
            //System.out.println(s);
            //System.out.println(checkedListsCount);
            //System.out.println(s.getCheckedLists());
            checkedListsCount+=s.getCheckedLists();
            //System.out.println(checkedListsCount);
            ocurrencesCount+=s.getOcurrences();
            blackListOcurrences.addAll(s.getBlackList());
        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    public static int getBlackListAlarmCount(){
        return BLACK_LIST_ALARM_COUNT;
    }
    
    
}
