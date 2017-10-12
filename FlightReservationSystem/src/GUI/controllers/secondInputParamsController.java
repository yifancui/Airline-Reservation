package GUI.controllers;


import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import GUI.StateMachine;
import Models.Airport;
import Models.Flight;
import Models.Order;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class secondInputParamsController {
	@FXML private DatePicker date;
		
	@FXML
    /**
     * 
     * An initialization method of the controller, used to handle state transitions
     * and populate GUI elements
     * 
     */
	public void initialize(){ }
	
	@FXML
    /**
     * A callback to be run when the "select flight" button is pressed
     * 
     */
	public void btnclickk() throws IOException{
		StateMachine sm = StateMachine.getInstance();
		if(!verifyInputs(sm.order.arr)) { return; }
		
		LocalDate day = date.getValue();
		ZoneId zoneId = ZoneId.of("Etc/GMT+" + Math.abs(sm.order.arr.gmtOffset));
		Instant instant = Instant.from(day.atStartOfDay(zoneId));
		Date date = Date.from(instant);
	
		Airport arr = sm.order.dep;
		Airport dep = sm.order.arr;
		
		sm.order.arr = arr;
		sm.order.dep = dep;
		sm.order.depDate = date;
		sm.order.secondRound = true;
		
		sm.switchState(StateMachine.state.display_flights);
	}
	
    /**
     * A helper method used to verify GUI inputs
     * @return	 Boolean validInputs
     */
	private boolean verifyInputs(Airport a) {
		LocalDate day   = date.getValue();
		
		if(day == null) {
			guiHelpers.throwPopup("Please input a date");
			return false;
		}
		
		// check for bookings prior to your original departure
		ZoneId zoneId = ZoneId.of("Etc/GMT+" + Math.abs(a.gmtOffset));
		Instant instant = Instant.from(day.atStartOfDay(zoneId));		
		if(Date.from(instant).getTime() <= StateMachine.getInstance().order.depDate.getTime()) {
			guiHelpers.throwPopup("No matter how much of a jetsetter you are,\nyou cannot fly back before you fly out");
			return false;
		}
		return true;
	}

}
