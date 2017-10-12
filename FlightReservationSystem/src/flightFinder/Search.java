package flightFinder;
import java.util.*;
import Models.Airport;
import Models.Flight;

import QueryManager.queryManager;



public class Search{
    private ArrayList<ArrayList<Flight>> ans = new ArrayList<ArrayList<Flight>>();
    private String end_code = null;
    private Date localStartTime = null;
    private int maxStopOver = -1;
	private boolean isFirstClass = false; 
    
    private HashMap<String, ArrayList<Flight>> myMap = new HashMap<String, ArrayList<Flight>>();

    /**
     * 
     * A main method of the class, which is to return all the result list.
     * 
     * This function passes departure airport code, arrival airport code, departure date, the maxium stop overs users 
     * would like to have and the seat class the users has chosen, then it would call dfs(depth first search) function 
     * later and the searching result corresponding to users' requirement. 
     * 
     * @param start 		departure airport
     * @param end			arrival airport
     * @param depTime		departure date users would like to leave (GMT)
     * @param maxStopOver	how many stop overs the users would like to take [0,2]
     * @param isFirstClass	boolean is true when it is first class, false when it is coach class
     * @return				ArrayList<ArrayList<Flight>>() e.g., valid flights
     */
    
    public ArrayList<ArrayList<Flight>> Search_Path(Airport start, Airport end, Date depTime, int maxStopOver, boolean isFirstClass){
        this.end_code = end.code;
        this.maxStopOver = maxStopOver;
        this.isFirstClass = isFirstClass;
		this.localStartTime = new Date(depTime.getTime() - start.gmtOffset * 60 * 60 * 1000);
        
        this.dfs(start.code, depTime, 0 , new ArrayList<Flight>());
        return this.ans;
    }
    
    /**
     * This is where we implement the Depth First Search algorithm, the main idea is to recursively call the dfs until
     *  "max stopover" achieved.
     * 
     * Call "getFlights" repeatedly to get all arrival airports by given departure airports and date.
     * Update now_code, depTime, depth and s repeatedly until we get the desired arrival airport, which meets time limit and has space. 
     * We have avoided the situation when the user flies back to where he has departed before.
     *  
     * @param landCode 		the current airport to search from (assumed to have just landed in)
     * @param landTime 		the arrival time from flight (null if beginning search)
     * @param depth			search depth
     * @param s				To store the arrival airports.
     */
    
    private void dfs(String landCode, Date landTime, int depth, ArrayList<Flight> s){
        if (landCode.equals(end_code)){
            ans.add((ArrayList<Flight>) s.clone()); // s.clone to list
            return;
        }
        if (depth > maxStopOver) return;
        
        for (Flight f : getFlights(landCode, landTime)){
        	if (canfly(f, landTime, depth)){
                s.add(f);
                dfs(f.arr.code, f.arrDate, depth+1,s);
                int last = s.size()-1;
                s.remove(last);
            }
        }
    }
    
    /**
     * A helper function, to check whether the flight has met the time constraint and seat constraint.
     * 
     * To check whether the stop over time is between 0.5 hour to 4 hours, and the flight has space for chosen seat class.
     * 
     * @param Flight		proposed flight to take form current airport (item to validate)	
     * @param landTime		arrival time at current airport (constraint to meet)
     * @param depth			stopover time constraint should just work when depth is to 0.
     * 
     * @return canFly		boolean corresponding to input legitimacy
     */
    
    private boolean canfly(Flight f, Date landTime, int depth){
    	boolean validDate = true;
    	boolean validLayover = true;
    	if(depth != 0) {
        	boolean early = f.depDate.getTime() < landTime.getTime() + 30*60*1000;	// 30*60*1000 is 30 minutes in milliseconds
        	boolean late = f.depDate.getTime() > landTime.getTime() + 4*60*60*1000;	// 4*60*60*1000 is 4 hours in milliseconds
        	validLayover = !early && !late;
    	} else {
    		validDate = f.depDate.getTime() > localStartTime.getTime();
    	}
    	
    	boolean hasSpace = true;
    	if(isFirstClass){
    		hasSpace = f.type.totFirst - f.firstSeats > 0;	
    	} else {
    		hasSpace = f.type.totCoach - f.coachSeats > 0;
    	}
    		return hasSpace && validLayover && validDate;
    }
    
    /**
     * A helper function to optimize the DFS and make it more efficient.
     * Basically "Don't query server if we've queried it before"
     * 
     * @param landCode		current departure airport code
     * @param landTime		current layer's arrival time
     * @return				
     */
    
    private List<Flight> getFlights(String landCode, Date landTime) {
    	if(!myMap.containsKey(landCode)) {
    		ArrayList<Flight> myQuery = queryManager.getDepFlights(landCode, landTime);
        	myMap.put(landCode, myQuery);
    	}
    	return myMap.get(landCode);
    }
}