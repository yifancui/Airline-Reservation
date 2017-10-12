package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * Data type to define customer-given inputs for specific GUI order
 * @author gunna
 *
 */
public class Order {
	public boolean firstClass;	// whether order is first class
	
	public Airport dep;			// departing airpot
	public Airport arr;			// arrival airport
	public Date depDate;		// date of departure
	public boolean roundtrip;	// whether we have a round trip or not
	public int stopovers;		// maximum number of stopovers
		
	public boolean secondRound = false;
	
	public ArrayList<Flight> firstFlightPath = new ArrayList<Flight>();
	public ArrayList<Flight> secondFlightPath = new ArrayList<Flight>();
	
	public Order(Airport dep, Airport arr, Date depDate, boolean firstClass, boolean roundtrip, int stopovers) {		
		this.firstClass = firstClass;
		this.dep = dep;
		this.arr = arr;
		this.depDate = depDate;
		this.roundtrip = roundtrip;
		this.stopovers = stopovers;
	}
}