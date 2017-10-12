package flightFinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import Models.Flight;
import Models.Airport;

public class TestFlightSearch {
	@Test
	/**
	 * Use Junit Test to test whether the "Search" class operate well enough.
	 */
	public void testFlishtSearch(){
		Search s = new Search();

		ArrayList<ArrayList<Flight>>res = new ArrayList<>();
		Date dep = new Date("2017 May 09");
		
		Airport depar = new Airport("Logan International", "BOS", -5);
		Airport arr = new Airport("Chicago O'Hare International", "ORD", -6);
		res = s.Search_Path(depar, arr, dep, 2, true);
		//System.out.println(res.size());
		int i =1;
		for(List<Flight> lf: res){
			//System.out.println(i++);
			for(Flight f: lf){
				System.out.println(f.toString());
			}
		}
	}

}
