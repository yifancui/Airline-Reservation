package GUI;

import java.util.ArrayList;

import Models.Flight;
import Models.Order;
import flightFinder.Search;

public class StateMachine {

	
	private StateMachine() { }
	
	public static StateMachine getInstance() {
		if(sm == null) {
			sm = new StateMachine();
		}
		return sm;
	}
	
	public enum state { input_params_first, input_params_second, display_flights, confirm_order, finish}	
	
	private static StateMachine sm;
	public Order order;
	public SceneSwitcher sceneSwitcher;
	public ArrayList<ArrayList<Flight>> flights = new ArrayList<ArrayList<Flight>>();
	
	private void performSearch() {
		 this.flights =  new Search().Search_Path(order.dep, order.arr, order.depDate,order.stopovers,order.firstClass);
		 System.out.println("hi");
	}
	
	public void switchState(state s) {
		switch(s) {
		case input_params_first:
			this.sceneSwitcher.displayFirst();
			break;
		case input_params_second:
			this.sceneSwitcher.displaySecond();
			break;
		case display_flights:
			performSearch();
			this.sceneSwitcher.displayFlightsDisplay();
			break;
		case confirm_order:
			this.sceneSwitcher.displayConfirm();			
			break;
		case finish:
			sceneSwitcher.close();
			break;
		default:
			break;
		}
	}
	
}