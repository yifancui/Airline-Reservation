package Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import GUI.StateMachine;

/**
 * Data type to hold XML args of WPI-defined Flight
 * @author gunna
 *
 */
public class Flight {
	public Airplane type;		// type of airplane
	public int duration;		// how long the flight is to last (in minutes)
	public int num;				// UID flight number
	
	public Airport dep;			// departing airport
	public Airport arr;			// arrival airport
	
	public Date depDate;		// departure time
	public Date arrDate;		// arrival time
	
	public double firstPrice;	// first class ticket price
	public double coachPrice;	// coach class ticket price
	
	public int firstSeats;		// # of already reserved first class seats
	public int coachSeats; 		// # of already reserved coach class seats
	
	public Flight(Airplane type, int duration, int num, Airport dep, Airport arr, String depTime, String arrTime, double firstPrice, double coachPrice, int firstSeats, int coachSeats) {
		this.type = type;
		this.duration = duration;
		this.num = num;
		this.dep = dep;
		this.arr = arr;
		this.depDate = getDateFromString(depTime);
		this.arrDate = getDateFromString(arrTime);
		this.firstPrice = firstPrice;
		this.coachPrice = coachPrice;
		this.firstSeats = firstSeats;
		this.coachSeats = coachSeats;
		//this.totalDuration = 0;
	}
	
	public String toString(){
		return "Flight " + this.num + ", " + this.dep.code + " --> " + this.arr.code + " DepDate " +"|"+ this.depDate+"|"
				     +" duration " + "|"+ this.duration+"|" + " Arrival Date "+"|" + this.arrDate+"|" + " firstSeats" + this.firstSeats 
				     +" coachSeats " + this.coachSeats;
	}
	
	public Date getDateFromString(String stringDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd hh:mm zzz",Locale.ENGLISH);
		
		try {
			Date date = sdf.parse(stringDate);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public double getPrice() {
		if(StateMachine.getInstance().order.firstClass) {
			return this.firstPrice;
		} else {
			return this.coachPrice;
		}
	}
	
	public int getDuration() { return this.duration; }
	public Date getDate() { return this.depDate; }
}
