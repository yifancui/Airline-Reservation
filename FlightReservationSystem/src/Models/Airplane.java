package Models;

/**
 * Data type to hold XML args of WPI-defined Airplane
 * @author gunna
 *
 */
public class Airplane {


	public String manufacturer;	// manufacturer of this plane
	public String model;			// model number of this plane
	public int totFirst;			// how many first class seats exist on this plane
	public int totCoach;			// how many coach class seats exist on this plane
	
	public Airplane(String manufacturer, String model, int totFirst, int totCoach) {
		this.manufacturer = manufacturer;
		this.model = model;
		this.totFirst = totFirst;
		this.totCoach = totCoach;
	}
	public String toString(){
		return "Airplane[manufacturer = "+manufacturer+",model = "+model+",totFirst = "+totFirst+",totCoach = "+totCoach+"]";
	}
}
